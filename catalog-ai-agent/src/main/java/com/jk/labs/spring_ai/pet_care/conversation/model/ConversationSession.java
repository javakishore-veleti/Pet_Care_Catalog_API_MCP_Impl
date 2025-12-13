package com.jk.labs.spring_ai.pet_care.conversation.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationSession {
    private String sessionId;
    private String userId;
    private Long createdAt;
    private Long lastActivity;
}