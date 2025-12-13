package com.jk.labs.spring_ai.pet_care.common.models.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LLMRequest {
    private String model;
    private List<ChatMessage> messages;
    private Double temperature;
    private Integer maxTokens;
    private Map<String, Object> tools;
}
