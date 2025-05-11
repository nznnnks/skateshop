package com.website.skateshop.repository;

import com.website.skateshop.entity.ProductOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrderEntity, Integer> {
    List<ProductOrderEntity> findByOrderId(Integer orderId);
    List<ProductOrderEntity> findByProductId(Integer productId);
}