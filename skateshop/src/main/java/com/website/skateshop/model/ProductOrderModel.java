package com.website.skateshop.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductOrderModel {
    private Integer id;

    @NotNull(message = "Количество не может быть пустым")
    @Positive(message = "Количество должно быть положительным")
    private Integer quantity;

    @NotNull(message = "Товар не может быть пустым")
    private Integer productId;
    private String productTitle;

    @NotNull(message = "Заказ не может быть пустым")
    private Integer orderId;
    private String orderInfo;

    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}