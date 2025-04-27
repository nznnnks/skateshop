package com.website.skateshop.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class OrderModel {
    private int id;

    @NotNull(message = "Дата заказа не может быть пустой")
    private LocalDate bookingDate;

    @NotBlank(message = "Статус не может быть пустым")
    @Size(max = 10, message = "Статус не может быть длиннее 10 символов")
    private String status;

    private Integer userId;
    private String userName;
    private Integer paymentId;
    private String paymentMethod;

    // Конструктор по умолчанию
    public OrderModel() {}

    // Конструктор с параметрами
    public OrderModel(int id, LocalDate bookingDate, String status) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}