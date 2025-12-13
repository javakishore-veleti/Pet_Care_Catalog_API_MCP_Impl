package com.jk.labs.spring_ai.pet_care.common.vector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VectorSearchResult {
    private String id;
    private Float score;
    private String text;
    private Object metadata;
}
