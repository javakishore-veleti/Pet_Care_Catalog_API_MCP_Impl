package com.jk.labs.spring_ai.pet_care.comparison.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComparisonResponse {

    private String agentName;

    /**
     * Packages being compared
     */
    private List<PackageComparison> packages;

    /**
     * Side-by-side feature matrix
     */
    private FeatureMatrix featureMatrix;

    /**
     * Price comparison
     */
    private PriceComparison priceComparison;

    /**
     * Winner/recommendation based on comparison
     */
    private ComparisonWinner winner;

    /**
     * Summary text for the comparison
     */
    private String summary;

    /**
     * Key differences highlighted
     */
    private List<String> keyDifferences;

    /**
     * Confidence in comparison
     */
    private Double confidence;

    /**
     * Suggested next action
     */
    private String nextAgentSuggestion;

    // ==================== Inner Classes ====================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackageComparison {
        private String packageCode;
        private String packageName;
        private String description;
        private BigDecimal monthlyPrice;
        private BigDecimal annualPrice;
        private List<String> includedServices;
        private Integer serviceCount;
        private Boolean includesDental;
        private Boolean includesSpayNeuter;
        private String targetAgeGroup;
        private String careLevel;
        private Double valueScore;  // Calculated value for money
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeatureMatrix {
        /**
         * Feature name -> Package Code -> Available (true/false or value)
         * Example: { "Dental Cleaning": { "DOG_CARE_PLUS": true, "DOG_CARE": false } }
         */
        private Map<String, Map<String, Object>> features;

        /**
         * List of all features being compared
         */
        private List<String> featureList;

        /**
         * List of package codes in comparison order
         */
        private List<String> packageOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceComparison {
        private String cheapestPackage;
        private String mostExpensivePackage;
        private BigDecimal priceDifference;
        private BigDecimal averagePrice;
        private String bestValuePackage;
        private Map<String, BigDecimal> monthlyPrices;
        private Map<String, BigDecimal> annualPrices;
        private Map<String, BigDecimal> savingsVsIndividual;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparisonWinner {
        private String packageCode;
        private String packageName;
        private String winReason;
        private List<String> advantages;
        private List<String> disadvantages;
        private Double matchScore;

        /**
         * Category-specific winners
         */
        private String bestForBudget;
        private String bestForCoverage;
        private String bestForValue;
    }
}