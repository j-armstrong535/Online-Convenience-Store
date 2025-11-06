package com.online.store.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.online.store.backend.model.Account;
import com.online.store.backend.model.Cart;
import com.online.store.backend.model.Payment;
import com.online.store.backend.model.PaymentMethod;
import com.online.store.backend.model.Receipt;
import com.online.store.backend.model.StoreAccount;
import com.online.store.backend.repository.ReceiptRepository;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    private ReceiptService receiptService;

    @BeforeEach
    void setUp() {
        receiptService = new ReceiptService(receiptRepository);
    }

    @Test
    void issueReceipt_buildsReceiptFromCartAndPayment() {
        Cart cart = cartWithItem();
        Account account = new StoreAccount("store", "store@example.com", "manager");
        account.setId("acct-1");
        Payment payment = new Payment(PaymentMethod.CARD, 10.0);
        payment.markSuccessful("C-123");

        when(receiptRepository.save(any(Receipt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Receipt receipt = receiptService.issueReceipt(account, cart, payment);

        assertEquals("acct-1", receipt.getCustomerAccountId());
        assertEquals(1, receipt.getItems().size());
        assertEquals(10.0, round(receipt.getSubtotal()));
        assertEquals(1.0, round(receipt.getTax()));
        assertEquals(0.0, round(receipt.getDeliverySurcharge()));
        assertEquals(11.0, round(receipt.getTotalCost()));
        assertEquals(PaymentMethod.CARD, receipt.getPaymentMethod());
        assertEquals("C-123", receipt.getPaymentReference());
        assertNotNull(receipt.getIssuedAt());
        verify(receiptRepository).save(receipt);
    }

    @Test
    void getReceiptsForCustomer_delegatesToRepository() {
        receiptService.getReceiptsForCustomer("cust-1");
        verify(receiptRepository).findByCustomerAccountIdOrderByIssuedAtDesc("cust-1");
    }

    @Test
    void findById_returnsReceiptWhenPresent() {
        Receipt receipt = new Receipt();
        receipt.setId("RCT-1");
        when(receiptRepository.findById("RCT-1")).thenReturn(Optional.of(receipt));

        Receipt found = receiptService.findById("RCT-1");

        assertEquals(receipt, found);
    }

    @Test
    void findById_throwsWhenMissing() {
        when(receiptRepository.findById("missing")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> receiptService.findById("missing"));
    }

    @Test
    void getAllReceipts_delegatesToRepository() {
        List<Receipt> receipts = List.of(new Receipt());
        when(receiptRepository.findAll()).thenReturn(receipts);

        List<Receipt> result = receiptService.getAllReceipts();

        assertEquals(receipts, result);
    }

    private Cart cartWithItem() {
        Cart cart = new Cart("user");
        com.online.store.backend.model.Product product = new com.online.store.backend.model.Product();
        product.setId("SKU-1");
        product.setName("Item");
        product.setPrice(10.0);
        cart.addProduct(product);
        return cart;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
