package com.jk.labs.spring_ai.pet_care.catalog.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name = "catalog_package")
@Entity
@Getter
@Setter
public class PackageEntity extends BaseEntity {

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "name", length = 1000)
    private String description;
}
