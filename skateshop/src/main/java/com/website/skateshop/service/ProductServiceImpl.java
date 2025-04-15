package com.website.skateshop.service;

import com.website.skateshop.entity.ProductEntity;
import com.website.skateshop.model.ProductModel;
import com.website.skateshop.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductModel> findAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public ProductModel findProductById(int id) {
        return productRepository.findById(id)
                .map(this::convertToModel)
                .orElse(null);
    }

    @Override
    public List<ProductModel> findProductsByTitle(String title) {
        return productRepository.findByProductTitleContainingIgnoreCase(title).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public ProductModel addProduct(ProductModel product) {
        ProductEntity entity = convertToEntity(product);
        ProductEntity saved = productRepository.save(entity);
        return convertToModel(saved);
    }

    @Override
    public ProductModel updateProduct(ProductModel product) {
        ProductEntity entity = convertToEntity(product);
        ProductEntity updated = productRepository.save(entity);
        return convertToModel(updated);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductModel> findProductsPaginated(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size)).getContent().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public long countProducts() {
        return productRepository.count();
    }

    private ProductModel convertToModel(ProductEntity entity) {
        return new ProductModel(
                entity.getId(),
                entity.getProductTitle(),
                entity.getPrice(),
                entity.getQuantity()
        );
    }

    private ProductEntity convertToEntity(ProductModel model) {
        return new ProductEntity(
                model.getId(),
                model.getProductTitle(),
                model.getPrice(),
                model.getQuantity()
        );
    }
}