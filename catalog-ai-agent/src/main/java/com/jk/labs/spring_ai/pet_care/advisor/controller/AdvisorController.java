package com.jk.labs.spring_ai.pet_care.advisor.controller;

import com.jk.labs.spring_ai.pet_care.advisor.service.AdvisorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/advisor")
@RequiredArgsConstructor
public class AdvisorController {

    private final AdvisorService advisorService;

    // TODO: Implement controller endpoints
}
