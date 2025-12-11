package com.jk.labs.spring_ai.pet_care.catalog.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageFeatureComparison {

    private String packageCode;
    private String packageName;
    private List<Boolean> hasFeature;
    private BigDecimal totalPrice;
}
