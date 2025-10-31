package com.online.store.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("store_accounts")
public class StoreAccount extends Account {

    // Additional fields for StoreAccount
    private String role; // e.g., "manager", "administrator"
    private String accessLevel;
    private String department;

    public StoreAccount() {
        super();
    }

    public StoreAccount(String username, String email, String role) {
        super();
        setUsername(username);
        setEmail(email);
        this.role = role;
    }

    // StoreAccount specific methods based on CRC card responsibilities

    /**
     * View and update store inventory
     * Collaborators: Inventory
     */
    public void manageInventory() {
        // Implementation for managing store inventory
    }

    /**
     * Generate reports and sales summarise
     * Collaborators: SalesRecord
     */
    public void generateSalesReports() {
        // Implementation for generating sales reports
    }

    /**
     * Approve or reject customer returns
     * Collaborators: Receipt, SalesRecord
     */
    public void processCustomerReturns() {
        // Implementation for processing customer returns
    }

    /**
     * Manage staff and supplier accounts
     * Collaborators: Account, Supplier
     */
    public void manageStaffAndSuppliers() {
        // Implementation for managing staff and supplier accounts
    }

    /**
     * Modify product categories and prices
     * Collaborators: Product, ProductCategory
     */
    public void modifyProductsAndCategories() {
        // Implementation for modifying products and categories
    }

    // Administrative methods for store management
    public boolean hasInventoryAccess() {
        return "manager".equals(role) || "administrator".equals(role);
    }

    public boolean hasReportAccess() {
        return "manager".equals(role) || "administrator".equals(role);
    }

    public boolean hasUserManagementAccess() {
        return "administrator".equals(role);
    }

    // Getters and Setters for StoreAccount specific fields
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}