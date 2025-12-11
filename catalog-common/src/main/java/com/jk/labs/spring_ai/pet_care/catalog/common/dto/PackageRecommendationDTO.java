package com.jk.labs.spring_ai.pet_care.catalog.common.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageRecommendationDTO {

    @NotNull
    private PackageDTO recommendedPackage;

    private String reason;
    private BigDecimal estimatedAnnualCost;
    private BigDecimal estimatedSavings;
    private List<String> keyFeatures;
    private List<PackageDTO> alternatives;
    private Double confidenceScore;
}
