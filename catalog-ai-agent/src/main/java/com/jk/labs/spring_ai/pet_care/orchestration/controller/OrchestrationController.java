package com.jk.labs.spring_ai.pet_care.orchestration.controller;
import com.jk.labs.spring_ai.pet_care.orchestration.service.OrchestrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/orchestration")
@RequiredArgsConstructor
public class OrchestrationController {
    private final OrchestrationService orchestrationService;

    // TODO: Implement controller endpoints
}