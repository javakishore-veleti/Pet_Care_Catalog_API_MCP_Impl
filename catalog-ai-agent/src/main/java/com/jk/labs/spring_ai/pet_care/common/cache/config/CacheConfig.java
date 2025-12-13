package com.jk.labs.spring_ai.pet_care.common.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cache")
public class CacheConfig {
    private Boolean enabled = true;
    private Long defaultTtl = 3600L;
    private Integer maxSize = 1000;
}
