package com.jk.labs.spring_ai.pet_care.catalog.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceDTO {

    private Long id;

    @NotBlank(message = "Service code is required")
    private String code;

    @NotBlank(message = "Service name is required")
    private String name;

    private String description;
    private String category;

    @DecimalMin(value = "0.0", message = "Price must be positive")
    private BigDecimal individualPrice;

    private Boolean isVirtual;
    private Integer quantity;
}
