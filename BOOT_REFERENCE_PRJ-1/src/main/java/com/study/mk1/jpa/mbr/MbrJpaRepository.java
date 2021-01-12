package com.study.mk1.jpa.mbr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MbrJpaRepository extends JpaRepository<MbrJpa, Long>{
	
	/**
	 * mbrId 로 권한과 회원정보 가져오기
	 * @param mbrId
	 * @return
	 */
	MbrJpa findByMbrId(String mbrId);
	
	/**
	 * mbrId 로 갯수 확인
	 * @param mbrId
	 * @return
	 */
	long countByMbrId(String mbrId);
}
