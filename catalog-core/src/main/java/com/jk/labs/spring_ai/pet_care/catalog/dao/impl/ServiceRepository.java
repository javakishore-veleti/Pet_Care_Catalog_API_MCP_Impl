package com.jk.labs.spring_ai.pet_care.catalog.dao.impl;
import com.jk.labs.spring_ai.pet_care.catalog.common.entity.ServiceEntity;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    Optional<ServiceEntity> findByCode(String code);

    List<ServiceEntity> findByCategory(ServiceCategory category);

    @Query("SELECT ps.service FROM PackageServiceEntity ps " +
            "WHERE ps.packageEntity.id = :packageId")
    List<ServiceEntity> findByPackageId(Long packageId);
}