package com.website.skateshop.service;

import com.website.skateshop.entity.BrandEntity;
import com.website.skateshop.model.BrandModel;
import com.website.skateshop.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<BrandModel> findAllBrands() {
        return brandRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public BrandModel findBrandById(int id) {
        return brandRepository.findById(id)
                .map(this::convertToModel)
                .orElse(null);
    }

    @Override
    public List<BrandModel> findBrandsByName(String name) {
        return brandRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public BrandModel addBrand(BrandModel brand) {
        if (brandRepository.existsByName(brand.getName())) {
            throw new IllegalArgumentException("Brand with this name already exists");
        }
        BrandEntity entity = new BrandEntity();
        entity.setName(brand.getName());
        BrandEntity saved = brandRepository.save(entity);
        return convertToModel(saved);
    }

    @Override
    public BrandModel updateBrand(BrandModel brand) {
        BrandEntity entity = brandRepository.findById(brand.getId())
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
        entity.setName(brand.getName());
        BrandEntity updated = brandRepository.save(entity);
        return convertToModel(updated);
    }

    @Override
    public void deleteBrand(int id) {
        brandRepository.deleteById(id);
    }

    @Override
    public List<BrandModel> findBrandsPaginated(int page, int size) {
        Page<BrandEntity> result = brandRepository.findAll(PageRequest.of(page, size));
        return result.getContent().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public long countBrands() {
        return brandRepository.count();
    }

    private BrandModel convertToModel(BrandEntity entity) {
        BrandModel model = new BrandModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }

    private BrandEntity convertToEntity(BrandModel model) {
        BrandEntity entity = new BrandEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        return entity;
    }
}