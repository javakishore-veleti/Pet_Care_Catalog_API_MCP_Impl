package com.jk.labs.spring_ai.pet_care.recommendation.controller;

import com.jk.labs.spring_ai.pet_care.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    // TODO: Implement controller endpoints
}
