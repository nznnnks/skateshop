package com.website.skateshop.controller;

import com.website.skateshop.model.CategoryModel;
import com.website.skateshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String getAllCategories(Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("categories", categoryService.findCategoriesPaginated(page, size));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) categoryService.countCategories() / size));
        return "categoryList";
    }

    @GetMapping("/searchById")
    public String findCategoryById(@RequestParam int id, Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        CategoryModel category = categoryService.findCategoryById(id);
        model.addAttribute("categories", category != null ? List.of(category) : List.of());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", 1);
        return "categoryList";
    }

    @GetMapping("/searchByName")
    public String findCategoriesByName(@RequestParam String name, Model model,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        List<CategoryModel> categories = categoryService.findCategoriesByName(name);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) categories.size() / size));
        return "categoryList";
    }

    @PostMapping("/add")
    public String addCategory(@RequestParam String name) {
        CategoryModel newCategory = new CategoryModel(0, name);
        categoryService.addCategory(newCategory);
        return "redirect:/categories";
    }

    @PostMapping("/update")
    public String updateCategory(@RequestParam int id, @RequestParam String name) {
        CategoryModel updatedCategory = new CategoryModel(id, name);
        categoryService.updateCategory(updatedCategory);
        return "redirect:/categories";
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam int id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}