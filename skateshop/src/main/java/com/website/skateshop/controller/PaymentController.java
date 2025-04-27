package com.website.skateshop.controller;

import com.website.skateshop.model.PaymentModel;
import com.website.skateshop.service.PaymentService;
import com.website.skateshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping
    public String getAllPayments(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("payments", paymentService.findPaymentsPaginated(page, size));
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) paymentService.countPayments() / size));
        return "paymentList";
    }

    @GetMapping("/searchById")
    public String findPaymentById(@RequestParam int id, Model model) {
        PaymentModel payment = paymentService.findPaymentById(id);
        model.addAttribute("payments", payment != null ? List.of(payment) : List.of());
        return "paymentList";
    }

    @GetMapping("/searchByMethod")
    public String findPaymentsByMethod(@RequestParam String method, Model model) {
        List<PaymentModel> payments = paymentService.findPaymentsByMethod(method);
        model.addAttribute("payments", payments);
        return "paymentList";
    }

    @PostMapping("/add")
    public String addPayment(@RequestParam int price,
                             @RequestParam String method,
                             @RequestParam String paymentDate,
                             @RequestParam Integer userId) {
        PaymentModel newPayment = new PaymentModel();
        newPayment.setPrice(price);
        newPayment.setMethod(method);
        newPayment.setPaymentDate(LocalDate.parse(paymentDate, DATE_FORMATTER));
        newPayment.setUserId(userId);

        paymentService.addPayment(newPayment);
        return "redirect:/payments";
    }

    @PostMapping("/update")
    public String updatePayment(@Valid @ModelAttribute PaymentModel payment,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/payments";
        }
        paymentService.updatePayment(payment);
        redirectAttributes.addFlashAttribute("message", "Платеж успешно обновлен");
        return "redirect:/payments";
    }

    @PostMapping("/delete")
    public String deletePayment(@RequestParam int id, RedirectAttributes redirectAttributes) {
        paymentService.deletePayment(id);
        redirectAttributes.addFlashAttribute("message", "Платеж успешно удален");
        return "redirect:/payments";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", "Ошибка: " + e.getMessage());
        return "error";
    }
}