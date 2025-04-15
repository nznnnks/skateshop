package com.website.skateshop.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class OrderModel {
    private int id;

    @NotNull(message = "Дата заказа не может быть пустой")
    private LocalDate bookingDate;

    @NotBlank(message = "Статус не может быть пустым")
    @Size(max = 10, message = "Статус не может быть длиннее 10 символов")
    private String status;

    public OrderModel(int id, LocalDate bookingDate, String status) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
