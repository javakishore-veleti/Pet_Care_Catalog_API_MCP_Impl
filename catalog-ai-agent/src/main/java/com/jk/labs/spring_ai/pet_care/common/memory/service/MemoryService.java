package com.jk.labs.spring_ai.pet_care.common.memory.service;

import com.jk.labs.spring_ai.pet_care.common.memory.model.ConversationHistory;
import com.jk.labs.spring_ai.pet_care.common.memory.model.MemoryEntry;

public interface MemoryService {
    void addEntry(String sessionId, MemoryEntry entry);
    ConversationHistory getHistory(String sessionId);
    void clearHistory(String sessionId);
}
