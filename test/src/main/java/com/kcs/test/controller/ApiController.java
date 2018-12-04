package com.kcs.test.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.net.URI;
import java.net.URLEncoder;
import java.security.MessageDigest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.kcs.test.db.DbManager;
import com.kcs.test.entity.User;

@Controller
public class ApiController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	public Map<String,Object> addrApi(String keyword, long search_page) {		
		String api_key = "KakaoAK 4fc244ff242b55f017fcbc25186555c0"; 
		String api_url = "https://dapi.kakao.com/v2/local/search/keyword.json";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			RestTemplate restTemplate = new RestTemplate(); 
			 
			HttpHeaders headers = new HttpHeaders(); 
			headers.setContentType(MediaType.APPLICATION_JSON);//JSON º¯È¯ 
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
