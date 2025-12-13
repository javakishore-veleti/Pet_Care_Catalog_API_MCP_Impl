package com.jk.labs.spring_ai.pet_care.conversation.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String response;
    private String sessionId;
    private String agentUsed;
}