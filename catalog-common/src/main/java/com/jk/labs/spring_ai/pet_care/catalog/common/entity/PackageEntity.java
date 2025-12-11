package com.jk.labs.spring_ai.pet_care.catalog.common.entity;

import com.jk.labs.spring_ai.pet_care.catalog.common.enums.AgeGroup;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.CareLevel;
import com.jk.labs.spring_ai.pet_care.catalog.common.enums.PetType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name = "packages")
@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageEntity extends BaseEntity {

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "name", length = 1000)
    private String description;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_type", nullable = false, length = 20)
    private PetType petType;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", nullable = false, length = 20)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "care_level", nullable = false, length = 20)
    private CareLevel careLevel;

    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "savings_percentage", precision = 5, scale = 2)
    private BigDecimal savingsPercentage;

    @Column(name = "includes_dental")
    private Boolean includesDental = false;

    @Column(name = "includes_spay_neuter")
    private Boolean includesSpayNeuter = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "packageEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<PackageServiceEntity> packageServices = new HashSet<>();


}
