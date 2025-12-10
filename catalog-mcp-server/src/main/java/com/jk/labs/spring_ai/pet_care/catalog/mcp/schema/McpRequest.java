package com.jk.labs.spring_ai.pet_care.catalog.mcp.schema;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class McpRequest {

    private String method;
    private String toolName;
    private Map<String, Object> arguments;
    private String requestId;
    private String version;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

}
