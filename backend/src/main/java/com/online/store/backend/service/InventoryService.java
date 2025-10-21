package com.online.store.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.store.backend.model.Inventory;
import com.online.store.backend.model.Product;
import com.online.store.backend.repository.ProductRepository;

@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    private final Inventory inventory = new Inventory();

    public void addProductToInventory(Product product, int quantity) {
        inventory.addProduct(product, quantity);
        productRepository.save(product);
    }

    public void restockProduct(String productId, int amount) {
        Optional<Product> productOpt = productRepository.findById(productId);
        productOpt.ifPresent(product -> {
            inventory.restockProduct(productId, amount);
            productRepository.save(product);
        });
    }

    public boolean reduceStock(String productId, int amount) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            boolean success = inventory.reduceStock(productId, amount);
            if (success) productRepository.save(productOpt.get());
            return success;
        }
        return false;
    }

    public Integer getStockLevel(String productId) {
        return inventory.getStockLevel(productId);
    }
}
