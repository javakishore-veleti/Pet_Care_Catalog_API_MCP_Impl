package com.jk.labs.spring_ai.pet_care.catalog.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable  // Marks this as an embeddable composite key
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class PackageServiceId implements Serializable {

    @Column(name = "package_id")
    private Long packageId;

    @Column(name = "service_id")
    private Long serviceId;
}
