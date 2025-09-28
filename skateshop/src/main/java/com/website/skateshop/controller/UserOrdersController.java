package com.website.skateshop.controller;

import com.website.skateshop.model.OrderModel;
import com.website.skateshop.model.UserModel;
import com.website.skateshop.service.OrderService;
import com.website.skateshop.service.UserService;
import com.website.skateshop.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserOrdersController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductOrderService productOrderService;

    @GetMapping("/orders")
    public String showUserOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        UserModel currentUser = getCurrentUserModel();
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<OrderModel> userOrders = orderService.findOrdersByUserId(currentUser.getId());

        model.addAttribute("orders", userOrders);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("productOrderService", productOrderService);

        return "user-orders";
    }

    @GetMapping("/orders/{orderId}")
    public String showOrderDetails(@PathVariable int orderId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        UserModel currentUser = getCurrentUserModel();
        if (currentUser == null) {
            return "redirect:/login";
        }

        OrderModel order = orderService.findOrderById(orderId);
        if (order == null || !order.getUserId().equals(currentUser.getId())) {
            model.addAttribute("error", "Order not found or access denied");
            return "redirect:/user/orders";
        }

        var orderItems = productOrderService.findByOrderId(orderId);

        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("currentUser", currentUser);

        return "order-details";
    }

    private UserModel getCurrentUserModel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String username = "";

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = (String) principal;
            }

            if (!username.isEmpty()) {
                List<UserModel> users = userService.findUsersByLogin(username);
                if (!users.isEmpty()) {
                    return users.get(0);
                }
            }
        }
        return null;
    }
}