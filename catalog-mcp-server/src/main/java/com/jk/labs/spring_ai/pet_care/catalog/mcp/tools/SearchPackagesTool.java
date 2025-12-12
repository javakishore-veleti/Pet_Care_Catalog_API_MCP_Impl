package com.jk.labs.spring_ai.pet_care.catalog.mcp.tools;

import com.jk.labs.spring_ai.pet_care.catalog.common.constants.McpConstants;
import com.jk.labs.spring_ai.pet_care.catalog.common.dto.PackageDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.AgeGroup;
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
public class SearchPackagesTool implements McpTool {

    private final PackageService packageService;

    @Override
    public String getName() {
        return McpConstants.TOOL_SEARCH_PACKAGES;
    }

    @Override
    public ToolDefinition getToolDefinition() {
        Map<String, Object> properties = new LinkedHashMap<>();

        properties.put("petType", Map.of(
                "type", "string",
                "enum", Arrays.asList("DOG", "CAT"),
                "description", "Type of pet (DOG or CAT)"
        ));

        properties.put("ageGroup", Map.of(
                "type", "string",
                "enum", Arrays.asList("PUPPY", "KITTEN", "ADULT", "SENIOR"),
                "description", "Age group of the pet"
        ));

        properties.put("hasDentalNeeds", Map.of(
                "type", "boolean",
                "description", "Whether pet requires dental care"
        ));

        properties.put("needsSpayNeuter", Map.of(
                "type", "boolean",
                "description", "Whether pet needs spay/neuter surgery"
        ));

        Map<String, Object> inputSchema = Map.of(
                "type", "object",
                "properties", properties
        );

        return ToolDefinition.create(
                getName(),
                "Search for pet wellness packages based on specific criteria like pet type, age, and special needs",
                inputSchema
        );
    }

    @Override
    public ToolResponse execute(Map<String, Object> arguments) {
        PetType petType = arguments.containsKey("petType")
                ? PetType.valueOf((String) arguments.get("petType"))
                : null;

        AgeGroup ageGroup = arguments.containsKey("ageGroup")
                ? AgeGroup.valueOf((String) arguments.get("ageGroup"))
                : null;

        Boolean hasDentalNeeds = (Boolean) arguments.get("hasDentalNeeds");
        Boolean needsSpayNeuter = (Boolean) arguments.get("needsSpayNeuter");

        List<PackageDTO> results = packageService.searchPackages(
                petType, ageGroup, hasDentalNeeds, needsSpayNeuter
        );

        Map<String, Object> content = Map.of(
                "packages", results,
                "count", results.size(),
                "searchCriteria", arguments
        );

        return ToolResponse.success(getName(), content);
    }

    @Override
    public String getCategory() {
        return "search";
    }
}