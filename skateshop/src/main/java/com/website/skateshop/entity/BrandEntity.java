package com.website.skateshop.entity;

import com.website.skateshop.model.BrandModel;
import jakarta.persistence.*;

@Entity
@Table(name = "brand")
public class BrandEntity extends BrandModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public int getId() {
        return super.getId();
    }

    @Column(name = "brandtitle", nullable = false, length = 30)
    @Override
    public String getName() {
        return super.getName();
    }

    public BrandEntity() {
        super(0, null);
    }

    public BrandEntity(int id, String name) {
        super(id, name);
    }
}