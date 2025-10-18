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

    public List<Product> getAllProducts() { return repo.findAll(); }

    public Product addProduct(Product p) { return repo.save(p); }

    public void deleteProduct(String id) { repo.deleteById(id); }

    public Product updateProduct(Product p) { return repo.save(p); }
}
