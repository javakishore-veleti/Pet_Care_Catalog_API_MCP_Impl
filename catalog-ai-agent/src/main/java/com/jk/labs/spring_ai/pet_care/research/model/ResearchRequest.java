package com.jk.labs.spring_ai.pet_care.research.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResearchRequest {
    private String query;
    private String sessionId;
    private Integer depth; // How deep to research
}