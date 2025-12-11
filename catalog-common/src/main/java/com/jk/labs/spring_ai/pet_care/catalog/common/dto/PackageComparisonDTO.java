package com.jk.labs.spring_ai.pet_care.catalog.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageComparisonDTO {

    private List<PackageDTO> packages;
    private ComparisonMatrix matrix;
    private String recommendation;
}
