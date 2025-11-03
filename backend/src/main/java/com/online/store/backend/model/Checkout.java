package com.online.store.backend.model;

/**
 * Coordinates the checkout workflow in line with the CRC specification.
 * It relies on collaborators (Cart, Payment, Receipt) provided by the service
 * layer.
 */
public class Checkout {

    private final Cart cart;
    private Payment payment;
    private Receipt receipt;

    public Checkout(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Checkout requires a cart");
        }
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public double calculateSubtotal() {
        return cart.getSubtotal();
    }

    public double calculateTax() {
        return cart.getTaxAmount();
    }

    public double calculateDeliverySurcharge() {
        return cart.getDeliverySurcharge();
    }

    public double calculateTotal() {
        return cart.getGrandTotal();
    }

    public void registerPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        this.payment = payment;
    }

    public Receipt generateReceipt(Account customerAccount) {
        if (payment == null || !payment.isSuccessful()) {
            throw new IllegalStateException("Cannot generate receipt without a successful payment");
        }
        Receipt.Builder builder = Receipt.builder()
                .forCustomer(customerAccount)
                .withCart(cart)
                .withPayment(payment);
        this.receipt = builder.build();
        return receipt;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public Payment getPayment() {
        return payment;
    }

    /**
     * Clears the cart once the transaction is complete.
     */
    public void clearCart() {
        cart.clear();
    }
}
