package com.website.skateshop.repository;

import com.website.skateshop.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    List<CategoryEntity> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
}