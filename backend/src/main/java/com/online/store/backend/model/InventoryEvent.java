package com.online.store.backend.model;

/**
 * Event payload describing a change in inventory.
 */
public class InventoryEvent {

    private final String productId;
    private final int delta;
    private final int newQuantity;

    public InventoryEvent(String productId, int delta, int newQuantity) {
        this.productId = productId;
        this.delta = delta;
        this.newQuantity = newQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getDelta() {
        return delta;
    }

    public int getNewQuantity() {
        return newQuantity;
    }
}
