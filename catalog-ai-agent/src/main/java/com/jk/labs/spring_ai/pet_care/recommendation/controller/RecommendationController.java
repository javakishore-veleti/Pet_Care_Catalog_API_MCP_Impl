package com.jk.labs.spring_ai.pet_care.recommendation.controller;

import com.jk.labs.spring_ai.pet_care.recommendation.model.RecommendationRequest;
import com.jk.labs.spring_ai.pet_care.recommendation.model.RecommendationResponse;
import com.jk.labs.spring_ai.pet_care.recommendation.service.RecommendationService;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/agents/recommendation")
@RequiredArgsConstructor
@Tag(name = "Recommendation Agent", description = "AI-powered package recommendation endpoints")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/recommend")
    @Operation(
            summary = "Get package recommendation",
            description = "Analyzes pet profile and needs to recommend the best wellness package",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Recommendation generated successfully",
                            content = @Content(schema = @Schema(implementation = RecommendationResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters")
            }
    )
    public ResponseEntity<RecommendationResponse> getRecommendation(
            @RequestBody RecommendationRequest request) {

        log.info("Recommendation request received for pet type: {}", request.getPetType());
        RecommendationResponse response = recommendationService.recommend(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/quick")
    @Operation(
            summary = "Quick recommendation",
            description = "Get a quick recommendation based on minimal pet information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Quick recommendation generated",
                            content = @Content(schema = @Schema(implementation = RecommendationResponse.class))
                    )
            }
    )
    public ResponseEntity<RecommendationResponse> quickRecommend(
            @Parameter(description = "Pet type (DOG or CAT)", required = true)
            @RequestParam String petType,

            @Parameter(description = "Pet age in years", required = true)
            @RequestParam Integer ageYears,

            @Parameter(description = "Additional months (optional)")
            @RequestParam(required = false) Integer ageMonths) {

        log.info("Quick recommendation request: {} - {} years", petType, ageYears);
        RecommendationResponse response = recommendationService.quickRecommend(
                petType, ageYears, ageMonths);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refine/{sessionId}")
    @Operation(
            summary = "Refine recommendation",
            description = "Refine an existing recommendation with additional context",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Recommendation refined successfully",
                            content = @Content(schema = @Schema(implementation = RecommendationResponse.class))
                    )
            }
    )
    public ResponseEntity<RecommendationResponse> refineRecommendation(
            @Parameter(description = "Session ID from previous interaction")
            @PathVariable String sessionId,

            @Parameter(description = "Additional context or requirements")
            @RequestBody String additionalContext) {

        log.info("Refining recommendation for session: {}", sessionId);
        RecommendationResponse response = recommendationService.refineRecommendation(
                sessionId, additionalContext);
        return ResponseEntity.ok(response);
    }
}