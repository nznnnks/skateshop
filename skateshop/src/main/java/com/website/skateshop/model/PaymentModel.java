package com.website.skateshop.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PaymentModel {
    private int id;

    @NotNull(message = "Сумма обязательна")
    @Positive(message = "Сумма должна быть положительной")
    private int price;

    @NotBlank(message = "Метод оплаты обязателен")
    @Size(max = 10, message = "Метод оплаты не длиннее 10 символов")
    private String method;

    private String paymentDateString;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PaymentModel() {
    }

    public PaymentModel(int id, int price, String method, String paymentDate) {
        this.id = id;
        this.price = price;
        this.method = method;
        this.setPaymentDate(paymentDate);
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getPaymentDate() {
        return paymentDateString;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDateString = paymentDate;
    }

    public LocalDate getPaymentDateAsLocalDate() {
        return paymentDateString != null ? LocalDate.parse(paymentDateString, DATE_FORMATTER) : null;
    }

    public void setPaymentDateFromLocalDate(LocalDate date) {
        this.paymentDateString = date != null ? date.format(DATE_FORMATTER) : null;
    }
}