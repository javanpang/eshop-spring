package com.es.productservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productServiceOpenAPI() {
        return new OpenAPI().info(new Info().title("Product Service API")
                        .description("Manages the Products for the e-shop")
                        .version("1.0"))
                .servers(List.of(new Server().url("http://localhost:8080/api")
                        .description("Local development"), new Server().url("https://api.eshop.com")
                        .description("Production")));
    }
}