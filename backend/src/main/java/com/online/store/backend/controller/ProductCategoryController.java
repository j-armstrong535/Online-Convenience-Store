package com.online.store.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.store.backend.model.ProductCategory;
import com.online.store.backend.service.ProductCategoryService;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class ProductCategoryController {

    private final ProductCategoryService categoryService;

    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Accessible by all users
    @GetMapping
    public List<ProductCategory> getAll() {
        return categoryService.findAll();
    }

    // Only accessed by Admin
    @PostMapping
    public ProductCategory create(@RequestBody ProductCategory category) {
        return categoryService.createCategory(category);
    }

    // Only accessed by Admin
    @PutMapping
    public ProductCategory update(@RequestBody ProductCategory category) {
        return categoryService.updateCategory(category);
    }

    // Only accessed by Admin
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        categoryService.deleteCategory(id);
    }
}
