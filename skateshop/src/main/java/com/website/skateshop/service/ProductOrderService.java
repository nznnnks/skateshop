package com.website.skateshop.service;

import com.website.skateshop.model.ProductOrderModel;
import java.util.List;

public interface ProductOrderService {
    List<ProductOrderModel> findAllProductOrders();
    ProductOrderModel findProductOrderById(Integer id);
    List<ProductOrderModel> findByOrderId(Integer orderId);
    List<ProductOrderModel> findByProductId(Integer productId);
    ProductOrderModel addProductOrder(ProductOrderModel productOrderModel);
    ProductOrderModel updateProductOrder(ProductOrderModel productOrderModel);
    void deleteProductOrder(Integer id);
    void deleteByOrderId(Integer orderId);
    List<ProductOrderModel> findProductOrdersPaginated(Integer page, Integer size);
    long countProductOrders();
}