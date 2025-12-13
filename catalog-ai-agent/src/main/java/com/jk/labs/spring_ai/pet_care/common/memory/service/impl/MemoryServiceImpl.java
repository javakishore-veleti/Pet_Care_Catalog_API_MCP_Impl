package com.jk.labs.spring_ai.pet_care.common.memory.service.impl;

import com.jk.labs.spring_ai.pet_care.common.memory.model.ConversationHistory;
import com.jk.labs.spring_ai.pet_care.common.memory.model.MemoryEntry;
import com.jk.labs.spring_ai.pet_care.common.memory.service.MemoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemoryServiceImpl implements MemoryService {

    @Override
    public void addEntry(String sessionId, MemoryEntry entry) {
        // TODO: Implement memory storage
    }

    @Override
    public ConversationHistory getHistory(String sessionId) {
        // TODO: Implement history retrieval
        return null;
    }

    @Override
    public void clearHistory(String sessionId) {
        // TODO: Implement history clearing
    }
}
