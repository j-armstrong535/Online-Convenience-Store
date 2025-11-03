package com.online.store.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.store.backend.model.Account;
import com.online.store.backend.model.Cart;
import com.online.store.backend.model.Checkout;
import com.online.store.backend.model.FulfilmentMethod;
import com.online.store.backend.model.Payment;
import com.online.store.backend.model.PaymentMethod;
import com.online.store.backend.model.Receipt;

@Service
public class CheckoutService {

    private final CartService cartService;
    private final PaymentService paymentService;
    private final ReceiptService receiptService;
    private final SalesRecordService salesRecordService;
    private final InventoryService inventoryService;
    private final AccountService accountService;

    public CheckoutService(
            CartService cartService,
            PaymentService paymentService,
            ReceiptService receiptService,
            SalesRecordService salesRecordService,
            InventoryService inventoryService,
            AccountService accountService) {
        this.cartService = cartService;
        this.paymentService = paymentService;
        this.receiptService = receiptService;
        this.salesRecordService = salesRecordService;
        this.inventoryService = inventoryService;
        this.accountService = accountService;
    }

    /**
     * Performs the full checkout workflow and returns the issued receipt and payment record.
     */
    @Transactional
    public Result checkout(Account customerAccount, PaymentMethod paymentMethod,
            FulfilmentMethod fulfilmentMethod, String cartUserId) {
        String cartOwnerId = (cartUserId != null && !cartUserId.isBlank())
                ? cartUserId
                : (customerAccount != null ? customerAccount.getId() : null);
        Cart cart = cartService.getCartForUser(cartOwnerId);
        cart.setFulfilmentMethod(fulfilmentMethod);
        cartService.save(cart);

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot complete checkout on an empty cart");
        }

        Checkout checkout = new Checkout(cart);
        Payment payment = paymentService.processPayment(paymentMethod, checkout.calculateTotal());
        checkout.registerPayment(payment);

        Receipt receipt = receiptService.issueReceipt(customerAccount, cart, payment);
        salesRecordService.recordSale(receipt);

        accountService.handleSuccessfulCheckout(customerAccount, receipt);

        inventoryService.applyOrder(cart);
        checkout.clearCart();
        cartService.clearCart(cartOwnerId);

        return new Result(receipt, payment);
    }

    public static class Result {
        private final Receipt receipt;
        private final Payment payment;

        public Result(Receipt receipt, Payment payment) {
            this.receipt = receipt;
            this.payment = payment;
        }

        public Receipt getReceipt() {
            return receipt;
        }

        public Payment getPayment() {
            return payment;
        }
    }
}
