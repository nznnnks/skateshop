package com.website.skateshop.repository;

import com.website.skateshop.entity.ProductOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrderEntity, Integer> {
    List<ProductOrderEntity> findByOrderId(Integer orderId);
    List<ProductOrderEntity> findByProductId(Integer productId);

    @Modifying
    @Query("DELETE FROM ProductOrderEntity po WHERE po.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") Integer orderId);
}