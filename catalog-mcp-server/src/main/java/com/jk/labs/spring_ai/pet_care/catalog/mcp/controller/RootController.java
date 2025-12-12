package com.jk.labs.spring_ai.pet_care.catalog.mcp.controller;

import com.jk.labs.spring_ai.pet_care.catalog.mcp.McpServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RootController {

    private final McpServer mcpServer;

    @GetMapping("/")
    @ResponseBody
    public Map<String, Object> welcome() {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("service", "Pet Care Catalog - MCP Server");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("timestamp", LocalDateTime.now());
        response.put("port", 8082);

        Map<String, Object> endpoints = getStringObjectMap();

        response.put("endpoints", endpoints);

        Map<String, Object> tools = new LinkedHashMap<>();
        tools.put("count", mcpServer.getToolCount());
        tools.put("available", mcpServer.getToolNames());

        response.put("tools", tools);

        Map<String, String> categories = new LinkedHashMap<>();
        categories.put("search", "Tools for searching packages");
        categories.put("information", "Tools for getting detailed information");
        categories.put("recommendation", "Tools for personalized recommendations");
        categories.put("analysis", "Tools for comparing and analyzing packages");

        response.put("categories", categories);

        Map<String, String> quickStart = new LinkedHashMap<>();
        quickStart.put("1", "GET /mcp/tools - See all available tools");
        quickStart.put("2", "POST /mcp/tools/search_packages with body: {\"petType\":\"DOG\"}");
        quickStart.put("3", "POST /mcp/tools/get_services with body: {}");

        response.put("quickStart", quickStart);

        response.put("documentation", "https://github.com/javakishore-veleti/Pet_Care_Catalog_API_MCP_Impl");

        return response;
    }

    private static Map<String, Object> getStringObjectMap() {
        Map<String, Object> endpoints = new LinkedHashMap<>();
        endpoints.put("tools", "/mcp/tools - List all available MCP tools");
        endpoints.put("tool_details", "/mcp/tools/{toolName} - Get specific tool definition");
        endpoints.put("execute_tool", "POST /mcp/tools/{toolName} - Execute a tool");
        endpoints.put("health", "/mcp/health - MCP server health check");
        endpoints.put("actuator_health", "/actuator/health - Spring actuator health");
        endpoints.put("metrics", "/actuator/metrics - Application metrics");
        return endpoints;
    }
}