package com.website.skateshop.controller;

import com.website.skateshop.model.ProductModel;
import com.website.skateshop.service.ProductService;
import com.website.skateshop.service.BrandService;
import com.website.skateshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String getAllProducts(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        if (brandService.findAllBrands().isEmpty() || categoryService.findAllCategories().isEmpty()) {
            model.addAttribute("error", "Перед добавлением товаров необходимо создать хотя бы один бренд и одну категорию");
        }

        model.addAttribute("products", productService.findProductsPaginated(page, size));
        model.addAttribute("brands", brandService.findAllBrands());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) productService.countProducts() / size));
        return "productList";
    }

    @GetMapping("/searchById")
    public String findProductById(@RequestParam int id, Model model) {
        model.addAttribute("products", productService.findProductById(id) != null ?
                List.of(productService.findProductById(id)) : List.of());
        model.addAttribute("brands", brandService.findAllBrands());
        model.addAttribute("categories", categoryService.findAllCategories());
        return "productList";
    }

    @GetMapping("/searchByTitle")
    public String findProductsByTitle(@RequestParam String title, Model model) {
        model.addAttribute("products", productService.findProductsByTitle(title));
        model.addAttribute("brands", brandService.findAllBrands());
        model.addAttribute("categories", categoryService.findAllCategories());
        return "productList";
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam String productTitle,
                             @RequestParam Integer price,
                             @RequestParam Integer quantity,
                             @RequestParam Integer brandId,
                             @RequestParam Integer categoryId) {
        ProductModel newProduct = new ProductModel();
        newProduct.setProductTitle(productTitle);
        newProduct.setPrice(price);
        newProduct.setQuantity(quantity);
        newProduct.setBrandId(brandId);
        newProduct.setCategoryId(categoryId);

        productService.addProduct(newProduct);
        return "redirect:/products";
    }

    @PostMapping("/update")
    public String updateProduct(@RequestParam int id,
                                @RequestParam String productTitle,
                                @RequestParam Integer price,
                                @RequestParam Integer quantity,
                                @RequestParam Integer brandId,
                                @RequestParam Integer categoryId) {
        ProductModel updatedProduct = new ProductModel();
        updatedProduct.setId(id);
        updatedProduct.setProductTitle(productTitle);
        updatedProduct.setPrice(price);
        updatedProduct.setQuantity(quantity);
        updatedProduct.setBrandId(brandId);
        updatedProduct.setCategoryId(categoryId);

        productService.updateProduct(updatedProduct);
        return "redirect:/products";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam int id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}