package com.jk.labs.spring_ai.pet_care.conversation.service;

import com.jk.labs.spring_ai.pet_care.conversation.model.ChatRequest;
import com.jk.labs.spring_ai.pet_care.conversation.model.ChatResponse;

public interface ConversationService {
    ChatResponse processMessage(ChatRequest request);
}