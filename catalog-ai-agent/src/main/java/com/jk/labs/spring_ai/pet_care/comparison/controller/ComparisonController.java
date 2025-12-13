package com.jk.labs.spring_ai.pet_care.comparison.controller;

import com.jk.labs.spring_ai.pet_care.comparison.service.ComparisonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/comparison")
@RequiredArgsConstructor
public class ComparisonController {

    private final ComparisonService comparisonService;

    // TODO: Implement controller endpoints
}
