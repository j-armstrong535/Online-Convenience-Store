package com.online.store.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("store_accounts")
public class StoreAccount extends Account {

    private String role; // e.g., "manager", "administrator"
    private String accessLevel;
    private String department;
    private int inventoryUpdates;
    private int reportsGenerated;
    private int returnsProcessed;
    private int staffActions;

    public StoreAccount() {
        super();
    }

    public StoreAccount(String username, String email, String role) {
        super();
        setUsername(username);
        setEmail(email);
        this.role = role;
    }

    /** View and update store inventory. */
    public void manageInventory() {
        inventoryUpdates++;
    }

    /** Generate reports and sales summarise. */
    public void generateSalesReports() {
        reportsGenerated++;
    }

    /** Approve or reject customer returns. */
    public void processCustomerReturns() {
        returnsProcessed++;
    }

    /** Manage staff and supplier accounts. */
    public void manageStaffAndSuppliers() {
        staffActions++;
    }

    /** Modify product categories and prices. */
    public void modifyProductsAndCategories() {
        inventoryUpdates++;
    }

    public boolean hasInventoryAccess() {
        return "manager".equals(role) || "administrator".equals(role);
    }

    public boolean hasReportAccess() {
        return "manager".equals(role) || "administrator".equals(role);
    }

    public boolean hasUserManagementAccess() {
        return "administrator".equals(role);
    }

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

    public int getInventoryUpdates() {
        return inventoryUpdates;
    }

    public int getReportsGenerated() {
        return reportsGenerated;
    }

    public int getReturnsProcessed() {
        return returnsProcessed;
    }

    public int getStaffActions() {
        return staffActions;
    }
}
