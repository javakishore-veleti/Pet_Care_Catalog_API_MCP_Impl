package com.jk.labs.spring_ai.pet_care.recommendation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequest {

    private String sessionId;

    // Pet Information
    private String petType;        // DOG, CAT
    private String petName;
    private Integer ageYears;
    private Integer ageMonths;
    private String breed;

    // Health & Care Needs
    private Boolean needsDentalCare;
    private Boolean needsVaccinations;
    private Boolean hasChronicConditions;
    private List<String> existingConditions;

    // Preferences
    private String budgetPreference;  // BASIC, STANDARD, PREMIUM
    private Boolean preferComprehensive;

    // Context from conversation
    private String userQuery;
    private String conversationContext;
}