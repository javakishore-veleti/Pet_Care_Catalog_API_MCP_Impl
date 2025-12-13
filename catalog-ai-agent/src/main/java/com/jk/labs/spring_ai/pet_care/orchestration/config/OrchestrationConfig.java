package com.jk.labs.spring_ai.pet_care.orchestration.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
@ConfigurationProperties(prefix = "orchestration")
public class OrchestrationConfig {
    private String strategy = "AUTO";
    private Integer maxAgentCalls = 5;
    private Boolean enableHandoff = true;
}