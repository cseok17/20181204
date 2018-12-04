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
	
	/* Ű���� �˻� ���� ��*/
	@RequestMapping(value = "/addr_action", method = RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> addr_action(@RequestBody Map<String, Object> data, HttpServletRequest req, HttpServletResponse res) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");
		EntityManager entityManager = factory.createEntityManager();
		
		try { 
			/* Ű���� �˻� �� ������ ���� */
			if ("curr".equals(data.get("gubun").toString())) {
				/* Ű���尡 �ű����� ������ �����ϴ� Ű�������� Ȯ�� */
				boolean keyword_exist = keyword_yn(entityManager, data.get("keyword").toString());
				
				if (!keyword_exist) {
					/* Ű���尡 �ű��� ��� INSERT */ 			
					keyword_insert(entityManager, data.get("keyword").toString());
				} else {
					/* Ű���尡 ������ ��� UPDATE */ 			
					keyword_update(entityManager, data.get("keyword").toString());
				}
			}
			
			/* Ű���� �˻�  */
			ApiController api = new ApiController();
			map = api.addrApi(data.get("keyword").toString(), Math.round((Double)data.get("search_page")));
			map.put("search_page", Math.round((Double)data.get("search_page")));

			HttpSession session = req.getSession(true);
		} catch (Exception e) {
			map.put("res_code", "99999");
			map.put("res_desc", "Ű���� �˻� �� ���� �߻�");
			logger.error("##### error : " + e.getMessage());
			entityManager.close();
			factory.close();
		} finally {
			entityManager.close();
			factory.close();
		}

		return map;
	}	
	
	/* Ű���尡 �ű����� ������ �����ϴ� Ű�������� Ȯ�� */
	public boolean keyword_yn(EntityManager entityManager, String keyword) {
		Object keyword_data = entityManager.find(Keyword.class, keyword);
		
		if (keyword_data == null || "".equals(keyword_data)) {
			return false;
		} else {
			return true;
		}
	}
	
	/* Ű���尡 �ű��� ��� INSERT */
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
	/* Ű���尡 ������ ��� UPDATE */
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
