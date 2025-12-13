package com.jk.labs.spring_ai.pet_care.common.mcp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mcp.client")
public class McpClientConfig {
    private String baseUrl = "http://localhost:8082";
    private Integer timeout = 30000;
    private Integer retries = 3;
}
