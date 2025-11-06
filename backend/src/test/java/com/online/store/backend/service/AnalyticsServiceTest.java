package com.online.store.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.online.store.backend.model.Inventory;
import com.online.store.backend.model.InventoryObserver;
import com.online.store.backend.model.Product;
import com.online.store.backend.model.Receipt;
import com.online.store.backend.repository.ProductRepository;
import com.online.store.backend.repository.ReceiptRepository;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReceiptRepository receiptRepository;

    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() throws Exception {
        resetInventorySingleton();
        analyticsService = new AnalyticsService(productRepository, receiptRepository);
    }

    @Test
    void getStoreSummary_calculatesCoreMetrics() {
        Receipt today1 = receipt("RCT-1", "cust-1", LocalDateTime.now(), "prod-snack", 40.0, 4.0, 0.0);
        Receipt today2 = receipt("RCT-2", "cust-1", LocalDateTime.now(), "prod-drink", 20.0, 2.0, 0.0);
        Receipt yesterday = receipt("RCT-3", "cust-2", LocalDateTime.now().minusDays(1), "prod-snack", 15.0, 1.5, 0.0);
        List<Receipt> receipts = List.of(today1, today2, yesterday);

        when(receiptRepository.findAll()).thenReturn(receipts);
        when(productRepository.findById("prod-snack")).thenReturn(Optional.of(product("prod-snack", "Snacks")));
        when(productRepository.findById("prod-drink")).thenReturn(Optional.of(product("prod-drink", "Beverages")));

        Inventory inventory = Inventory.getInstance();
        Product low = product("low-stock", "Category");
        Product healthy = product("healthy-stock", "Category");
        inventory.addProduct(low, 3);
        inventory.addProduct(healthy, 10);

        Map<String, Object> summary = analyticsService.getStoreSummary();

        assertEquals(66.0, (double) summary.get("dailyRevenue"));
        assertEquals("Snacks", summary.get("topCategory"));
        assertEquals(50.0, (double) summary.get("repeatCustomers"));
        assertEquals(1L, summary.get("lowInventory"));
    }

    @Test
    void getWeeklyRevenue_returnsSevenDaySeries() {
        LocalDate today = LocalDate.now();
        List<Receipt> receipts = new ArrayList<>();
        receipts.add(receipt("RCT-1", "cust-1", today.atStartOfDay(), "prod-1", 10.0, 1.0, 0.0));
        receipts.add(receipt("RCT-2", "cust-2", today.minusDays(2).atStartOfDay(), "prod-2", 20.0, 2.0, 0.0));
        receipts.add(receipt("RCT-3", "cust-3", today.minusDays(6).atStartOfDay(), "prod-3", 30.0, 3.0, 0.0));

        when(receiptRepository.findAll()).thenReturn(receipts);

        Map<String, Double> weeklyRevenue = analyticsService.getWeeklyRevenue();

        assertEquals(7, weeklyRevenue.size());
        assertEquals(11.0, weeklyRevenue.get(today.toString()));
        assertEquals(22.0, weeklyRevenue.get(today.minusDays(2).toString()));
        assertEquals(33.0, weeklyRevenue.get(today.minusDays(6).toString()));
        assertTrue(weeklyRevenue.values().stream().allMatch(value -> value >= 0));
    }

    private Receipt receipt(String id, String customerId, LocalDateTime issuedAt, String productId,
            double subtotal, double tax, double delivery) {
        Receipt receipt = new Receipt();
        receipt.setId(id);
        receipt.setCustomerAccountId(customerId);
        receipt.setItems(List.of(new Receipt.LineItem(productId, "Item", 1, subtotal)));
        receipt.setSubtotal(subtotal);
        receipt.setTax(tax);
        receipt.setDeliverySurcharge(delivery);
        receipt.setTotalCost(Math.round((subtotal + tax + delivery) * 100.0) / 100.0);
        receipt.setPaymentMethod(com.online.store.backend.model.PaymentMethod.CARD);
        receipt.setIssuedAt(issuedAt);
        return receipt;
    }

    private Product product(String id, String category) {
        Product product = new Product();
        product.setId(id);
        product.setName(id);
        product.setCategory(category);
        return product;
    }

    private void resetInventorySingleton() throws Exception {
        Inventory inventory = Inventory.getInstance();
        clearMap(inventory, "productStock");
        clearMap(inventory, "categoryIndex");
        Field observersField = Inventory.class.getDeclaredField("observers");
        observersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<InventoryObserver> observers = (List<InventoryObserver>) observersField.get(inventory);
        observers.clear();
    }

    private void clearMap(Inventory inventory, String fieldName) throws Exception {
        Field field = Inventory.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, ?> map = (Map<String, ?>) field.get(inventory);
        map.clear();
    }
}
