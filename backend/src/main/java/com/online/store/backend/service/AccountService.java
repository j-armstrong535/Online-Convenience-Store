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
import com.online.store.backend.repository.CustomerAccountRepository;
import com.online.store.backend.repository.StoreAccountRepository;

/**
 * Coordinates creation and persistence of account aggregates.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerAccountRepository customerAccountRepository;
    private final StoreAccountRepository storeAccountRepository;

    public AccountService(
            AccountRepository accountRepository,
            CustomerAccountRepository customerAccountRepository,
            StoreAccountRepository storeAccountRepository) {
        this.accountRepository = accountRepository;
        this.customerAccountRepository = customerAccountRepository;
        this.storeAccountRepository = storeAccountRepository;
    }

    /** Find account by ID **/
    public Optional<Account> findById(String accountId) {
        if (!StringUtils.hasText(accountId)) {
            return Optional.empty();
        }
        return accountRepository.findById(accountId);
    }

    /** ✅ Updated method: finds by email in all three collections **/
    public Optional<Account> findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return Optional.empty();
        }

        // Try the base account collection first (if exists)
        Optional<Account> base = accountRepository.findByEmail(email);
        if (base.isPresent()) return base;

        // Then check customer_accounts collection
        Optional<CustomerAccount> customer = customerAccountRepository.findByEmail(email);
        if (customer.isPresent()) return Optional.of(customer.get());

        // Finally check store_accounts collection
        Optional<StoreAccount> store = storeAccountRepository.findByEmail(email);
        if (store.isPresent()) return Optional.of(store.get());

        // Nothing found
        return Optional.empty();
    }

    /** Save any account **/
    public Account saveAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        if (account instanceof CustomerAccount customerAccount) {;
            return customerAccountRepository.save(customerAccount);
        } else if (account instanceof StoreAccount storeAccount) {
            return storeAccountRepository.save(storeAccount);
        } else {
            return accountRepository.save(account);
        }
    }

    /** Create Customer Account **/
    public CustomerAccount createCustomerAccount(String username, String email, String password,
                                                 String firstName, String lastName, String shippingAddress) {
        CustomerAccount account = (CustomerAccount) AccountFactory.builder(AccountType.CUSTOMER, username, email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .shippingAddress(shippingAddress)
                .build();

        account.storeCustomerProfile();
        return customerAccountRepository.save(account);
    }

    /** Create Store Manager Account **/
    public StoreAccount createStoreManagerAccount(String username, String email, String password,
                                                  String firstName, String lastName, String department) {
        StoreAccount account = (StoreAccount) AccountFactory.builder(AccountType.STORE_MANAGER, username, email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .department(department)
                .accessLevel("manager")
                .build();

        return storeAccountRepository.save(account);
    }

    /** Create Store Admin Account **/
    public StoreAccount createStoreAdminAccount(String username, String email, String password,
                                                String firstName, String lastName) {
        StoreAccount account = (StoreAccount) AccountFactory.builder(AccountType.STORE_ADMIN, username, email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .accessLevel("administrator")
                .build();

        return storeAccountRepository.save(account);
    }

    /** Create account from string type **/
    public Account createAccountFromString(String accountType, String username, String email) {
        Account account = AccountFactory.createAccount(accountType, username, email);
        return saveAccount(account);
    }

    /** Record customer cart interaction **/
    public void recordCartInteraction(String accountId) {
        findById(accountId).ifPresent(account -> {
            if (account instanceof CustomerAccount customer) {
                customer.manageShoppingCart();
                customerAccountRepository.save(customer);
            }
        });
    }

    /** Handle checkout **/
    public void handleSuccessfulCheckout(Account account, Receipt receipt) {
        if (account instanceof CustomerAccount customer) {
            customer.initiateCheckout();
            customer.recordPurchase(receipt);
            customerAccountRepository.save(customer);
        }
    }
}
