package com.online.store.backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.online.store.backend.model.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    Optional<Account> findByEmail(String email);
}
