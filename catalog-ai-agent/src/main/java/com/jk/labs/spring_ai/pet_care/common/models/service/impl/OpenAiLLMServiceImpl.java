package com.jk.labs.spring_ai.pet_care.common.models.service.impl;

import com.jk.labs.spring_ai.pet_care.common.models.model.LLMRequest;
import com.jk.labs.spring_ai.pet_care.common.models.model.LLMResponse;
import com.jk.labs.spring_ai.pet_care.common.models.service.LLMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiLLMServiceImpl implements LLMService {

    @Override
    public LLMResponse chat(LLMRequest request) {
        // TODO: Implement OpenAI chat
        return null;
    }

    @Override
    public LLMResponse streamChat(LLMRequest request) {
        // TODO: Implement OpenAI streaming chat
        return null;
    }
}
