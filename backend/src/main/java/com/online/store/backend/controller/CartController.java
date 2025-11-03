package com.online.store.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.online.store.backend.model.Cart;
import com.online.store.backend.model.FulfilmentMethod;
import com.online.store.backend.model.Product;
import com.online.store.backend.service.CartService;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Cart getCart(@RequestParam(value = "userId", required = false) String userId) {
        return cartService.getCartForUser(userId);
    }

    @PostMapping("/add")
    public Cart addToCart(@RequestBody Product product,
                          @RequestParam(value = "userId", required = false) String userId) {
        if (product == null || !StringUtils.hasText(product.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product with valid id is required");
        }
        return cartService.addToCart(product, userId);
    }

    @DeleteMapping("/remove/{productId}")
    public Cart removeFromCart(@PathVariable String productId,
                               @RequestParam(value = "userId", required = false) String userId) {
        if (!StringUtils.hasText(productId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "productId is required");
        }
        return cartService.removeFromCart(productId, userId);
    }

    @PutMapping("/fulfilment")
    public Cart updateFulfilment(@RequestParam FulfilmentMethod method,
                                 @RequestParam(value = "userId", required = false) String userId) {
        if (method == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fulfilment method is required");
        }
        return cartService.updateFulfilmentMethod(userId, method);
    }

    @DeleteMapping("/clear")
    public void clearCart(@RequestParam(value = "userId", required = false) String userId) {
        cartService.clearCart(userId);
    }
}
