package com.online.store.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("products")
public class Product {
    @Id
    private String id;
    private String name;
    private String category;
    private double price;
    private int stock;
    private Boolean productType;
    private Boolean isFeatured;

    public Product() {
    }

    public Product(String id, String name, String category, double price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    // Getters and setters

    // Dynamic: return Cloudinary image URL based on product ID
    public String getImageUrl() {
        if (id == null || id.isEmpty())
            return null;
        return "https://res.cloudinary.com/dtglrc8my/image/upload/" + id + ".jpg";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
