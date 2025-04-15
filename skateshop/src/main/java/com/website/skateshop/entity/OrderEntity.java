package com.website.skateshop.entity;

import jakarta.persistence.*;
import com.website.skateshop.model.OrderModel;

import java.time.LocalDate;

@Entity
@Table(name = "booking")
public class OrderEntity extends OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public int getId() {
        return super.getId();
    }

    @Column(name = "bookingdate")
    @Override
    public LocalDate getBookingDate() {
        return super.getBookingDate();
    }

    @Column(name = "status")
    @Override
    public String getStatus() {
        return super.getStatus();
    }

    public OrderEntity() {
        super(0, null, null);
    }

    public OrderEntity(int id, LocalDate bookingDate, String status) {
        super(id, bookingDate, status);
    }
}
