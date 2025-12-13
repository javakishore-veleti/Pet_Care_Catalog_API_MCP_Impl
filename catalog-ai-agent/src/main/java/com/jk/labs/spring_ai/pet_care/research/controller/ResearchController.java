package com.jk.labs.spring_ai.pet_care.research.controller;

import com.jk.labs.spring_ai.pet_care.research.service.ResearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/research")
@RequiredArgsConstructor
public class ResearchController {

    private final ResearchService researchService;

    // TODO: Implement controller endpoints
}
