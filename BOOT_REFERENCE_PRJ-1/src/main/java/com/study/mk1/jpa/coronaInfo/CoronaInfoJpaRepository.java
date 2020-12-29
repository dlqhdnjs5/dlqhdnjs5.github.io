package com.study.mk1.jpa.coronaInfo;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CoronaInfoJpaRepository extends JpaRepository<CoronaInfoJpa , String>{

	@Query("SELECT c FROM CoronaInfoJpa c WHERE 1=1 AND cityNm <> '합계' ORDER BY createdDt DESC")
	public Page<CoronaInfoJpa> findAllExceptTotal(Pageable pageable);
}
