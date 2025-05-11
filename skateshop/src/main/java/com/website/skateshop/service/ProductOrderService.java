package com.website.skateshop.service;

import com.website.skateshop.model.ProductOrderModel;
import java.util.List;

public interface ProductOrderService {
    List<ProductOrderModel> findAllProductOrders();
    ProductOrderModel findProductOrderById(int id);
    List<ProductOrderModel> findProductOrdersByOrderId(int orderId);
    List<ProductOrderModel> findProductOrdersByProductId(int productId);
    ProductOrderModel addProductOrder(ProductOrderModel productOrder);
    ProductOrderModel updateProductOrder(ProductOrderModel productOrder);
    void deleteProductOrder(int id);
    List<ProductOrderModel> findProductOrdersPaginated(int page, int size);
    long countProductOrders();
}