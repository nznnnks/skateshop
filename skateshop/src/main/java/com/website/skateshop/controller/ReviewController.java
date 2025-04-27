package com.website.skateshop.controller;

import com.website.skateshop.model.ReviewModel;
import com.website.skateshop.service.OrderService;
import com.website.skateshop.service.ReviewService;
import com.website.skateshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping
    public String getAllReviews(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("reviews", reviewService.findReviewsPaginated(page, size));
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("orders", orderService.findAllOrders()); // Добавляем список заказов
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) reviewService.countReviews() / size));
        return "reviewList";
    }

    @GetMapping("/searchById")
    public String findReviewById(@RequestParam int id, Model model) {
        ReviewModel review = reviewService.findReviewById(id);
        model.addAttribute("reviews", review != null ? List.of(review) : List.of());
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        return "reviewList";
    }

    @GetMapping("/searchByRating")
    public String findReviewsByRating(@RequestParam int rating, Model model,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        List<ReviewModel> reviews = reviewService.findReviewsByRating(rating);
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) reviews.size() / size));
        return "reviewList";
    }

    @PostMapping("/add")
    public String addReview(@RequestParam String reviewTitle,
                            @RequestParam Integer rating,
                            @RequestParam(required = false) String reviewDate,
                            @RequestParam Integer userId,
                            @RequestParam Integer orderId) {
        LocalDate parsedDate = reviewDate != null ?
                LocalDate.parse(reviewDate, DATE_FORMATTER) : LocalDate.now();

        ReviewModel newReview = new ReviewModel();
        newReview.setReviewTitle(reviewTitle);
        newReview.setRating(rating);
        newReview.setReviewDate(parsedDate);
        newReview.setUserId(userId);
        newReview.setOrderId(orderId);

        reviewService.addReview(newReview);
        return "redirect:/reviews";
    }

    @PostMapping("/update")
    public String updateReview(@RequestParam int id,
                               @RequestParam String reviewTitle,
                               @RequestParam Integer rating,
                               @RequestParam String reviewDate) {
        LocalDate parsedDate = LocalDate.parse(reviewDate, DATE_FORMATTER);
        ReviewModel updatedReview = new ReviewModel();
        updatedReview.setId(id);
        updatedReview.setReviewTitle(reviewTitle);
        updatedReview.setRating(rating);
        updatedReview.setReviewDate(parsedDate);
        reviewService.updateReview(updatedReview);
        return "redirect:/reviews";
    }

    @PostMapping("/delete")
    public String deleteReview(@RequestParam int id) {
        reviewService.deleteReview(id);
        return "redirect:/reviews";
    }
}