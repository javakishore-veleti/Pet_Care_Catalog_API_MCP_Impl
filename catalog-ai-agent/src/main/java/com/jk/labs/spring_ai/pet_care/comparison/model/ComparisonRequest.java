package com.jk.labs.spring_ai.pet_care.comparison.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComparisonRequest {

    /**
     * Session ID for conversation continuity
     */
    private String sessionId;

    /**
     * Package codes to compare (2-4 packages)
     */
    private List<String> packageCodes;

    /**
     * Pet context for relevant comparison
     */
    private String petType;
    private Integer petAgeYears;
    private Integer petAgeMonths;

    /**
     * User's priorities for weighted comparison
     */
    private List<String> priorities;  // e.g., ["price", "dental", "comprehensive"]

    /**
     * Budget constraint (optional)
     */
    private Double maxBudget;

    /**
     * Specific features to focus comparison on
     */
    private List<String> focusFeatures;  // e.g., ["dental", "vaccinations", "diagnostics"]

    /**
     * Natural language query for context
     */
    private String userQuery;
}