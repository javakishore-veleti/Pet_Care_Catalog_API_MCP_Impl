package com.jk.labs.spring_ai.pet_care.conversation.controller;

import com.jk.labs.spring_ai.pet_care.conversation.model.ChatRequest;
import com.jk.labs.spring_ai.pet_care.conversation.model.ChatResponse;
import com.jk.labs.spring_ai.pet_care.conversation.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        log.info("Received chat request: {}", request.getMessage());

        ChatResponse response = conversationService.processMessage(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "chat-controller"
        ));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
                "service", "Pet Care AI Agent - Chat API",
                "endpoint", "/api/chat",
                "method", "POST",
                "example", Map.of(
                        "message", "search dog packages",
                        "sessionId", "optional-session-id",
                        "userId", "optional-user-id"
                )
        ));
    }
}