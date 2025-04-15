package com.website.skateshop.controller;

import com.website.skateshop.model.ProductModel;
import com.website.skateshop.service.ProductService;
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

    @GetMapping
    public String getAllProducts(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("products", productService.findProductsPaginated(page, size));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) productService.countProducts() / size));
        return "productList";
    }

    @GetMapping("/searchById")
    public String findProductById(@RequestParam int id, Model model) {
        ProductModel product = productService.findProductById(id);
        model.addAttribute("products", product != null ? List.of(product) : List.of());
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        return "productList";
    }

    @GetMapping("/searchByTitle")
    public String findProductsByTitle(@RequestParam String title, Model model,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        List<ProductModel> products = productService.findProductsByTitle(title);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) products.size() / size));
        return "productList";
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam String productTitle,
                             @RequestParam Integer price,
                             @RequestParam Integer quantity) {
        ProductModel newProduct = new ProductModel(0, productTitle, price, quantity);
        productService.addProduct(newProduct);
        return "redirect:/products";
    }

    @PostMapping("/update")
    public String updateProduct(@RequestParam int id,
                                @RequestParam String productTitle,
                                @RequestParam Integer price,
                                @RequestParam Integer quantity) {
        ProductModel updatedProduct = new ProductModel(id, productTitle, price, quantity);
        productService.updateProduct(updatedProduct);
        return "redirect:/products";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam int id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}