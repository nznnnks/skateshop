package com.website.skateshop.entity;

import com.website.skateshop.model.ReviewModel;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "review")
public class ReviewEntity extends ReviewModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public int getId() {
        return super.getId();
    }

    @Column(name = "reviewtitle", nullable = false, length = 200)
    @Override
    public String getReviewTitle() {
        return super.getReviewTitle();
    }

    @Column(name = "rating", nullable = false)
    @Override
    public Integer getRating() {
        return super.getRating();
    }

    @Column(name = "reviewdate", nullable = false, columnDefinition = "date default CURRENT_DATE")
    @Override
    public LocalDate getReviewDate() {
        return super.getReviewDate();
    }

    public ReviewEntity() {
        super();
    }

    public ReviewEntity(int id, String reviewTitle, Integer rating, LocalDate reviewDate) {
        super(id, reviewTitle, rating, reviewDate);
    }
}