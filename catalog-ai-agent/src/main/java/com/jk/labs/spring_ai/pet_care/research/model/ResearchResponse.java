package com.jk.labs.spring_ai.pet_care.research.model;

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
public class ResearchResponse {
    private String summary;
    private List<Map<String, Object>> findings;
    private Double confidence;
    private String agentName;
}