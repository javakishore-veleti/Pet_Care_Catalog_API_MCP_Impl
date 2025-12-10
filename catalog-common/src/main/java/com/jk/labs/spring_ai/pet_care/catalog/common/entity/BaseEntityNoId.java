package com.jk.labs.spring_ai.pet_care.catalog.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseEntityNoId {

    @Column(name = "status", length = 25)
    private String status;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "created_dt")
    private Date createdDt;

    @Column(name = "updated_dt")
    private Date updatedDt;
}
