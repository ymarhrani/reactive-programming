package com.learning.functionalendpoints.repository;

import com.learning.functionalendpoints.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
}
