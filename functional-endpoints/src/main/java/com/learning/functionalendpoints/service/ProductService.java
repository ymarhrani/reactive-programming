package com.learning.functionalendpoints.service;

import com.learning.functionalendpoints.model.Product;
import com.learning.functionalendpoints.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Mono<Product> getProductById(int id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("")));
    }


}
