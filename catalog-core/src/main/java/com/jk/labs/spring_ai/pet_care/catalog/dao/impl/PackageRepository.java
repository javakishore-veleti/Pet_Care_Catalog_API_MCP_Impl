package com.jk.labs.spring_ai.pet_care.catalog.dao.impl;

import com.jk.labs.spring_ai.pet_care.catalog.common.entity.PackageEntity;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.AgeGroup;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.CareLevel;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Long> {

    Optional<PackageEntity> findByCodeAndIsActiveTrue(String code);

    List<PackageEntity> findByIsActiveTrue();

    List<PackageEntity> findByPetTypeAndIsActiveTrue(PetType petType);

    List<PackageEntity> findByPetTypeAndAgeGroupAndIsActiveTrue(
            PetType petType, AgeGroup ageGroup);

    Optional<PackageEntity> findByPetTypeAndAgeGroupAndCareLevelAndIsActiveTrue(
            PetType petType, AgeGroup ageGroup, CareLevel careLevel);

    @Query("SELECT p FROM PackageEntity p WHERE p.active = true " +
            "AND (:petType IS NULL OR p.petType = :petType) " +
            "AND (:ageGroup IS NULL OR p.ageGroup = :ageGroup)")
    List<PackageEntity> searchPackages(PetType petType, AgeGroup ageGroup);
}