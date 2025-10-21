package com.online.store.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.online.store.backend.model.Product;
import com.online.store.backend.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public String addProduct(@RequestBody Product product, @RequestParam int quantity) {
        inventoryService.addProductToInventory(product, quantity);
        return "Product added to inventory successfully.";
    }

    @PutMapping("/restock/{id}")
    public String restock(@PathVariable String id, @RequestParam int amount) {
        inventoryService.restockProduct(id, amount);
        return "Product restocked successfully.";
    }

    @PutMapping("/reduce/{id}")
    public String reduce(@PathVariable String id, @RequestParam int amount) {
        boolean success = inventoryService.reduceStock(id, amount);
        return success ? "Stock reduced successfully." : "Not enough stock or product not found.";
    }

    @GetMapping("/check/{id}")
    public Integer checkStock(@PathVariable String id) {
        return inventoryService.getStockLevel(id);
    }
}
