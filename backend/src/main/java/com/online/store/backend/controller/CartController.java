package com.online.store.backend.controller;

import com.online.store.backend.model.Cart;
import com.online.store.backend.model.Product;
import com.online.store.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public Cart getCart() {
        return cartService.getCart();
    }

    @PostMapping("/add")
    public Cart addToCart(@RequestBody Product product) {
        return cartService.addToCart(product);
    }

    @DeleteMapping("/remove/{productId}")
    public Cart removeFromCart(@PathVariable String productId) {
        return cartService.removeFromCart(productId);
    }

    @DeleteMapping("/clear")
    public void clearCart() {
        cartService.clearCart();
    }
}
