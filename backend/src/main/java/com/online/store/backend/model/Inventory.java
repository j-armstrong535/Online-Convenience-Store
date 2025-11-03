package com.online.store.backend.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Singleton inventory aggregate, maintaining product stock grouped by
 * categories.
 * Implements the Singleton and Observer patterns.
 */
public class Inventory {

    private static final Inventory INSTANCE = new Inventory();

    private final Map<String, Product> productStock = new ConcurrentHashMap<>();
    private final Map<String, ProductCategory> categoryIndex = new ConcurrentHashMap<>();
    private final List<InventoryObserver> observers = new CopyOnWriteArrayList<>();

    private Inventory() {
    }

    public static Inventory getInstance() {
        return INSTANCE;
    }

    public void registerCategory(ProductCategory category) {
        if (category != null && category.getName() != null) {
            categoryIndex.put(category.getName().toLowerCase(), category);
        }
    }

    public void addProduct(Product product, int quantity) {
        if (product == null || product.getId() == null) {
            return;
        }
        ProductCategory category = categoryIndex.getOrDefault(
                product.getCategory() != null ? product.getCategory().toLowerCase() : "",
                null);
        if (category != null) {
            product.setCategory(category.getName());
        }
        product.setStock(quantity);
        productStock.put(product.getId(), product);
        notifyObservers(product.getId(), quantity, quantity);
    }

    public void restockProduct(String productId, int amount) {
        Product product = productStock.get(productId);
        if (product != null && amount > 0) {
            int newQty = product.getStock() + amount;
            product.setStock(newQty);
            notifyObservers(productId, amount, newQty);
        }
    }

    public boolean reduceStock(String productId, int amount) {
        Product product = productStock.get(productId);
        if (product != null && amount > 0 && product.getStock() >= amount) {
            int newQty = product.getStock() - amount;
            product.setStock(newQty);
            notifyObservers(productId, -amount, newQty);
            return true;
        }
        return false;
    }

    public Integer getStockLevel(String productId) {
        Product product = productStock.get(productId);
        return product != null ? product.getStock() : null;
    }

    public Map<String, Product> getAllStock() {
        return Collections.unmodifiableMap(productStock);
    }

    public void registerObserver(InventoryObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void unregisterObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String productId, int delta, int newQuantity) {
        InventoryEvent event = new InventoryEvent(productId, delta, newQuantity);
        observers.forEach(observer -> observer.onInventoryChanged(event));
    }
}
