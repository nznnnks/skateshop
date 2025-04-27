package com.website.skateshop.service;

import com.website.skateshop.entity.OrderEntity;
import com.website.skateshop.entity.ReviewEntity;
import com.website.skateshop.entity.UserEntity;
import com.website.skateshop.model.ReviewModel;
import com.website.skateshop.repository.OrderRepository;
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
    private final OrderRepository orderRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
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
        ReviewModel model = new ReviewModel();
        model.setId(entity.getId());
        model.setReviewTitle(entity.getReviewTitle());
        model.setRating(entity.getRating());
        model.setReviewDate(entity.getReviewDate());

        // Связь с User
        if (entity.getUser() != null) {
            model.setUserId(entity.getUser().getId());
            model.setUserName(entity.getUser().getName() + " " + entity.getUser().getSurname());
        }

        // Связь с Order
        if (entity.getOrder() != null) {
            model.setOrderId(entity.getOrder().getId());
            model.setOrderInfo("Заказ #" + entity.getOrder().getId() +
                    " (" + entity.getOrder().getBookingDate() + ")");
        }

        return model;
    }

    private ReviewEntity convertToEntity(ReviewModel model) {
        ReviewEntity entity = new ReviewEntity();
        entity.setId(model.getId());
        entity.setReviewTitle(model.getReviewTitle());
        entity.setRating(model.getRating());
        entity.setReviewDate(model.getReviewDate());

        // Связь с User
        if (model.getUserId() != null) {
            UserEntity user = userRepository.findById(model.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            entity.setUser(user);
        }

        // Связь с Order
        if (model.getOrderId() != null) {
            OrderEntity order = orderRepository.findById(model.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));
            entity.setOrder(order);
        }

        return entity;
    }
}