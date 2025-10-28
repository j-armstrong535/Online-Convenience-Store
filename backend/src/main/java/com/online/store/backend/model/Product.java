// package com.online.store.backend.model;

// import org.springframework.data.annotation.Id;
// import org.springframework.data.mongodb.core.mapping.Document;

// @Document("products")
// public class Product {

//     @Id
//     private String id;
//     private String name;
//     private String category;
//     private Double price;
//     private Integer stock; // now private to Product, not directly accessible externally
//     private Boolean productType;
//     private Boolean isFeatured;

//     public Product() {}

//     public Product(String id, String name, String category, Double price, Integer stock) {
//         this.id = id;
//         this.name = name;
//         this.category = category;
//         this.price = price;
//         this.stock = stock;
//     }

//     // Dynamic: return Cloudinary image URL based on product ID
//     public String getImageUrl() {
//         if (id == null || id.isEmpty()) return null;
//         return "https://res.cloudinary.com/dtglrc8my/image/upload/" + id + ".jpg";
//     }

//     // --- Getters/Setters (excluding stock) ---
//     public String getId() { return id; }
//     public void setId(String id) { this.id = id; }

//     public String getName() { return name; }
//     public void setName(String name) { this.name = name; }

//     public String getCategory() { return category; }
//     public void setCategory(String category) { this.category = category; }

//     public Double getPrice() { return price; }
//     public void setPrice(Double price) { this.price = price; }

//     public Boolean getProductType() { return productType; }
//     public void setProductType(Boolean productType) { this.productType = productType; }

//     public Boolean getIsFeatured() { return isFeatured; }
//     public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }

//     // Stock is now managed only by Inventory
//     Integer getStock() { return stock; }       // protected visibility
//     void setStock(Integer stock) { this.stock = stock; }

//     @Override
//     public String toString() {
//         return "Product{" +
//                 "id='" + id + '\'' +
//                 ", name='" + name + '\'' +
//                 ", category='" + category + '\'' +
//                 ", price=" + price +
//                 ", productType=" + productType +
//                 ", isFeatured=" + isFeatured +
//                 '}';
//     }
// }
package com.online.store.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("products")
public class Product {

    @Id
    private String id;
    private String name;
    private String category;
    private Double price;
    private Integer stock;
    private Boolean productType;
    private Boolean isFeatured;

    public Product() {}

    public Product(String id, String name, String category, Double price, Integer stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    // Cloudinary image URL
    public String getImageUrl() {
        if (id == null || id.isEmpty()) return null;
        return "https://res.cloudinary.com/dtglrc8my/image/upload/" + id + ".jpg";
    }

    // --- Getters/Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    void setStock(Integer stock) { this.stock = stock; }

    public Boolean getProductType() { return productType; }
    public void setProductType(Boolean productType) { this.productType = productType; }

    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", productType=" + productType +
                ", isFeatured=" + isFeatured +
                '}';
    }
}
