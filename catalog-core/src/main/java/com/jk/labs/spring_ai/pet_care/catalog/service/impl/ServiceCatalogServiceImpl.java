package com.jk.labs.spring_ai.pet_care.catalog.service.impl;

import com.jk.labs.spring_ai.pet_care.catalog.common.dto.ServiceDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.service.ServiceCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class ServiceCatalogServiceImpl implements ServiceCatalogService {

    @Override
    public List<ServiceDTO> getAllServices(String category) {
        log.debug("getAllServices - category: {}", category);
        return createMockServices().stream()
                .filter(s -> category == null || s.getCategory().equals(category))
                .toList();
    }

    @Override
    public ServiceDTO getServiceByCode(String code) {
        log.debug("getServiceByCode: {}", code);
        return createMockServices().stream()
                .filter(s -> s.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ServiceDTO> getAllActiveServices() {
        log.debug("getAllActiveServices called");
        return createMockServices();
    }

    private List<ServiceDTO> createMockServices() {
        return List.of(
                ServiceDTO.builder()
                        .id(1L)
                        .code("VACC_RABIES")
                        .name("Rabies Vaccination")
                        .description("Annual rabies vaccination")
                        .category("VACCINATION")
                        .individualPrice(BigDecimal.valueOf(25.00))
                        .virtual(false)
                        .build(),
                ServiceDTO.builder()
                        .id(2L)
                        .code("EXAM_WELLNESS")
                        .name("Wellness Exam")
                        .description("Comprehensive wellness examination")
                        .category("WELLNESS")
                        .individualPrice(BigDecimal.valueOf(65.00))
                        .virtual(false)
                        .build(),
                ServiceDTO.builder()
                        .id(3L)
                        .code("DENTAL_CLEANING")
                        .name("Dental Cleaning")
                        .description("Professional dental cleaning")
                        .category("DENTAL")
                        .individualPrice(BigDecimal.valueOf(350.00))
                        .virtual(false)
                        .build()
        );
    }
}