package com.jk.labs.spring_ai.pet_care.conversation.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
@ConfigurationProperties(prefix = "conversation")
public class ConversationConfig {
    private Integer maxHistorySize = 10;
    private Long sessionTimeout = 3600000L; // 1 hour
}