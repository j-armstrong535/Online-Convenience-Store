package com.online.store.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.online.store.backend.model.Product;

public interface IProductRepository extends MongoRepository<Product, String> {
}
