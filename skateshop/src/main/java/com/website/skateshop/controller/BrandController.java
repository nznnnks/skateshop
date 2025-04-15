package com.website.skateshop.controller;

import com.website.skateshop.model.BrandModel;
import com.website.skateshop.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public String getAllBrands(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("brands", brandService.findBrandsPaginated(page, size));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) brandService.countBrands() / size));
        return "brandList";
    }

    @GetMapping("/searchById")
    public String findBrandById(@RequestParam int id, Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        BrandModel brand = brandService.findBrandById(id);
        model.addAttribute("brands", brand != null ? List.of(brand) : List.of());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", 1);
        return "brandList";
    }

    @GetMapping("/searchByName")
    public String findBrandsByName(@RequestParam String name, Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        List<BrandModel> brands = brandService.findBrandsByName(name);
        model.addAttribute("brands", brands);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) brands.size() / size));
        return "brandList";
    }

    @PostMapping("/add")
    public String addBrand(@RequestParam String name) {
        BrandModel newBrand = new BrandModel(0, name);
        brandService.addBrand(newBrand);
        return "redirect:/brands";
    }

    @PostMapping("/update")
    public String updateBrand(@RequestParam int id, @RequestParam String name) {
        BrandModel updatedBrand = new BrandModel(id, name);
        brandService.updateBrand(updatedBrand);
        return "redirect:/brands";
    }

    @PostMapping("/delete")
    public String deleteBrand(@RequestParam int id) {
        brandService.deleteBrand(id);
        return "redirect:/brands";
    }
}