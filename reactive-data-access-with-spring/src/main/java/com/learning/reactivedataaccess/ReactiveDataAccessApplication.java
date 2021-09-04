package com.learning.reactivedataaccess;

import com.learning.reactivedataaccess.model.Product;
import com.learning.reactivedataaccess.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactiveDataAccessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveDataAccessApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository) {
        return args -> {
			Flux<Product> productsFlux = Flux.range(2, 10)
					.map(val -> new Product("PC " + val, val * 1000));
			productRepository.deleteAll()
                    .thenMany(productRepository.saveAll(productsFlux))
					.thenMany(productRepository.findAll()
							.doOnNext(System.out::println))
					.subscribe();
        };
    }
}
