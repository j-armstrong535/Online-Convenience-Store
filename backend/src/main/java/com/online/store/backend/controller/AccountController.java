package com.online.store.backend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.store.backend.model.Account;
import com.online.store.backend.model.AccountFactory;
import com.online.store.backend.model.AccountType;
import com.online.store.backend.model.StoreAccount;
import com.online.store.backend.service.AccountService;

/**
 * REST Controller demonstrating account creation and login via AccountFactory and AccountService.
 */
@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /** -------------------- ACCOUNT CREATION (GENERIC) -------------------- **/
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

            account = accountService.saveAccount(account);

            return success("Account created successfully", Map.of(
                    "id", account.getId(),
                    "username", account.getUsername(),
                    "email", account.getEmail(),
                    "type", account.getClass().getSimpleName()
            ));
        } catch (Exception e) {
            return error("Error creating account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** -------------------- CUSTOMER ACCOUNT CREATION -------------------- **/
    @PostMapping("/customer")
    public ResponseEntity<Map<String, Object>> createCustomerAccount(@RequestBody CustomerAccountRequest request) {
        try {
            Account account = accountService.createCustomerAccount(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getShippingAddress()
            );

            return success("Customer account created successfully", Map.of(
                    "username", account.getUsername(),
                    "email", account.getEmail(),
                    "id", account.getId()
            ));
        } catch (Exception e) {
            return error("Error creating customer account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** -------------------- MANAGER ACCOUNT CREATION -------------------- **/
    @PostMapping("/manager")
    public ResponseEntity<Map<String, Object>> createManagerAccount(@RequestBody ManagerAccountRequest request) {
        try {
            Account account = accountService.createStoreManagerAccount(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getDepartment()
            );

            return success("Manager account created successfully", Map.of(
                    "username", account.getUsername(),
                    "email", account.getEmail(),
                    "id", account.getId()
            ));
        } catch (Exception e) {
            return error("Error creating manager account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** -------------------- LOGIN ENDPOINT -------------------- **/
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        Optional<Account> accountOpt = accountService.findByEmail(request.getEmail());

        if (accountOpt.isEmpty()) {
            return error("Account not found", HttpStatus.UNAUTHORIZED);
        }

        Account account = accountOpt.get();
        if (!account.getPassword().equals(request.getPassword())) {
            return error("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        // Determine type
        String type = "customer";
        if (account instanceof StoreAccount store) {
            type = store.getAccessLevel().equalsIgnoreCase("administrator")
                    ? "store_admin"
                    : "store_manager";
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", account.getId());
        data.put("username", account.getUsername());
        data.put("email", account.getEmail());
        data.put("accountType", type);
        data.put("firstName", account.getFirstName());
        data.put("lastName", account.getLastName());

        return success("Login successful", data);
    }

    /** -------------------- GET ACCOUNT BY EMAIL -------------------- **/
    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> getByEmail(@PathVariable String email) {
        Optional<Account> accountOpt = accountService.findByEmail(email);

        if (accountOpt.isEmpty()) {
            return error("Account not found for email: " + email, HttpStatus.NOT_FOUND);
        }

        Account account = accountOpt.get();
        return success("Account found", Map.of(
                "id", account.getId(),
                "username", account.getUsername(),
                "email", account.getEmail(),
                "type", account.getClass().getSimpleName()
        ));
    }

    /** -------------------- ACCOUNT TYPES -------------------- **/
    @GetMapping("/types")
    public ResponseEntity<Map<String, Object>> getAccountTypes() {
        return success("Account types fetched", Map.of("accountTypes", AccountType.values()));
    }

    /** -------------------- Helper: success / error builders -------------------- **/
    private ResponseEntity<Map<String, Object>> success(String message, Object data) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("message", message);
        body.put("data", data);
        return ResponseEntity.ok(body);
    }

    private ResponseEntity<Map<String, Object>> error(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }

    /** -------------------- Request DTOs -------------------- **/
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

        public AccountType getAccountType() {
            return AccountType.fromString(accountType);
        }

        public void setAccountType(String accountType) { this.accountType = accountType; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getShippingAddress() { return shippingAddress; }
        public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public String getAccessLevel() { return accessLevel; }
        public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
    }

    public static class CustomerAccountRequest {
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String shippingAddress;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getShippingAddress() { return shippingAddress; }
        public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    }

    public static class ManagerAccountRequest {
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String department;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
    }

    public static class LoginRequest {
        private String email;
        private String password;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
