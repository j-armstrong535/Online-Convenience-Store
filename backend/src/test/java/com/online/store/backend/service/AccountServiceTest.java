package com.online.store.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.online.store.backend.model.Account;
import com.online.store.backend.model.CustomerAccount;
import com.online.store.backend.model.Receipt;
import com.online.store.backend.model.StoreAccount;
import com.online.store.backend.repository.AccountRepository;
import com.online.store.backend.repository.CustomerAccountRepository;
import com.online.store.backend.repository.StoreAccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerAccountRepository customerAccountRepository;

    @Mock
    private StoreAccountRepository storeAccountRepository;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepository, customerAccountRepository, storeAccountRepository);
    }

    @Test
    void findById_returnsEmptyWhenIdBlank() {
        assertTrue(accountService.findById(null).isEmpty());
        assertTrue(accountService.findById("").isEmpty());
        verify(accountRepository, never()).findById(any());
    }

    @Test
    void findById_usesRepositoryWhenIdPresent() {
        CustomerAccount account = new CustomerAccount("user", "user@example.com");
        when(accountRepository.findById("123")).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.findById("123");

        assertTrue(result.isPresent());
        assertEquals(account, result.get());
    }

    @Test
    void findByEmail_returnsEmptyWhenEmailBlank() {
        assertTrue(accountService.findByEmail(null).isEmpty());
        assertTrue(accountService.findByEmail(" ").isEmpty());
        verify(accountRepository, never()).findByEmail(any());
    }

    @Test
    void findByEmail_returnsFromBaseRepositoryFirst() {
        StoreAccount account = new StoreAccount("manager", "manager@example.com", "manager");
        when(accountRepository.findByEmail("manager@example.com")).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.findByEmail("manager@example.com");

        assertTrue(result.isPresent());
        assertEquals(account, result.get());
        verify(customerAccountRepository, never()).findByEmail(any());
        verify(storeAccountRepository, never()).findByEmail(any());
    }

    @Test
    void findByEmail_checksCustomerThenStore() {
        CustomerAccount customer = new CustomerAccount("cust", "cust@example.com");
        when(accountRepository.findByEmail("cust@example.com")).thenReturn(Optional.empty());
        when(customerAccountRepository.findByEmail("cust@example.com")).thenReturn(Optional.of(customer));

        Optional<Account> result = accountService.findByEmail("cust@example.com");

        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
    }

    @Test
    void findByEmail_checksStoreWhenCustomerMissing() {
        StoreAccount storeAccount = new StoreAccount("store", "store@example.com", "manager");
        when(accountRepository.findByEmail("store@example.com")).thenReturn(Optional.empty());
        when(customerAccountRepository.findByEmail("store@example.com")).thenReturn(Optional.empty());
        when(storeAccountRepository.findByEmail("store@example.com")).thenReturn(Optional.of(storeAccount));

        Optional<Account> result = accountService.findByEmail("store@example.com");

        assertTrue(result.isPresent());
        assertEquals(storeAccount, result.get());
    }

    @Test
    void saveAccount_throwsWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> accountService.saveAccount(null));
    }

    @Test
    void saveAccount_routesCustomerAccounts() {
        CustomerAccount customer = new CustomerAccount("cust", "cust@example.com");
        when(customerAccountRepository.save(customer)).thenReturn(customer);

        Account saved = accountService.saveAccount(customer);

        assertEquals(customer, saved);
        verify(customerAccountRepository).save(customer);
        verify(storeAccountRepository, never()).save(any());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void saveAccount_routesStoreAccounts() {
        StoreAccount storeAccount = new StoreAccount("mgr", "mgr@example.com", "manager");
        when(storeAccountRepository.save(storeAccount)).thenReturn(storeAccount);

        Account saved = accountService.saveAccount(storeAccount);

        assertEquals(storeAccount, saved);
        verify(storeAccountRepository).save(storeAccount);
        verify(customerAccountRepository, never()).save(any());
    }

    @Test
    void saveAccount_routesGenericAccounts() {
        Account account = new Account() {};
        when(accountRepository.save(account)).thenReturn(account);

        Account saved = accountService.saveAccount(account);

        assertEquals(account, saved);
        verify(accountRepository).save(account);
    }

    @Test
    void createCustomerAccount_populatesAndPersists() {
        when(customerAccountRepository.save(any(CustomerAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerAccount account = accountService.createCustomerAccount(
                "cust",
                "cust@example.com",
                "secret",
                "Pat",
                "Jones",
                "123 Main St");

        assertEquals("cust@example.com", account.getEmail());
        assertEquals("Pat", account.getFirstName());
        assertEquals("Jones", account.getLastName());
        assertEquals("123 Main St", account.getShippingAddress());
        assertNotNull(account.getProfileLastUpdated());
        verify(customerAccountRepository).save(account);
    }

    @Test
    void createStoreManagerAccount_populatesDepartment() {
        when(storeAccountRepository.save(any(StoreAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StoreAccount account = accountService.createStoreManagerAccount(
                "manager",
                "manager@example.com",
                "pw",
                "Jamie",
                "Lee",
                "Produce");

        assertEquals("Produce", account.getDepartment());
        assertEquals("manager", account.getAccessLevel());
        verify(storeAccountRepository).save(account);
    }

    @Test
    void createStoreAdminAccount_setsAccessLevel() {
        when(storeAccountRepository.save(any(StoreAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StoreAccount account = accountService.createStoreAdminAccount(
                "admin",
                "admin@example.com",
                "pw",
                "Alex",
                "Garcia");

        assertEquals("administrator", account.getAccessLevel());
        verify(storeAccountRepository).save(account);
    }

    @Test
    void createAccountFromString_delegatesToSaveAccount() {
        when(customerAccountRepository.save(any(CustomerAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account account = accountService.createAccountFromString("customer", "user", "user@example.com");

        assertTrue(account instanceof CustomerAccount);
        verify(customerAccountRepository).save(any(CustomerAccount.class));
    }

    @Test
    void recordCartInteraction_updatesCustomerAccounts() {
        CustomerAccount customer = new CustomerAccount("cust", "cust@example.com");
        when(accountRepository.findById("cust-id")).thenReturn(Optional.of(customer));
        when(customerAccountRepository.save(customer)).thenReturn(customer);

        accountService.recordCartInteraction("cust-id");

        assertNotNull(customer.getLastCartInteraction());
        verify(customerAccountRepository).save(customer);
    }

    @Test
    void recordCartInteraction_ignoresNonCustomers() {
        StoreAccount storeAccount = new StoreAccount("mgr", "mgr@example.com", "manager");
        when(accountRepository.findById("store-id")).thenReturn(Optional.of(storeAccount));

        accountService.recordCartInteraction("store-id");

        verify(customerAccountRepository, never()).save(any());
    }

    @Test
    void handleSuccessfulCheckout_updatesCustomerState() {
        CustomerAccount customer = new CustomerAccount("cust", "cust@example.com");
        Receipt receipt = Receipt.builder()
                .withId("RCT-1")
                .withCart(TestData.cartWithItem())
                .withPayment(TestData.successfulPayment())
                .build();
        when(customerAccountRepository.save(customer)).thenReturn(customer);

        accountService.handleSuccessfulCheckout(customer, receipt);

        assertEquals(1, customer.getCheckoutCount());
        assertTrue(customer.viewOrderHistory().contains("RCT-1"));
        verify(customerAccountRepository).save(customer);
    }

    @Test
    void handleSuccessfulCheckout_ignoresNonCustomers() {
        StoreAccount storeAccount = new StoreAccount("mgr", "mgr@example.com", "manager");
        accountService.handleSuccessfulCheckout(storeAccount, null);
        verify(customerAccountRepository, never()).save(any());
    }

    private static class TestData {
        static com.online.store.backend.model.Cart cartWithItem() {
            com.online.store.backend.model.Cart cart = new com.online.store.backend.model.Cart("cust");
            com.online.store.backend.model.Product product = new com.online.store.backend.model.Product();
            product.setId("SKU-1");
            product.setName("Product");
            product.setPrice(10.0);
            cart.addProduct(product);
            return cart;
        }

        static com.online.store.backend.model.Payment successfulPayment() {
            com.online.store.backend.model.Payment payment = new com.online.store.backend.model.Payment(
            com.online.store.backend.model.PaymentMethod.CARD,
                    10.0);
            payment.markSuccessful("C-12345");
            return payment;
        }
    }
}
