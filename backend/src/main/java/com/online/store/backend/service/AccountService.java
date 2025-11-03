package com.online.store.backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.online.store.backend.model.Account;
import com.online.store.backend.model.AccountFactory;
import com.online.store.backend.model.AccountType;
import com.online.store.backend.model.CustomerAccount;
import com.online.store.backend.model.Receipt;
import com.online.store.backend.model.StoreAccount;
import com.online.store.backend.repository.AccountRepository;

/**
 * Coordinates creation and persistence of account aggregates.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> findById(String accountId) {
        if (!StringUtils.hasText(accountId)) {
            return Optional.empty();
        }
        return accountRepository.findById(accountId);
    }

    public Optional<Account> findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return Optional.empty();
        }
        return accountRepository.findByEmail(email);
    }

    public Account saveAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        if (account instanceof CustomerAccount customerAccount) {
            customerAccount.storeCustomerProfile();
        }
        return accountRepository.save(account);
    }

    public CustomerAccount createCustomerAccount(String username, String email, String password,
            String firstName, String lastName, String shippingAddress) {
        CustomerAccount account = (CustomerAccount) AccountFactory.builder(AccountType.CUSTOMER, username, email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .shippingAddress(shippingAddress)
                .build();
        account.storeCustomerProfile();
        return accountRepository.save(account);
    }

    public StoreAccount createStoreManagerAccount(String username, String email, String password,
            String firstName, String lastName, String department) {
        StoreAccount account = (StoreAccount) AccountFactory.builder(AccountType.STORE_MANAGER, username, email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .department(department)
                .accessLevel("manager")
                .build();
        return accountRepository.save(account);
    }

    public StoreAccount createStoreAdminAccount(String username, String email, String password,
            String firstName, String lastName) {
        StoreAccount account = (StoreAccount) AccountFactory.builder(AccountType.STORE_ADMIN, username, email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .accessLevel("administrator")
                .build();
        return accountRepository.save(account);
    }

    public Account createAccountFromString(String accountType, String username, String email) {
        Account account = AccountFactory.createAccount(accountType, username, email);
        return saveAccount(account);
    }

    public void recordCartInteraction(String accountId) {
        findById(accountId).ifPresent(account -> {
            if (account instanceof CustomerAccount customer) {
                customer.manageShoppingCart();
                accountRepository.save(customer);
            }
        });
    }

    public void handleSuccessfulCheckout(Account account, Receipt receipt) {
        if (account instanceof CustomerAccount customer) {
            customer.initiateCheckout();
            customer.recordPurchase(receipt);
            accountRepository.save(customer);
        }
    }
}
