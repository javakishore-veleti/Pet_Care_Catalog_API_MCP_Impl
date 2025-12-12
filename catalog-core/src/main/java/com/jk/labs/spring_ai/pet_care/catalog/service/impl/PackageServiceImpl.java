package com.jk.labs.spring_ai.pet_care.catalog.service.impl;

import com.jk.labs.spring_ai.pet_care.catalog.common.dto.PackageDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.dto.PackageComparisonDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.dto.PackageRecommendationDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.AgeGroup;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.PetType;
import com.jk.labs.spring_ai.pet_care.catalog.common.service.PackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class PackageServiceImpl implements PackageService {

    @Override
    public List<PackageDTO> getAllPackages() {
        log.debug("getAllPackages called");
        return createMockPackages();
    }

    @Override
    public PackageDTO getPackageByCode(String code) {
        log.debug("getPackageByCode: {}", code);
        return createMockPackages().stream()
                .filter(p -> p.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<PackageDTO> searchPackages(PetType petType, AgeGroup ageGroup,
                                           Boolean hasDentalNeeds, Boolean needsSpayNeuter) {
        log.debug("searchPackages - petType: {}, ageGroup: {}", petType, ageGroup);
        return createMockPackages().stream()
                .filter(p -> petType == null || p.getPetType().equals(petType.name()))
                .filter(p -> ageGroup == null || p.getAgeGroup().equals(ageGroup.name()))
                .toList();
    }

    @Override
    public PackageRecommendationDTO recommendPackage(PetType petType, Integer ageYears,
                                                     Integer ageMonths, Boolean hasChronicConditions,
                                                     Boolean needsDentalCare, Boolean needsSpayNeuter,
                                                     Double budget) {
        log.debug("recommendPackage called");
        PackageDTO pkg = createMockPackages().get(0);
        return PackageRecommendationDTO.builder()
                .recommendedPackage(pkg)
                .reason("Based on your pet profile, this package is recommended")
                .estimatedAnnualCost(pkg.getBasePrice())
                .estimatedSavings(BigDecimal.valueOf(200))
                .keyFeatures(List.of("Vaccinations", "Wellness Exams", "Diagnostics"))
                .alternatives(List.of())
                .build();
    }

    @Override
    public PackageComparisonDTO comparePackages(List<String> packageCodes) {
        log.debug("comparePackages: {}", packageCodes);
        List<PackageDTO> packages = createMockPackages();
        return PackageComparisonDTO.builder()
                .packages(packages)
                .build();
    }

    @Override
    public Map<String, Object> calculateSavings(String packageCode) {
        log.debug("calculateSavings: {}", packageCode);
        return Map.of(
                "packageCode", packageCode,
                "packagePrice", 550.00,
                "individualServicesPrice", 785.00,
                "savings", 235.00,
                "savingsPercentage", 30.0
        );
    }

    private List<PackageDTO> createMockPackages() {
        return List.of(
                PackageDTO.builder()
                        .id(1L)
                        .code("DOG_ACTIVE_CARE")
                        .name("Active Care for Dogs")
                        .description("Comprehensive care for adult dogs")
                        .petType("DOG")
                        .ageGroup("ADULT")
                        .careLevel("ACTIVE_CARE")
                        .basePrice(BigDecimal.valueOf(550.00))
                        .savingsPercentage(BigDecimal.valueOf(30.00))
                        .includesDental(false)
                        .includesSpayNeuter(false)
                        .build()
        );
    }
}