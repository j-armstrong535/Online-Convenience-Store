package com.online.store.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CartItemTest {

    @Test
    void getTotalPrice_multipliesQuantityAndPrice() {
        Product product = new Product();
        product.setId("SKU-1");
        product.setName("Item");
        product.setPrice(4.5);

        CartItem item = new CartItem(product, 3);

        assertEquals(13.5, item.getTotalPrice());
    }
}
