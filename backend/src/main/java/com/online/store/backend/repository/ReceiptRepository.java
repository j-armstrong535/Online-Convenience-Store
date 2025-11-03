package com.online.store.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.online.store.backend.model.Receipt;

@Repository
public interface ReceiptRepository extends MongoRepository<Receipt, String> {
    List<Receipt> findByCustomerAccountIdOrderByIssuedAtDesc(String customerAccountId);
}
