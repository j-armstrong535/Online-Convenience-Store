package com.online.store.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ReceiptBuilderTest {

    @Test
    void builder_createsReceiptWithDerivedTotals() {
        Cart cart = new Cart("cust");
        cart.setFulfilmentMethod(FulfilmentMethod.DELIVERY);
        cart.addProduct(product("SKU-1", "Tea", 12.0));
        cart.addProduct(product("SKU-2", "Coffee", 8.0));

        Payment payment = new Payment(PaymentMethod.CARD, cart.getGrandTotal());
        payment.markSuccessful("C-123");

        CustomerAccount customer = new CustomerAccount("cust", "cust@example.com");
        customer.setId("cust-1");

        Receipt receipt = Receipt.builder()
                .withId("RCT-1")
                .forCustomer(customer)
                .withCart(cart)
                .withPayment(payment)
                .build();

        assertEquals("cust-1", receipt.getCustomerAccountId());
        assertEquals(2, receipt.getItems().size());
        assertEquals(20.0, receipt.getSubtotal());
        assertEquals(2.0, receipt.getTax());
        assertEquals(7.5, receipt.getDeliverySurcharge());
        assertEquals(29.5, receipt.getTotalCost());
        assertEquals(PaymentMethod.CARD, receipt.getPaymentMethod());
        assertNotNull(receipt.getIssuedAt());
    }

    @Test
    void builder_throwsWhenCartNull() {
        assertThrows(IllegalArgumentException.class, () -> Receipt.builder().withCart(null));
    }

    @Test
    void builder_throwsWhenPaymentNull() {
        Cart cart = new Cart("cust");
        cart.addProduct(product("SKU-1", "Tea", 10.0));
        assertThrows(IllegalArgumentException.class, () -> Receipt.builder().withCart(cart).withPayment(null));
    }

    @Test
    void builder_throwsWhenPaymentNotSuccessful() {
        Cart cart = new Cart("cust");
        cart.addProduct(product("SKU-1", "Tea", 10.0));
        Payment payment = new Payment(PaymentMethod.CARD, 10.0);

        assertThrows(IllegalStateException.class, () -> Receipt.builder()
                .withCart(cart)
                .withPayment(payment));
    }

    @Test
    void build_throwsWhenCartHasNoItems() {
        Cart cart = new Cart("cust");
        Payment payment = new Payment(PaymentMethod.CARD, 0.0);
        payment.markSuccessful("C-1");

        assertThrows(IllegalStateException.class, () -> Receipt.builder()
                .withCart(cart)
                .withPayment(payment)
                .build());
    }

    private Product product(String id, String name, double price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
