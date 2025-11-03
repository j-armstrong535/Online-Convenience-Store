package com.online.store.backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.online.store.backend.model.CustomerAccount;

@Repository
public interface CustomerAccountRepository extends MongoRepository<CustomerAccount, String> {
    Optional<CustomerAccount> findByEmail(String email);
}
