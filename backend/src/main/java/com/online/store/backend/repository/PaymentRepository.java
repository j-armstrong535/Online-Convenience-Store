package com.online.store.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.online.store.backend.model.Payment;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
}
