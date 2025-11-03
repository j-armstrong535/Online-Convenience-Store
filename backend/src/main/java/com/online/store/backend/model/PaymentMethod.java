package com.online.store.backend.model;

/**
 * Enumerates supported payment mechanisms.
 * The design document treats payment as a mock, so this list can expand later.
 */
public enum PaymentMethod {
    CASH,
    CARD,
    PAYPAL,
    APPLE_PAY
}
