package com.website.skateshop.service;

import com.website.skateshop.entity.*;
import com.website.skateshop.model.OrderModel;
import com.website.skateshop.repository.OrderRepository;
import com.website.skateshop.repository.UserRepository;
import com.website.skateshop.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
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
        OrderEntity entity = orderRepository.findById(order.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        entity.setBookingDate(order.getBookingDate());
        entity.setStatus(order.getStatus());

        if (order.getUserId() != null) {
            UserEntity user = userRepository.findById(order.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            entity.setUser(user);
        }

        if (order.getPaymentId() != null) {
            PaymentEntity payment = paymentRepository.findById(order.getPaymentId())
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
            entity.setPayment(payment);
        }

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
        OrderModel model = new OrderModel();
        model.setId(entity.getId());
        model.setBookingDate(entity.getBookingDate());
        model.setStatus(entity.getStatus());

        if (entity.getUser() != null) {
            model.setUserId(entity.getUser().getId());
            model.setUserName(entity.getUser().getName() + " " + entity.getUser().getSurname());
        }

        if (entity.getPayment() != null) {
            model.setPaymentId(entity.getPayment().getId());
            model.setPaymentMethod(entity.getPayment().getMethod());
        }

        return model;
    }

    private OrderEntity convertToEntity(OrderModel model) {
        OrderEntity entity = new OrderEntity();
        entity.setId(model.getId());

        entity.setBookingDate(model.getBookingDate());
        entity.setStatus(model.getStatus());

        if (model.getUserId() != null) {
            UserEntity user = userRepository.findById(model.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            entity.setUser(user);
        }

        if (model.getPaymentId() != null) {
            PaymentEntity payment = paymentRepository.findById(model.getPaymentId())
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
            entity.setPayment(payment);
        }

        return entity;
    }
}