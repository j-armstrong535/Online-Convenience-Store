package com.online.store.backend.service;

import org.springframework.stereotype.Component;
import com.online.store.backend.model.Account;
import com.online.store.backend.model.AccountFactory;
import com.online.store.backend.model.AccountType;
import com.online.store.backend.model.CustomerAccount;
import com.online.store.backend.model.StoreAccount;

/**
 * Demo class showing various ways to use the AccountFactory
 * This demonstrates the flexibility and power of the Factory pattern
 */
@Component
public class AccountFactoryDemo {

    public void demonstrateFactoryUsage() {
        System.out.println("=== Account Factory Pattern Demo ===\n");

        // 1. Simple factory method usage
        System.out.println("1. Simple Factory Creation:");
        Account customer1 = AccountFactory.createAccount(AccountType.CUSTOMER, "john_doe", "john@email.com");
        Account manager1 = AccountFactory.createAccount(AccountType.STORE_MANAGER, "jane_manager", "jane@store.com");
        Account admin1 = AccountFactory.createAccount(AccountType.STORE_ADMIN, "admin_user", "admin@store.com");

        System.out.println(
                "Created customer: " + customer1.getUsername() + " (" + customer1.getClass().getSimpleName() + ")");
        System.out.println(
                "Created manager: " + manager1.getUsername() + " (" + manager1.getClass().getSimpleName() + ")");
        System.out.println("Created admin: " + admin1.getUsername() + " (" + admin1.getClass().getSimpleName() + ")\n");

        // 2. Factory with string types (useful for REST APIs)
        System.out.println("2. Factory with String Types:");
        Account customer2 = AccountFactory.createAccount("customer", "mary_customer", "mary@email.com");
        Account manager2 = AccountFactory.createAccount("store_manager", "bob_manager", "bob@store.com");

        System.out.println("Created from string 'customer': " + customer2.getUsername());
        System.out.println("Created from string 'store_manager': " + manager2.getUsername() + "\n");

        // 3. Builder pattern for complex objects
        System.out.println("3. Builder Pattern Usage:");
        Account complexCustomer = AccountFactory.builder(AccountType.CUSTOMER, "alice_smith", "alice@email.com")
                .password("securePassword123")
                .firstName("Alice")
                .lastName("Smith")
                .shippingAddress("123 Main St, City, State 12345")
                .paymentMethod("Visa ending in 1234")
                .build();

        CustomerAccount aliceCustomer = (CustomerAccount) complexCustomer;
        System.out
                .println("Built complex customer: " + aliceCustomer.getFirstName() + " " + aliceCustomer.getLastName());
        System.out.println("Shipping address: " + aliceCustomer.getShippingAddress());
        System.out.println("Payment method: " + aliceCustomer.getPaymentMethod() + "\n");

        // 4. Builder pattern for store accounts
        Account complexManager = AccountFactory.builder(AccountType.STORE_MANAGER, "david_mgr", "david@store.com")
                .password("managerPass456")
                .firstName("David")
                .lastName("Wilson")
                .department("Electronics")
                .accessLevel("senior_manager")
                .build();

        StoreAccount davidManager = (StoreAccount) complexManager;
        System.out.println("Built complex manager: " + davidManager.getFirstName() + " " + davidManager.getLastName());
        System.out.println("Department: " + davidManager.getDepartment());
        System.out.println("Role: " + davidManager.getRole());
        System.out.println("Access Level: " + davidManager.getAccessLevel());
        System.out.println("Has inventory access: " + davidManager.hasInventoryAccess() + "\n");

        // 5. Demonstrate polymorphic behavior
        System.out.println("4. Polymorphic Behavior:");
        Account[] accounts = { customer1, manager1, admin1, complexCustomer, complexManager };

        for (Account account : accounts) {
            System.out.println("Account: " + account.getUsername() + " (" + account.getClass().getSimpleName() + ")");

            if (account instanceof CustomerAccount) {
                System.out.println("  - Customer can manage shopping cart and view order history");
            } else if (account instanceof StoreAccount) {
                StoreAccount store = (StoreAccount) account;
                System.out.println("  - Store account role: " + store.getRole());
                System.out.println("  - Has inventory access: " + store.hasInventoryAccess());
                System.out.println("  - Has user management access: " + store.hasUserManagementAccess());
            }
        }

        System.out.println("\n=== Demo Complete ===");
    }
}