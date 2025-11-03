package com.online.store.backend.model;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Stores details of completed transactions for reporting and analytics.
 * Implements the responsibilities described in the SalesRecord CRC card.
 */
@Document("sales_records")
public class SalesRecord {

    @Id
    private String id;
    private String receiptId;
    private String customerAccountId;
    private LocalDateTime recordedAt;
    private double totalRevenue;
    private double taxCollected;
    private double deliverySurcharge;
    private PaymentMethod paymentMethod;
    private Map<String, Integer> itemsSold = new LinkedHashMap<>();

    public SalesRecord() {
    }

    public SalesRecord(String receiptId) {
        this.receiptId = receiptId;
        this.recordedAt = LocalDateTime.now();
    }

    public static SalesRecord fromReceipt(Receipt receipt) {
        SalesRecord record = new SalesRecord(receipt.getId());
        record.customerAccountId = receipt.getCustomerAccountId();
        record.totalRevenue = receipt.getTotalCost();
        record.taxCollected = receipt.getTax();
        record.deliverySurcharge = receipt.getDeliverySurcharge();
        record.paymentMethod = receipt.getPaymentMethod();
        record.recordedAt = LocalDateTime.now();
        receipt.getItems().forEach(item -> record.itemsSold.merge(
                item.getProductId(),
                item.getQuantity(),
                Integer::sum));
        return record;
    }

    public void applyAdjustment(double amount) {
        this.totalRevenue = Math.round((this.totalRevenue + amount) * 100.0) / 100.0;
    }

    public void addOrUpdateItem(String productId, int quantity) {
        itemsSold.merge(productId, quantity, Integer::sum);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getCustomerAccountId() {
        return customerAccountId;
    }

    public void setCustomerAccountId(String customerAccountId) {
        this.customerAccountId = customerAccountId;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getTaxCollected() {
        return taxCollected;
    }

    public void setTaxCollected(double taxCollected) {
        this.taxCollected = taxCollected;
    }

    public double getDeliverySurcharge() {
        return deliverySurcharge;
    }

    public void setDeliverySurcharge(double deliverySurcharge) {
        this.deliverySurcharge = deliverySurcharge;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Map<String, Integer> getItemsSold() {
        return itemsSold;
    }

    public void setItemsSold(Map<String, Integer> itemsSold) {
        this.itemsSold = itemsSold;
    }
}
