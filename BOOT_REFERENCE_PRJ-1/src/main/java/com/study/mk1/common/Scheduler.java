package com.study.mk1.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.procedure.internal.Util.ResultClassesResolutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.study.mk1.cmp.components.CoronaCommonComponent;
import com.study.mk1.config.GlobalPropertySource;
import com.study.mk1.entity.CoronaInfo;
import com.study.mk1.interfaces.CoronaVirusApiController;
import com.study.mk1.jpa.coronaInfo.CoronaInfoJpa;

@Component
public class Scheduler {
	
	@Autowired
	CoronaCommonComponent coronaCommonComponent;
	
	@Autowired
	GlobalPropertySource globalPropertySource;
	
	private static Logger log = LoggerFactory.getLogger(Scheduler.class);
	
	/**
	 * 코로나 관련 정보 인터페이스 호출 스케쥴러
	 */
	@Scheduled(cron = "0 39 20 * * ?")
	public void selectCoronaInfoScheduler() {
		
		log.info(this.getClass()+".selectCoronaInfoScheduler() [START] {}",globalPropertySource.getBaseUri().toString());
		
		try {
			
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectionRequestTimeout(5000);//타임아웃설정 5초
			factory.setReadTimeout(5000); //타임아웃 설정 5초
				
			HttpHeaders header = new HttpHeaders();
			HttpEntity<?> entity = new HttpEntity<>(header);
			header.setContentType(MediaType.APPLICATION_JSON);
			
			UriComponents uriComponents = UriComponentsBuilder.fromUriString(globalPropertySource.getBaseUri().toString())
			.path("/inf/api/getCoronaConfirmedInfoXmlParse")
			.build();
	
	
			RestTemplate restTemplate = new RestTemplate(factory);
			
			ResponseEntity< Map[]> responseMap = restTemplate.getForEntity(uriComponents.toUri(), Map[].class );
			List<Map<String,String>> list = Arrays.asList(responseMap.getBody());
			
			List<CoronaInfoJpa> coronaInfoList = new ArrayList<>();
	
			for(Map<String,String> map : list) {
				
				CoronaInfoJpa obj = new CoronaInfoJpa();
				obj.setCreatedDt(map.get("stdDay"));
				obj.setCityNm(map.get("gubun"));		    	
				obj.setDefCnt(map.get("defCnt"));
				obj.setDeathCnt(map.get("deathCnt"));
				obj.setIsolatingCnt(map.get("isolIngCnt"));
				obj.setIsolClearCnt(map.get("isolClearCnt"));
				obj.setIncDec(map.get("incDec"));
				coronaInfoList.add(obj);
				
			}
	
			coronaCommonComponent.insertCoronaInfo(coronaInfoList);
	
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info(this.getClass()+".selectCoronaInfoScheduler() [ERROR] {}",e.toString());
			log.error(this.getClass()+".selectCoronaInfoScheduler() [ERROR] {}",e.toString());
			e.printStackTrace();
		}

	}
	
}

