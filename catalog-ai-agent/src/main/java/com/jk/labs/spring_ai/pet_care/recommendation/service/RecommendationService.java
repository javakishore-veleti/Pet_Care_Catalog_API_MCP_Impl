package com.jk.labs.spring_ai.pet_care.recommendation.service;

import com.jk.labs.spring_ai.pet_care.recommendation.model.RecommendationRequest;
import com.jk.labs.spring_ai.pet_care.recommendation.model.RecommendationResponse;

public interface RecommendationService {

    /**
     * Generate package recommendation based on pet profile and needs
     */
    RecommendationResponse recommend(RecommendationRequest request);

    /**
     * Quick recommendation based on minimal info (pet type + age)
     */
    RecommendationResponse quickRecommend(String petType, Integer ageYears, Integer ageMonths);

    /**
     * Re-evaluate recommendation with additional context
     */
    RecommendationResponse refineRecommendation(String sessionId, String additionalContext);
}