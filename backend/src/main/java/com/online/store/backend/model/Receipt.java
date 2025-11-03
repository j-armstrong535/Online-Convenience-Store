package com.online.store.backend.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Receipt represents the proof of purchase described in the object design.
 * Responsibilities include calculating totals and confirming payment success.
 */
@Document("receipts")
public class Receipt {

    private static final double DEFAULT_TAX_RATE = 0.1;
    private static final double DEFAULT_DELIVERY_SURCHARGE = 7.5;

    @Id
    private String id;
    private String customerAccountId;
    private List<LineItem> items;
    private double subtotal;
    private double tax;
    private double deliverySurcharge;
    private double totalCost;
    private PaymentMethod paymentMethod;
    private LocalDateTime issuedAt;
    private String paymentReference;

    public Receipt() {
    }

    private Receipt(Builder builder) {
        this.id = builder.id != null ? builder.id : generateReceiptId();
        this.customerAccountId = builder.customerAccountId;
        this.items = builder.items;
        this.subtotal = builder.subtotal;
        this.tax = builder.tax;
        this.deliverySurcharge = builder.deliverySurcharge;
        this.totalCost = builder.totalCost;
        this.paymentMethod = builder.paymentMethod;
        this.issuedAt = builder.issuedAt;
        this.paymentReference = builder.paymentReference;
    }

    private String generateReceiptId() {
        return "RCT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerAccountId() {
        return customerAccountId;
    }

    public void setCustomerAccountId(String customerAccountId) {
        this.customerAccountId = customerAccountId;
    }

    public List<LineItem> getItems() {
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getDeliverySurcharge() {
        return deliverySurcharge;
    }

    public void setDeliverySurcharge(double deliverySurcharge) {
        this.deliverySurcharge = deliverySurcharge;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    /**
     * Builder used to construct receipts while encapsulating calculation logic.
     */
    public static class Builder {
        private String id;
        private String customerAccountId;
        private List<LineItem> items;
        private double subtotal;
        private double tax;
        private double deliverySurcharge;
        private double totalCost;
        private PaymentMethod paymentMethod;
        private LocalDateTime issuedAt;
        private String paymentReference;
        private double taxRate = DEFAULT_TAX_RATE;
        private Double deliveryOverride;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder forCustomer(Account account) {
            this.customerAccountId = account != null ? account.getId() : null;
            return this;
        }

        public Builder withCart(Cart cart) {
            if (cart == null) {
                throw new IllegalArgumentException("Cart cannot be null when building a receipt");
            }
            this.items = cart.getItems().stream()
                    .map(item -> new LineItem(
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getProduct().getPrice()))
                    .collect(Collectors.toList());
            this.subtotal = items.stream().mapToDouble(LineItem::getLineTotal).sum();
            // Delivery surcharge defaults to zero unless delivery is selected
            this.deliverySurcharge = cart.requiresDelivery()
                    ? (deliveryOverride != null ? deliveryOverride : DEFAULT_DELIVERY_SURCHARGE)
                    : 0.0;
            this.tax = round(subtotal * taxRate);
            this.totalCost = round(subtotal + tax + deliverySurcharge);
            return this;
        }

        public Builder withPayment(Payment payment) {
            if (payment == null) {
                throw new IllegalArgumentException("Payment cannot be null when building a receipt");
            }
            if (!payment.isSuccessful()) {
                throw new IllegalStateException("Receipt cannot be issued for an unsuccessful payment");
            }
            this.paymentMethod = payment.getMethod();
            this.paymentReference = payment.getTransactionReference();
            return this;
        }

        public Builder taxRate(double taxRate) {
            this.taxRate = taxRate;
            return this;
        }

        public Builder deliverySurcharge(double surcharge) {
            this.deliveryOverride = surcharge;
            return this;
        }

        public Receipt build() {
            if (issuedAt == null) {
                issuedAt = LocalDateTime.now();
            }
            if (items == null || items.isEmpty()) {
                throw new IllegalStateException("Cannot issue receipt without line items");
            }
            if (paymentMethod == null) {
                throw new IllegalStateException("Payment method must be supplied");
            }
            return new Receipt(this);
        }

        private double round(double value) {
            return Math.round(value * 100.0) / 100.0;
        }
    }

    /**
     * Describes a single line item on the receipt.
     */
    public static class LineItem {
        private String productId;
        private String productName;
        private int quantity;
        private double unitPrice;
        private double lineTotal;

        public LineItem() {
        }

        public LineItem(String productId, String productName, int quantity, double unitPrice) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.lineTotal = Math.round(unitPrice * quantity * 100.0) / 100.0;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getLineTotal() {
            return lineTotal;
        }

        public void setLineTotal(double lineTotal) {
            this.lineTotal = lineTotal;
        }
    }
}
