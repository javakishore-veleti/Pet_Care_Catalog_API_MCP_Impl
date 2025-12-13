package com.jk.labs.spring_ai.pet_care.sales.controller;

import com.jk.labs.spring_ai.pet_care.sales.service.SalesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    // TODO: Implement controller endpoints
}
