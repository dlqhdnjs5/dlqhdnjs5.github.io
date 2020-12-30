package com.study.mk1.interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@RestController
@RequestMapping("/inf/api")
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
					.queryParam(URLEncoder.encode("ServiceKey","UTF-8"), URLEncoder.encode(key,"UTF-8") )
					.queryParam(URLEncoder.encode("pageNo","UTF-8"), URLEncoder.encode("1","UTF-8"))
					.queryParam(URLEncoder.encode("numOfRows","UTF-8"), URLEncoder.encode("10","UTF-8"))
					.queryParam(URLEncoder.encode("startCreateDt","UTF-8"), URLEncoder.encode("20200101","UTF-8"))
					.queryParam(URLEncoder.encode("numOfRows","UTF-8"), URLEncoder.encode("20201101","UTF-8"))
					.build();
			
			RestTemplate restTemplate = new RestTemplate(factory);
			
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
	
	/**
	 * 확진자 관련 정보 Api xml 문서 파싱
	 * @param request
	 * @param response
	 */
	@GetMapping("/getCoronaConfirmedInfoXmlParse")
	public List<Map<String,String>> getCoronaConfirmedInfoXmlParse(HttpServletRequest request, HttpServletResponse response)  {
		
		
		List<Map<String,String>> resultList  = new ArrayList<>();
		try {
			
			Date dDate = new Date();
			dDate = new Date(dDate.getTime()+(1000*60*60*24*-1));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
			String yesterday = sdf.format(dDate);

			
			
			StringBuilder urlBuilder = new StringBuilder(bogunUrl); /*URL*/
	        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + key); /*Service Key*/
	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
	        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode(yesterday, "UTF-8")); /*검색할 생성일 범위의 시작*/
	        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode(yesterday, "UTF-8")); /*검색할 생성일 범위의 종료*/
	        URL url = new URL(urlBuilder.toString());
	        
	        /**
	         * xml 파싱
	         */
	        DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
	        Document doc = dBuilder.parse(url.toString());
	        
	        doc.getDocumentElement().normalize();
	        NodeList nList = doc.getElementsByTagName("item");
	        System.out.println("파싱할 리스트 수 : "+ nList.getLength()); 
	        
	        for(int temp = 0; temp < nList.getLength(); temp++){		
	        	Node nNode = nList.item(temp);
	        	if(nNode.getNodeType() == Node.ELEMENT_NODE){
	        		
	        		Map result = new HashMap();
	        		
	        		Element eElement = (Element) nNode;
	        		String stdDt = getTagValue("stdDay", eElement);
	        		stdDt = stdDt.replaceAll("[^0-9]","");
	        		log.info("날짜 : " +getTagValue("stdDay", eElement));
	        		log.info("시도명 : " +getTagValue("gubun", eElement));
	        		log.info("확진자수 : "  +getTagValue("defCnt", eElement));
	        		log.info("사망자수 : "+getTagValue("deathCnt", eElement));
	        		log.info("격리자 수 : "  +getTagValue("isolIngCnt", eElement));
	        		log.info("격리 해제 수 : "  +getTagValue("isolClearCnt", eElement));
	        		log.info("전일대비 증감수 : "+getTagValue("incDec", eElement));
	        		log.info("##############################");
	        		
	        		result.put("stdDay", stdDt);
	        		result.put("gubun", getTagValue("gubun", eElement));
	        		result.put("defCnt", getTagValue("defCnt", eElement));
	        		result.put("deathCnt", getTagValue("deathCnt", eElement));
	        		result.put("isolIngCnt", getTagValue("isolIngCnt", eElement));
	        		result.put("isolClearCnt", getTagValue("isolClearCnt", eElement));
	        		result.put("incDec", getTagValue("incDec", eElement));
	        		
	        		resultList.add(result);
	    
	        	}	
	        }
	
	        
			
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			
			e.printStackTrace();
			
        }catch(Exception e) {
        	
			e.printStackTrace();
			
		}
		
		return resultList;
	}
	
	/**
	 * 확진자 관련 정보 Api xml 문서 파싱
	 * @param request
	 * @param response
	 */
	@GetMapping("/getCoronaConfirmedInfoXmlParse2")
	public ResponseEntity<List<Map<String,String>>> getCoronaConfirmedInfoXmlParse2(HttpServletRequest request, HttpServletResponse response)  {
		
		
		List<Map<String,String>> resultList  = new ArrayList<>();
		try {
			
			Date dDate = new Date();
			dDate = new Date(dDate.getTime()+(1000*60*60*24*-1));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
			String yesterday = sdf.format(dDate);

			
			
			StringBuilder urlBuilder = new StringBuilder(bogunUrl); /*URL*/
	        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + key); /*Service Key*/
	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
	        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode(yesterday, "UTF-8")); /*검색할 생성일 범위의 시작*/
	        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode(yesterday, "UTF-8")); /*검색할 생성일 범위의 종료*/
	        URL url = new URL(urlBuilder.toString());
	        
	        /**
	         * xml 파싱
	         */
	        DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
	        Document doc = dBuilder.parse(url.toString());
	        
	        doc.getDocumentElement().normalize();
	        NodeList nList = doc.getElementsByTagName("item");
	        System.out.println("파싱할 리스트 수 : "+ nList.getLength()); 
	        
	        for(int temp = 0; temp < nList.getLength(); temp++){		
	        	Node nNode = nList.item(temp);
	        	if(nNode.getNodeType() == Node.ELEMENT_NODE){
	        		
	        		Map result = new HashMap();
	        		
	        		Element eElement = (Element) nNode;
	        		String stdDt = getTagValue("stdDay", eElement);
	        		stdDt = stdDt.replaceAll("[^0-9]","");
	        		log.info("날짜 : " +getTagValue("stdDay", eElement));
	        		log.info("시도명 : " +getTagValue("gubun", eElement));
	        		log.info("확진자수 : "  +getTagValue("defCnt", eElement));
	        		log.info("사망자수 : "+getTagValue("deathCnt", eElement));
	        		log.info("격리자 수 : "  +getTagValue("isolIngCnt", eElement));
	        		log.info("격리 해제 수 : "  +getTagValue("isolClearCnt", eElement));
	        		log.info("전일대비 증감수 : "+getTagValue("incDec", eElement));
	        		log.info("##############################");
	        		
	        		result.put("stdDay", stdDt);
	        		result.put("gubun", getTagValue("gubun", eElement));
	        		result.put("defCnt", getTagValue("defCnt", eElement));
	        		result.put("deathCnt", getTagValue("deathCnt", eElement));
	        		result.put("isolIngCnt", getTagValue("isolIngCnt", eElement));
	        		result.put("isolClearCnt", getTagValue("isolClearCnt", eElement));
	        		result.put("incDec", getTagValue("incDec", eElement));
	        		
	        		resultList.add(result);
	    
	        	}	
	        }
	
	        
			
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			
			e.printStackTrace();
			
        }catch(Exception e) {
        	
			e.printStackTrace();
			
		}
		
		return new ResponseEntity<List<Map<String,String>>>(resultList,HttpStatus.OK);
	}
	
	private static String getTagValue(String tag, Element eElement) {
	    NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    if(nValue == null) 
	        return null;
	    return nValue.getNodeValue();
	}
	
	@GetMapping("/getCoronaConfirmedInfoSample")
	public void getCoronaConfirmedInfoSample(HttpServletRequest request, HttpServletResponse response) throws IOException  {
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
	
	@GetMapping("/getCoronaConfirmedInfoSampleWithHttpClient")
	public void getCoronaConfirmedInfoSampleWithHttpClient(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		try {
			
			 StringBuilder urlBuilder = new StringBuilder(bogunUrl); /*URL*/
		        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + key); /*Service Key*/
		        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
		        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
		        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode("20200410", "UTF-8")); /*검색할 생성일 범위의 시작*/
		        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode("20200410", "UTF-8")); /*검색할 생성일 범위의 종료*/
		        URL url = new URL(urlBuilder.toString());
		        log.info(urlBuilder.toString());
				
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpResponse httpResponse = httpClient.execute(new HttpGet(url.toString()));
				
				BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				
				String inputLine;
				StringBuilder sb = new StringBuilder();
				while((inputLine = br.readLine()) != null) {
					sb.append(inputLine);
				}
				
				br.close();
				
				log.info(sb.toString());
		        
		}catch(HttpClientErrorException | HttpServerErrorException | IOException e) {
			e.printStackTrace();
		}
		 
	}
	
}
