package com.online.store.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("carts")
public class Cart {

    @Id
    private String id;
    private String userId; // optional if you implement login
    private List<CartItem> items = new ArrayList<>();

    public Cart() {}

    public Cart(String userId) {
        this.userId = userId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public void addProduct(Product product) {
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
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "userId='" + userId + '\'' +
                ", totalItems=" + items.size() +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }
}
