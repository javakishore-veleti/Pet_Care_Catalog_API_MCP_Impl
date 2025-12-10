package com.jk.labs.spring_ai.pet_care.catalog.mcp.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToolResponse {

    private Boolean success;
    private Object content;
    private String error;
    private String toolName;
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;

    public static ToolResponse of(String toolName, Object content, Map<String, Object> metadata) {
        return ToolResponse.builder().success(true).toolName(toolName).content(content).metadata(metadata).timestamp(LocalDateTime.now()).build();
    }

    public static ToolResponse of(String toolName, String error) {
        return ToolResponse.builder().success(false).toolName(toolName).error(error).timestamp(LocalDateTime.now()).build();
    }
}
