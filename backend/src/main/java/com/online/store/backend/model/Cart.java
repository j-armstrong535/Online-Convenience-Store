package com.online.store.backend.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("carts")
public class Cart {

    private static final double TAX_RATE = 0.1;
    private static final double DELIVERY_SURCHARGE = 7.5;

    @Id
    private String id;
    private String userId; // optional if you implement login
    private List<CartItem> items = new ArrayList<>();
    private FulfilmentMethod fulfilmentMethod = FulfilmentMethod.PICKUP;

    public Cart() {
    }

    public Cart(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public FulfilmentMethod getFulfilmentMethod() {
        return fulfilmentMethod;
    }

    public void setFulfilmentMethod(FulfilmentMethod fulfilmentMethod) {
        this.fulfilmentMethod = fulfilmentMethod;
    }

    public void addProduct(Product product) {
        if (product == null) {
            return;
        }
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        items.add(new CartItem(product, 1));
    }

    public void removeProduct(String productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public double getTotalPrice() {
        return getGrandTotal();
    }

    public double getSubtotal() {
        return round(items.stream().mapToDouble(CartItem::getTotalPrice).sum());
    }

    public double getTaxAmount() {
        return round(getSubtotal() * TAX_RATE);
    }

    public double getDeliverySurcharge() {
        return requiresDelivery() ? DELIVERY_SURCHARGE : 0.0;
    }

    public double getGrandTotal() {
        return round(getSubtotal() + getTaxAmount() + getDeliverySurcharge());
    }

    public boolean requiresDelivery() {
        return fulfilmentMethod == FulfilmentMethod.DELIVERY;
    }

    public void clear() {
        items.clear();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "userId='" + userId + '\'' +
                ", totalItems=" + items.size() +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
