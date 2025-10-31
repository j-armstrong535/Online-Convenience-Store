package com.online.store.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("customer_accounts")
public class CustomerAccount extends Account {

    // Additional fields for CustomerAccount
    private String shippingAddress;
    private String paymentMethod;
    private String orderHistory;

    public CustomerAccount() {
        super();
    }

    public CustomerAccount(String username, String email) {
        super();
        setUsername(username);
        setEmail(email);
    }

    // CustomerAccount specific methods based on CRC card responsibilities

    /**
     * Store customer profile (account information)
     * Collaborators: Database
     */
    public void storeCustomerProfile() {
        // Implementation for storing customer profile information
    }

    /**
     * Access and manage ShoppingCart contents
     * Collaborators: ShoppingCart
     */
    public void manageShoppingCart() {
        // Implementation for managing shopping cart
    }

    /**
     * View order history and receipts
     * Collaborators: Receipt, SalesRecord
     */
    public void viewOrderHistory() {
        // Implementation for viewing order history
    }

    /**
     * Initiate checkout and payment process
     * Collaborators: Checkout, Payment
     */
    public void initiateCheckout() {
        // Implementation for checkout process
    }

    /**
     * Update or delete account information
     * Collaborators: Database
     */
    public void updateAccountInfo() {
        // Implementation for updating account information
    }

    /**
     * Can view product / category information
     * Collaborators: Inventory
     */
    public void viewProductInfo() {
        // Implementation for viewing product information
    }

    // Getters and Setters for CustomerAccount specific fields
    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(String orderHistory) {
        this.orderHistory = orderHistory;
    }
}