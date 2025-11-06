package com.online.store.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.online.store.backend.model.Account;
import com.online.store.backend.model.Cart;
import com.online.store.backend.model.CustomerAccount;
import com.online.store.backend.model.FulfilmentMethod;
import com.online.store.backend.model.Payment;
import com.online.store.backend.model.PaymentMethod;
import com.online.store.backend.model.Product;
import com.online.store.backend.model.Receipt;
import com.online.store.backend.service.CheckoutService.Result;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private CartService cartService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private ReceiptService receiptService;
    @Mock
    private SalesRecordService salesRecordService;
    @Mock
    private InventoryService inventoryService;
    @Mock
    private AccountService accountService;

    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        checkoutService = new CheckoutService(
                cartService,
                paymentService,
                receiptService,
                salesRecordService,
                inventoryService,
                accountService);
    }

    @Test
    void checkout_runsFullWorkflow() {
        CustomerAccount account = new CustomerAccount("cust", "cust@example.com");
        account.setId("customer-1");

        Cart cart = new Cart("customer-1");
        cart.addProduct(product("SKU-1", 10.0));
        cart.addProduct(product("SKU-1", 10.0));
        cart.setFulfilmentMethod(FulfilmentMethod.DELIVERY);
        double amountWithDelivery = cart.getGrandTotal();
        cart.setFulfilmentMethod(FulfilmentMethod.PICKUP);
        when(cartService.getCartForUser("customer-1")).thenReturn(cart);
        when(cartService.save(cart)).thenReturn(cart);

        Payment payment = new Payment(PaymentMethod.CARD, amountWithDelivery);
        payment.markSuccessful("C-123");
        when(paymentService.processPayment(PaymentMethod.CARD, amountWithDelivery)).thenReturn(payment);

        Receipt receipt = Receipt.builder()
                .withId("RCT-1")
                .forCustomer(account)
                .withCart(cart)
                .withPayment(payment)
                .build();
        when(receiptService.issueReceipt(account, cart, payment)).thenReturn(receipt);
        when(salesRecordService.recordSale(receipt)).thenReturn(new com.online.store.backend.model.SalesRecord("RCT-1"));

        Result result = checkoutService.checkout(
                account,
                PaymentMethod.CARD,
                FulfilmentMethod.DELIVERY,
                "");

        assertNotNull(result);
        assertEquals(receipt, result.getReceipt());
        assertEquals(payment, result.getPayment());
        assertEquals(FulfilmentMethod.DELIVERY, cart.getFulfilmentMethod());
        assertTrue(cart.getItems().isEmpty());

        verify(cartService).getCartForUser("customer-1");
        verify(cartService).save(cart);
        verify(paymentService).processPayment(PaymentMethod.CARD, amountWithDelivery);
        verify(receiptService).issueReceipt(account, cart, payment);
        verify(salesRecordService).recordSale(receipt);
        verify(accountService).handleSuccessfulCheckout(account, receipt);
        verify(inventoryService).applyOrder(cart);
        verify(cartService).clearCart("customer-1");
    }

    @Test
    void checkout_throwsWhenCartEmpty() {
        Cart emptyCart = new Cart("customer-2");
        when(cartService.getCartForUser("customer-2")).thenReturn(emptyCart);
        when(cartService.save(emptyCart)).thenReturn(emptyCart);

        Account account = new CustomerAccount("cust2", "cust2@example.com");
        account.setId("customer-2");

        assertThrows(IllegalStateException.class, () -> checkoutService.checkout(
                account,
                PaymentMethod.CARD,
                FulfilmentMethod.PICKUP,
                "customer-2"));
    }

    @Test
    void checkout_usesCartUserIdWhenProvided() {
        Cart cart = new Cart("guest");
        cart.addProduct(product("SKU-2", 5.0));
        when(cartService.getCartForUser("guest")).thenReturn(cart);
        when(cartService.save(cart)).thenReturn(cart);

        Payment payment = new Payment(PaymentMethod.PAYPAL, cart.getGrandTotal());
        payment.markSuccessful("P-1");
        when(paymentService.processPayment(PaymentMethod.PAYPAL, cart.getGrandTotal())).thenReturn(payment);

        Receipt receipt = Receipt.builder()
                .withId("RCT-2")
                .forCustomer(null)
                .withCart(cart)
                .withPayment(payment)
                .build();
        when(receiptService.issueReceipt(null, cart, payment)).thenReturn(receipt);
        when(salesRecordService.recordSale(receipt)).thenReturn(new com.online.store.backend.model.SalesRecord("RCT-2"));

        Result result = checkoutService.checkout(
                null,
                PaymentMethod.PAYPAL,
                FulfilmentMethod.PICKUP,
                "guest");

        assertEquals(receipt, result.getReceipt());
        verify(cartService).getCartForUser("guest");
        verify(cartService).clearCart("guest");
        verify(accountService).handleSuccessfulCheckout(null, receipt);
    }

    private Product product(String id, double price) {
        Product product = new Product();
        product.setId(id);
        product.setName("Product " + id);
        product.setPrice(price);
        return product;
    }
}
