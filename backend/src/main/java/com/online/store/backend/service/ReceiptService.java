package com.online.store.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.online.store.backend.model.Account;
import com.online.store.backend.model.Cart;
import com.online.store.backend.model.Payment;
import com.online.store.backend.model.Receipt;
import com.online.store.backend.repository.ReceiptRepository;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Receipt issueReceipt(Account customerAccount, Cart cart, Payment payment) {
        Receipt receipt = Receipt.builder()
                .forCustomer(customerAccount)
                .withCart(cart)
                .withPayment(payment)
                .build();
        return receiptRepository.save(receipt);
    }

    public List<Receipt> getReceiptsForCustomer(String customerAccountId) {
        return receiptRepository.findByCustomerAccountIdOrderByIssuedAtDesc(customerAccountId);
    }

    public Receipt findById(String receiptId) {
        return receiptRepository.findById(receiptId)
                .orElseThrow(() -> new IllegalArgumentException("Receipt not found: " + receiptId));
    }

    public List<Receipt> getAllReceipts() {
        return receiptRepository.findAll();
    }
}
