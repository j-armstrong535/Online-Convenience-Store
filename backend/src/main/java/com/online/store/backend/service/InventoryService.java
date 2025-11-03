package com.online.store.backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.online.store.backend.model.Cart;
import com.online.store.backend.model.CartItem;
import com.online.store.backend.model.Inventory;
import com.online.store.backend.model.InventoryObserver;
import com.online.store.backend.model.Product;
import com.online.store.backend.model.ProductCategory;
import com.online.store.backend.repository.ProductRepository;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final Inventory inventory = Inventory.getInstance();

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void registerObserver(InventoryObserver observer) {
        inventory.registerObserver(observer);
    }

    public void registerCategory(ProductCategory category) {
        inventory.registerCategory(category);
    }

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
            if (success) {
                productRepository.save(productOpt.get());
            }
            return success;
        }
        return false;
    }

    public void applyOrder(Cart cart) {
        for (CartItem item : cart.getItems()) {
            reduceStock(item.getProduct().getId(), item.getQuantity());
        }
    }

    public Integer getStockLevel(String productId) {
        return inventory.getStockLevel(productId);
    }
}
