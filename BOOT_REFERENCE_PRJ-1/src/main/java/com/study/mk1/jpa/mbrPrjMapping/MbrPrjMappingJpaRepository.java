package com.study.mk1.jpa.mbrPrjMapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;

@Repository
public interface MbrPrjMappingJpaRepository extends JpaRepository<MbrPrjMappingJpa, Long>{

}
