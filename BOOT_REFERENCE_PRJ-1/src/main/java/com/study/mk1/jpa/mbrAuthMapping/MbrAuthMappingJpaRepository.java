package com.study.mk1.jpa.mbrAuthMapping;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MbrAuthMappingJpaRepository extends JpaRepository<MbrAuthMappingJpa, Long>{
	
}
