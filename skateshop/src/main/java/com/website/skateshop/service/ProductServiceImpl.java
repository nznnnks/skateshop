package com.website.skateshop.service;

import com.website.skateshop.entity.*;
import com.website.skateshop.model.ProductModel;
import com.website.skateshop.repository.ProductRepository;
import com.website.skateshop.repository.BrandRepository;
import com.website.skateshop.repository.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              BrandRepository brandRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
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
    public ProductModel addProduct(ProductModel productModel) {
        ProductEntity entity = convertToEntity(productModel);
        ProductEntity saved = productRepository.save(entity);
        return convertToModel(saved);
    }

    @Override
    public ProductModel updateProduct(ProductModel productModel) {
        ProductEntity entity = productRepository.findById(productModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        entity.setProductTitle(productModel.getProductTitle());
        entity.setPrice(productModel.getPrice());
        entity.setQuantity(productModel.getQuantity());

        if (!entity.getBrand().getId().equals(productModel.getBrandId())) {
            BrandEntity brand = brandRepository.findById(productModel.getBrandId())
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
            entity.setBrand(brand);
        }

        if (!entity.getCategory().getId().equals(productModel.getCategoryId())) {
            CategoryEntity category = categoryRepository.findById(productModel.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            entity.setCategory(category);
        }

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
        ProductModel model = new ProductModel();
        model.setId(entity.getId());
        model.setProductTitle(entity.getProductTitle());
        model.setPrice(entity.getPrice());
        model.setQuantity(entity.getQuantity());
        model.setBrandId(entity.getBrand().getId());
        model.setCategoryId(entity.getCategory().getId());
        return model;
    }

    private ProductEntity convertToEntity(ProductModel model) {
        ProductEntity entity = new ProductEntity();
        entity.setId(model.getId());
        entity.setProductTitle(model.getProductTitle());
        entity.setPrice(model.getPrice());
        entity.setQuantity(model.getQuantity());

        BrandEntity brand = brandRepository.findById(model.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
        entity.setBrand(brand);

        CategoryEntity category = categoryRepository.findById(model.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        entity.setCategory(category);

        return entity;
    }
}