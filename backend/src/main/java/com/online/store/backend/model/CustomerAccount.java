package com.online.store.backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("customer_accounts")
public class CustomerAccount extends Account {

    private String shippingAddress;
    private String paymentMethod;
    private LocalDateTime profileLastUpdated;
    private LocalDateTime lastCartInteraction;
    private int checkoutCount;
    private final List<String> receiptHistory = new ArrayList<>();

    public CustomerAccount() {
        super();
    }

    public CustomerAccount(String username, String email) {
        super();
        setUsername(username);
        setEmail(email);
    }

    /** Store customer profile (account information). */
    public void storeCustomerProfile() {
        this.profileLastUpdated = LocalDateTime.now();
    }

    /** Access and manage ShoppingCart contents. */
    public void manageShoppingCart() {
        this.lastCartInteraction = LocalDateTime.now();
    }

    /** View order history and receipts. */
    public List<String> viewOrderHistory() {
        return Collections.unmodifiableList(receiptHistory);
    }

    /** Initiate checkout and payment process. */
    public void initiateCheckout() {
        this.checkoutCount++;
    }

    /** Update or delete account information. */
    public void updateAccountInfo() {
        this.profileLastUpdated = LocalDateTime.now();
    }

    /** Can view product / category information. */
    public void viewProductInfo() {
        // Method kept for parity with CRC; additional behaviour can be added later.
    }

    public void recordPurchase(Receipt receipt) {
        if (receipt != null) {
            receiptHistory.add(receipt.getId());
        }
    }

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

    public LocalDateTime getProfileLastUpdated() {
        return profileLastUpdated;
    }

    public LocalDateTime getLastCartInteraction() {
        return lastCartInteraction;
    }

    public int getCheckoutCount() {
        return checkoutCount;
    }
}
