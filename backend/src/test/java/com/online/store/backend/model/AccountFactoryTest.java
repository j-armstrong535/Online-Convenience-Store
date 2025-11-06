package com.online.store.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AccountFactoryTest {

    @Test
    void createAccount_returnsCorrectSubclass() {
        Account customer = AccountFactory.createAccount(AccountType.CUSTOMER, "cust", "cust@example.com");
        Account manager = AccountFactory.createAccount(AccountType.STORE_MANAGER, "mgr", "mgr@example.com");
        Account admin = AccountFactory.createAccount(AccountType.STORE_ADMIN, "adm", "adm@example.com");

        assertTrue(customer instanceof CustomerAccount);
        assertTrue(manager instanceof StoreAccount);
        assertTrue(admin instanceof StoreAccount);
        assertEquals("manager", ((StoreAccount) manager).getRole());
        assertEquals("administrator", ((StoreAccount) admin).getRole());
    }

    @Test
    void createAccount_throwsForUnsupportedType() {
        assertThrows(IllegalArgumentException.class,
                () -> AccountFactory.createAccount((AccountType) null, "user", "user@example.com"));
    }

    @Test
    void builder_setsStoreSpecificFields() {
        StoreAccount account = (StoreAccount) AccountFactory.builder(AccountType.STORE_MANAGER, "mgr", "mgr@example.com")
                .firstName("Jamie")
                .lastName("Lee")
                .department("Produce")
                .accessLevel("manager")
                .build();

        assertEquals("Jamie", account.getFirstName());
        assertEquals("Lee", account.getLastName());
        assertEquals("Produce", account.getDepartment());
        assertEquals("manager", account.getAccessLevel());
    }

    @Test
    void builder_setsCustomerSpecificFields() {
        CustomerAccount account = (CustomerAccount) AccountFactory.builder(AccountType.CUSTOMER, "cust", "cust@example.com")
                .firstName("Pat")
                .lastName("Jones")
                .shippingAddress("123 Main St")
                .paymentMethod("CREDIT_CARD")
                .build();

        assertEquals("Pat", account.getFirstName());
        assertEquals("Jones", account.getLastName());
        assertEquals("123 Main St", account.getShippingAddress());
        assertEquals("CREDIT_CARD", account.getPaymentMethod());
    }

    @Test
    void builder_acceptsStringType() {
        Account account = AccountFactory.builder("store_admin", "adm", "adm@example.com")
                .build();

        assertTrue(account instanceof StoreAccount);
        assertEquals("administrator", ((StoreAccount) account).getRole());
    }
}
