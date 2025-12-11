package com.jk.labs.spring_ai.pet_care.catalog.mcp;

import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolDefinition;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolResponse;

import java.util.Map;

public interface McpTool {

    // unique name for this tool
    String getName();

    // Tool Definition including schema
    ToolDefinition getToolDefinition();

    // Execute the tool with given arguments
    ToolResponse execute(Map<String, Object> arguments);

    default boolean isEnabled() {
        return true;
    }

    // Category for the organization
    default String getCategory() {
        return "General";
    }



}
