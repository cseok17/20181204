package com.kcs.test.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.kcs.test.db.DbManager;
import com.kcs.test.entity.User;
import com.kcs.test.controller.PswdController;

@Controller
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest req, HttpServletResponse res) {		
		
		HttpSession session = req.getSession(true);
		session.getAttribute("user_id");
		
		System.out.println(session.getAttribute("user_id"));
		
		/* 초기 사용자 데이터 생성 */
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");
		EntityManager entityManager = factory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction(); 
		
		String init_user = "test";
		String init_pswd = "test";
		
		try {	
			Object user_data = entityManager.find(User.class, init_user);
			
			if (user_data == null || user_data == "") {
				System.out.println("Insert Init User");
				transaction.begin(); 
	
				/* 비밀번호 암호화 */
				PswdController pswdctl = new PswdController();
				String hash_pswd = pswdctl.hash(init_pswd);
				System.out.println(hash_pswd);
				
				//사용자 생성
				User newUser = new User(init_user, hash_pswd);
				entityManager.persist(newUser);
	
				entityManager.flush(); 
	
				transaction.commit(); 
			} else {
				System.out.println("Exist Init User");
			}
		} catch (Exception e) {
			logger.error("##### error : " + e.getMessage());
			entityManager.close();
			factory.close();
		} finally {
			entityManager.close();
			factory.close();
		}	
				
		return "login";
	}
	
	/* 로그인 확인 버튼 클릭 시 */
	@RequestMapping(value = "/login_action", method = RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> login_action(@RequestBody String data, HttpServletRequest req, HttpServletResponse res) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		JSONObject obj = new JSONObject(data);
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");
		EntityManager entityManager = factory.createEntityManager();

		try {	
			Object user_data = entityManager.find(User.class, obj.getString("id"));
			
			JSONObject obj_data = new JSONObject(user_data);
			
			PswdController pswdctl = new PswdController();
			String hash_pswd = pswdctl.hash(obj.getString("pswd"));
					
			if (hash_pswd.equals(obj_data.getString("pswd"))) {
				map.put("res_code", "00000");
				map.put("res_desc", "success");
			} else {
				map.put("res_code", "90001");
				map.put("res_desc", "Password Error");
			}
			
			HttpSession session = req.getSession(true);
			session.setAttribute("user_id", obj.getString("id"));

		} catch (Exception e) {
			map.put("res_code", "90002");
			map.put("res_desc", "Login Error");
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

	/* 가입 버튼 클릭 시 */
	@RequestMapping(value = "/login_join", method = RequestMethod.POST)
	@ResponseBody
	public String login_join(@RequestBody String data, HttpServletRequest req, HttpServletResponse res) {
		
		JSONObject obj = new JSONObject(data);
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");
		EntityManager entityManager = factory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction(); 
		
		try {	
			transaction.begin(); 

			//비밀번호 암호화
			PswdController pswdctl = new PswdController();
			String hash_pswd = pswdctl.hash(obj.getString("pswd"));
			System.out.println(hash_pswd);
			
			//사용자 생성
			User newUser = new User(obj.getString("id"), hash_pswd);
			entityManager.persist(newUser);

			entityManager.flush(); 

			transaction.commit(); 
			
			HttpSession session = req.getSession(true);
			session.setAttribute("user_id", obj.getString("id"));
		} catch (Exception e) {
			logger.error("##### error : " + e.getMessage());
			transaction.rollback(); 
			entityManager.close();
			factory.close();
		} finally {
			entityManager.close();
			factory.close();
		}	
		return data;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		
		ModelAndView mav = new ModelAndView();
		DbManager dbManager;
		EntityManager entityManager;
		
		Query query; 
		List list; 
		
		dbManager = new DbManager();
		
		try
		{
			mav.setViewName("user_list");
			
			entityManager = dbManager.getEntityManager();
			
			// get list 
			query = entityManager.createQuery("from User"); 
			list = query.getResultList(); 
			
			logger.debug("##### list.size : " + list.size());
			
			mav.addObject("list", list);
			System.out.println(mav);
		}catch (Exception e)
		{
			logger.error("##### error : " + e.getMessage());
		}finally
		{
			dbManager.closeEntityManager();
		}
		
		return mav;
	}
	
}
