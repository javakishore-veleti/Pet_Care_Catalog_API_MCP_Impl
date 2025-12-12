package com.jk.labs.spring_ai.pet_care.catalog.common.service;

import com.jk.labs.spring_ai.pet_care.catalog.common.dto.ServiceDTO;

import java.util.List;

public interface ServiceCatalogService {

    /**
     * Get all services, optionally filtered by category
     */
    List<ServiceDTO> getAllServices(String category);

    /**
     * Get service by code
     */
    ServiceDTO getServiceByCode(String code);

    /**
     * Get all active services
     */
    List<ServiceDTO> getAllActiveServices();
}