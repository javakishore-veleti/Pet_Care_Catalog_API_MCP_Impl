package com.jk.labs.spring_ai.pet_care.common.mcp.service.impl;

import com.jk.labs.spring_ai.pet_care.common.mcp.config.McpClientConfig;
import com.jk.labs.spring_ai.pet_care.common.mcp.model.ToolRequest;
import com.jk.labs.spring_ai.pet_care.common.mcp.model.ToolResponse;
import com.jk.labs.spring_ai.pet_care.common.mcp.service.McpClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpRestClientServiceImpl implements McpClientService {

    private final McpClientConfig config;
    private final RestTemplate restTemplate;

    @Override
    public ToolResponse callTool(ToolRequest request) {
        log.info("Calling MCP tool: {} with args: {}", request.getToolName(), request.getArguments());

        try {
            String url = config.getBaseUrl() + "/mcp/tools/" + request.getToolName();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = request.getArguments() != null ? request.getArguments() : Map.of();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);

            log.debug("MCP tool response: {}", response);

            if (response == null) {
                return ToolResponse.builder()
                        .success(false)
                        .error("No response from MCP server")
                        .build();
            }

            return ToolResponse.builder()
                    .success((Boolean) response.get("success"))
                    .content(response.get("content"))
                    .error((String) response.get("error"))
                    .build();

        } catch (Exception e) {
            log.error("Error calling MCP tool: {}", request.getToolName(), e);
            return ToolResponse.builder()
                    .success(false)
                    .error("Failed to call tool: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ToolResponse searchPackages(Object criteria) {
        ToolRequest request = ToolRequest.builder()
                .toolName("search_packages")
                .arguments(criteria instanceof Map ? (Map<String, Object>) criteria : convertToMap(criteria))
                .build();
        return callTool(request);
    }

    @Override
    public ToolResponse getPackageDetails(String packageCode) {
        ToolRequest request = ToolRequest.builder()
                .toolName("get_package_details")
                .arguments(Map.of("packageCode", packageCode))
                .build();
        return callTool(request);
    }

    @Override
    public ToolResponse recommendPackage(Object profile) {
        ToolRequest request = ToolRequest.builder()
                .toolName("recommend_package")
                .arguments(profile instanceof Map ? (Map<String, Object>) profile : convertToMap(profile))
                .build();
        return callTool(request);
    }

    @Override
    public ToolResponse comparePackages(Object codes) {
        ToolRequest request = ToolRequest.builder()
                .toolName("compare_packages")
                .arguments(codes instanceof Map ? (Map<String, Object>) codes : Map.of("packageCodes", codes))
                .build();
        return callTool(request);
    }

    @Override
    public ToolResponse calculateSavings(String packageCode) {
        ToolRequest request = ToolRequest.builder()
                .toolName("calculate_savings")
                .arguments(Map.of("packageCode", packageCode))
                .build();
        return callTool(request);
    }

    @Override
    public ToolResponse getServices(String category) {
        Map<String, Object> args = new HashMap<>();
        if (category != null && !category.isEmpty()) {
            args.put("category", category);
        }
        ToolRequest request = ToolRequest.builder()
                .toolName("get_services")
                .arguments(args)
                .build();
        return callTool(request);
    }

    private Map<String, Object> convertToMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        return Map.of("data", obj);
    }
}