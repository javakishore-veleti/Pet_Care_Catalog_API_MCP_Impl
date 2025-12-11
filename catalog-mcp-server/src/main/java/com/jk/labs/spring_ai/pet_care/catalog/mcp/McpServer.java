package com.jk.labs.spring_ai.pet_care.catalog.mcp;

import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolDefinition;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class McpServer {

    private final Map<String, McpTool> tools;

    public McpServer() {
        this.tools = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void setup() {
        // Auto-register tools to Spring Context
        for(McpTool tool : tools.values()) {
            registerMcpTool(tool);
        }
    }

    public void registerMcpTool(McpTool tool) {
        if(tool.isEnabled()) {
            tools.put(tool.getName(), tool);
            log.info("Registered MCP Tool: {}", tool.getName());
        }
    }

    public void unregisterMcpTool(String toolName) {
        McpTool tool = tools.remove(toolName);
        log.info("Unregistered MCP Tool: {}", toolName);
    }

    public List<ToolDefinition> listTools() {
        return tools.values().stream().map(McpTool::getToolDefinition).sorted(Comparator.comparing(ToolDefinition::getName)).toList();
    }

    public List<ToolDefinition> listToolsByCategory(String category) {
        return tools.values().stream().filter(tool -> tool.getCategory().equalsIgnoreCase(category)).map(
                McpTool::getToolDefinition).sorted(Comparator.comparing(ToolDefinition::getName)).toList();
    }

    public Optional<McpTool> getTool(String toolName) {
        return Optional.ofNullable(tools.get(toolName));
    }

    public ToolResponse executeTool(String toolName, Map<String, Object> arguments) {
        McpTool tool = tools.get(toolName);
        if(tool == null) {
            return ToolResponse.of(toolName, "Tool not found " + toolName);
        }
        try {
            return tool.execute(arguments);
        } catch (Exception e) {
            return ToolResponse.of(toolName,  "Execution failed: " + e.getMessage());
        }
    }

    /**
     * Check if a tool exists
     */
    public boolean hasTool(String toolName) {
        return tools.containsKey(toolName);
    }

    /**
     * Get count of registered tools
     */
    public int getToolCount() {
        return tools.size();
    }

    /**
     * Get all tool names
     */
    public Set<String> getToolNames() {
        return new HashSet<>(tools.keySet());
    }

}
