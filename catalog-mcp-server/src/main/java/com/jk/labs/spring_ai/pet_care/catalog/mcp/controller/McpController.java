package com.jk.labs.spring_ai.pet_care.catalog.mcp.controller;

import com.jk.labs.spring_ai.pet_care.catalog.common.dto.ApiResponse;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.*;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.service.McpToolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mcp")
@RequiredArgsConstructor
public class McpController {

    private final McpToolService mcpToolService;

    @GetMapping("/tools")
    public ResponseEntity<ApiResponse<List<ToolDefinition>>> listTools() {
        log.info("GET /mcp/tools - Listing all tools");
        List<ToolDefinition> tools = mcpToolService.listAllTools();
        return ResponseEntity.ok(ApiResponse.success(
                "Retrieved " + tools.size() + " tools", tools));
    }

    @GetMapping("/tools/{toolName}")
    public ResponseEntity<ApiResponse<ToolDefinition>> getToolDefinition(
            @PathVariable String toolName) {
        log.info("GET /mcp/tools/{} - Getting tool definition", toolName);
        ToolDefinition tool = mcpToolService.getToolDefinition(toolName);
        return ResponseEntity.ok(ApiResponse.success(tool));
    }

    @PostMapping("/tools/{toolName}")
    public ResponseEntity<ToolResponse> executeTool(
            @PathVariable String toolName,
            @RequestBody(required = false) Map<String, Object> arguments) {
        log.info("POST /mcp/tools/{} - Executing tool", toolName);

        if (arguments == null) {
            arguments = Map.of();
        }

        ToolResponse response = mcpToolService.executeTool(toolName, arguments);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/request")
    public ResponseEntity<McpResponse> processRequest(@RequestBody McpRequest request) {
        log.info("POST /mcp/request - Processing MCP request: {}",
                request.getRequestId());

        McpResponse response = mcpToolService.processRequest(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tools/category/{category}")
    public ResponseEntity<ApiResponse<List<ToolDefinition>>> getToolsByCategory(
            @PathVariable String category) {
        log.info("GET /mcp/tools/category/{} - Getting tools by category", category);
        List<ToolDefinition> tools = mcpToolService.getToolsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(tools));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "mcp-server",
                "tools", mcpToolService.listAllTools().size(),
                "timestamp", System.currentTimeMillis()
        ));
    }
}