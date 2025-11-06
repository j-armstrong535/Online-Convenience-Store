package com.online.store.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CustomerAccountTest {

    @Test
    void recordPurchase_addsReceiptIdToHistory() {
        CustomerAccount account = new CustomerAccount("user", "user@example.com");
        Receipt receipt = new Receipt();
        receipt.setId("RCT-1");

        account.recordPurchase(receipt);

        assertTrue(account.viewOrderHistory().contains("RCT-1"));
    }

    @Test
    void manageShoppingCart_updatesInteractionTimestamp() {
        CustomerAccount account = new CustomerAccount("user", "user@example.com");

        account.manageShoppingCart();

        assertNotNull(account.getLastCartInteraction());
    }

    @Test
    void initiateCheckout_incrementsCount() {
        CustomerAccount account = new CustomerAccount("user", "user@example.com");

        account.initiateCheckout();
        account.initiateCheckout();

        assertEquals(2, account.getCheckoutCount());
    }

    @Test
    void storeCustomerProfile_updatesTimestamp() {
        CustomerAccount account = new CustomerAccount("user", "user@example.com");

        account.storeCustomerProfile();

        assertNotNull(account.getProfileLastUpdated());
    }
}
