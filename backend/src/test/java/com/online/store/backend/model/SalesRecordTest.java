package com.online.store.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class SalesRecordTest {

    @Test
    void fromReceipt_populatesFields() {
        Receipt receipt = new Receipt();
        receipt.setId("RCT-1");
        receipt.setCustomerAccountId("cust-1");
        receipt.setItems(List.of(new Receipt.LineItem("SKU-1", "Item", 2, 5.0)));
        receipt.setTotalCost(12.0);
        receipt.setTax(1.0);
        receipt.setDeliverySurcharge(1.0);
        receipt.setPaymentMethod(PaymentMethod.CARD);

        SalesRecord record = SalesRecord.fromReceipt(receipt);

        assertEquals("RCT-1", record.getReceiptId());
        assertEquals("cust-1", record.getCustomerAccountId());
        assertEquals(12.0, record.getTotalRevenue());
        assertEquals(1.0, record.getTaxCollected());
        assertEquals(1.0, record.getDeliverySurcharge());
        assertEquals(PaymentMethod.CARD, record.getPaymentMethod());
        assertEquals(2, record.getItemsSold().get("SKU-1").intValue());
    }

    @Test
    void applyAdjustment_roundsToTwoDecimals() {
        SalesRecord record = new SalesRecord("RCT-2");
        record.setTotalRevenue(100.0);

        record.applyAdjustment(-5.555);

        assertEquals(94.45, record.getTotalRevenue());
    }

    @Test
    void addOrUpdateItem_accumulatesQuantities() {
        SalesRecord record = new SalesRecord("RCT-3");

        record.addOrUpdateItem("SKU-1", 2);
        record.addOrUpdateItem("SKU-1", 3);

        assertEquals(5, record.getItemsSold().get("SKU-1").intValue());
    }
}
