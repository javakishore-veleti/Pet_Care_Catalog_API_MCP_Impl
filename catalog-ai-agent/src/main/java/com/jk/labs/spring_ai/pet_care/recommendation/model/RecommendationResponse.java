package com.jk.labs.spring_ai.pet_care.recommendation.model;

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
public class RecommendationResponse {

    private String agentName;

    // Primary Recommendation
    private PackageRecommendation primaryRecommendation;

    // Alternative Options
    private List<PackageRecommendation> alternatives;

    // Explanation
    private String reasoning;
    private List<String> keyBenefits;
    private List<String> considerations;

    // Confidence & Metadata
    private Double confidence;
    private String recommendationType;  // PERFECT_MATCH, BEST_FIT, BUDGET_CONSCIOUS

    // For handoff to other agents
    private Boolean suggestComparison;
    private String nextAgentSuggestion;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackageRecommendation {
        private String packageCode;
        private String packageName;
        private String description;
        private BigDecimal monthlyPrice;
        private BigDecimal annualPrice;
        private List<String> includedServices;
        private Double matchScore;
        private String matchReason;
    }
}