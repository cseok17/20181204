package com.kcs.test.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kcs.test.entity.Keyword;

@Controller
public class KeywordController {

	private static final Logger logger = LoggerFactory.getLogger(KeywordController.class);
	
	/* 인기검색어 (TOP10) 조회 */
	@RequestMapping(value = "/keyword_action", method = RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> login_action(@RequestBody Map data, HttpServletRequest req, HttpServletResponse res) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		JSONObject obj = new JSONObject(data);

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");
		EntityManager entityManager = factory.createEntityManager();

		try {	
			TypedQuery<Keyword> query =
					entityManager.createQuery("SELECT c FROM Keyword c ORDER BY c.count DESC", Keyword.class);
			List<Keyword> result = query.getResultList();
			
			JSONArray keywordArray = new JSONArray();
			
			int i = 0;
			for (Keyword row : result) {
				JSONObject keywordObj = new JSONObject();
			 	keywordObj.put("keyword", result.get(i).getKeyword());
				keywordObj.put("count", result.get(i).getCount());
				keywordArray.add(keywordObj);
				i++;
				
				if (i > 9) {
					break;
				}
			}

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("keyword", keywordArray);
			
			map = (Map)jsonObj; 
			
		} catch (Exception e) {
			map.put("res_code", "99999");
			map.put("res_desc", "키워드 검색 중 오류 발생");
			entityManager.close();
			factory.close();
		} finally {
			entityManager.close();
			factory.close();
		}
		System.out.println(map);
		return map;
	}
}
