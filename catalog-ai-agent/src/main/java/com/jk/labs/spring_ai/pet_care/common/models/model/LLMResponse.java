package com.jk.labs.spring_ai.pet_care.common.models.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LLMResponse {
    private String content;
    private String finishReason;
    private Integer tokensUsed;
}
