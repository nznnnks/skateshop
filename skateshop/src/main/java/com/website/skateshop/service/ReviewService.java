package com.website.skateshop.service;

import com.website.skateshop.model.ReviewModel;
import java.util.List;

public interface ReviewService {
    List<ReviewModel> findAllReviews();
    ReviewModel findReviewById(int id);
    List<ReviewModel> findReviewsByRating(int rating);
    ReviewModel addReview(ReviewModel review);
    ReviewModel updateReview(ReviewModel review);
    void deleteReview(int id);
    List<ReviewModel> findReviewsPaginated(int page, int size);
    long countReviews();
}