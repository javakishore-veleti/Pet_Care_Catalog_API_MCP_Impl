package com.jk.labs.spring_ai.pet_care.catalog.common.dao;

import com.jk.labs.spring_ai.pet_care.catalog.common.dto.PackageDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.AgeGroup;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.CareLevel;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.PetType;

import java.util.List;
import java.util.Optional;

public interface PackageDao {

    List<PackageDTO> findAll();

    Optional<PackageDTO> findByCode(String code);

    Optional<PackageDTO> findById(Long id);

    List<PackageDTO> findByPetType(PetType petType);

    List<PackageDTO> findByPetTypeAndAgeGroup(PetType petType, AgeGroup ageGroup);

    Optional<PackageDTO> findByPetTypeAndAgeGroupAndCareLevel(
            PetType petType, AgeGroup ageGroup, CareLevel careLevel);

    List<PackageDTO> searchPackages(PetType petType, AgeGroup ageGroup,
                                    Boolean hasDentalNeeds, Boolean needsSpayNeuter);

    PackageDTO save(PackageDTO packageDTO);

    void deleteById(Long id);
}