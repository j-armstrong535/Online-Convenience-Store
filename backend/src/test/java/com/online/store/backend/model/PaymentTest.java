package com.online.store.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void markSuccessful_updatesStatusAndReference() {
        Payment payment = new Payment(PaymentMethod.CARD, 25.0);

        payment.markSuccessful("C-123");

        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
        assertEquals("C-123", payment.getTransactionReference());
        assertNull(payment.getFailureReason());
        assertNotNull(payment.getProcessedAt());
        assertTrue(payment.isSuccessful());
    }

    @Test
    void markFailed_updatesStatusAndReason() {
        Payment payment = new Payment(PaymentMethod.PAYPAL, 30.0);

        payment.markFailed("Declined");

        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        assertEquals("Declined", payment.getFailureReason());
        assertNull(payment.getTransactionReference());
        assertNotNull(payment.getProcessedAt());
        assertFalse(payment.isSuccessful());
    }
}
