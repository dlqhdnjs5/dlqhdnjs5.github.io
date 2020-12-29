package com.study.mk1.cmp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.mk1.jpa.coronaInfo.CoronaInfoJpa;
import com.study.mk1.jpa.coronaInfo.CoronaInfoJpaRepository;

@Service
public class CoronaCommanService {
	
	@Autowired
	CoronaInfoJpaRepository coronaInfoJpaRepository;

	/**
	 * 코로나 정보 등록
	 * @param coronaInfoList
	 * @throws Exception
	 */
	public void insertCoronaInfo(List<CoronaInfoJpa> coronaInfoList) throws Exception{
		
			coronaInfoJpaRepository.saveAll(coronaInfoList);
			
	};
	
}
