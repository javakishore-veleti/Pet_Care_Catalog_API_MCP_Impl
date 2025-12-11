package com.jk.labs.spring_ai.pet_care.catalog.common.dao;

import com.jk.labs.spring_ai.pet_care.catalog.common.dto.ServiceDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.ServiceCategory;

import java.util.List;
import java.util.Optional;

public interface ServiceDao {

    List<ServiceDTO> findAll();

    Optional<ServiceDTO> findByCode(String code);

    Optional<ServiceDTO> findById(Long id);

    List<ServiceDTO> findByCategory(ServiceCategory category);

    List<ServiceDTO> findByPackageId(Long packageId);

    ServiceDTO save(ServiceDTO serviceDTO);
}
