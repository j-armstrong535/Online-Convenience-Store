package com.online.store.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.online.store.backend.model.Cart;
import com.online.store.backend.model.FulfilmentMethod;
import com.online.store.backend.model.Product;
import com.online.store.backend.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private AccountService accountService;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService(cartRepository, accountService);
    }

    @Test
    void getCartForUser_createsNewCartWhenAbsent() {
        when(cartRepository.findByUserId("bob")).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.getCartForUser("bob");

        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository).save(cartCaptor.capture());
        assertEquals("bob", cartCaptor.getValue().getUserId());
        assertEquals("bob", result.getUserId());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void addToCart_guestDoesNotRecordInteraction() {
        Cart cart = new Cart("guest");
        Product product = new Product();
        product.setId("P1");
        product.setName("Milk");
        product.setPrice(2.50);

        when(cartRepository.findByUserId("guest")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.addToCart(product);

        assertEquals(1, result.getItems().size());
        assertEquals(1, result.getItems().get(0).getQuantity());
        verify(accountService, never()).recordCartInteraction(anyString());
    }

    @Test
    void addToCart_namedUserRecordsInteraction() {
        Cart cart = new Cart("alice");
        Product product = new Product();
        product.setId("SKU-1");
        product.setName("Bread");
        product.setPrice(3.00);

        when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.addToCart(product, "alice");

        verify(accountService).recordCartInteraction("alice");
    }

    @Test
    void removeFromCart_updatesCartAndRecordsForUser() {
        Cart cart = new Cart("alice");
        Product product = new Product();
        product.setId("SKU-2");
        product.setName("Coffee");
        product.setPrice(10.0);
        cart.addProduct(product);

        when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart updated = cartService.removeFromCart("SKU-2", "alice");

        assertTrue(updated.getItems().isEmpty());
        verify(accountService).recordCartInteraction("alice");
    }

    @Test
    void updateFulfilmentMethod_setsMethodAndRecords() {
        Cart cart = new Cart("alice");
        when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart updated = cartService.updateFulfilmentMethod("alice", FulfilmentMethod.DELIVERY);

        assertSame(FulfilmentMethod.DELIVERY, updated.getFulfilmentMethod());
        verify(accountService).recordCartInteraction("alice");
    }

    @Test
    void clearCart_guestSkipsInteraction() {
        Cart cart = new Cart("guest");
        cart.addProduct(product("SKU-1"));
        when(cartRepository.findByUserId("guest")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.clearCart();

        assertTrue(cart.getItems().isEmpty());
        verify(accountService, never()).recordCartInteraction(anyString());
    }

    @Test
    void clearCart_namedUserRecordsInteraction() {
        Cart cart = new Cart("alice");
        cart.addProduct(product("SKU-1"));
        when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.clearCart("alice");

        assertTrue(cart.getItems().isEmpty());
        verify(accountService).recordCartInteraction("alice");
    }

    @Test
    void addToCart_existingProductIncrementsQuantity() {
        Cart cart = new Cart("guest");
        Product product = product("SKU-9");
        cart.addProduct(product);

        when(cartRepository.findByUserId("guest")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart updated = cartService.addToCart(product);

        assertEquals(1, updated.getItems().size());
        assertEquals(2, updated.getItems().get(0).getQuantity());
        verify(accountService, never()).recordCartInteraction(anyString());
    }

    @Test
    void removeFromCart_guestDoesNotRecordInteraction() {
        Cart cart = new Cart("guest");
        cart.addProduct(product("SKU-10"));
        when(cartRepository.findByUserId("guest")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.removeFromCart("SKU-10");

        verify(accountService, never()).recordCartInteraction(anyString());
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void getCart_defaultsToGuestWhenUserIdMissing() {
        Cart cart = new Cart("guest");
        when(cartRepository.findByUserId("guest")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.getCartForUser("");

        assertEquals("guest", result.getUserId());
        verify(cartRepository, never()).save(any(Cart.class));
        verify(accountService, never()).recordCartInteraction(anyString());
    }

    private Product product(String id) {
        Product product = new Product();
        product.setId(id);
        product.setName("Product " + id);
        product.setPrice(5.0);
        return product;
    }
}
