package com.jk.labs.spring_ai.pet_care.catalog.mapper;

import com.jk.labs.spring_ai.pet_care.catalog.common.dto.PackageDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.dto.ServiceDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.entity.PackageEntity;
import com.jk.labs.spring_ai.pet_care.catalog.common.entity.PackageServiceEntity;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PackageMapper {

    @Mapping(target = "petType", expression = "java(entity.getPetType().name())")
    @Mapping(target = "ageGroup", expression = "java(entity.getAgeGroup().name())")
    @Mapping(target = "careLevel", expression = "java(entity.getCareLevel().name())")
    @Mapping(target = "services", source = "packageServices")
    PackageDTO toDto(PackageEntity entity);

    List<PackageDTO> toDtoList(List<PackageEntity> entities);

    @Mapping(target = "id", source = "service.id")  // Map from the actual service
    @Mapping(target = "name", source = "service.name")
    @Mapping(target = "code", source = "service.code")
    @Mapping(target = "description", source = "service.description")
    @Mapping(target = "category", expression = "java(packageService.getService().getCategory().name())")
    @Mapping(target = "individualPrice", source = "service.individualPrice")
    @Mapping(target = "virtual", source = "service.virtual")
    @Mapping(target = "quantity", source = "quantity")
    ServiceDTO packageServiceToServiceDto(PackageServiceEntity packageService);
}