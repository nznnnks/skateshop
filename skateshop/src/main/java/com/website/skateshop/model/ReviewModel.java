package com.website.skateshop.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ReviewModel {
    private Integer id;

    @NotBlank(message = "Заголовок отзыва не может быть пустым")
    @Size(max = 200, message = "Заголовок отзыва не может быть длиннее 200 символов")
    private String reviewTitle;

    @NotNull(message = "Рейтинг не может быть пустым")
    @Min(value = 1, message = "Рейтинг должен быть не менее 1")
    @Max(value = 5, message = "Рейтинг должен быть не более 5")
    private Integer rating;

    @NotNull(message = "Дата отзыва не может быть пустой")
    private LocalDate reviewDate;

    private Integer userId;
    private String userName;

    public ReviewModel() {
    }

    public ReviewModel(Integer id, String reviewTitle, Integer rating, LocalDate reviewDate) {
        this.id = id;
        this.reviewTitle = reviewTitle;
        this.rating = rating;
        this.reviewDate = reviewDate;
    }

    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}