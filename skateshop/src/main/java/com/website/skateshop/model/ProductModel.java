package com.website.skateshop.model;

import jakarta.validation.constraints.*;

public class ProductModel {
    private Integer id;

    @NotBlank
    @Size(max = 30)
    private String productTitle;

    @NotNull
    @Positive
    private Integer price;

    @NotNull
    @Positive
    private Integer quantity;

    private Integer brandId;
    private String brandName; // Новое поле

    private Integer categoryId;
    private String categoryName; // Новое поле

    // Геттеры и сеттеры для всех полей
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getProductTitle() { return productTitle; }
    public void setProductTitle(String productTitle) { this.productTitle = productTitle; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getBrandId() { return brandId; }
    public void setBrandId(Integer brandId) { this.brandId = brandId; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}