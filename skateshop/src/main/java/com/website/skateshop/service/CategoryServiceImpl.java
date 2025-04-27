package com.website.skateshop.service;

import com.website.skateshop.entity.CategoryEntity;
import com.website.skateshop.model.CategoryModel;
import com.website.skateshop.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryModel> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryModel findCategoryById(int id) {
        return categoryRepository.findById(id)
                .map(this::convertToModel)
                .orElse(null);
    }

    @Override
    public List<CategoryModel> findCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryModel addCategory(CategoryModel category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setName(category.getName());
        CategoryEntity saved = categoryRepository.save(entity);
        return convertToModel(saved);
    }

    @Override
    public CategoryModel updateCategory(CategoryModel category) {
        CategoryEntity entity = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        entity.setName(category.getName());
        CategoryEntity updated = categoryRepository.save(entity);
        return convertToModel(updated);
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryModel> findCategoriesPaginated(int page, int size) {
        Page<CategoryEntity> result = categoryRepository.findAll(PageRequest.of(page, size));
        return result.getContent().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public long countCategories() {
        return categoryRepository.count();
    }

    private CategoryModel convertToModel(CategoryEntity entity) {
        CategoryModel model = new CategoryModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }

    private CategoryEntity convertToEntity(CategoryModel model) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        return entity;
    }
}