package com.study.mk1.jpa.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.mk1.jpa.mbrPrjMapping.MbrPrjMappingJpa;

@Repository
public interface  PetJpaRepository extends JpaRepository<PetJpa, Long>{

}
