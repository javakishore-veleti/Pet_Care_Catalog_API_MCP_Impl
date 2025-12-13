package com.jk.labs.spring_ai.pet_care.research.service;

import com.jk.labs.spring_ai.pet_care.research.model.ResearchRequest;
import com.jk.labs.spring_ai.pet_care.research.model.ResearchResponse;

public interface ResearchService {
    ResearchResponse research(ResearchRequest request);
}