package com.website.skateshop.service;

import com.website.skateshop.model.BrandModel;

import java.util.List;

public interface BrandService {
    List<BrandModel> findAllBrands();
    BrandModel findBrandById(int id);
    List<BrandModel> findBrandsByName(String name);
    BrandModel addBrand(BrandModel brand);
    BrandModel updateBrand(BrandModel brand);
    void deleteBrand(int id);

    List<BrandModel> findBrandsPaginated(int page, int size);
    long countBrands();
}