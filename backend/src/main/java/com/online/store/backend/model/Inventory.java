package com.online.store.backend.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles product stock operations internally.
 */
public class Inventory {
    private final Map<String, Product> productStock = new HashMap<>();

    public void addProduct(Product product, int quantity) {
        product.setStock(quantity);
        productStock.put(product.getId(), product);
    }

    public void restockProduct(String productId, int amount) {
        Product p = productStock.get(productId);
        if (p != null && amount > 0) {
            p.setStock(p.getStock() + amount);
        }
    }

    public boolean reduceStock(String productId, int amount) {
        Product p = productStock.get(productId);
        if (p != null && p.getStock() >= amount) {
            p.setStock(p.getStock() - amount);
            return true;
        }
        return false;
    }

    public Integer getStockLevel(String productId) {
        Product p = productStock.get(productId);
        return (p != null) ? p.getStock() : null;
    }

    public Map<String, Product> getAllStock() {
        return productStock;
    }
}
