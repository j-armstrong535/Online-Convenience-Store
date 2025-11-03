package com.online.store.backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.online.store.backend.model.StoreAccount;

@Repository
public interface StoreAccountRepository extends MongoRepository<StoreAccount, String> {
    Optional<StoreAccount> findByEmail(String email);
}
