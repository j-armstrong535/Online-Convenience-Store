package com.online.store.backend.service;

import org.springframework.stereotype.Service;
import com.online.store.backend.model.Account;
import com.online.store.backend.model.AccountFactory;
import com.online.store.backend.model.AccountType;
import com.online.store.backend.model.CustomerAccount;
import com.online.store.backend.model.StoreAccount;

/**
 * Service class demonstrating the use of AccountFactory
 * This shows how the Factory pattern simplifies account creation
 */
@Service
public class AccountService {

    /**
     * Creates a customer account using the factory pattern
     */
    public Account createCustomerAccount(String username, String email, String password,
            String firstName, String lastName, String shippingAddress) {
        return AccountFactory.builder(AccountType.CUSTOMER, username, email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .shippingAddress(shippingAddress)
                .build();
    }

    /**
     * Creates a store manager account using the factory pattern
     */
    public Account createStoreManagerAccount(String username, String email, String password,
            String firstName, String lastName, String department) {
        return AccountFactory.builder(AccountType.STORE_MANAGER, username, email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .department(department)
                .accessLevel("manager")
                .build();
    }

    /**
     * Creates a store admin account using the factory pattern
     */
    public Account createStoreAdminAccount(String username, String email, String password,
            String firstName, String lastName) {
        return AccountFactory.builder(AccountType.STORE_ADMIN, username, email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .accessLevel("administrator")
                .build();
    }

    /**
     * Creates an account from string type (useful for REST endpoints)
     */
    public Account createAccountFromString(String accountType, String username, String email) {
        return AccountFactory.createAccount(accountType, username, email);
    }

    /**
     * Example method showing type-safe operations after account creation
     */
    public void performAccountSpecificOperations(Account account) {
        if (account instanceof CustomerAccount) {
            CustomerAccount customer = (CustomerAccount) account;
            customer.manageShoppingCart();
            customer.viewOrderHistory();
        } else if (account instanceof StoreAccount) {
            StoreAccount storeAccount = (StoreAccount) account;
            if (storeAccount.hasInventoryAccess()) {
                storeAccount.manageInventory();
            }
            if (storeAccount.hasReportAccess()) {
                storeAccount.generateSalesReports();
            }
        }
    }
}