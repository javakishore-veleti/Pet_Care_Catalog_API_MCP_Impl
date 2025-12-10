package com.jk.labs.spring_ai.pet_care.catalog.mcp.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToolDefinition {

    private String name;
    private String description;
    private Map<String, Object> inputSchema;
    private String version;
    private Boolean enabled;

    public static ToolDefinition create(String name, String description, Map<String, Object> inputSchema) {
        return ToolDefinition.builder()
                .name(name)
                .description(description)
                .inputSchema(inputSchema)
                .version("1.0.0")
                .enabled(true)
                .build();
    }


}
