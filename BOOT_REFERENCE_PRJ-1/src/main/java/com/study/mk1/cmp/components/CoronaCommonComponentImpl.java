package com.study.mk1.cmp.components;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.study.mk1.cmp.services.CoronaCommanService;
import com.study.mk1.cmp.services.MbrService;
import com.study.mk1.jpa.coronaInfo.CoronaInfoJpa;

@Component
@Transactional
public class CoronaCommonComponentImpl implements CoronaCommonComponent{

	private static Logger log = LoggerFactory.getLogger(CoronaCommonComponentImpl.class);
	
	
	@Autowired
	CoronaCommanService coronaCommanService;
	
	
	public void insertCoronaInfo(List<CoronaInfoJpa> coronaInfoList) {
		
		try {
			
			coronaCommanService.insertCoronaInfo(coronaInfoList);
			
		}catch(Exception e) {
			log.info(this.getClass()+".insertCoronaInfo() insert [ERROR] dto => {}",coronaInfoList);
			throw new RuntimeException();
		}
		
	};
}
