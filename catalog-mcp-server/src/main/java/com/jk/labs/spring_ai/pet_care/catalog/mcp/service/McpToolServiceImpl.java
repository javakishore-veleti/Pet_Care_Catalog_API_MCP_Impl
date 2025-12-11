package com.jk.labs.spring_ai.pet_care.catalog.mcp.service;

import com.jk.labs.spring_ai.pet_care.catalog.common.exception.McpException;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.McpServer;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.McpTool;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.McpRequest;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.McpResponse;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolDefinition;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpToolServiceImpl implements McpToolService{

    private final McpServer mcpServer;

    @Override
    public List<ToolDefinition> listAllTools() {
        log.debug("Listing all MCP tools");
        return mcpServer.listTools();
    }

    @Override
    public ToolDefinition getToolDefinition(String toolName) {
        log.debug("Getting tool definition for: {}", toolName);

        return mcpServer.getTool(toolName)
                .map(McpTool::getToolDefinition)
                .orElseThrow(() -> new McpException("Tool not found: " + toolName, toolName));
    }

    @Override
    public ToolResponse executeTool(String toolName, Map<String, Object> arguments) {
        log.info("Executing MCP tool: {} with arguments: {}", toolName, arguments);

        if (!mcpServer.hasTool(toolName)) {
            log.warn("Attempted to execute non-existent tool: {}", toolName);
            return ToolResponse.error(toolName, "Tool not found: " + toolName);
        }

        try {
            return mcpServer.executeTool(toolName, arguments);
        } catch (Exception e) {
            log.error("Error executing tool: {}", toolName, e);
            return ToolResponse.error(toolName,
                    "Tool execution failed: " + e.getMessage());
        }
    }

    @Override
    public McpResponse processRequest(McpRequest request) {
        log.info("Processing MCP request: {} - {}",
                request.getRequestId(), request.getMethod());

        try {
            switch (request.getMethod()) {
                case "tools/list":
                    return McpResponse.success(
                            request.getRequestId(),
                            listAllTools());

                case "tools/call":
                    ToolResponse toolResponse = executeTool(
                            request.getToolName(),
                            request.getArguments());

                    if (toolResponse.getSuccess()) {
                        return McpResponse.success(
                                request.getRequestId(),
                                toolResponse);
                    } else {
                        return McpResponse.error(
                                request.getRequestId(),
                                toolResponse.getError(),
                                "TOOL_EXECUTION_ERROR");
                    }

                default:
                    return McpResponse.error(
                            request.getRequestId(),
                            "Unknown method: " + request.getMethod(),
                            "INVALID_METHOD");
            }
        } catch (Exception e) {
            log.error("Error processing MCP request", e);
            return McpResponse.error(
                    request.getRequestId(),
                    e.getMessage(),
                    "INTERNAL_ERROR");
        }
    }

    @Override
    public boolean toolExists(String toolName) {
        return mcpServer.hasTool(toolName);
    }

    @Override
    public List<ToolDefinition> getToolsByCategory(String category) {
        log.debug("Getting tools by category: {}", category);
        return mcpServer.listToolsByCategory(category);
    }
}
