package com.online.store.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.online.store.backend.model.SalesRecord;
import com.online.store.backend.service.SalesRecordService;

@RestController
@RequestMapping("/api/sales-records")
@CrossOrigin(origins = "*")
public class SalesRecordController {

    private final SalesRecordService salesRecordService;

    public SalesRecordController(SalesRecordService salesRecordService) {
        this.salesRecordService = salesRecordService;
    }

    @GetMapping("/top")
    public Map<String, Integer> getTopSelling(@RequestParam(defaultValue = "5") int limit) {
        return salesRecordService.topSellingProducts(limit);
    }

    @GetMapping("/range")
    public List<SalesRecord> getSalesInRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return salesRecordService.findBetween(from, to);
    }

    @PostMapping("/{recordId}/adjust")
    public SalesRecord adjustRecord(@PathVariable String recordId, @RequestParam double amount) {
        return salesRecordService.applyAdjustment(recordId, amount);
    }
}
