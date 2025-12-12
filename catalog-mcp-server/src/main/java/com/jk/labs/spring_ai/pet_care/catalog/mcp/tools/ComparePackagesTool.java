package com.jk.labs.spring_ai.pet_care.catalog.mcp.tools;

import com.jk.labs.spring_ai.pet_care.catalog.common.constants.McpConstants;
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
public class ComparePackagesTool implements McpTool {

    private final PackageService packageService;

    @Override
    public String getName() {
        return McpConstants.TOOL_COMPARE_PACKAGES;
    }

    @Override
    public ToolDefinition getToolDefinition() {
        return ToolDefinition.create(
                getName(),
                "Compare multiple wellness packages side-by-side with detailed feature comparison matrix",
                Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "packageCodes", Map.of(
                                        "type", "array",
                                        "items", Map.of("type", "string"),
                                        "description", "List of 2-4 package codes to compare",
                                        "minItems", 2,
                                        "maxItems", 4
                                )
                        ),
                        "required", List.of("packageCodes")
                )
        );
    }

    @Override
    public ToolResponse execute(Map<String, Object> arguments) {
        @SuppressWarnings("unchecked")
        List<String> packageCodes = (List<String>) arguments.get("packageCodes");

        if (packageCodes == null || packageCodes.size() < 2) {
            return ToolResponse.error(getName(),
                    "Please provide at least 2 package codes to compare");
        }

        if (packageCodes.size() > 4) {
            return ToolResponse.error(getName(),
                    "Maximum 4 packages can be compared at once");
        }

        var comparison = packageService.comparePackages(packageCodes);

        return ToolResponse.success(getName(), comparison);
    }

    @Override
    public String getCategory() {
        return "analysis";
    }
}