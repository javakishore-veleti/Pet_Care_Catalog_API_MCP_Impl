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
public class CalculateSavingsTool implements McpTool {

    private final PackageService packageService;

    @Override
    public String getName() {
        return McpConstants.TOOL_CALCULATE_SAVINGS;
    }

    @Override
    public ToolDefinition getToolDefinition() {
        return ToolDefinition.create(
                getName(),
                "Calculate potential savings when purchasing a package versus individual services",
                Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "packageCode", Map.of(
                                        "type", "string",
                                        "description", "Package code to analyze"
                                )
                        ),
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

        var savingsAnalysis = packageService.calculateSavings(packageCode);

        if (savingsAnalysis == null || savingsAnalysis.isEmpty()) {
            return ToolResponse.error(getName(),
                    "Could not calculate savings for package: " + packageCode);
        }

        return ToolResponse.success(getName(), savingsAnalysis);
    }

    @Override
    public String getCategory() {
        return "analysis";
    }
}