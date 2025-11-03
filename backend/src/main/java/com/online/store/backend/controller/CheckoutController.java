package com.online.store.backend.controller;

import java.time.LocalDateTime;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.online.store.backend.model.Account;
import com.online.store.backend.model.AccountType;
import com.online.store.backend.model.CustomerAccount;
import com.online.store.backend.model.FulfilmentMethod;
import com.online.store.backend.model.PaymentMethod;
import com.online.store.backend.model.Receipt;
import com.online.store.backend.model.StoreAccount;
import com.online.store.backend.service.AccountService;
import com.online.store.backend.service.CheckoutService;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "*")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final AccountService accountService;

    public CheckoutController(CheckoutService checkoutService, AccountService accountService) {
        this.checkoutService = checkoutService;
        this.accountService = accountService;
    }

    @PostMapping
    public CheckoutResponse checkout(@RequestBody CheckoutRequest request) {
        if (request.getPaymentMethod() == null) {
            throw new IllegalArgumentException("paymentMethod is required");
        }
        if (request.getFulfilmentMethod() == null) {
            request.setFulfilmentMethod(FulfilmentMethod.PICKUP);
        }
        Account account = buildAccount(request);
        CheckoutService.Result result = checkoutService.checkout(
                account,
                request.getPaymentMethod(),
                request.getFulfilmentMethod());
        Receipt receipt = result.getReceipt();
        return new CheckoutResponse(
                receipt.getId(),
                receipt.getTotalCost(),
                receipt.getIssuedAt(),
                receipt.getPaymentReference(),
                result.getPayment().getMethod());
    }

    private Account buildAccount(CheckoutRequest request) {
        if (request == null) {
            return null;
        }

        if (StringUtils.hasText(request.getCustomerAccountId())) {
            return accountService.findById(request.getCustomerAccountId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        }

        if (StringUtils.hasText(request.getEmail())) {
            Optional<Account> existing = accountService.findByEmail(request.getEmail());
            if (existing.isPresent()) {
                Account account = existing.get();
                applyCustomerDetails(account, request);
                return accountService.saveAccount(account);
            }
        }

        if (!StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getEmail())) {
            return null; // guest checkout
        }

        AccountType accountType = request.getAccountType() != null
                ? request.getAccountType()
                : AccountType.CUSTOMER;

        Account account;
        switch (accountType) {
            case STORE_MANAGER:
                account = accountService.createStoreManagerAccount(
                        request.getUsername(),
                        request.getEmail(),
                        "",
                        request.getFirstName(),
                        request.getLastName(),
                        request.getDepartment());
                break;
            case STORE_ADMIN:
                account = accountService.createStoreAdminAccount(
                        request.getUsername(),
                        request.getEmail(),
                        "",
                        request.getFirstName(),
                        request.getLastName());
                break;
            case CUSTOMER:
            default:
                account = accountService.createCustomerAccount(
                        request.getUsername(),
                        request.getEmail(),
                        "",
                        request.getFirstName(),
                        request.getLastName(),
                        request.getShippingAddress());
                applyCustomerDetails(account, request);
                account = accountService.saveAccount(account);
                break;
        }
        return account;
    }

    private void applyCustomerDetails(Account account, CheckoutRequest request) {
        if (account instanceof CustomerAccount customer) {
            if (StringUtils.hasText(request.getShippingAddress())) {
                customer.setShippingAddress(request.getShippingAddress());
            }
            if (request.getPaymentMethod() != null) {
                customer.setPaymentMethod(request.getPaymentMethod().name());
            }
        } else if (account instanceof StoreAccount storeAccount) {
            if (StringUtils.hasText(request.getDepartment())) {
                storeAccount.setDepartment(request.getDepartment());
            }
        }
    }

    public static class CheckoutRequest {
        private String customerAccountId;
        private String username;
        private String email;
        private PaymentMethod paymentMethod;
        private FulfilmentMethod fulfilmentMethod;
        private AccountType accountType;
        private String shippingAddress;
        private String firstName;
        private String lastName;
        private String department;

        public String getCustomerAccountId() {
            return customerAccountId;
        }

        public void setCustomerAccountId(String customerAccountId) {
            this.customerAccountId = customerAccountId;
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

        public PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public FulfilmentMethod getFulfilmentMethod() {
            return fulfilmentMethod;
        }

        public void setFulfilmentMethod(FulfilmentMethod fulfilmentMethod) {
            this.fulfilmentMethod = fulfilmentMethod;
        }

        public AccountType getAccountType() {
            return accountType;
        }

        public void setAccountType(AccountType accountType) {
            this.accountType = accountType;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
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

    public static class CheckoutResponse {
        private final String receiptId;
        private final double total;
        private final LocalDateTime issuedAt;
        private final String paymentReference;
        private final PaymentMethod paymentMethod;

        public CheckoutResponse(String receiptId, double total, LocalDateTime issuedAt,
                String paymentReference, PaymentMethod paymentMethod) {
            this.receiptId = receiptId;
            this.total = total;
            this.issuedAt = issuedAt;
            this.paymentReference = paymentReference;
            this.paymentMethod = paymentMethod;
        }

        public String getReceiptId() {
            return receiptId;
        }

        public double getTotal() {
            return total;
        }

        public LocalDateTime getIssuedAt() {
            return issuedAt;
        }

        public String getPaymentReference() {
            return paymentReference;
        }

        public PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }
    }
}
