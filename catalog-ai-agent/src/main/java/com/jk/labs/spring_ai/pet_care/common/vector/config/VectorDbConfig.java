package com.jk.labs.spring_ai.pet_care.common.vector.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "vector.db")
public class VectorDbConfig {
    private Boolean enabled = false;
    private String provider = "chromadb";
    private String host = "localhost";
    private Integer port = 8000;
}
