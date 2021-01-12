package com.study.mk1.jpa.sysUpperCd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.cdi.JpaRepositoryExtension;

public interface SysUpperCdJpaRepository extends JpaRepository<SysUpperCdJpa, String>{

	/**
	 * 공통코드 (부모코드로 조회)
	 * @param upperCd
	 * @return
	 */
	SysUpperCdJpa findByUpperCd(String upperCd);
}
