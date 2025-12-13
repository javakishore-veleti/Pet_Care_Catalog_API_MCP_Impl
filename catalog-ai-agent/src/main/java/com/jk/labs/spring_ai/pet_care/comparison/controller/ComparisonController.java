package com.jk.labs.spring_ai.pet_care.comparison.controller;

import com.jk.labs.spring_ai.pet_care.comparison.model.ComparisonRequest;
import com.jk.labs.spring_ai.pet_care.comparison.model.ComparisonResponse;
import com.jk.labs.spring_ai.pet_care.comparison.service.ComparisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/agents/comparison")
@RequiredArgsConstructor
@Tag(name = "Comparison Agent", description = "Package comparison and analysis endpoints")
public class ComparisonController {

    private final ComparisonService comparisonService;

    @PostMapping("/compare")
    @Operation(
            summary = "Full package comparison",
            description = "Compare multiple packages with detailed analysis, feature matrix, and winner recommendation",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comparison completed successfully",
                            content = @Content(schema = @Schema(implementation = ComparisonResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request - need at least 2 packages")
            }
    )
    public ResponseEntity<ComparisonResponse> compare(
            @RequestBody ComparisonRequest request) {

        log.info("Comparison request received for packages: {}", request.getPackageCodes());
        ComparisonResponse response = comparisonService.compare(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/quick")
    @Operation(
            summary = "Quick comparison of two packages",
            description = "Quick side-by-side comparison of exactly two packages",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Quick comparison completed",
                            content = @Content(schema = @Schema(implementation = ComparisonResponse.class))
                    )
            }
    )
    public ResponseEntity<ComparisonResponse> quickCompare(
            @Parameter(description = "First package code", required = true, example = "DOG_GROWNUP_CARE")
            @RequestParam String package1,

            @Parameter(description = "Second package code", required = true, example = "DOG_GROWNUP_CARE_PLUS")
            @RequestParam String package2) {

        log.info("Quick comparison: {} vs {}", package1, package2);
        ComparisonResponse response = comparisonService.quickCompare(package1, package2);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/multiple")
    @Operation(
            summary = "Compare multiple packages",
            description = "Compare 2-4 packages by their codes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Multiple package comparison completed",
                            content = @Content(schema = @Schema(implementation = ComparisonResponse.class))
                    )
            }
    )
    public ResponseEntity<ComparisonResponse> compareMultiple(
            @Parameter(description = "List of package codes to compare (2-4)", required = true)
            @RequestParam List<String> packages) {

        log.info("Multiple package comparison: {}", packages);
        ComparisonResponse response = comparisonService.compareMultiple(packages);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/focus")
    @Operation(
            summary = "Compare with specific focus areas",
            description = "Compare packages with emphasis on specific features like dental, price, or coverage",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Focused comparison completed",
                            content = @Content(schema = @Schema(implementation = ComparisonResponse.class))
                    )
            }
    )
    public ResponseEntity<ComparisonResponse> compareWithFocus(
            @Parameter(description = "List of package codes to compare", required = true)
            @RequestParam List<String> packages,

            @Parameter(description = "Features to focus on", required = true,
                    example = "[\"dental\", \"price\", \"vaccinations\"]")
            @RequestParam List<String> focus) {

        log.info("Focused comparison of {} on features: {}", packages, focus);
        ComparisonResponse response = comparisonService.compareWithFocus(packages, focus);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/for-pet")
    @Operation(
            summary = "Compare packages for a specific pet",
            description = "Compare packages with consideration for a specific pet type and age",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pet-specific comparison completed",
                            content = @Content(schema = @Schema(implementation = ComparisonResponse.class))
                    )
            }
    )
    public ResponseEntity<ComparisonResponse> compareForPet(
            @Parameter(description = "List of package codes to compare", required = true)
            @RequestParam List<String> packages,

            @Parameter(description = "Pet type (DOG or CAT)", required = true)
            @RequestParam String petType,

            @Parameter(description = "Pet age in years", required = true)
            @RequestParam Integer ageYears) {

        log.info("Pet-specific comparison for {} ({} years): {}", petType, ageYears, packages);
        ComparisonResponse response = comparisonService.compareForPet(packages, petType, ageYears);
        return ResponseEntity.ok(response);
    }
}