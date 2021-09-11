package com.learning.functionalendpoints.handler;

import com.learning.functionalendpoints.model.Product;
import com.learning.functionalendpoints.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductHandler {
    private final ProductRepository productRepository;

    public Mono<ServerResponse> getAllProducts(ServerRequest request) {
        return ServerResponse.ok().body(productRepository.findAll(), Product.class);
    }

    public Mono<ServerResponse> getProductById(ServerRequest request) {
        String id = request.pathVariable("id");
        return productRepository.findById(Integer.valueOf(id))
                .flatMap(product -> ServerResponse.ok().bodyValue(product))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> addNewProduct(ServerRequest request) {
        Mono<Product> productMono = request.bodyToMono(Product.class);
        return productMono
                .flatMap(productRepository::save)
                .flatMap(savedProduct -> ServerResponse.ok().bodyValue(savedProduct));
    }

    public Mono<ServerResponse> updateProduct(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Product> productMono = request.bodyToMono(Product.class);
        return productRepository.findById(Integer.valueOf(id))
                .zipWith(productMono)
                .map(products -> refreshProductData(products.getT1(), products.getT2()))
                .flatMap(productRepository::save)
                .flatMap(product -> ServerResponse.ok().bodyValue(product))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        String id = request.pathVariable("id");
        return productRepository.findById(Integer.valueOf(id))
                .flatMap(product -> ServerResponse.ok().body(productRepository.delete(product), Void.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteAll(ServerRequest request) {
        return productRepository.deleteAll()
                .then(ServerResponse.ok().build());
    }

    private Product refreshProductData(Product dbProduct, Product newProduct) {
        dbProduct.setAmount(newProduct.getAmount());
        dbProduct.setName(newProduct.getName());
        return dbProduct;
    }
}
