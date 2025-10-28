package com.online.store.backend.service;

import com.online.store.backend.model.Cart;
import com.online.store.backend.model.Product;
import com.online.store.backend.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // Temporary: single "guest" user
    private final String USER_ID = "guest";

    public Cart getCart() {
        Cart cart = cartRepository.findByUserId(USER_ID);
        if (cart == null) {
            cart = new Cart(USER_ID);
            cartRepository.save(cart);
        }
        return cart;
    }

    public Cart addToCart(Product product) {
        Cart cart = getCart();
        cart.addProduct(product);
        return cartRepository.save(cart);
    }

    public Cart removeFromCart(String productId) {
        Cart cart = getCart();
        cart.removeProduct(productId);
        return cartRepository.save(cart);
    }

    public void clearCart() {
        Cart cart = getCart();
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
