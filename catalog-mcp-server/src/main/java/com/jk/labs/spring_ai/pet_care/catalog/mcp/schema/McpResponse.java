package com.jk.labs.spring_ai.pet_care.catalog.mcp.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpResponse {

    private String requestId;
    private Boolean success;
    private Object result;
    private String error;
    private String errorCode;
    private String toolName;
    private LocalDateTime timestamp;

    public static McpResponse success(String requestId, Object result) {
        return McpResponse.builder()
                .requestId(requestId)
                .success(true)
                .result(result)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static McpResponse error(String requestId, String error, String errorCode) {
        return McpResponse.builder()
                .requestId(requestId)
                .success(false)
                .error(error)
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
