package com.jk.labs.spring_ai.pet_care.catalog.dao.impl;

import com.jk.labs.spring_ai.pet_care.catalog.common.dao.PackageDao;
import com.jk.labs.spring_ai.pet_care.catalog.common.entity.PackageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageDaoImpl extends PackageDao, CrudRepository<PackageEntity, String> {
}
