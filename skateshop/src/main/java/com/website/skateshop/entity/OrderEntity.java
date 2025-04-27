package com.website.skateshop.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "bookingdate")
    private LocalDate bookingDate;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = true, referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = true)
    private PaymentEntity payment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviews = new ArrayList<>();

    // Constructors, getters, and setters
    public OrderEntity() {
    }

    public OrderEntity(LocalDate bookingDate, String status) {
        this.bookingDate = bookingDate;
        this.status = status;
    }

    // Add all getters and setters for the fields
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
    public PaymentEntity getPayment() { return payment; }
    public void setPayment(PaymentEntity payment) { this.payment = payment; }
    public List<ReviewEntity> getReviews() { return reviews; }
    public void setReviews(List<ReviewEntity> reviews) { this.reviews = reviews; }

    // Methods for managing relationships
    public void addReview(ReviewEntity review) {
        reviews.add(review);
        review.setOrder(this);
    }

    public void removeReview(ReviewEntity review) {
        reviews.remove(review);
        review.setOrder(null);
    }
}