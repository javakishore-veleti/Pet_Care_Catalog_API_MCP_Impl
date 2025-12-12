package com.jk.labs.spring_ai.pet_care.catalog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI petCareApiConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pet Care Catalog API")
                        .description("REST API for Pet Care Wellness Packages")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("JK Labs")
                                .email("support@jklabs.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://localhost:8080")
                                .description("Production Server")));
    }
}