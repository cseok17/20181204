package com.kcs.test.controller;

import java.util.HashMap;
import java.util.Map;
import java.net.URI;
import java.net.URLEncoder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class ApiController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	/* 카카오 키워드 검색 API */
	public Map<String,Object> addrApi(String keyword, long search_page) {		
		String api_key = "KakaoAK 4fc244ff242b55f017fcbc25186555c0"; 
		String api_url = "https://dapi.kakao.com/v2/local/search/keyword.json";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			RestTemplate restTemplate = new RestTemplate(); 
			 
			HttpHeaders headers = new HttpHeaders(); 
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", api_key); 
			 
			HttpEntity entity = new HttpEntity("parameters", headers); 
			
			String enc_keyword = URLEncoder.encode(keyword, "UTF-8");
			
			URI url=URI.create(api_url + "?query=" + enc_keyword + "&page=" + search_page); 
			
			ResponseEntity response= restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			
			
			JSONParser jsonParser = new JSONParser(); 
			JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody().toString()); 
			map = (Map)jsonObject;
			
		} catch(Exception e){
			logger.error("##### error : " + e.getMessage());
		}

		return map;
	}

		
	
}
