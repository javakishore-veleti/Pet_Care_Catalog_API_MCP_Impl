package com.jk.labs.spring_ai.pet_care.common.memory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationHistory {
    private String sessionId;
    private List<MemoryEntry> entries;
    private Integer maxEntries;
}
