package com.website.skateshop.controller;

import com.website.skateshop.model.OrderModel;
import com.website.skateshop.service.OrderService;
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

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping
    public String getAllOrders(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("orders", orderService.findOrdersPaginated(page, size));
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
    public String addOrder(@RequestParam String bookingDate, @RequestParam String status) {
        LocalDate parsedDate = LocalDate.parse(bookingDate, DATE_FORMATTER);
        OrderModel newOrder = new OrderModel(0, parsedDate, status);
        orderService.addOrder(newOrder);
        return "redirect:/orders";
    }

    @PostMapping("/update")
    public String updateOrder(@RequestParam int id, @RequestParam String bookingDate, @RequestParam String status) {
        LocalDate parsedDate = LocalDate.parse(bookingDate, DATE_FORMATTER);
        OrderModel updatedOrder = new OrderModel(id, parsedDate, status);
        orderService.updateOrder(updatedOrder);
        return "redirect:/orders";
    }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam int id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}
