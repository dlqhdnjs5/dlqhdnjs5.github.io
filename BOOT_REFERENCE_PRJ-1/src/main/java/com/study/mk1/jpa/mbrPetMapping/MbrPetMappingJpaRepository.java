package com.study.mk1.jpa.mbrPetMapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.mk1.jpa.mbrPrjMapping.MbrPrjMappingJpa;

@Repository
public interface MbrPetMappingJpaRepository extends JpaRepository<MbrPetMappingJpa, Long>{

}
