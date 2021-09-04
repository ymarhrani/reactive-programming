package com.learning.reactivedataaccess.controller;

import com.learning.reactivedataaccess.exception.EntityNotFoundException;
import com.learning.reactivedataaccess.model.Product;
import com.learning.reactivedataaccess.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Publisher<ResponseEntity<List<Product>>> getProducts(@RequestParam(value = "name", required = false) String name) {
        if (StringUtils.hasText(name)) {
            return productService.getProductByName(name)
                    .map(product -> ResponseEntity.ok(List.of(product)))
                    .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
        } else {
            return productService.getAllProducts()
                    .collectList()
                    .map(ResponseEntity::ok);
        }
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable int id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    public Mono<ResponseEntity<?>> saveNewProduct(@RequestBody Product product) {
        return productService.createProduct(product)
                .map(newProduct -> ResponseEntity.status(HttpStatus.CREATED).body(newProduct));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable int id, @RequestBody Mono<Product> updatedProduct) {
        return productService.updateProduct(id, updatedProduct)
                .map(ResponseEntity::ok)
                .onErrorResume(EntityNotFoundException.class, error -> Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id)
                .map(ResponseEntity::ok)
                .onErrorResume(EntityNotFoundException.class, error -> Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping
    public Mono<Void> deleteAllProducts() {
        return productService.deleteAllProducts();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> getProductsAsStream() {
        return productService.getDelayedProducts();
    }
}
