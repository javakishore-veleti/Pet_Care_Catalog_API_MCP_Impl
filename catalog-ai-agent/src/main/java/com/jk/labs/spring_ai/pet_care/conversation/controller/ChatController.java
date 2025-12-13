package com.jk.labs.spring_ai.pet_care.conversation.controller;
import com.jk.labs.spring_ai.pet_care.conversation.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ConversationService conversationService;
    // TODO: Implement chat endpoints
}