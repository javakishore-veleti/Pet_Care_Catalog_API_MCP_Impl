package com.jk.labs.spring_ai.pet_care.comparison.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "agent.comparison")
public class ComparisonConfig {

    /**
     * Enable/disable the comparison agent
     */
    private Boolean enabled = true;

    /**
     * AI model to use (future: when integrating with OpenAI)
     */
    private String model = "gpt-4o-mini";

    /**
     * Temperature for AI responses
     */
    private Double temperature = 0.3;

    /**
     * Maximum number of packages to compare at once
     */
    private Integer maxPackagesToCompare = 4;

    /**
     * Minimum packages required for comparison
     */
    private Integer minPackagesToCompare = 2;

    /**
     * Default features to always include in comparison matrix
     */
    private String[] defaultComparisonFeatures = {
            "Monthly Price",
            "Annual Price",
            "Services Included",
            "Dental Coverage",
            "Spay/Neuter Coverage"
    };

    /**
     * Weight for price in scoring (0-1)
     */
    private Double priceWeight = 0.3;

    /**
     * Weight for coverage/services in scoring (0-1)
     */
    private Double coverageWeight = 0.4;

    /**
     * Weight for value score in scoring (0-1)
     */
    private Double valueWeight = 0.3;
}