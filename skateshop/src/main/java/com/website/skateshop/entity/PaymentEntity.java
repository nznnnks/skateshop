package com.website.skateshop.entity;

import com.website.skateshop.model.PaymentModel;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payment")
public class PaymentEntity extends PaymentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public int getId() {
        return super.getId();
    }

    @Column(name = "price", nullable = false)
    @Override
    public int getPrice() {
        return super.getPrice();
    }

    @Column(name = "paymentmethod", nullable = false, length = 10)
    @Override
    public String getMethod() {
        return super.getMethod();
    }

    @Column(name = "paymentdate", nullable = false, columnDefinition = "date default NOW()")
    public LocalDate getPaymentLocalDate() {
        return super.getPaymentDateAsLocalDate();
    }

    public void setPaymentLocalDate(LocalDate date) {
        super.setPaymentDateFromLocalDate(date);
    }

    @Transient
    @Override
    public String getPaymentDate() {
        return super.getPaymentDate();
    }

    public PaymentEntity() {
        super();
    }

    public PaymentEntity(int id, int price, String method, String paymentDate) {
        super(id, price, method, paymentDate);
    }
}