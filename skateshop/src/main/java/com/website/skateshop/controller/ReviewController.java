package com.website.skateshop.controller;

import com.website.skateshop.model.ReviewModel;
import com.website.skateshop.service.ReviewService;
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

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping
    public String getAllReviews(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("reviews", reviewService.findReviewsPaginated(page, size));
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
                            @RequestParam(required = false) String reviewDate) {
        LocalDate parsedDate = reviewDate != null ? LocalDate.parse(reviewDate, DATE_FORMATTER) : LocalDate.now();
        ReviewModel newReview = new ReviewModel(0, reviewTitle, rating, parsedDate);
        reviewService.addReview(newReview);
        return "redirect:/reviews";
    }

    @PostMapping("/update")
    public String updateReview(@RequestParam int id,
                               @RequestParam String reviewTitle,
                               @RequestParam Integer rating,
                               @RequestParam String reviewDate) {
        LocalDate parsedDate = LocalDate.parse(reviewDate, DATE_FORMATTER);
        ReviewModel updatedReview = new ReviewModel(id, reviewTitle, rating, parsedDate);
        reviewService.updateReview(updatedReview);
        return "redirect:/reviews";
    }

    @PostMapping("/delete")
    public String deleteReview(@RequestParam int id) {
        reviewService.deleteReview(id);
        return "redirect:/reviews";
    }
}