package com.website.skateshop.controller;

import com.website.skateshop.model.ProductOrderModel;
import com.website.skateshop.service.ProductOrderService;
import com.website.skateshop.service.ProductService;
import com.website.skateshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product-orders")
public class ProductOrderController {

    private final ProductOrderService productOrderService;
    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public ProductOrderController(ProductOrderService productOrderService,
                                  ProductService productService,
                                  OrderService orderService) {
        this.productOrderService = productOrderService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping
    public String showProductOrdersDefault(Model model,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return showProductOrderList(model, page, size);
    }

    @GetMapping("/list")
    public String showProductOrderList(Model model,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("productOrders", productOrderService.findProductOrdersPaginated(page, size));
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", (int) Math.ceil((double) productOrderService.countProductOrders() / size));
        model.addAttribute("productService", productService);
        model.addAttribute("orderService", orderService);
        return "productOrderList";
    }

    @GetMapping("/searchById")
    public String findProductOrderById(@RequestParam Integer id, Model model) {
        ProductOrderModel productOrder = productOrderService.findProductOrderById(id);
        model.addAttribute("productOrders", productOrder != null ? List.of(productOrder) : List.of());
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        model.addAttribute("productService", productService);
        model.addAttribute("orderService", orderService);
        return "productOrderList";
    }

    @GetMapping("/searchByOrderId")
    public String findProductOrdersByOrderId(@RequestParam Integer orderId, Model model,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        List<ProductOrderModel> productOrders = productOrderService.findByOrderId(orderId);
        model.addAttribute("productOrders", productOrders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) productOrders.size() / size));
        model.addAttribute("productService", productService);
        model.addAttribute("orderService", orderService);
        return "productOrderList";
    }

    @GetMapping("/searchByProductId")
    public String findProductOrdersByProductId(@RequestParam Integer productId, Model model,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        List<ProductOrderModel> productOrders = productOrderService.findByProductId(productId);
        model.addAttribute("productOrders", productOrders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) productOrders.size() / size));
        model.addAttribute("productService", productService);
        model.addAttribute("orderService", orderService);
        return "productOrderList";
    }

    @PostMapping("/add")
    public String addProductOrder(@RequestParam Integer productId,
                                  @RequestParam Integer orderId,
                                  @RequestParam Integer quantity) {
        ProductOrderModel newProductOrder = new ProductOrderModel();
        newProductOrder.setProductId(productId);
        newProductOrder.setOrderId(orderId);
        newProductOrder.setQuantity(quantity);
        productOrderService.addProductOrder(newProductOrder);
        return "redirect:/product-orders/list";
    }

    @PostMapping("/update")
    public String updateProductOrder(@RequestParam Integer id,
                                     @RequestParam Integer quantity) {
        ProductOrderModel updatedProductOrder = productOrderService.findProductOrderById(id);
        if (updatedProductOrder != null) {
            updatedProductOrder.setQuantity(quantity);
            productOrderService.updateProductOrder(updatedProductOrder);
        }
        return "redirect:/product-orders/list";
    }

    @PostMapping("/delete")
    public String deleteProductOrder(@RequestParam Integer id) {
        productOrderService.deleteProductOrder(id);
        return "redirect:/product-orders/list";
    }

    @PostMapping("/deleteByOrder")
    public String deleteByOrderId(@RequestParam Integer orderId) {
        productOrderService.deleteByOrderId(orderId);
        return "redirect:/product-orders/list";
    }
}