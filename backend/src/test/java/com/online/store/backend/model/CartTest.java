package com.online.store.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CartTest {

    @Test
    void addProduct_incrementsQuantityForDuplicateItems() {
        Cart cart = new Cart("user");
        Product product = product("SKU-1", 5.0);

        cart.addProduct(product);
        cart.addProduct(product);

        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
        assertEquals(10.0, cart.getSubtotal());
    }

    @Test
    void removeProduct_removesMatchingItem() {
        Cart cart = new Cart("user");
        Product a = product("A", 3.0);
        Product b = product("B", 4.0);
        cart.addProduct(a);
        cart.addProduct(b);

        cart.removeProduct("A");

        assertEquals(1, cart.getItems().size());
        assertEquals("B", cart.getItems().get(0).getProduct().getId());
    }

    @Test
    void totals_includeDeliverySurchargeWhenApplicable() {
        Cart cart = new Cart("user");
        cart.setFulfilmentMethod(FulfilmentMethod.DELIVERY);
        cart.addProduct(product("SKU-2", 12.5));

        assertEquals(12.5, cart.getSubtotal());
        assertEquals(1.25, cart.getTaxAmount());
        assertEquals(7.5, cart.getDeliverySurcharge());
        assertEquals(21.25, cart.getGrandTotal());
    }

    @Test
    void requiresDelivery_reflectsFulfilmentMethod() {
        Cart cart = new Cart("user");
        assertFalse(cart.requiresDelivery());

        cart.setFulfilmentMethod(FulfilmentMethod.DELIVERY);
        assertTrue(cart.requiresDelivery());
    }

    @Test
    void clear_removesAllItems() {
        Cart cart = new Cart("user");
        cart.addProduct(product("SKU-1", 2.0));
        cart.addProduct(product("SKU-2", 3.0));

        cart.clear();

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
