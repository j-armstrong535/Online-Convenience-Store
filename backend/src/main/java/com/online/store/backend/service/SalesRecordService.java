package com.online.store.backend.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.online.store.backend.model.InventoryEvent;
import com.online.store.backend.model.InventoryObserver;
import com.online.store.backend.model.Receipt;
import com.online.store.backend.model.SalesRecord;
import com.online.store.backend.repository.SalesRecordRepository;

@Service
public class SalesRecordService implements InventoryObserver {

    private final SalesRecordRepository salesRecordRepository;
    private final Map<String, Integer> latestInventoryLevels = new ConcurrentHashMap<>();

    public SalesRecordService(SalesRecordRepository salesRecordRepository, InventoryService inventoryService) {
        this.salesRecordRepository = salesRecordRepository;
        inventoryService.registerObserver(this);
    }

    public SalesRecord recordSale(Receipt receipt) {
        SalesRecord record = SalesRecord.fromReceipt(receipt);
        return salesRecordRepository.save(record);
    }

    public List<SalesRecord> findBetween(LocalDateTime from, LocalDateTime to) {
        return salesRecordRepository.findByRecordedAtBetween(from, to);
    }

    public Map<String, Integer> topSellingProducts(int limit) {
        Map<String, Integer> aggregated = new LinkedHashMap<>();
        salesRecordRepository.findAll().forEach(record -> record.getItemsSold()
                .forEach((productId, quantity) -> aggregated.merge(productId, quantity, Integer::sum)));

        return aggregated.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum,
                        LinkedHashMap::new));
    }

    public SalesRecord applyAdjustment(String recordId, double amount) {
        SalesRecord record = salesRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("SalesRecord not found: " + recordId));
        record.applyAdjustment(amount);
        return salesRecordRepository.save(record);
    }

    @Override
    public void onInventoryChanged(InventoryEvent event) {
        latestInventoryLevels.put(event.getProductId(), event.getNewQuantity());
    }

    public Map<String, Integer> getLatestInventoryLevels() {
        return latestInventoryLevels;
    }
}
