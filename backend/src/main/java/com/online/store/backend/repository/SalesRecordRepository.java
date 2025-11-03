package com.online.store.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.online.store.backend.model.SalesRecord;

@Repository
public interface SalesRecordRepository extends MongoRepository<SalesRecord, String> {
    List<SalesRecord> findByRecordedAtBetween(LocalDateTime from, LocalDateTime to);
}
