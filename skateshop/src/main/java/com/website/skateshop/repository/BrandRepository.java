package com.website.skateshop.repository;

import com.website.skateshop.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
    List<BrandEntity> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
}