package com.online.store.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.store.backend.model.Product;
import com.online.store.backend.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;

    // Accessible by all users
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    // Only accessed by Admin
    public Product addProduct(Product p) {
        return repo.save(p);
    }

    // Only accessed by Admin
    public void deleteProduct(String id) {
        repo.deleteById(id);
    }

    // Only accessed by Admin
    public Product updateProduct(Product p) {
        return repo.save(p);
    }
}
