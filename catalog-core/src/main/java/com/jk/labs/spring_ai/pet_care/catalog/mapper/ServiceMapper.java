package com.jk.labs.spring_ai.pet_care.catalog.mapper;

import com.jk.labs.spring_ai.pet_care.catalog.common.dto.ServiceDTO;
import com.jk.labs.spring_ai.pet_care.catalog.common.entity.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceMapper {

    @Mapping(target = "category", source = "category")
    ServiceDTO toDto(ServiceEntity entity);

    List<ServiceDTO> toDtoList(List<ServiceEntity> entities);

    @Mapping(target = "packageServices", ignore = true)
    @Mapping(target = "createdDt", ignore = true)
    ServiceEntity toEntity(ServiceDTO dto);
}
