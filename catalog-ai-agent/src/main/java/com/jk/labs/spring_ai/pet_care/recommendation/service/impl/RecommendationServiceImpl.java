package com.jk.labs.spring_ai.pet_care.recommendation.service.impl;

import com.jk.labs.spring_ai.pet_care.common.mcp.model.ToolResponse;
import com.jk.labs.spring_ai.pet_care.common.mcp.service.McpClientService;
import com.jk.labs.spring_ai.pet_care.recommendation.config.RecommendationConfig;
import com.jk.labs.spring_ai.pet_care.recommendation.model.RecommendationRequest;
import com.jk.labs.spring_ai.pet_care.recommendation.model.RecommendationResponse;
import com.jk.labs.spring_ai.pet_care.recommendation.model.RecommendationResponse.PackageRecommendation;
import com.jk.labs.spring_ai.pet_care.recommendation.service.RecommendationService;
import com.jk.labs.spring_ai.pet_care.research.model.ResearchRequest;
import com.jk.labs.spring_ai.pet_care.research.model.ResearchResponse;
import com.jk.labs.spring_ai.pet_care.research.service.ResearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private static final String AGENT_NAME = "Recommendation Agent";

    private final McpClientService mcpClientService;
    private final ResearchService researchService;
    private final RecommendationConfig config;

    @Override
    public RecommendationResponse recommend(RecommendationRequest request) {
        log.info("{} - Processing recommendation for {} {} (age: {}y {}m)",
                AGENT_NAME, request.getPetType(), request.getPetName(),
                request.getAgeYears(), request.getAgeMonths());

        // Step 1: Determine age group
        String ageGroup = determineAgeGroup(request.getPetType(),
                request.getAgeYears(), request.getAgeMonths());

        // Step 2: Research available packages (leverage Research Agent)
        ResearchResponse researchData = gatherResearchData(request, ageGroup);

        // Step 3: Get MCP recommendation
        ToolResponse mcpRecommendation = getMcpRecommendation(request, ageGroup);

        // Step 4: Build recommendation response
        return buildRecommendation(request, ageGroup, researchData, mcpRecommendation);
    }

    @Override
    public RecommendationResponse quickRecommend(String petType, Integer ageYears, Integer ageMonths) {
        RecommendationRequest request = RecommendationRequest.builder()
                .petType(petType.toUpperCase())
                .ageYears(ageYears)
                .ageMonths(ageMonths != null ? ageMonths : 0)
                .build();

        return recommend(request);
    }

    @Override
    public RecommendationResponse refineRecommendation(String sessionId, String additionalContext) {
        log.info("{} - Refining recommendation for session: {}", AGENT_NAME, sessionId);

        // In a full implementation, this would retrieve session context
        // For now, create a basic request from the additional context
        RecommendationRequest request = parseContextToRequest(additionalContext);
        request.setSessionId(sessionId);
        request.setConversationContext(additionalContext);

        return recommend(request);
    }

    private String determineAgeGroup(String petType, Integer ageYears, Integer ageMonths) {
        int totalMonths = (ageYears != null ? ageYears * 12 : 0) +
                (ageMonths != null ? ageMonths : 0);

        // Age group thresholds vary by pet type
        if ("DOG".equalsIgnoreCase(petType)) {
            if (totalMonths < 12) return "PUPPY";
            if (totalMonths >= 84) return "SENIOR";  // 7+ years
            return "ADULT";
        } else if ("CAT".equalsIgnoreCase(petType)) {
            if (totalMonths < 12) return "KITTEN";
            if (totalMonths >= 132) return "SENIOR";  // 11+ years
            return "ADULT";
        }

        return "ADULT"; // Default
    }

    private ResearchResponse gatherResearchData(RecommendationRequest request, String ageGroup) {
        String researchQuery = buildResearchQuery(request, ageGroup);

        ResearchRequest researchRequest = ResearchRequest.builder()
                .query(researchQuery)
                .sessionId(request.getSessionId())
                .depth(2)
                .build();

        return researchService.research(researchRequest);
    }

    private String buildResearchQuery(RecommendationRequest request, String ageGroup) {
        StringBuilder query = new StringBuilder();
        query.append("packages for ").append(request.getPetType().toLowerCase());
        query.append(" ").append(ageGroup.toLowerCase());

        if (Boolean.TRUE.equals(request.getNeedsDentalCare())) {
            query.append(" with dental service");
        }
        if (Boolean.TRUE.equals(request.getHasChronicConditions())) {
            query.append(" special care");
        }

        return query.toString();
    }

    private ToolResponse getMcpRecommendation(RecommendationRequest request, String ageGroup) {
        Map<String, Object> params = new HashMap<>();
        params.put("petType", request.getPetType());
        params.put("ageYears", request.getAgeYears());
        params.put("ageMonths", request.getAgeMonths());
        params.put("ageGroup", ageGroup);

        if (request.getNeedsDentalCare() != null) {
            params.put("needsDentalCare", request.getNeedsDentalCare());
        }
        if (request.getNeedsVaccinations() != null) {
            params.put("needsVaccinations", request.getNeedsVaccinations());
        }
        if (request.getHasChronicConditions() != null) {
            params.put("hasChronicConditions", request.getHasChronicConditions());
        }

        return mcpClientService.recommendPackage(params);
    }

    private RecommendationResponse buildRecommendation(
            RecommendationRequest request,
            String ageGroup,
            ResearchResponse researchData,
            ToolResponse mcpRecommendation) {

        // Parse MCP response to get primary recommendation
        PackageRecommendation primary = parsePrimaryRecommendation(mcpRecommendation, request);

        // Get alternatives from research data
        List<PackageRecommendation> alternatives = parseAlternatives(researchData, primary);

        // Build reasoning
        String reasoning = buildReasoning(request, ageGroup, primary);
        List<String> keyBenefits = extractKeyBenefits(primary, request);
        List<String> considerations = buildConsiderations(request, alternatives);

        // Determine confidence
        double confidence = calculateConfidence(mcpRecommendation, researchData);

        // Determine recommendation type
        String recommendationType = determineRecommendationType(primary, request);

        // Check if comparison would be helpful
        boolean suggestComparison = alternatives.size() > 1 &&
                alternatives.get(0).getMatchScore() > 0.7;

        return RecommendationResponse.builder()
                .agentName(AGENT_NAME)
                .primaryRecommendation(primary)
                .alternatives(alternatives)
                .reasoning(reasoning)
                .keyBenefits(keyBenefits)
                .considerations(considerations)
                .confidence(confidence)
                .recommendationType(recommendationType)
                .suggestComparison(suggestComparison)
                .nextAgentSuggestion(suggestComparison ? "Comparison Agent" : null)
                .build();
    }

    @SuppressWarnings("unchecked")
    private PackageRecommendation parsePrimaryRecommendation(
            ToolResponse mcpResponse, RecommendationRequest request) {

        if (!mcpResponse.getSuccess() || mcpResponse.getContent() == null) {
            // Return a default recommendation based on age group
            return getDefaultRecommendation(request);
        }

        try {
            Map<String, Object> content = (Map<String, Object>) mcpResponse.getContent();
            Map<String, Object> recommendation = (Map<String, Object>) content.get("recommendation");

            if (recommendation == null) {
                return getDefaultRecommendation(request);
            }

            return PackageRecommendation.builder()
                    .packageCode((String) recommendation.get("packageCode"))
                    .packageName((String) recommendation.get("packageName"))
                    .description((String) recommendation.get("description"))
                    .monthlyPrice(toBigDecimal(recommendation.get("monthlyPrice")))
                    .annualPrice(toBigDecimal(recommendation.get("annualPrice")))
                    .includedServices(toStringList(recommendation.get("services")))
                    .matchScore(toDouble(recommendation.get("matchScore"), 0.85))
                    .matchReason((String) recommendation.get("matchReason"))
                    .build();

        } catch (Exception e) {
            log.warn("Error parsing MCP recommendation: {}", e.getMessage());
            return getDefaultRecommendation(request);
        }
    }

    private PackageRecommendation getDefaultRecommendation(RecommendationRequest request) {
        String ageGroup = determineAgeGroup(request.getPetType(),
                request.getAgeYears(), request.getAgeMonths());

        // Map to appropriate default package
        String packageCode;
        String packageName;
        BigDecimal monthlyPrice;

        switch (ageGroup) {
            case "PUPPY":
            case "KITTEN":
                packageCode = request.getPetType() + "_TIMELY_CARE";
                packageName = "Timely Care";
                monthlyPrice = new BigDecimal("29.99");
                break;
            case "SENIOR":
                packageCode = request.getPetType() + "_ELDER_CARE";
                packageName = "Elder Care";
                monthlyPrice = new BigDecimal("49.99");
                break;
            default:
                packageCode = request.getPetType() + "_GROWNUP_CARE";
                packageName = "Grown-Up Care";
                monthlyPrice = new BigDecimal("39.99");
        }

        // If dental care needed, suggest Plus version
        if (Boolean.TRUE.equals(request.getNeedsDentalCare())) {
            packageCode += "_PLUS";
            packageName += " Plus";
            monthlyPrice = monthlyPrice.add(new BigDecimal("15.00"));
        }

        return PackageRecommendation.builder()
                .packageCode(packageCode)
                .packageName(packageName)
                .description("Recommended package for your " + ageGroup.toLowerCase() + " " +
                        request.getPetType().toLowerCase())
                .monthlyPrice(monthlyPrice)
                .annualPrice(monthlyPrice.multiply(new BigDecimal("12")))
                .includedServices(getDefaultServices(ageGroup))
                .matchScore(0.75)
                .matchReason("Based on age and pet type")
                .build();
    }

    private List<String> getDefaultServices(String ageGroup) {
        List<String> services = new ArrayList<>();
        services.add("Wellness Exams");
        services.add("Basic Vaccinations");

        switch (ageGroup) {
            case "PUPPY":
            case "KITTEN":
                services.add("Puppy/Kitten Vaccines");
                services.add("Deworming");
                break;
            case "SENIOR":
                services.add("Senior Blood Panel");
                services.add("Joint Health Check");
                break;
            default:
                services.add("Annual Diagnostics");
                services.add("Preventive Care");
        }

        return services;
    }

    @SuppressWarnings("unchecked")
    private List<PackageRecommendation> parseAlternatives(
            ResearchResponse researchData, PackageRecommendation primary) {

        List<PackageRecommendation> alternatives = new ArrayList<>();

        if (researchData.getFindings() == null) {
            return alternatives;
        }

        for (Map<String, Object> finding : researchData.getFindings()) {
            if ("packages".equals(finding.get("type"))) {
                Object data = finding.get("data");
                if (data instanceof List) {
                    List<Map<String, Object>> packages = (List<Map<String, Object>>) data;
                    for (Map<String, Object> pkg : packages) {
                        String code = (String) pkg.get("packageCode");
                        if (code != null && !code.equals(primary.getPackageCode())) {
                            alternatives.add(PackageRecommendation.builder()
                                    .packageCode(code)
                                    .packageName((String) pkg.get("packageName"))
                                    .description((String) pkg.get("description"))
                                    .monthlyPrice(toBigDecimal(pkg.get("monthlyPrice")))
                                    .matchScore(toDouble(pkg.get("matchScore"), 0.6))
                                    .build());
                        }
                    }
                }
            }
        }

        // Limit to top 3 alternatives
        return alternatives.stream()
                .sorted((a, b) -> Double.compare(
                        b.getMatchScore() != null ? b.getMatchScore() : 0,
                        a.getMatchScore() != null ? a.getMatchScore() : 0))
                .limit(3)
                .toList();
    }

    private String buildReasoning(RecommendationRequest request, String ageGroup,
                                  PackageRecommendation primary) {

        StringBuilder reasoning = new StringBuilder();
        reasoning.append("Based on your ").append(ageGroup.toLowerCase())
                .append(" ").append(request.getPetType().toLowerCase());

        if (request.getPetName() != null) {
            reasoning.append(" ").append(request.getPetName());
        }

        reasoning.append(", I recommend the **").append(primary.getPackageName())
                .append("** package. ");

        if (Boolean.TRUE.equals(request.getNeedsDentalCare())) {
            reasoning.append("This includes dental care coverage which you indicated is important. ");
        }

        if (Boolean.TRUE.equals(request.getHasChronicConditions())) {
            reasoning.append("The package provides ongoing support for chronic conditions. ");
        }

        if ("SENIOR".equals(ageGroup)) {
            reasoning.append("For senior pets, comprehensive diagnostics are essential for early detection. ");
        } else if ("PUPPY".equals(ageGroup) || "KITTEN".equals(ageGroup)) {
            reasoning.append("Young pets need a strong foundation with proper vaccinations and check-ups. ");
        }

        return reasoning.toString();
    }

    private List<String> extractKeyBenefits(PackageRecommendation primary, RecommendationRequest request) {
        List<String> benefits = new ArrayList<>();

        benefits.add("Tailored for " + determineAgeGroup(request.getPetType(),
                request.getAgeYears(), request.getAgeMonths()).toLowerCase() + " pets");

        if (primary.getIncludedServices() != null && !primary.getIncludedServices().isEmpty()) {
            benefits.add("Includes " + primary.getIncludedServices().size() + " essential services");
        }

        if (primary.getMonthlyPrice() != null) {
            benefits.add("Affordable at $" + primary.getMonthlyPrice() + "/month");
        }

        if (Boolean.TRUE.equals(request.getNeedsDentalCare())) {
            benefits.add("Dental coverage included");
        }

        return benefits;
    }

    private List<String> buildConsiderations(RecommendationRequest request,
                                             List<PackageRecommendation> alternatives) {

        List<String> considerations = new ArrayList<>();

        if (!alternatives.isEmpty()) {
            considerations.add("Consider comparing with " + alternatives.size() +
                    " alternative package(s) that may also suit your needs");
        }

        if (Boolean.TRUE.equals(request.getHasChronicConditions())) {
            considerations.add("Discuss specific chronic conditions with your vet for personalized coverage");
        }

        if (request.getBudgetPreference() == null) {
            considerations.add("Let me know your budget preference for more tailored options");
        }

        return considerations;
    }

    private double calculateConfidence(ToolResponse mcpRecommendation, ResearchResponse researchData) {
        double baseConfidence = 0.5;

        if (mcpRecommendation.getSuccess()) {
            baseConfidence += 0.3;
        }

        if (researchData.getConfidence() != null) {
            baseConfidence += researchData.getConfidence() * 0.2;
        }

        return Math.min(baseConfidence, 0.95);
    }

    private String determineRecommendationType(PackageRecommendation primary, RecommendationRequest request) {
        if (primary.getMatchScore() != null && primary.getMatchScore() > 0.9) {
            return "PERFECT_MATCH";
        }

        if ("BASIC".equalsIgnoreCase(request.getBudgetPreference())) {
            return "BUDGET_CONSCIOUS";
        }

        return "BEST_FIT";
    }

    private RecommendationRequest parseContextToRequest(String context) {
        // Simple parsing - in real implementation, use NLP or more sophisticated parsing
        RecommendationRequest request = new RecommendationRequest();
        String lowerContext = context.toLowerCase();

        if (lowerContext.contains("dog")) {
            request.setPetType("DOG");
        } else if (lowerContext.contains("cat")) {
            request.setPetType("CAT");
        }

        if (lowerContext.contains("dental")) {
            request.setNeedsDentalCare(true);
        }

        if (lowerContext.contains("senior") || lowerContext.contains("old")) {
            request.setAgeYears(10);
        } else if (lowerContext.contains("puppy") || lowerContext.contains("kitten")) {
            request.setAgeMonths(6);
        }

        request.setUserQuery(context);

        return request;
    }

    // Utility methods
    private BigDecimal toBigDecimal(Object value) {
        if (value == null) return null;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return new BigDecimal(value.toString());
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double toDouble(Object value, double defaultValue) {
        if (value == null) return defaultValue;
        if (value instanceof Number) return ((Number) value).doubleValue();
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> toStringList(Object value) {
        if (value == null) return new ArrayList<>();
        if (value instanceof List) return (List<String>) value;
        return List.of(value.toString());
    }
}