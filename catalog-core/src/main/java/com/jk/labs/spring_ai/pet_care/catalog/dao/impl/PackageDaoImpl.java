package com.jk.labs.spring_ai.pet_care.catalog.dao.impl;

import com.jk.labs.spring_ai.pet_care.catalog.common.dao.PackageDao;
import com.jk.labs.spring_ai.pet_care.catalog.common.dto.PackageDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.AgeGroup;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.CareLevel;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.PetType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PackageDaoImpl implements PackageDao {

    @Override
    public List<PackageDTO> findAll() {
        return List.of();
    }

    @Override
    public Optional<PackageDTO> findByCode(String code) {
        return Optional.empty();
    }

    @Override
    public Optional<PackageDTO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<PackageDTO> findByPetType(PetType petType) {
        return List.of();
    }

    @Override
    public List<PackageDTO> findByPetTypeAndAgeGroup(PetType petType, AgeGroup ageGroup) {
        return List.of();
    }

    @Override
    public Optional<PackageDTO> findByPetTypeAndAgeGroupAndCareLevel(PetType petType, AgeGroup ageGroup, CareLevel careLevel) {
        return Optional.empty();
    }

    @Override
    public List<PackageDTO> searchPackages(PetType petType, AgeGroup ageGroup, Boolean hasDentalNeeds, Boolean needsSpayNeuter) {
        return List.of();
    }

    @Override
    public PackageDTO save(PackageDTO packageDTO) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
