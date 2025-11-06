package com.online.store.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.online.store.backend.model.Payment;
import com.online.store.backend.model.PaymentMethod;
import com.online.store.backend.model.PaymentStatus;
import com.online.store.backend.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentRepository);
    }

    @Test
    void processPayment_marksAsSuccessfulAndPersists() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.processPayment(PaymentMethod.CARD, 42.50);

        assertEquals(PaymentMethod.CARD, payment.getMethod());
        assertEquals(42.50, payment.getAmount());
        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
        assertNotNull(payment.getProcessedAt());
        assertNotNull(payment.getTransactionReference());
        assertTrue(payment.getTransactionReference().startsWith("C-"));
        assertNull(payment.getFailureReason());
        verify(paymentRepository).save(payment);
    }

    @Test
    void markFailed_updatesStatusAndPersists() {
        Payment payment = new Payment(PaymentMethod.PAYPAL, 15.0);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.markFailed(payment, "Declined");

        assertEquals(PaymentStatus.FAILED, result.getStatus());
        assertNotNull(result.getProcessedAt());
        assertEquals("Declined", result.getFailureReason());
        assertNull(result.getTransactionReference());
        verify(paymentRepository).save(payment);
    }
}
