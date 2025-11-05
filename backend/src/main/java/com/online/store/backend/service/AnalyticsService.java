package com.online.store.backend.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.online.store.backend.model.Inventory;
import com.online.store.backend.model.Product;
import com.online.store.backend.model.Receipt;
import com.online.store.backend.repository.ProductRepository;
import com.online.store.backend.repository.ReceiptRepository;

@Service
public class AnalyticsService {

    private final ProductRepository productRepository;
    private final ReceiptRepository receiptRepository;

    public AnalyticsService(ProductRepository productRepository, ReceiptRepository receiptRepository) {
        this.productRepository = productRepository;
        this.receiptRepository = receiptRepository;
    }

    public Map<String, Object> getStoreSummary() {
        Map<String, Object> summary = new HashMap<>();

        // Daily revenue
        LocalDate today = LocalDate.now();
        double dailyRevenue = receiptRepository.findAll().stream()
                .filter(r -> r.getIssuedAt() != null && r.getIssuedAt().toLocalDate().equals(today))
                .mapToDouble(Receipt::getTotalCost)
                .sum();
        summary.put("dailyRevenue", dailyRevenue);

        // Top-selling category
        Map<String, Long> categoryCounts = new HashMap<>();
        for (Receipt receipt : receiptRepository.findAll()) {
            if (receipt.getItems() == null) continue;
            for (Receipt.LineItem item : receipt.getItems()) {
                Product product = productRepository.findById(item.getProductId()).orElse(null);
                if (product != null && product.getCategory() != null) {
                    categoryCounts.merge(product.getCategory(), 1L, Long::sum);
                }
            }
        }

        String topCategory = categoryCounts.entrySet().stream()
                .max(Entry.comparingByValue())
                .map(Entry::getKey)
                .orElse("N/A");
        summary.put("topCategory", topCategory);

        // Repeat customers
        Map<String, Long> customerCounts = receiptRepository.findAll().stream()
                .filter(r -> r.getCustomerAccountId() != null)
                .collect(Collectors.groupingBy(Receipt::getCustomerAccountId, Collectors.counting()));
        long repeat = customerCounts.values().stream().filter(v -> v > 1).count();
        long total = customerCounts.size();
        summary.put("repeatCustomers", total > 0 ? (repeat * 100.0 / total) : 0.0);

        // Low inventory
        long lowStock = Inventory.getInstance().getAllStock().values().stream()
                .filter(p -> {
                    Integer stock = Inventory.getInstance().getStockLevel(p.getId());
                    return stock != null && stock <= 5;
                }).count();
        summary.put("lowInventory", lowStock);

        return summary;
    }

    public Map<String, Double> getWeeklyRevenue() {
        Map<String, Double> result = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            double total = receiptRepository.findAll().stream()
                    .filter(r -> r.getIssuedAt() != null && r.getIssuedAt().toLocalDate().equals(date))
                    .mapToDouble(Receipt::getTotalCost)
                    .sum();
            result.put(date.toString(), total);
        }
        return result;
    }
}
