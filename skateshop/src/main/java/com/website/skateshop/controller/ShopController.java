package com.website.skateshop.controller;

import com.website.skateshop.model.CartModel;
import com.website.skateshop.model.CartItem;
import com.website.skateshop.model.ProductModel;
import com.website.skateshop.model.OrderModel;
import com.website.skateshop.model.ProductOrderModel;
import com.website.skateshop.model.UserModel;
import com.website.skateshop.service.ProductService;
import com.website.skateshop.service.OrderService;
import com.website.skateshop.service.ProductOrderService;
import com.website.skateshop.service.UserService;
import com.website.skateshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    private final Map<String, String> categoryMapping = new HashMap<>();

    public ShopController() {
        categoryMapping.put("shoes", "Shoes");
        categoryMapping.put("wear", "Clothing");
        categoryMapping.put("boards", "Boards");
        categoryMapping.put("wheels", "Wheels");
        categoryMapping.put("fingerboards", "Fingerboards");
        categoryMapping.put("protection", "Protection");
    }

    @GetMapping
    public String showProducts(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "12") int size,
                               @RequestParam(required = false) String category) {

        List<ProductModel> products;
        String categoryName = null;

        if (category != null && !category.isEmpty()) {
            categoryName = categoryMapping.get(category);
            if (categoryName != null) {
                products = productService.findProductsByCategory(categoryName);
                model.addAttribute("currentCategory", categoryName);
            } else {
                products = productService.findProductsPaginated(page, size);
            }
        } else {
            products = productService.findProductsPaginated(page, size);
        }

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) productService.countProducts() / size));
        model.addAttribute("categories", categoryMapping);

        return "shop";
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam int productId,
                            @RequestParam int quantity,
                            @RequestParam(required = false) String category,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        CartModel cart = getOrCreateCart(session);
        ProductModel product = productService.findProductById(productId);

        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Product not found");
            return getRedirectUrl(category);
        }

        if (product.getQuantity() < quantity) {
            redirectAttributes.addFlashAttribute("error", "Insufficient stock. Available: " + product.getQuantity());
            return getRedirectUrl(category);
        }

        cart.addItem(product, quantity);
        redirectAttributes.addFlashAttribute("success", "Product '" + product.getProductTitle() + "' added to cart");

        return getRedirectUrl(category);
    }

    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        CartModel cart = getOrCreateCart(session);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/cart/update")
    public String updateCartItem(@RequestParam int productId,
                                 @RequestParam int quantity,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        CartModel cart = getOrCreateCart(session);

        if (quantity <= 0) {
            cart.removeItem(productId);
            redirectAttributes.addFlashAttribute("success", "Product removed from cart");
        } else {
            ProductModel product = productService.findProductById(productId);
            if (product != null && product.getQuantity() < quantity) {
                redirectAttributes.addFlashAttribute("error", "Insufficient stock. Available: " + product.getQuantity());
                return "redirect:/shop/cart";
            }
            cart.updateQuantity(productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Quantity updated");
        }

        return "redirect:/shop/cart";
    }

    @PostMapping("/cart/remove")
    public String removeCartItem(@RequestParam int productId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        CartModel cart = getOrCreateCart(session);
        cart.removeItem(productId);
        redirectAttributes.addFlashAttribute("success", "Product removed from cart");
        return "redirect:/shop/cart";
    }

    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login?checkout=true";
        }

        CartModel cart = getOrCreateCart(session);
        if (cart.getItems().isEmpty()) {
            return "redirect:/shop/cart";
        }

        for (CartItem item : cart.getItems()) {
            ProductModel currentProduct = productService.findProductById(item.getProduct().getId());
            if (currentProduct == null) {
                model.addAttribute("error", "Product '" + item.getProduct().getProductTitle() + "' no longer available");
                return "cart";
            }
            if (currentProduct.getQuantity() < item.getQuantity()) {
                model.addAttribute("error", "Insufficient stock for '" + item.getProduct().getProductTitle() + "'. Available: " + currentProduct.getQuantity());
                return "cart";
            }
        }

        UserModel currentUser = getCurrentUserModel();
        if (currentUser == null) {
            return "redirect:/login?checkout=true";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("payments", paymentService.findAllPayments());
        model.addAttribute("currentUser", currentUser);

        return "checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(@RequestParam Integer paymentId,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login?checkout=true";
        }

        CartModel cart = getOrCreateCart(session);

        if (cart.getItems().isEmpty()) {
            return "redirect:/shop/cart";
        }

        try {
            UserModel currentUser = getCurrentUserModel();
            if (currentUser == null || currentUser.getId() == null) {
                model.addAttribute("error", "User not found");
                return "redirect:/login";
            }

            for (CartItem item : cart.getItems()) {
                ProductModel currentProduct = productService.findProductById(item.getProduct().getId());
                if (currentProduct.getQuantity() < item.getQuantity()) {
                    model.addAttribute("error", "Insufficient stock for '" + item.getProduct().getProductTitle() + "'. Available: " + currentProduct.getQuantity());
                    model.addAttribute("cart", cart);
                    model.addAttribute("payments", paymentService.findAllPayments());
                    model.addAttribute("currentUser", currentUser);
                    return "checkout";
                }
            }

            OrderModel order = new OrderModel();
            order.setBookingDate(LocalDate.now());
            order.setStatus("new");
            order.setUserId(currentUser.getId());
            order.setPaymentId(paymentId);

            OrderModel savedOrder = orderService.addOrder(order);

            for (CartItem item : cart.getItems()) {
                ProductOrderModel productOrder = new ProductOrderModel();
                productOrder.setOrderId(savedOrder.getId());
                productOrder.setProductId(item.getProduct().getId());
                productOrder.setQuantity(item.getQuantity());

                productOrderService.addProductOrder(productOrder);

                productService.updateProductQuantity(
                        item.getProduct().getId(),
                        -item.getQuantity()
                );
            }

            cart.clear();

            redirectAttributes.addFlashAttribute("orderId", savedOrder.getId());
            redirectAttributes.addFlashAttribute("success", "Order #" + savedOrder.getId() + " created successfully!");
            return "redirect:/shop/orderConfirmation/" + savedOrder.getId();

        } catch (Exception e) {
            model.addAttribute("error", "Order processing error: " + e.getMessage());
            model.addAttribute("cart", cart);
            model.addAttribute("payments", paymentService.findAllPayments());
            model.addAttribute("currentUser", getCurrentUserModel());
            return "checkout";
        }
    }

    @GetMapping("/orderConfirmation/{orderId}")
    public String showOrderConfirmation(@PathVariable int orderId, Model model) {
        OrderModel order = orderService.findOrderById(orderId);
        if (order == null) {
            return "redirect:/shop";
        }

        model.addAttribute("order", order);
        return "order-confirmation";
    }

    @PostMapping("/clearCart")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        CartModel cart = getOrCreateCart(session);
        cart.clear();
        redirectAttributes.addFlashAttribute("success", "Cart cleared");
        return "redirect:/shop/cart";
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                return (String) principal;
            }
        }
        return null;
    }

    private UserModel getCurrentUserModel() {
        String username = getCurrentUsername();
        if (username != null) {
            List<UserModel> users = userService.findUsersByLogin(username);
            if (!users.isEmpty()) {
                return users.get(0);
            }
        }
        return null;
    }

    private CartModel getOrCreateCart(HttpSession session) {
        CartModel cart = (CartModel) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartModel();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private String getRedirectUrl(String category) {
        if (category != null && !category.isEmpty()) {
            return "redirect:/shop?category=" + category;
        }
        return "redirect:/shop";
    }
}