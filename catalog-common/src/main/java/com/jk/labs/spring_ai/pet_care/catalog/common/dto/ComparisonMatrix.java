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
public class ComparisonMatrix {

    private List<String> features;
    private List<PackageFeatureComparison> comparisons;
}
