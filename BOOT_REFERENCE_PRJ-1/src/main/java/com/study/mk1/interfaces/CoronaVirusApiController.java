package com.study.mk1.interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CoronaVirusApiController {

	/**
	 * 보건 복지부 코로나 바이러스 시도발생 현황 일반 인증키
	 */
	private static final String key = "CUSFKB7vmLNtCHcwg2OQZa2O8TjJFqE97atnu2P4rqmJ85CQ7CeJ5R3Y0BasHvno0lgMouBV%2FKFxroGeEVhLyQ%3D%3D";

	private static final String bogunUrl = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson";
	
	private static Logger log = LoggerFactory.getLogger(CoronaVirusApiController.class);
	
	
	@GetMapping("/getCoronaConfirmedInfo")
	public void getCoronaConfirmedInfo(HttpServletRequest request, HttpServletResponse response)  {
		
		try {
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectionRequestTimeout(5000);//타임아웃설정 5초
			factory.setReadTimeout(5000); //타임아웃 설정 5초
			
			HttpHeaders header = new HttpHeaders();
			HttpEntity<?> entity = new HttpEntity<>(header);
			header.setContentType(MediaType.APPLICATION_JSON);
			
			UriComponents uriComponent = UriComponentsBuilder.fromUriString(bogunUrl)
					.queryParam("serviceKey", URLEncoder.encode(key,"UTF-8") )
					.queryParam("startCreateDt", URLEncoder.encode("20200101","UTF-8"))
					.queryParam("endCreateDt", URLEncoder.encode("20201101","UTF-8"))
					.build();
			
			RestTemplate restTemplate = new RestTemplate(factory);
			log.info("url : {}",uriComponent.toUri());
			ResponseEntity<Map> responseMap = restTemplate.exchange(uriComponent.toUri(), HttpMethod.GET, entity , Map.class);
			
			Map<String,Object> resultMap = new HashMap<>();
			
			resultMap.put("resultCode", responseMap.getStatusCode());
			resultMap.put("header", responseMap.getHeaders());
			resultMap.put("body", responseMap.getBody());
			
			if(HttpStatus.OK.equals(responseMap.getStatusCode() )) {
				
				try(Writer writer = response.getWriter()){
					
					writer.write(resultMap.get("body").toString());
					
				}catch(IOException e) {
					
					e.printStackTrace();
					
				}
				
			}else {
				log.warn("API STATUS CODE : {}",responseMap.getStatusCode());
			}
			
			log.info(this.getClass()+".getCoronaConfirmedInfo() result -> {}",resultMap.get("resultCode"));
			
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			
			e.printStackTrace();
			
        }catch(Exception e) {
        	
			e.printStackTrace();
			
		}
	}
	
	@GetMapping("/getCoronaConfirmedInfoSample")
	public void getCoronaConfirmedInfo2(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		try {
			
			 StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson"); /*URL*/
		        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + key); /*Service Key*/
		        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
		        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
		        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode("20200410", "UTF-8")); /*검색할 생성일 범위의 시작*/
		        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode("20200410", "UTF-8")); /*검색할 생성일 범위의 종료*/
		        URL url = new URL(urlBuilder.toString());
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Content-type", "application/json");
		        System.out.println("Response code: " + conn.getResponseCode());
		        BufferedReader rd;
		        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
		            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		        } else {
		            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		        }
		        StringBuilder sb = new StringBuilder();
		        String line;
		        while ((line = rd.readLine()) != null) {
		            sb.append(line);
		        }
		        rd.close();
		        conn.disconnect();
		        System.out.println(sb.toString());
		        
		}catch(HttpClientErrorException | HttpServerErrorException | IOException e) {
			e.printStackTrace();
		}
		 
	}
	
}
