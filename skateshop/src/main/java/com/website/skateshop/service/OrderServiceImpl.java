package com.website.skateshop.service;

import com.website.skateshop.entity.OrderEntity;
import com.website.skateshop.model.OrderModel;
import com.website.skateshop.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderModel> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public OrderModel findOrderById(int id) {
        return orderRepository.findById(id)
                .map(this::convertToModel)
                .orElse(null);
    }

    @Override
    public List<OrderModel> findOrdersByStatus(String status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public OrderModel addOrder(OrderModel order) {
        OrderEntity entity = convertToEntity(order);
        OrderEntity saved = orderRepository.save(entity);
        return convertToModel(saved);
    }

    @Override
    public OrderModel updateOrder(OrderModel order) {
        OrderEntity entity = convertToEntity(order);
        OrderEntity updated = orderRepository.save(entity);
        return convertToModel(updated);
    }

    @Override
    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderModel> findOrdersPaginated(int page, int size) {
        Page<OrderEntity> result = orderRepository.findAll(PageRequest.of(page, size));
        return result.getContent().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public long countOrders() {
        return orderRepository.count();
    }

    private OrderModel convertToModel(OrderEntity entity) {
        return new OrderModel(
                entity.getId(),
                entity.getBookingDate(),
                entity.getStatus()
        );
    }

    private OrderEntity convertToEntity(OrderModel model) {
        return new OrderEntity(
                model.getId(),
                model.getBookingDate(),
                model.getStatus()
        );
    }
}
