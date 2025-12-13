package com.jk.labs.spring_ai.pet_care.orchestration.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionPlan {
    private List<AgentTask> tasks;
    private String strategy;
}