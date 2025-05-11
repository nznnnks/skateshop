package com.website.skateshop.service;

import com.website.skateshop.entity.*;
import com.website.skateshop.model.ProductOrderModel;
import com.website.skateshop.repository.ProductOrderRepository;
import com.website.skateshop.repository.ProductRepository;
import com.website.skateshop.repository.OrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    private final ProductOrderRepository productOrderRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ProductOrderServiceImpl(ProductOrderRepository productOrderRepository,
                                   ProductRepository productRepository,
                                   OrderRepository orderRepository) {
        this.productOrderRepository = productOrderRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<ProductOrderModel> findAllProductOrders() {
        return productOrderRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public ProductOrderModel findProductOrderById(int id) {
        return productOrderRepository.findById(id)
                .map(this::convertToModel)
                .orElse(null);
    }

    @Override
    public List<ProductOrderModel> findProductOrdersByOrderId(int orderId) {
        return productOrderRepository.findByOrderId(orderId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductOrderModel> findProductOrdersByProductId(int productId) {
        return productOrderRepository.findByProductId(productId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public ProductOrderModel addProductOrder(ProductOrderModel productOrder) {
        ProductOrderEntity entity = convertToEntity(productOrder);
        ProductOrderEntity saved = productOrderRepository.save(entity);
        return convertToModel(saved);
    }

    @Override
    public ProductOrderModel updateProductOrder(ProductOrderModel productOrder) {
        ProductOrderEntity entity = productOrderRepository.findById(productOrder.getId())
                .orElseThrow(() -> new IllegalArgumentException("ProductOrder not found"));

        entity.setQuantity(productOrder.getQuantity());

        if (productOrder.getProductId() != null) {
            ProductEntity product = productRepository.findById(productOrder.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            entity.setProduct(product);
        }

        if (productOrder.getOrderId() != null) {
            OrderEntity order = orderRepository.findById(productOrder.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));
            entity.setOrder(order);
        }

        ProductOrderEntity updated = productOrderRepository.save(entity);
        return convertToModel(updated);
    }

    @Override
    public void deleteProductOrder(int id) {
        productOrderRepository.deleteById(id);
    }

    @Override
    public List<ProductOrderModel> findProductOrdersPaginated(int page, int size) {
        return productOrderRepository.findAll(PageRequest.of(page, size)).getContent().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public long countProductOrders() {
        return productOrderRepository.count();
    }

    private ProductOrderModel convertToModel(ProductOrderEntity entity) {
        ProductOrderModel model = new ProductOrderModel();
        model.setId(entity.getId());
        model.setQuantity(entity.getQuantity());

        if (entity.getProduct() != null) {
            model.setProductId(entity.getProduct().getId());
            model.setProductTitle(entity.getProduct().getProductTitle());
        }

        if (entity.getOrder() != null) {
            model.setOrderId(entity.getOrder().getId());
            model.setOrderInfo("Заказ #" + entity.getOrder().getId() +
                    " (" + entity.getOrder().getBookingDate() + ")");
        }

        return model;
    }

    private ProductOrderEntity convertToEntity(ProductOrderModel model) {
        ProductOrderEntity entity = new ProductOrderEntity();
        entity.setId(model.getId());
        entity.setQuantity(model.getQuantity());

        if (model.getProductId() != null) {
            ProductEntity product = productRepository.findById(model.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            entity.setProduct(product);
        }

        if (model.getOrderId() != null) {
            OrderEntity order = orderRepository.findById(model.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));
            entity.setOrder(order);
        }

        return entity;
    }
}