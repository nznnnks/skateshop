package com.website.skateshop.service;

import com.website.skateshop.model.CategoryModel;

import java.util.List;

public interface CategoryService {
    List<CategoryModel> findAllCategories();
    CategoryModel findCategoryById(int id);
    List<CategoryModel> findCategoriesByName(String name);
    CategoryModel addCategory(CategoryModel category);
    CategoryModel updateCategory(CategoryModel category);
    void deleteCategory(int id);

    List<CategoryModel> findCategoriesPaginated(int page, int size);
    long countCategories();
}