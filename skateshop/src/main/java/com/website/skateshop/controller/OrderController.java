package com.website.skateshop.controller;

import com.website.skateshop.model.OrderModel;
import com.website.skateshop.service.OrderService;
import com.website.skateshop.service.PaymentService;
import com.website.skateshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentService paymentService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping
    public String getAllOrders(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("orders", orderService.findOrdersPaginated(page, size));
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("payments", paymentService.findAllPayments());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) orderService.countOrders() / size));
        return "orderList";
    }

    @GetMapping("/searchById")
    public String findOrderById(@RequestParam int id, Model model) {
        OrderModel order = orderService.findOrderById(id);
        model.addAttribute("orders", order != null ? List.of(order) : List.of());
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        return "orderList";
    }

    @GetMapping("/searchByStatus")
    public String findOrdersByStatus(@RequestParam String status, Model model,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        List<OrderModel> orders = orderService.findOrdersByStatus(status);
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) orders.size() / size));
        return "orderList";
    }

    @PostMapping("/add")
    public String addOrder(@RequestParam String bookingDate,
                           @RequestParam String status,
                           @RequestParam Integer userId,
                           @RequestParam Integer paymentId) {
        OrderModel newOrder = new OrderModel();
        newOrder.setId(null); // Явно устанавливаем null для нового заказа
        newOrder.setBookingDate(LocalDate.parse(bookingDate, DATE_FORMATTER));
        newOrder.setStatus(status);
        newOrder.setUserId(userId);
        newOrder.setPaymentId(paymentId);

        orderService.addOrder(newOrder);
        return "redirect:/orders";
    }

    @PostMapping("/update")
    public String updateOrder(@RequestParam int id,
                              @RequestParam String bookingDate,
                              @RequestParam String status,
                              @RequestParam Integer userId,
                              @RequestParam Integer paymentId) {
        OrderModel updatedOrder = new OrderModel();
        updatedOrder.setId(id);
        updatedOrder.setBookingDate(LocalDate.parse(bookingDate, DATE_FORMATTER));
        updatedOrder.setStatus(status);
        updatedOrder.setUserId(userId);
        updatedOrder.setPaymentId(paymentId);

        orderService.updateOrder(updatedOrder);
        return "redirect:/orders";
    }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam int id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}