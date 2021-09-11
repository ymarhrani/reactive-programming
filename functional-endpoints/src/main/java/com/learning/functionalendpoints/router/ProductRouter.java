package com.learning.functionalendpoints.router;

import com.learning.functionalendpoints.handler.ProductHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ProductHandler productHandler) {
        return RouterFunctions.route()
                .GET("/products/{id}", productHandler::getProductById)
                .GET("/products", productHandler::getAllProducts)
                .POST("/products", productHandler::addNewProduct)
                .PUT("/products/{id}", productHandler::updateProduct)
                .DELETE("/products/{id}", productHandler::deleteProduct)
                .DELETE("/products", productHandler::deleteAll)
                .build();
    }
}
