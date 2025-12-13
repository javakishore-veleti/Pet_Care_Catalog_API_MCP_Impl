package com.jk.labs.spring_ai.pet_care.comparison.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "agent.comparison")
public class ComparisonConfig {
    private Boolean enabled = true;
    private String model = "gpt-4o-mini";
    private Double temperature = 0.7;
}
