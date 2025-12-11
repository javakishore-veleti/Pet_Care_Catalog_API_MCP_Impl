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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageDTO {

    private Long id;

    @NotBlank(message = "Package code is required")
    private String code;

    @NotBlank(message = "Package name is required")
    private String name;

    private String description;

    @NotNull(message = "Pet type is required")
    private String petType;

    @NotNull(message = "Age group is required")
    private String ageGroup;

    @NotNull(message = "Care level is required")
    private String careLevel;

    @DecimalMin(value = "0.0", message = "Base price must be positive")
    private BigDecimal basePrice;

    @DecimalMin(value = "0.0", message = "Savings percentage must be positive")
    @DecimalMax(value = "100.0", message = "Savings percentage cannot exceed 100")
    private BigDecimal savingsPercentage;

    private Boolean includesDental;
    private Boolean includesSpayNeuter;

    private List<ServiceDTO> services;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
