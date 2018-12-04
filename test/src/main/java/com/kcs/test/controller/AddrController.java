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
import com.kcs.test.db.DbManager;
import com.kcs.test.entity.Keyword;
import com.kcs.test.controller.ApiController;

@Controller
public class AddrController {
	
	private static final Logger logger = LoggerFactory.getLogger(AddrController.class);
	
	@RequestMapping(value = "/addr", method = RequestMethod.GET, produces = "text/plain; charset=utf-8")
	public String addr(HttpServletRequest req, HttpServletResponse res) throws Exception {		
		
		HttpSession session = req.getSession(true);
		session.getAttribute("user_id");
		
		System.out.println(session.getAttribute("user_id"));
				
		return "addr";
	}
	
	@RequestMapping(value = "/addr_action", method = RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> addr_action(@RequestBody Map<String, Object> data, HttpServletRequest req, HttpServletResponse res) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");
		EntityManager entityManager = factory.createEntityManager();
		
		System.out.println(data);
		
		try {
			if ("curr".equals(data.get("gubun").toString())) {
				System.out.println("Keyword Insert / Update");
				boolean keyword_exist = keyword_yn(entityManager, data.get("keyword").toString());
				
				if (!keyword_exist) {
					System.out.println("Insert keyword_data"); 			
					keyword_insert(entityManager, data.get("keyword").toString());
	
				} else {
					System.out.println("Update keyword_data"); 			
					keyword_update(entityManager, data.get("keyword").toString());
				}
			}
			
			/* API - START */
			ApiController api = new ApiController();
			map = api.addrApi(data.get("keyword").toString(), Math.round((Double)data.get("search_page")));
			map.put("search_page", Math.round((Double)data.get("search_page")));

			HttpSession session = req.getSession(true);
		} catch (Exception e) {
			map.put("res_code", "90002");
			map.put("res_desc", "로그인 중 오류 발생");
			logger.error("##### error : " + e.getMessage());
			entityManager.close();
			factory.close();
		} finally {
			entityManager.close();
			factory.close();
		}

		System.out.println(map);
		return map;
	}	
	
	public boolean keyword_yn(EntityManager entityManager, String keyword) {
		Object keyword_data = entityManager.find(Keyword.class, keyword);
		System.out.println("addr_action - keyword_data"); 			
		System.out.println(keyword_data); 
		
		if (keyword_data == null || keyword_data == "") {
			System.out.println("addr_action - keyword_data_null"); 			
			return false;
		} else {
			System.out.println("addr_action - keyword_data_notnull");
			return true;
		}
	}
	
	public void keyword_insert(EntityManager entityManager, String keyword) {
		EntityTransaction transaction = entityManager.getTransaction(); 
		try {
			transaction.begin(); 
			System.out.println("addr_action - Insert keyword_data"); 			
			
			Keyword newKeyword = new Keyword(keyword, 1);
			entityManager.persist(newKeyword);
			
			entityManager.flush(); 
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		} 
	}
	
	public void keyword_update(EntityManager entityManager, String keyword) {
		EntityTransaction transaction = entityManager.getTransaction(); 
		try {
			transaction.begin(); 
		
			System.out.println("Update keyword"); 			
			
			Keyword uptKeyword = entityManager.find(Keyword.class, keyword); 
			System.out.println("Update keyword count : " + uptKeyword.getCount()); 			
			
			uptKeyword.setCount(uptKeyword.getCount() + 1);
			
			entityManager.flush(); 
			transaction.commit(); 
		} catch (Exception e) {
			transaction.rollback();
		} 
	}
}
