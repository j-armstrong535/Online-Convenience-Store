package com.online.store.backend.service;

import org.springframework.stereotype.Service;

import com.online.store.backend.model.Cart;
import com.online.store.backend.model.FulfilmentMethod;
import com.online.store.backend.model.Product;
import com.online.store.backend.repository.CartRepository;

@Service
public class CartService {

    private static final String DEFAULT_USER_ID = "guest";

    private final CartRepository cartRepository;
    private final AccountService accountService;

    public CartService(CartRepository cartRepository, AccountService accountService) {
        this.cartRepository = cartRepository;
        this.accountService = accountService;
    }

    public Cart getCart() {
        return getCartForUser(DEFAULT_USER_ID);
    }

    public Cart getCartForUser(String userId) {
        String resolvedUserId = resolveUserId(userId);
        return cartRepository.findByUserId(resolvedUserId)
                .orElseGet(() -> cartRepository.save(new Cart(resolvedUserId)));
    }

    public Cart addToCart(Product product) {
        return addToCart(product, DEFAULT_USER_ID);
    }

    public Cart addToCart(Product product, String userId) {
        Cart cart = getCartForUser(userId);
        cart.addProduct(product);
        Cart saved = cartRepository.save(cart);
        recordCartInteraction(userId);
        return saved;
    }

    public Cart removeFromCart(String productId) {
        return removeFromCart(productId, DEFAULT_USER_ID);
    }

    public Cart removeFromCart(String productId, String userId) {
        Cart cart = getCartForUser(userId);
        cart.removeProduct(productId);
        Cart saved = cartRepository.save(cart);
        recordCartInteraction(userId);
        return saved;
    }

    public Cart updateFulfilmentMethod(String userId, FulfilmentMethod method) {
        Cart cart = getCartForUser(userId);
        cart.setFulfilmentMethod(method);
        Cart saved = cartRepository.save(cart);
        recordCartInteraction(userId);
        return saved;
    }

    public void clearCart() {
        clearCart(DEFAULT_USER_ID);
    }

    public void clearCart(String userId) {
        Cart cart = getCartForUser(userId);
        cart.clear();
        cartRepository.save(cart);
        recordCartInteraction(userId);
    }

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    private void recordCartInteraction(String userId) {
        String resolvedUserId = resolveUserId(userId);
        if (!DEFAULT_USER_ID.equals(resolvedUserId)) {
            accountService.recordCartInteraction(resolvedUserId);
        }
    }

    private String resolveUserId(String userId) {
        return (userId == null || userId.isEmpty()) ? DEFAULT_USER_ID : userId;
    }
}
