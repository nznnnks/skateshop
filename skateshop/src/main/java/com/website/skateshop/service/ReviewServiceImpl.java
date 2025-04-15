package com.website.skateshop.service;

import com.website.skateshop.entity.ReviewEntity;
import com.website.skateshop.model.ReviewModel;
import com.website.skateshop.repository.ReviewRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<ReviewModel> findAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewModel findReviewById(int id) {
        return reviewRepository.findById(id)
                .map(this::convertToModel)
                .orElse(null);
    }

    @Override
    public List<ReviewModel> findReviewsByRating(int rating) {
        return reviewRepository.findByRating(rating).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewModel addReview(ReviewModel review) {
        if (review.getReviewDate() == null) {
            review.setReviewDate(LocalDate.now());
        }
        ReviewEntity entity = convertToEntity(review);
        ReviewEntity saved = reviewRepository.save(entity);
        return convertToModel(saved);
    }

    @Override
    public ReviewModel updateReview(ReviewModel review) {
        ReviewEntity entity = convertToEntity(review);
        ReviewEntity updated = reviewRepository.save(entity);
        return convertToModel(updated);
    }

    @Override
    public void deleteReview(int id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public List<ReviewModel> findReviewsPaginated(int page, int size) {
        return reviewRepository.findAll(PageRequest.of(page, size)).getContent().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public long countReviews() {
        return reviewRepository.count();
    }

    private ReviewModel convertToModel(ReviewEntity entity) {
        return new ReviewModel(
                entity.getId(),
                entity.getReviewTitle(),
                entity.getRating(),
                entity.getReviewDate()
        );
    }

    private ReviewEntity convertToEntity(ReviewModel model) {
        return new ReviewEntity(
                model.getId(),
                model.getReviewTitle(),
                model.getRating(),
                model.getReviewDate()
        );
    }
}