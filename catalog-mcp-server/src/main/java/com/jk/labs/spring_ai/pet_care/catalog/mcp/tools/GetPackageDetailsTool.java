package com.jk.labs.spring_ai.pet_care.catalog.mcp.tools;

import com.jk.labs.spring_ai.pet_care.catalog.common.constants.McpConstants;
import com.jk.labs.spring_ai.pet_care.catalog.common.dto.PackageDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.service.PackageService;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.McpTool;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolDefinition;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GetPackageDetailsTool implements McpTool {

    private final PackageService packageService;

    @Override
    public String getName() {
        return McpConstants.TOOL_GET_PACKAGE_DETAILS;
    }

    @Override
    public ToolDefinition getToolDefinition() {
        Map<String, Object> properties = Map.of(
                "packageCode", Map.of(
                        "type", "string",
                        "description", "Unique package code (e.g., 'DOG_ACTIVE_CARE')"
                )
        );

        return ToolDefinition.create(
                getName(),
                "Get comprehensive details about a specific wellness package including all services and pricing",
                Map.of(
                        "type", "object",
                        "properties", properties,
                        "required", List.of("packageCode")
                )
        );
    }

    @Override
    public ToolResponse execute(Map<String, Object> arguments) {
        String packageCode = (String) arguments.get("packageCode");

        if (packageCode == null || packageCode.trim().isEmpty()) {
            return ToolResponse.error(getName(), "packageCode is required");
        }

        PackageDTO packageDTO = packageService.getPackageByCode(packageCode);

        if (packageDTO == null) {
            return ToolResponse.error(getName(),
                    "Package not found: " + packageCode);
        }

        return ToolResponse.success(getName(), packageDTO);
    }

    @Override
    public String getCategory() {
        return "information";
    }
}