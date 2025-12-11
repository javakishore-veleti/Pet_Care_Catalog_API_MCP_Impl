package com.jk.labs.spring_ai.pet_care.catalog.common.dao;

import com.jk.labs.spring_ai.pet_care.catalog.common.entity.PackageServiceEntity;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.ServiceCategory;

import java.util.List;
import java.util.Optional;

public interface PackageServiceDao {

    List<PackageServiceEntity> findAll();

    Optional<PackageServiceEntity> findByServiceCode(String code);

    Optional<PackageServiceEntity> findById(Long id);

    List<PackageServiceEntity> findByCategory(ServiceCategory category);

    List<PackageServiceEntity> findByPackageId(Long packageId);

    PackageServiceEntity save(PackageServiceEntity serviceDTO);
}
