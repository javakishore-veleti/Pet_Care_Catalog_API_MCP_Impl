package com.jk.labs.spring_ai.pet_care.catalog.mcp.tools;

import com.jk.labs.spring_ai.pet_care.catalog.common.constants.McpConstants;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.PetType;
import com.jk.labs.spring_ai.pet_care.catalog.common.service.PackageService;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.McpTool;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolDefinition;
import com.jk.labs.spring_ai.pet_care.catalog.mcp.schema.ToolResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class RecommendPackageTool implements McpTool {

    private final PackageService packageService;

    @Override
    public String getName() {
        return McpConstants.TOOL_RECOMMEND_PACKAGE;
    }

    @Override
    public ToolDefinition getToolDefinition() {
        Map<String, Object> properties = new LinkedHashMap<>();

        properties.put("petType", Map.of(
                "type", "string",
                "enum", Arrays.asList("DOG", "CAT"),
                "description", "Type of pet"
        ));

        properties.put("ageYears", Map.of(
                "type", "number",
                "description", "Age in years"
        ));

        properties.put("ageMonths", Map.of(
                "type", "number",
                "description", "Additional months (0-11)"
        ));

        properties.put("hasChronicConditions", Map.of(
                "type", "boolean",
                "description", "Does pet have chronic health conditions"
        ));

        properties.put("needsDentalCare", Map.of(
                "type", "boolean",
                "description", "Does pet need dental care"
        ));

        properties.put("needsSpayNeuter", Map.of(
                "type", "boolean",
                "description", "Does pet need spay/neuter surgery"
        ));

        properties.put("budget", Map.of(
                "type", "number",
                "description", "Annual budget for pet care in dollars"
        ));

        return ToolDefinition.create(
                getName(),
                "Get AI-powered package recommendation based on comprehensive pet profile and specific needs",
                Map.of(
                        "type", "object",
                        "properties", properties,
                        "required", Arrays.asList("petType", "ageYears")
                )
        );
    }

    @Override
    public ToolResponse execute(Map<String, Object> arguments) {
        PetType petType = PetType.valueOf((String) arguments.get("petType"));
        Integer ageYears = ((Number) arguments.get("ageYears")).intValue();
        Integer ageMonths = arguments.containsKey("ageMonths")
                ? ((Number) arguments.get("ageMonths")).intValue()
                : 0;
        Boolean hasChronicConditions = (Boolean) arguments.getOrDefault("hasChronicConditions", false);
        Boolean needsDentalCare = (Boolean) arguments.getOrDefault("needsDentalCare", false);
        Boolean needsSpayNeuter = (Boolean) arguments.getOrDefault("needsSpayNeuter", false);
        Double budget = arguments.containsKey("budget")
                ? ((Number) arguments.get("budget")).doubleValue()
                : null;

        var recommendation = packageService.recommendPackage(
                petType, ageYears, ageMonths, hasChronicConditions,
                needsDentalCare, needsSpayNeuter, budget
        );

        return ToolResponse.success(getName(), recommendation);
    }

    @Override
    public String getCategory() {
        return "recommendation";
    }
}