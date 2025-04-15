package com.website.skateshop.entity;

import jakarta.persistence.*;
import com.website.skateshop.model.ProductModel;

@Entity
@Table(name = "product")
public class ProductEntity extends ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public int getId() {
        return super.getId();
    }

    @Column(name = "producttitle", nullable = false, length = 30)
    @Override
    public String getProductTitle() {
        return super.getProductTitle();
    }

    @Column(name = "price", nullable = false)
    @Override
    public Integer getPrice() {
        return super.getPrice();
    }

    @Column(name = "quantity", nullable = false)
    @Override
    public Integer getQuantity() {
        return super.getQuantity();
    }

    public ProductEntity() {
        super(0, null, null, null);
    }

    public ProductEntity(int id, String productTitle, Integer price, Integer quantity) {
        super(id, productTitle, price, quantity);
    }
}