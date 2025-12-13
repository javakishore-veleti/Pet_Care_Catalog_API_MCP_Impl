package com.jk.labs.spring_ai.pet_care.common.mcp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolResponse {
    private Boolean success;
    private Object content;
    private String error;
}
