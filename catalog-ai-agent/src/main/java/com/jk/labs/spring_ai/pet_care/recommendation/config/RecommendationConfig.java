package com.jk.labs.spring_ai.pet_care.recommendation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "agent.recommendation")
public class RecommendationConfig {

    /**
     * Enable/disable the recommendation agent
     */
    private Boolean enabled = true;

    /**
     * AI model to use for recommendations (future: when integrating with OpenAI)
     */
    private String model = "gpt-4o-mini";

    /**
     * Temperature for AI responses (0.0 = deterministic, 1.0 = creative)
     */
    private Double temperature = 0.5;

    /**
     * Minimum confidence threshold to provide recommendation
     */
    private Double minConfidenceThreshold = 0.6;

    /**
     * Maximum number of alternatives to return
     */
    private Integer maxAlternatives = 3;

    /**
     * Age thresholds for dogs (in months)
     */
    private AgeThresholds dogAgeThresholds = new AgeThresholds(12, 84);

    /**
     * Age thresholds for cats (in months)
     */
    private AgeThresholds catAgeThresholds = new AgeThresholds(12, 132);

    /**
     * Default package mappings by age group
     */
    private Map<String, String> defaultPackages = Map.of(
            "PUPPY", "TIMELY_CARE",
            "KITTEN", "TIMELY_CARE",
            "ADULT", "GROWNUP_CARE",
            "SENIOR", "ELDER_CARE"
    );

    @Data
    public static class AgeThresholds {
        private Integer puppyMaxMonths;
        private Integer seniorMinMonths;

        public AgeThresholds() {
            this.puppyMaxMonths = 12;
            this.seniorMinMonths = 84;
        }

        public AgeThresholds(Integer puppyMaxMonths, Integer seniorMinMonths) {
            this.puppyMaxMonths = puppyMaxMonths;
            this.seniorMinMonths = seniorMinMonths;
        }
    }
}