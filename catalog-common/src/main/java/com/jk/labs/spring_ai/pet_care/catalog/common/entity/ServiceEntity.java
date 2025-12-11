package com.jk.labs.spring_ai.pet_care.catalog.common.entity;

import com.jk.labs.spring_ai.pet_care.catalog.common.enums.ServiceCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name = "services", schema = "catalog")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity extends BaseEntity {

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ServiceCategory category;

    @Column(name = "individual_price", precision = 10, scale = 2)
    private BigDecimal individualPrice;

    @Column(name = "is_virtual")
    private Boolean isVirtual = false;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<PackageServiceEntity> packageServices = new HashSet<>();

}
