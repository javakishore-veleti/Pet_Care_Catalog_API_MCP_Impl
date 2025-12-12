package com.jk.labs.spring_ai.pet_care.catalog.common.service;

import com.jk.labs.spring_ai.pet_care.catalog.common.dto.PackageComparisonDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.dto.PackageDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.dto.PackageRecommendationDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.AgeGroup;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.PetType;

import java.util.List;
import java.util.Map;

public interface PackageService {

    /**
     * Get all active packages
     */
    List<PackageDTO> getAllPackages();

    /**
     * Get package by code
     */
    PackageDTO getPackageByCode(String code);

    /**
     * Search packages by criteria
     */
    List<PackageDTO> searchPackages(
            PetType petType,
            AgeGroup ageGroup,
            Boolean hasDentalNeeds,
            Boolean needsSpayNeuter);

    /**
     * Get package recommendation based on pet profile
     */
    PackageRecommendationDTO recommendPackage(
            PetType petType,
            Integer ageYears,
            Integer ageMonths,
            Boolean hasChronicConditions,
            Boolean needsDentalCare,
            Boolean needsSpayNeuter,
            Double budget);

    /**
     * Compare multiple packages
     */
    PackageComparisonDTO comparePackages(List<String> packageCodes);

    /**
     * Calculate savings for a package
     */
    Map<String, Object> calculateSavings(String packageCode);

}
