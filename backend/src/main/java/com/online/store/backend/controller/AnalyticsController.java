package com.online.store.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.store.backend.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        return analyticsService.getStoreSummary();
    }

    @GetMapping("/revenue")
    public Map<String, Double> getWeeklyRevenue() {
        return analyticsService.getWeeklyRevenue();
    }

    @GetMapping("/test")
    public String test() {
        return "Analytics endpoint working!"; // Simple test endpoint to verify controller is set up
    }
}
