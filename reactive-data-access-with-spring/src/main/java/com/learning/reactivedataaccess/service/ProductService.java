package com.learning.reactivedataaccess.service;

import com.learning.reactivedataaccess.exception.EntityNotFoundException;
import com.learning.reactivedataaccess.model.Product;
import com.learning.reactivedataaccess.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Flux<Product> getAllProducts() {
        return repository.findAll();
    }

    public Flux<Product> getDelayedProducts() {
        return repository.findAll()
                .delayElements(Duration.ofSeconds(1L));
    }

    public Mono<Product> getProductById(int id) {
        return repository.findById(id);
    }
    
    public Mono<Product> getProductByName(String name) {
        return repository.findByName(name);
    }
    
    public Mono<Product> createProduct(final Product product) {
        return repository.save(product);
    }

    public Mono<Product> updateProduct(int productId, final Mono<Product> productMono) {
        return repository.findById(productId)
                .flatMap(product -> productMono.map(updatedProduct -> {
                    product.setName(updatedProduct.getName());
                    product.setAmount(updatedProduct.getAmount());
                    return product;
                }))
                .flatMap(repository::save)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(Product.class, String.valueOf(productId))));
    }

    public Mono<Void> deleteProduct(final int id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(Product.class, String.valueOf(id))))
                .flatMap(repository::delete);
    }

    public Mono<Void> deleteAllProducts() {
        return repository.deleteAll();
    }

}