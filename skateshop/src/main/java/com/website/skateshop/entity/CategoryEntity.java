package com.website.skateshop.entity;

import com.website.skateshop.model.CategoryModel;
import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class CategoryEntity extends CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public int getId() {
        return super.getId();
    }

    @Column(name = "categorytitle", nullable = false, length = 30)
    @Override
    public String getName() {
        return super.getName();
    }

    public CategoryEntity() {
        super(0, null);
    }

    public CategoryEntity(int id, String name) {
        super(id, name);
    }
}