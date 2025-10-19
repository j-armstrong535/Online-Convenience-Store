package com.online.store.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.store.backend.model.Product;
import com.online.store.backend.repository.IProductRepository;

@Service
public class ProductService {
    @Autowired
    private IProductRepository repo;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product addProduct(Product p) {
        return repo.save(p);
    }

    public void deleteProduct(String id) {
        repo.deleteById(id);
    }

    public Product updateProduct(Product p) {
        return repo.save(p);
    }
}
