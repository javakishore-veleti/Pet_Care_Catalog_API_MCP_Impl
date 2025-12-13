package com.jk.labs.spring_ai.pet_care.common.models.service;

import com.jk.labs.spring_ai.pet_care.common.models.model.LLMRequest;
import com.jk.labs.spring_ai.pet_care.common.models.model.LLMResponse;

public interface LLMService {
    LLMResponse chat(LLMRequest request);
    LLMResponse streamChat(LLMRequest request);
}
