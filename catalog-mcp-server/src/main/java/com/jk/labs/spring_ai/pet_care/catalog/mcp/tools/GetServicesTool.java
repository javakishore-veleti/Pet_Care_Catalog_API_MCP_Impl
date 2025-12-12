package com.jk.labs.spring_ai.pet_care.catalog.mcp.tools;

import com.jk.labs.spring_ai.pet_care.catalog.common.constants.McpConstants;
import com.jk.labs.spring_ai.pet_care.catalog.common.dto.ServiceDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.service.ServiceCatalogService;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.McpTool;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolDefinition;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GetServicesTool implements McpTool {

    private final ServiceCatalogService serviceCatalogService;

    @Override
    public String getName() {
        return McpConstants.TOOL_GET_SERVICES;
    }

    @Override
    public ToolDefinition getToolDefinition() {
        return ToolDefinition.create(
                getName(),
                "Get list of all available pet care services with descriptions and pricing",
                Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "category", Map.of(
                                        "type", "string",
                                        "description", "Filter by service category (optional)",
                                        "enum", Arrays.asList("VACCINATION", "DIAGNOSTIC",
                                                "DENTAL", "SURGERY", "VIRTUAL_CARE", "WELLNESS", "PREVENTIVE")
                                )
                        )
                )
        );
    }

    @Override
    public ToolResponse execute(Map<String, Object> arguments) {
        String category = (String) arguments.get("category");

        List<ServiceDTO> services = serviceCatalogService.getAllServices(category);

        Map<String, Object> content = Map.of(
                "services", services,
                "count", services.size(),
                "category", category != null ? category : "all"
        );

        return ToolResponse.success(getName(), content);
    }

    @Override
    public String getCategory() {
        return "information";
    }
}