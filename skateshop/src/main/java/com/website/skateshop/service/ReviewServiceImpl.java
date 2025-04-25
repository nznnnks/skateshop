package com.website.skateshop.service;

import com.website.skateshop.entity.ReviewEntity;
import com.website.skateshop.entity.UserEntity;
import com.website.skateshop.model.ReviewModel;
import com.website.skateshop.repository.ReviewRepository;
import com.website.skateshop.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
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

        if (review.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        UserEntity user = userRepository.findById(review.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ReviewEntity entity = convertToEntity(review);
        entity.setUser(user);

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
        ReviewModel model = new ReviewModel();
        model.setId(entity.getId());
        model.setReviewTitle(entity.getReviewTitle());
        model.setRating(entity.getRating());
        model.setReviewDate(entity.getReviewDate());

        if (entity.getUser() != null) {
            model.setUserId(entity.getUser().getId());
            model.setUserName(entity.getUser().getName() + " " + entity.getUser().getSurname());
        }

        return model;
    }

    private ReviewEntity convertToEntity(ReviewModel model) {
        ReviewEntity entity = new ReviewEntity();
        entity.setId(model.getId());
        entity.setReviewTitle(model.getReviewTitle());
        entity.setRating(model.getRating());
        entity.setReviewDate(model.getReviewDate());

        if (model.getUserId() != null) {
            UserEntity user = new UserEntity();
            user.setId(model.getUserId());
            entity.setUser(user);
        }

        return entity;
    }
}