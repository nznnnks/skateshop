package com.website.skateshop.service;

import com.website.skateshop.model.ProductModel;
import java.util.List;

public interface ProductService {
    List<ProductModel> findAllProducts();
    ProductModel findProductById(int id);
    List<ProductModel> findProductsByTitle(String title);
    ProductModel addProduct(ProductModel product);
    ProductModel updateProduct(ProductModel product);
    void deleteProduct(int id);
    List<ProductModel> findProductsPaginated(int page, int size);
    long countProducts();
}