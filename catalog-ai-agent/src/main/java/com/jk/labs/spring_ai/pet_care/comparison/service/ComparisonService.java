package com.jk.labs.spring_ai.pet_care.comparison.service;

import com.jk.labs.spring_ai.pet_care.comparison.model.ComparisonRequest;
import com.jk.labs.spring_ai.pet_care.comparison.model.ComparisonResponse;

import java.util.List;

public interface ComparisonService {

    /**
     * Full comparison with all details
     */
    ComparisonResponse compare(ComparisonRequest request);

    /**
     * Quick comparison of two packages by code
     */
    ComparisonResponse quickCompare(String packageCode1, String packageCode2);

    /**
     * Compare multiple packages
     */
    ComparisonResponse compareMultiple(List<String> packageCodes);

    /**
     * Compare packages with specific focus areas
     */
    ComparisonResponse compareWithFocus(List<String> packageCodes, List<String> focusFeatures);

    /**
     * Get comparison for a specific pet context
     */
    ComparisonResponse compareForPet(List<String> packageCodes, String petType, Integer ageYears);
}