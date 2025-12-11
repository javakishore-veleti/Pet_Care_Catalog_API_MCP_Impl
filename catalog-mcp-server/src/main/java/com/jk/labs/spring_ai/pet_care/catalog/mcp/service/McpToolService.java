package com.jk.labs.spring_ai.pet_care.catalog.mcp.service;

import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.McpRequest;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.McpResponse;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolDefinition;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolResponse;

import java.util.List;
import java.util.Map;

public interface McpToolService {

    /**
     * List all available tools
     */
    List<ToolDefinition> listAllTools();

    /**
     * Get a specific tool definition
     */
    ToolDefinition getToolDefinition(String toolName);

    /**
     * Execute a tool
     */
    ToolResponse executeTool(String toolName, Map<String, Object> arguments);

    /**
     * Process an MCP request
     */
    McpResponse processRequest(McpRequest request);

    /**
     * Check if a tool exists
     */
    boolean toolExists(String toolName);

    /**
     * Get tools by category
     */
    List<ToolDefinition> getToolsByCategory(String category);
}
