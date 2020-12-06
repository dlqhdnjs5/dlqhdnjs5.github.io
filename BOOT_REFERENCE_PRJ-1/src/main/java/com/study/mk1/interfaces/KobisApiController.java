package com.study.mk1.interfaces;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.study.mk1.controllers.SimpleTestController;
import com.study.mk1.data.KobisApiDTO;
import com.study.mk1.data.KobisApiResult;

@RestController
public class KobisApiController {
	
	 /*영화 진흥 위원회 API 키 */
	 private final String key = "d51c621b6255f6a26ef87af3e48d2bcf";
	 /*영화 진흥 위원회 API Url */
	 private final String KobisUrl =  "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json";
	 
	 private static Logger log = LoggerFactory.getLogger(KobisApiController.class);
	 
	/**
	 * 영화 진흥 위원회  API
	 * @param req
	 * @param res
	 */
	@GetMapping("/GetKobisData")
	public void callAPI(HttpServletRequest req,HttpServletResponse res){
		
		HashMap<String, Object> result = new HashMap<>();

		try {
			
			/*HttpComponentsClientHttpRequestFactory 객체를 통해 타임아웃을 제어 할수 있다.*/
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(); 
			factory.setConnectTimeout(5000);//타임아웃설정 5초
			factory.setReadTimeout(5000); //타임아웃 설정 5초
			
			HttpHeaders header = new HttpHeaders();
			HttpEntity<?> entity = new HttpEntity<>(header);
			
			UriComponents uriComponent = UriComponentsBuilder.fromUriString(KobisUrl)
										.queryParam("key", key)
										.queryParam("targetDt", "20181225")
										.build();
			
			RestTemplate restTemplate = new RestTemplate(factory);
			
			ResponseEntity<Map> apiResultMap = restTemplate.exchange(uriComponent.toUri(), HttpMethod.GET, entity , Map.class);
			
			result.put("statusCode", apiResultMap.getStatusCode());
			result.put("header", apiResultMap.getHeaders());
			result.put("body", apiResultMap.getBody());
			
			log.info("API STATUS CODE  : {}",result.get("statusCode"));
			log.info("API HEADER : {}",result.get("header"));
			log.info("API BODY : {}",result.get("body"));
			
			if(HttpStatus.OK.equals(apiResultMap.getStatusCode() )) {
				
				try(Writer writer = res.getWriter()){
					
					writer.write(result.get("body").toString());
					
				}catch(IOException e) {
					
					e.printStackTrace();
					
				}
				
			}else {
				log.warn("API STATUS CODE : {}",apiResultMap.getStatusCode());
			}
			
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			
			e.printStackTrace();
			
        }catch(Exception e) {
        	
			e.printStackTrace();
			
		}
		
	}

}
