package com.website.skateshop.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class PaymentModel {
    private Integer id;

    @NotNull(message = "Сумма обязательна")
    @Positive(message = "Сумма должна быть положительной")
    private Integer price;

    @NotBlank(message = "Метод оплаты обязателен")
    @Size(max = 10, message = "Метод оплаты не длиннее 10 символов")
    private String method;

    @NotNull(message = "Дата платежа обязательна")
    private LocalDate paymentDate;

    private Integer userId;

    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}