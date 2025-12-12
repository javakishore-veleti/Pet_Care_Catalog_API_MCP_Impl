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
@RequiredArgsConstructor  // This will inject the List<McpTool>
public class McpServer {

    private final Map<String, McpTool> tools = new ConcurrentHashMap<>();
    private final List<McpTool> toolList;  // Spring will inject all McpTool beans

    @PostConstruct
    public void initialize() {
        // Auto-register all MCP tools from Spring context
        for (McpTool tool : toolList) {
            registerMcpTool(tool);
        }
        log.info("MCP Server initialized with {} tools", tools.size());
    }

    public void registerMcpTool(McpTool tool) {
        if (tool.isEnabled()) {
            tools.put(tool.getName(), tool);
            log.info("Registered MCP tool: {} (category: {})",
                    tool.getName(), tool.getCategory());
        } else {
            log.debug("Tool {} is disabled, skipping registration", tool.getName());
        }
    }

    public void unregisterMcpTool(String toolName) {
        McpTool removed = tools.remove(toolName);
        if (removed != null) {
            log.info("Unregistered MCP tool: {}", toolName);
        }
    }

    public List<ToolDefinition> listTools() {
        return tools.values().stream()
                .map(McpTool::getToolDefinition)
                .sorted(Comparator.comparing(ToolDefinition::getName))
                .toList();
    }

    public List<ToolDefinition> listToolsByCategory(String category) {
        return tools.values().stream()
                .filter(tool -> tool.getCategory().equalsIgnoreCase(category))
                .map(McpTool::getToolDefinition)
                .sorted(Comparator.comparing(ToolDefinition::getName))
                .toList();
    }

    public Optional<McpTool> getTool(String toolName) {
        return Optional.ofNullable(tools.get(toolName));
    }

    public ToolResponse executeTool(String toolName, Map<String, Object> arguments) {
        log.debug("Executing tool: {} with arguments: {}", toolName, arguments);

        McpTool tool = tools.get(toolName);
        if (tool == null) {
            log.warn("Tool not found: {}", toolName);
            return ToolResponse.error(toolName, "Tool not found: " + toolName);
        }

        try {
            ToolResponse response = tool.execute(arguments);
            log.debug("Tool {} executed successfully", toolName);
            return response;
        } catch (Exception e) {
            log.error("Error executing tool: {}", toolName, e);
            return ToolResponse.error(toolName,
                    "Execution failed: " + e.getMessage());
        }
    }

    public boolean hasTool(String toolName) {
        return tools.containsKey(toolName);
    }

    public int getToolCount() {
        return tools.size();
    }

    public Set<String> getToolNames() {
        return new HashSet<>(tools.keySet());
    }
}