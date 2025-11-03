package com.online.store.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Domain object representing a logical grouping of products.
 * Aligns with the ProductCategory CRC card in the object design document.
 */
@Document("product_categories")
public class ProductCategory {

    @Id
    private String id;
    private String name;
    private String description;
    private boolean perishable;

    public ProductCategory() {
    }

    public ProductCategory(String name, String description, boolean perishable) {
        this.name = name;
        this.description = description;
        this.perishable = perishable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPerishable() {
        return perishable;
    }

    public void setPerishable(boolean perishable) {
        this.perishable = perishable;
    }

    /**
     * Indicates whether the supplied product belongs to this category.
     */
    public boolean handles(Product product) {
        if (product == null || product.getCategory() == null) {
            return false;
        }
        return product.getCategory().equalsIgnoreCase(name);
    }
}
