package com.website.skateshop.service;

import com.website.skateshop.model.OrderModel;

import java.util.List;

public interface OrderService {
    List<OrderModel> findAllOrders();
    OrderModel findOrderById(int id);
    List<OrderModel> findOrdersByStatus(String status);
    OrderModel addOrder(OrderModel order);
    OrderModel updateOrder(OrderModel order);
    void deleteOrder(int id);

    List<OrderModel> findOrdersPaginated(int page, int size);
    long countOrders();
}