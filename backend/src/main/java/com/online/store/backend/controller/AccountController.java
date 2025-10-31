package com.online.store.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.online.store.backend.model.Account;
import com.online.store.backend.model.AccountFactory;
import com.online.store.backend.model.AccountType;
import com.online.store.backend.service.AccountService;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller demonstrating the use of AccountFactory in API endpoints
 * Shows how the Factory pattern simplifies account creation in web services
 */
@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * Creates a new account using the factory pattern
     * POST /api/accounts
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAccount(@RequestBody CreateAccountRequest request) {
        try {
            Account account = AccountFactory
                    .builder(request.getAccountType(), request.getUsername(), request.getEmail())
                    .password(request.getPassword())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .shippingAddress(request.getShippingAddress())
                    .paymentMethod(request.getPaymentMethod())
                    .department(request.getDepartment())
                    .accessLevel(request.getAccessLevel())
                    .build();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Account created successfully");
            response.put("accountType", account.getClass().getSimpleName());
            response.put("username", account.getUsername());
            response.put("email", account.getEmail());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Invalid account type: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error creating account: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Creates a simple customer account
     * POST /api/accounts/customer
     */
    @PostMapping("/customer")
    public ResponseEntity<Map<String, Object>> createCustomerAccount(@RequestBody CustomerAccountRequest request) {
        try {
            Account account = accountService.createCustomerAccount(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getShippingAddress());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customer account created successfully");
            response.put("username", account.getUsername());
            response.put("email", account.getEmail());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error creating customer account: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Creates a store manager account
     * POST /api/accounts/manager
     */
    @PostMapping("/manager")
    public ResponseEntity<Map<String, Object>> createManagerAccount(@RequestBody ManagerAccountRequest request) {
        try {
            Account account = accountService.createStoreManagerAccount(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getDepartment());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Manager account created successfully");
            response.put("username", account.getUsername());
            response.put("email", account.getEmail());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error creating manager account: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Gets available account types
     * GET /api/accounts/types
     */
    @GetMapping("/types")
    public ResponseEntity<Map<String, Object>> getAccountTypes() {
        Map<String, Object> response = new HashMap<>();
        response.put("accountTypes", AccountType.values());
        return ResponseEntity.ok(response);
    }

    // Inner classes for request DTOs
    public static class CreateAccountRequest {
        private String accountType;
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String shippingAddress;
        private String paymentMethod;
        private String department;
        private String accessLevel;

        // Getters and setters
        public AccountType getAccountType() {
            return AccountType.fromString(accountType);
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getAccessLevel() {
            return accessLevel;
        }

        public void setAccessLevel(String accessLevel) {
            this.accessLevel = accessLevel;
        }
    }

    public static class CustomerAccountRequest {
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String shippingAddress;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
        }
    }

    public static class ManagerAccountRequest {
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String department;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }
    }
}