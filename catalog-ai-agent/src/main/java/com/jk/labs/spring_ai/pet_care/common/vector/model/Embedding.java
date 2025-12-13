package com.jk.labs.spring_ai.pet_care.common.vector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Embedding {
    private String id;
    private List<Float> vector;
    private String text;
    private Object metadata;
}
