package com.jk.labs.spring_ai.pet_care.catalog.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name = "catalog_package_service")
@Entity
@Getter
@Setter
public class PackageServiceEntity extends BaseEntityNoId {

    @EmbeddedId  // Use a composite key
    private PackageServiceId id;

    @ManyToOne
    @MapsId("packageId")
    private PackageEntity packageEntity;

    @ManyToOne
    @MapsId("serviceId")
    private ServiceEntity service;
}
