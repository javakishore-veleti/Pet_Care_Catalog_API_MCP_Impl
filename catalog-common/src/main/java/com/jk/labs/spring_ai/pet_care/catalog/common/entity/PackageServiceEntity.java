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

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("packageId")
    @JoinColumn(name = "package_id")
    private PackageEntity packageEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("serviceId")
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    private Integer quantity = 1;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
