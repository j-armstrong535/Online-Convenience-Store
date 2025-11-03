package com.online.store.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.online.store.backend.model.ProductCategory;
import com.online.store.backend.repository.ProductCategoryRepository;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository repository;
    private final InventoryService inventoryService;

    public ProductCategoryService(ProductCategoryRepository repository, InventoryService inventoryService) {
        this.repository = repository;
        this.inventoryService = inventoryService;
    }

    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    public ProductCategory createCategory(ProductCategory category) {
        ProductCategory saved = repository.save(category);
        inventoryService.registerCategory(saved);
        return saved;
    }

    public ProductCategory updateCategory(ProductCategory category) {
        ProductCategory saved = repository.save(category);
        inventoryService.registerCategory(saved);
        return saved;
    }

    public void deleteCategory(String id) {
        repository.deleteById(id);
    }

    public ProductCategory findByName(String name) {
        return repository.findByNameIgnoreCase(name);
    }
}
