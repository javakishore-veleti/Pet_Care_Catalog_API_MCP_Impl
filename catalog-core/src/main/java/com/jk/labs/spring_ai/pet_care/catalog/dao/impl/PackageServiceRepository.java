package com.jk.labs.spring_ai.pet_care.catalog.dao.impl;

import com.jk.labs.spring_ai.pet_care.catalog.common.entity.PackageServiceEntity;
import com.jk.labs.spring_ai.pet_care.catalog.common.entity.PackageServiceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageServiceRepository extends
        JpaRepository<PackageServiceEntity, PackageServiceId> {

    List<PackageServiceEntity> findByPackageEntityId(Long packageId);

    List<PackageServiceEntity> findByServiceId(Long serviceId);
}
