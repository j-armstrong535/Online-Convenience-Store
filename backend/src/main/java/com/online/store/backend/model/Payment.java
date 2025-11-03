package com.online.store.backend.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents the record of a payment captured during checkout.
 */
@Document("payments")
public class Payment {

    @Id
    private String id;
    private PaymentMethod method;
    private PaymentStatus status;
    private double amount;
    private LocalDateTime processedAt;
    private String transactionReference;
    private String failureReason;

    public Payment() {
    }

    public Payment(PaymentMethod method, double amount) {
        this.method = method;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    /**
     * Marks the payment as successful with a generated reference.
     */
    public void markSuccessful(String reference) {
        this.status = PaymentStatus.SUCCESS;
        this.transactionReference = reference;
        this.processedAt = LocalDateTime.now();
        this.failureReason = null;
    }

    /**
     * Marks the payment as failed with a reason.
     */
    public void markFailed(String reason) {
        this.status = PaymentStatus.FAILED;
        this.processedAt = LocalDateTime.now();
        this.failureReason = reason;
        this.transactionReference = null;
    }

    public boolean isSuccessful() {
        return status == PaymentStatus.SUCCESS;
    }
}
