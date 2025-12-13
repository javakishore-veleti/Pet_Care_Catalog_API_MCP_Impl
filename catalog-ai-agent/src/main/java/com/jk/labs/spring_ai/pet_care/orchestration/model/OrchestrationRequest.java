package com.jk.labs.spring_ai.pet_care.orchestration.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrchestrationRequest {
private String query;
private String sessionId;
}








