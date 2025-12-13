package com.jk.labs.spring_ai.pet_care.common.models.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String role;
    private String content;
    private Long timestamp;
}
