package com.online.store.backend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.online.store.backend.model.Payment;
import com.online.store.backend.model.PaymentMethod;
import com.online.store.backend.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Mock payment processor: marks all payments as successful and persists them.
     */
    public Payment processPayment(PaymentMethod method, double amount) {
        Payment payment = new Payment(method, amount);
        payment.markSuccessful(generateReference(method));
        return paymentRepository.save(payment);
    }

    public Payment markFailed(Payment payment, String reason) {
        payment.markFailed(reason);
        return paymentRepository.save(payment);
    }

    private String generateReference(PaymentMethod method) {
        return method.name().substring(0, 1) + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
