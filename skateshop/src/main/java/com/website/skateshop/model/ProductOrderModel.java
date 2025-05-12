package com.website.skateshop.model;

public class ProductOrderModel {
    private Integer id;
    private Integer productId;
    private String productTitle;
    private Integer orderId;
    private Integer quantity;

    public ProductOrderModel() {
    }

    public ProductOrderModel(Integer id, Integer productId, String productTitle, Integer orderId, Integer quantity) {
        this.id = id;
        this.productId = productId;
        this.productTitle = productTitle;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}