package com.online.store.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CheckoutTest {

    @Test
    void constructor_throwsWhenCartNull() {
        assertThrows(IllegalArgumentException.class, () -> new Checkout(null));
    }

    @Test
    void calculateMethods_delegateToCart() {
        Cart cart = new Cart("user");
        cart.addProduct(product("SKU-1", 10.0));
        cart.setFulfilmentMethod(FulfilmentMethod.DELIVERY);

        Checkout checkout = new Checkout(cart);

        assertEquals(cart.getSubtotal(), checkout.calculateSubtotal());
        assertEquals(cart.getTaxAmount(), checkout.calculateTax());
        assertEquals(cart.getDeliverySurcharge(), checkout.calculateDeliverySurcharge());
        assertEquals(cart.getGrandTotal(), checkout.calculateTotal());
    }

    @Test
    void registerPayment_throwsWhenNull() {
        Cart cart = new Cart("user");
        Checkout checkout = new Checkout(cart);

        assertThrows(IllegalArgumentException.class, () -> checkout.registerPayment(null));
    }

    @Test
    void generateReceipt_requiresSuccessfulPayment() {
        Cart cart = new Cart("user");
        cart.addProduct(product("SKU-2", 8.0));
        Checkout checkout = new Checkout(cart);

        Payment payment = new Payment(PaymentMethod.CARD, cart.getGrandTotal());
        checkout.registerPayment(payment);
        assertThrows(IllegalStateException.class, () -> checkout.generateReceipt(null));

        payment.markSuccessful("C-1");
        Receipt receipt = checkout.generateReceipt(null);

        assertNotNull(receipt);
        assertEquals(cart.getItems().size(), receipt.getItems().size());
        assertEquals(payment, checkout.getPayment());
        assertEquals(receipt, checkout.getReceipt());
    }

    @Test
    void clearCart_removesLineItems() {
        Cart cart = new Cart("user");
        cart.addProduct(product("SKU-3", 6.0));
        Checkout checkout = new Checkout(cart);

        checkout.clearCart();

        assertTrue(cart.getItems().isEmpty());
    }

    private Product product(String id, double price) {
        Product product = new Product();
        product.setId(id);
        product.setName("Product " + id);
        product.setPrice(price);
        return product;
    }
}
