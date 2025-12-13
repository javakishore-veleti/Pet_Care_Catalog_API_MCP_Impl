package com.jk.labs.spring_ai.pet_care.common.models.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.ai.openai")
public class OpenAiConfig {
    private String apiKey;
    private String model;
    private Double temperature;
    private Integer maxTokens;
}
