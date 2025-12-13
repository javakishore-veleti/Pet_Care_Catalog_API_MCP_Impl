package com.jk.labs.spring_ai.pet_care.conversation.service.impl;

import com.jk.labs.spring_ai.pet_care.common.mcp.model.ToolResponse;
import com.jk.labs.spring_ai.pet_care.common.mcp.service.McpClientService;
import com.jk.labs.spring_ai.pet_care.conversation.model.ChatRequest;
import com.jk.labs.spring_ai.pet_care.conversation.model.ChatResponse;
import com.jk.labs.spring_ai.pet_care.conversation.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"TextBlockMigration", "unchecked", "unused"})
@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final McpClientService mcpClientService;

    public ChatResponse processMessage(ChatRequest request) {
        log.info("Processing chat message: {}", request.getMessage());

        String sessionId = request.getSessionId() != null
                ? request.getSessionId()
                : UUID.randomUUID().toString();

        // Simple intent detection based on keywords
        String response = detectIntentAndExecute(request.getMessage());

        return ChatResponse.builder()
                .response(response)
                .sessionId(sessionId)
                .agentUsed("simple-handler")
                .build();
    }

    private String detectIntentAndExecute(String message) {
        String lowerMessage = message.toLowerCase();

        // Search packages
        if (lowerMessage.contains("search") || lowerMessage.contains("find") || lowerMessage.contains("show")) {
            return handleSearch(message);
        }

        // Get package details
        if (lowerMessage.contains("details") || lowerMessage.contains("about") || lowerMessage.contains("tell me about")) {
            return handlePackageDetails(message);
        }

        // Recommendations
        if (lowerMessage.contains("recommend") || lowerMessage.contains("suggest") || lowerMessage.contains("best")) {
            return handleRecommendation(message);
        }

        // Compare
        if (lowerMessage.contains("compare") || lowerMessage.contains("difference")) {
            return handleComparison(message);
        }

        // Services
        if (lowerMessage.contains("services") || lowerMessage.contains("what's included")) {
            return handleServices(message);
        }

        // Default help message
        return "Hello! I'm your Pet Care Advisor. I can help you with:\n" +
                "- Search for packages (try: 'search dog packages')\n" +
                "- Get package details (try: 'details about DOG_ACTIVE_CARE')\n" +
                "- Get recommendations (try: 'recommend package for 3 year old dog')\n" +
                "- Compare packages (try: 'compare packages')\n" +
                "- View services (try: 'show me services')\n\n" +
                "What would you like to know?";
    }

    private String handleSearch(String message) {
        Map<String, Object> criteria = getStringObjectMap(message);

        ToolResponse response = mcpClientService.searchPackages(criteria);

        if (response.getSuccess()) {
            Map<String, Object> content = (Map<String, Object>) response.getContent();
            int count = (int) content.get("count");
            return String.format("I found %d packages matching your criteria:\n%s",
                    count, formatSearchResults(content));
        } else {
            return "Sorry, I couldn't search packages. Error: " + response.getError();
        }
    }

    private static Map<String, Object> getStringObjectMap(String message) {
        Map<String, Object> criteria = new HashMap<>();

        // Extract pet type
        if (message.toLowerCase().contains("dog")) {
            criteria.put("petType", "DOG");
        } else if (message.toLowerCase().contains("cat")) {
            criteria.put("petType", "CAT");
        }

        // Extract age group
        if (message.toLowerCase().contains("puppy")) {
            criteria.put("ageGroup", "PUPPY");
        } else if (message.toLowerCase().contains("kitten")) {
            criteria.put("ageGroup", "KITTEN");
        } else if (message.toLowerCase().contains("senior")) {
            criteria.put("ageGroup", "SENIOR");
        } else if (message.toLowerCase().contains("adult")) {
            criteria.put("ageGroup", "ADULT");
        }
        return criteria;
    }

    private String handlePackageDetails(String message) {
        // Extract package code (simple extraction)
        String packageCode = extractPackageCode(message);

        if (packageCode == null) {
            return "Please specify a package code (e.g., DOG_ACTIVE_CARE)";
        }

        ToolResponse response = mcpClientService.getPackageDetails(packageCode);

        if (response.getSuccess()) {
            return formatPackageDetails(response.getContent());
        } else {
            return "Sorry, I couldn't find that package. Error: " + response.getError();
        }
    }

    private String handleRecommendation(String message) {
        Map<String, Object> profile = new HashMap<>();

        // Extract pet type
        if (message.toLowerCase().contains("dog")) {
            profile.put("petType", "DOG");
        } else if (message.toLowerCase().contains("cat")) {
            profile.put("petType", "CAT");
        } else {
            return "Please specify if you have a dog or cat.";
        }

        // Extract age (simple extraction)
        String[] words = message.split(" ");
        for (String word : words) {
            if (word.matches("\\d+")) {
                profile.put("ageYears", Integer.parseInt(word));
                break;
            }
        }

        if (!profile.containsKey("ageYears")) {
            profile.put("ageYears", 3); // Default
        }

        ToolResponse response = mcpClientService.recommendPackage(profile);

        if (response.getSuccess()) {
            return formatRecommendation(response.getContent());
        } else {
            return "Sorry, I couldn't generate a recommendation. Error: " + response.getError();
        }
    }

    private String handleComparison(String message) {
        // For now, compare default packages
        ToolResponse response = mcpClientService.comparePackages(
                java.util.List.of("DOG_ACTIVE_CARE", "DOG_SENIOR_CARE")
        );

        if (response.getSuccess()) {
            return formatComparison(response.getContent());
        } else {
            return "Sorry, I couldn't compare packages. Error: " + response.getError();
        }
    }

    private String handleServices(String message) {
        String category = null;

        if (message.toLowerCase().contains("dental")) {
            category = "DENTAL";
        } else if (message.toLowerCase().contains("vaccination")) {
            category = "VACCINATION";
        }

        ToolResponse response = mcpClientService.getServices(category);

        if (response.getSuccess()) {
            return formatServices(response.getContent());
        } else {
            return "Sorry, I couldn't retrieve services. Error: " + response.getError();
        }
    }

    private String extractPackageCode(String message) {
        String[] words = message.toUpperCase().split(" ");
        for (String word : words) {
            if (word.contains("DOG_") || word.contains("CAT_")) {
                return word;
            }
        }
        return null;
    }

    private String formatSearchResults(Map<String, Object> content) {
        // Simple formatting - enhance as needed
        return content.toString();
    }

    private String formatPackageDetails(Object content) {
        return "Package Details:\n" + content.toString();
    }

    private String formatRecommendation(Object content) {
        return "Recommendation:\n" + content.toString();
    }

    private String formatComparison(Object content) {
        return "Package Comparison:\n" + content.toString();
    }

    private String formatServices(Object content) {
        return "Available Services:\n" + content.toString();
    }
}