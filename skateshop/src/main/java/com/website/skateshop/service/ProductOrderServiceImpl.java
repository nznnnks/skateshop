package com.website.skateshop.service;

import com.website.skateshop.entity.ProductEntity;
import com.website.skateshop.entity.ProductOrderEntity;
import com.website.skateshop.entity.OrderEntity;
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
    public ProductOrderModel findProductOrderById(Integer id) {
        return productOrderRepository.findById(id)
                .map(this::convertToModel)
                .orElse(null);
    }

    @Override
    public List<ProductOrderModel> findByOrderId(Integer orderId) {
        return productOrderRepository.findByOrderId(orderId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductOrderModel> findByProductId(Integer productId) {
        return productOrderRepository.findByProductId(productId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public ProductOrderModel addProductOrder(ProductOrderModel productOrderModel) {
        ProductOrderEntity entity = convertToEntity(productOrderModel);
        ProductOrderEntity savedEntity = productOrderRepository.save(entity);
        return convertToModel(savedEntity);
    }

    @Override
    public ProductOrderModel updateProductOrder(ProductOrderModel productOrderModel) {
        ProductOrderEntity entity = productOrderRepository.findById(productOrderModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product order not found"));

        entity.setQuantity(productOrderModel.getQuantity());

        ProductOrderEntity updatedEntity = productOrderRepository.save(entity);
        return convertToModel(updatedEntity);
    }

    @Override
    public void deleteProductOrder(Integer id) {
        productOrderRepository.deleteById(id);
    }

    @Override
    public void deleteByOrderId(Integer orderId) {
        productOrderRepository.deleteByOrderId(orderId);
    }

    @Override
    public List<ProductOrderModel> findProductOrdersPaginated(Integer page, Integer size) {
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
        model.setProductId(entity.getProduct().getId());
        model.setProductTitle(entity.getProduct().getProductTitle());
        model.setOrderId(entity.getOrder().getId());
        model.setQuantity(entity.getQuantity());
        return model;
    }

    private ProductOrderEntity convertToEntity(ProductOrderModel model) {
        ProductOrderEntity entity = new ProductOrderEntity();
        entity.setId(model.getId());

        ProductEntity product = productRepository.findById(model.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        entity.setProduct(product);

        OrderEntity order = orderRepository.findById(model.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        entity.setOrder(order);

        entity.setQuantity(model.getQuantity());
        return entity;
    }
}