package com.online.store.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.online.store.backend.model.Receipt;
import com.online.store.backend.service.ReceiptService;

@RestController
@RequestMapping("/api/receipts")
@CrossOrigin(origins = "*")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/{receiptId}")
    public Receipt getReceipt(@PathVariable String receiptId) {
        return receiptService.findById(receiptId);
    }

    @GetMapping
    public List<Receipt> getReceiptsForCustomer(@RequestParam("customerId") String customerId) {
        return receiptService.getReceiptsForCustomer(customerId);
    }
}
