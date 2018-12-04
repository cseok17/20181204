package com.kcs.test.controller;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.kcs.test.entity.Keyword;
import com.kcs.test.controller.ApiController;

@Controller
public class AddrController {
	
	private static final Logger logger = LoggerFactory.getLogger(AddrController.class);
	
	@RequestMapping(value = "/addr", method = RequestMethod.GET, produces = "text/plain; charset=utf-8")
	public String addr(HttpServletRequest req, HttpServletResponse res) throws Exception {		
		
		HttpSession session = req.getSession(true);
		session.getAttribute("user_id");
				
		return "addr";
	}
	
	/* 키워드 검색 실행 시*/
	@RequestMapping(value = "/addr_action", method = RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> addr_action(@RequestBody Map<String, Object> data, HttpServletRequest req, HttpServletResponse res) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");
		EntityManager entityManager = factory.createEntityManager();
		
		try { 
			/* 키워드 검색 시 데이터 저장 */
			if ("curr".equals(data.get("gubun").toString())) {
				/* 키워드가 신규인지 기존에 존재하는 키워드인지 확인 */
				boolean keyword_exist = keyword_yn(entityManager, data.get("keyword").toString());
				
				if (!keyword_exist) {
					/* 키워드가 신규일 경우 INSERT */ 			
					keyword_insert(entityManager, data.get("keyword").toString());
				} else {
					/* 키워드가 존재할 경우 UPDATE */ 			
					keyword_update(entityManager, data.get("keyword").toString());
				}
			}
			
			/* 키워드 검색  */
			ApiController api = new ApiController();
			map = api.addrApi(data.get("keyword").toString(), Math.round((Double)data.get("search_page")));
			map.put("search_page", Math.round((Double)data.get("search_page")));

			HttpSession session = req.getSession(true);
		} catch (Exception e) {
			map.put("res_code", "99999");
			map.put("res_desc", "키워드 검색 중 오류 발생");
			logger.error("##### error : " + e.getMessage());
			entityManager.close();
			factory.close();
		} finally {
			entityManager.close();
			factory.close();
		}

		return map;
	}	
	
	/* 키워드가 신규인지 기존에 존재하는 키워드인지 확인 */
	public boolean keyword_yn(EntityManager entityManager, String keyword) {
		Object keyword_data = entityManager.find(Keyword.class, keyword);
		
		if (keyword_data == null || "".equals(keyword_data)) {
			return false;
		} else {
			return true;
		}
	}
	
	/* 키워드가 신규일 경우 INSERT */
	public void keyword_insert(EntityManager entityManager, String keyword) {
		EntityTransaction transaction = entityManager.getTransaction(); 
		try {
			transaction.begin(); 
			
			Keyword newKeyword = new Keyword(keyword, 1);
			entityManager.persist(newKeyword);
			
			entityManager.flush(); 
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		} 
	}
	/* 키워드가 존재할 경우 UPDATE */
	public void keyword_update(EntityManager entityManager, String keyword) {
		EntityTransaction transaction = entityManager.getTransaction(); 
		try {
			transaction.begin(); 
		
			Keyword uptKeyword = entityManager.find(Keyword.class, keyword); 
			
			uptKeyword.setCount(uptKeyword.getCount() + 1);
			
			entityManager.flush(); 
			transaction.commit(); 
		} catch (Exception e) {
			transaction.rollback();
		} 
	}
}
