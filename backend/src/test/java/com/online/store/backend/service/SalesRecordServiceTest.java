package com.online.store.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.online.store.backend.model.Cart;
import com.online.store.backend.model.InventoryEvent;
import com.online.store.backend.model.Payment;
import com.online.store.backend.model.PaymentMethod;
import com.online.store.backend.model.Receipt;
import com.online.store.backend.model.SalesRecord;
import com.online.store.backend.repository.SalesRecordRepository;

@ExtendWith(MockitoExtension.class)
class SalesRecordServiceTest {

    @Mock
    private SalesRecordRepository salesRecordRepository;

    @Mock
    private InventoryService inventoryService;

    private SalesRecordService salesRecordService;

    @BeforeEach
    void setUp() {
        salesRecordService = new SalesRecordService(salesRecordRepository, inventoryService);
    }

    @Test
    void constructor_registersAsInventoryObserver() {
        verify(inventoryService).registerObserver(salesRecordService);
    }

    @Test
    void recordSale_convertsReceiptAndSaves() {
        Receipt receipt = receipt("RCT-1", PaymentMethod.CARD, 12.0, 2);
        when(salesRecordRepository.save(any(SalesRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SalesRecord record = salesRecordService.recordSale(receipt);

        assertEquals("RCT-1", record.getReceiptId());
        assertEquals(receipt.getCustomerAccountId(), record.getCustomerAccountId());
        assertEquals(receipt.getTotalCost(), record.getTotalRevenue());
        assertEquals(receipt.getTax(), record.getTaxCollected());
        assertTrue(record.getItemsSold().containsKey(receipt.getItems().get(0).getProductId()));
        verify(salesRecordRepository).save(record);
    }

    @Test
    void findBetween_delegatesToRepository() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        salesRecordService.findBetween(from, to);

        verify(salesRecordRepository).findByRecordedAtBetween(from, to);
    }

    @Test
    void topSellingProducts_aggregatesAndLimitsResults() {
        SalesRecord record1 = new SalesRecord("RCT-1");
        record1.setItemsSold(mapOf("A", 5, "B", 2));
        SalesRecord record2 = new SalesRecord("RCT-2");
        record2.setItemsSold(mapOf("A", 3, "C", 4));
        when(salesRecordRepository.findAll()).thenReturn(List.of(record1, record2));

        Map<String, Integer> top = salesRecordService.topSellingProducts(2);

        assertEquals(2, top.size());
        assertEquals(8, top.get("A"));
        assertEquals(4, top.get("C"));
    }

    @Test
    void applyAdjustment_updatesTotalRevenue() {
        SalesRecord record = new SalesRecord("RCT-3");
        record.setTotalRevenue(100.0);
        when(salesRecordRepository.findById("id")).thenReturn(Optional.of(record));
        when(salesRecordRepository.save(record)).thenReturn(record);

        SalesRecord adjusted = salesRecordService.applyAdjustment("id", -5.5);

        assertEquals(94.5, adjusted.getTotalRevenue());
        verify(salesRecordRepository).save(record);
    }

    @Test
    void applyAdjustment_throwsWhenMissing() {
        when(salesRecordRepository.findById("missing")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> salesRecordService.applyAdjustment("missing", 10.0));
    }

    @Test
    void onInventoryChanged_tracksLatestLevels() {
        InventoryEvent event = new InventoryEvent("SKU-1", -1, 4);

        salesRecordService.onInventoryChanged(event);

        assertEquals(4, salesRecordService.getLatestInventoryLevels().get("SKU-1"));
    }

    private Receipt receipt(String id, PaymentMethod method, double price, int quantity) {
        Cart cart = new Cart("cust");
        com.online.store.backend.model.Product product = new com.online.store.backend.model.Product();
        product.setId("SKU-" + id);
        product.setName("Product");
        product.setPrice(price);
        for (int i = 0; i < quantity; i++) {
            cart.addProduct(product);
        }
        Payment payment = new Payment(method, cart.getGrandTotal());
        payment.markSuccessful(method.name().substring(0, 1) + "-123");

        return Receipt.builder()
                .withId(id)
                .forCustomer(null)
                .withCart(cart)
                .withPayment(payment)
                .build();
    }

    private Map<String, Integer> mapOf(Object... entries) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < entries.length; i += 2) {
            String key = (String) entries[i];
            Integer value = (Integer) entries[i + 1];
            map.put(key, value);
        }
        return map;
    }
}
