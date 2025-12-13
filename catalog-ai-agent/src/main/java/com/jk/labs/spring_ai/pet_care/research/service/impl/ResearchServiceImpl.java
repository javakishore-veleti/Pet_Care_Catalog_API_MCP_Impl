package com.jk.labs.spring_ai.pet_care.research.service.impl;

import com.jk.labs.spring_ai.pet_care.common.mcp.model.ToolResponse;
import com.jk.labs.spring_ai.pet_care.common.mcp.service.McpClientService;
import com.jk.labs.spring_ai.pet_care.research.model.ResearchRequest;
import com.jk.labs.spring_ai.pet_care.research.model.ResearchResponse;
import com.jk.labs.spring_ai.pet_care.research.service.ResearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResearchServiceImpl implements ResearchService {

    private final McpClientService mcpClientService;

    public ResearchResponse research(ResearchRequest request) {
        log.info("Research Agent - Processing: {}", request.getQuery());

        String query = request.getQuery().toLowerCase();
        List<Map<String, Object>> findings = new ArrayList<>();

        // Research packages based on query
        if (query.contains("package") || query.contains("plan")) {
            findings.add(researchPackages(query));
        }

        // Research services
        if (query.contains("service") || query.contains("include")) {
            findings.add(researchServices(query));
        }

        // Compile research summary
        String summary = compileSummary(findings);

        return ResearchResponse.builder()
                .summary(summary)
                .findings(findings)
                .confidence(calculateConfidence(findings))
                .agentName("Research Agent")
                .build();
    }

    private Map<String, Object> researchPackages(String query) {
        Map<String, Object> criteria = extractSearchCriteria(query);
        ToolResponse response = mcpClientService.searchPackages(criteria);

        Map<String, Object> finding = new HashMap<>();
        finding.put("type", "packages");
        finding.put("data", response.getContent());
        finding.put("success", response.getSuccess());

        return finding;
    }

    private Map<String, Object> researchServices(String query) {
        String category = extractCategory(query);
        ToolResponse response = mcpClientService.getServices(category);

        Map<String, Object> finding = new HashMap<>();
        finding.put("type", "services");
        finding.put("data", response.getContent());
        finding.put("success", response.getSuccess());

        return finding;
    }

    private Map<String, Object> extractSearchCriteria(String query) {
        Map<String, Object> criteria = new HashMap<>();

        if (query.contains("dog")) criteria.put("petType", "DOG");
        if (query.contains("cat")) criteria.put("petType", "CAT");
        if (query.contains("puppy")) criteria.put("ageGroup", "PUPPY");
        if (query.contains("kitten")) criteria.put("ageGroup", "KITTEN");
        if (query.contains("senior")) criteria.put("ageGroup", "SENIOR");
        if (query.contains("adult")) criteria.put("ageGroup", "ADULT");

        return criteria;
    }

    private String extractCategory(String query) {
        if (query.contains("dental")) return "DENTAL";
        if (query.contains("vaccination")) return "VACCINATION";
        if (query.contains("surgery")) return "SURGERY";
        return null;
    }

    private String compileSummary(List<Map<String, Object>> findings) {
        StringBuilder summary = new StringBuilder("Research Summary:\n\n");

        for (Map<String, Object> finding : findings) {
            String type = (String) finding.get("type");
            summary.append("- Found information about ").append(type).append("\n");
        }

        return summary.toString();
    }

    private Double calculateConfidence(List<Map<String, Object>> findings) {
        long successCount = findings.stream()
                .filter(f -> Boolean.TRUE.equals(f.get("success")))
                .count();
        return findings.isEmpty() ? 0.0 : (double) successCount / findings.size();
    }
}