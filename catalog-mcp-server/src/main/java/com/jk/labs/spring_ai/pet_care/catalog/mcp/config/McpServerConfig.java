package com.jk.labs.spring_ai.pet_care.catalog.mcp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "mcp.server")
public class McpServerConfig {

    private Boolean enabled = true;
    private String version = "1.0.0";
    private Integer port = 8081;
    private Protocol protocol = new Protocol();
    private Map<String, ToolConfig> tools = new HashMap<>();

    @Data
    public static class Protocol {
        private Integer timeout = 30000;
        private Long maxRequestSize = 10485760L; // 10MB
    }

    @Data
    public static class ToolConfig {
        private Boolean enabled = true;
        private Integer rateLimit;
        private Integer cacheTtl;
    }

    public boolean isToolEnabled(String toolName) {
        ToolConfig config = tools.get(toolName);
        return config == null || config.getEnabled();
    }
}