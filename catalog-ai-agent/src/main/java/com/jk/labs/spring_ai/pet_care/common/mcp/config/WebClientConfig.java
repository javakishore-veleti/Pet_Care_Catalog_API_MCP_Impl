package com.jk.labs.spring_ai.pet_care.common.mcp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(McpClientConfig mcpClientConfig) {
        return WebClient.builder()
                .baseUrl(mcpClientConfig.getBaseUrl())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}