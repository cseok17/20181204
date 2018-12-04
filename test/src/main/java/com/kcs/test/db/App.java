package com.kcs.test.db;

import java.util.List;

import javax.persistence.EntityManager; 
import javax.persistence.EntityManagerFactory; 
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.kcs.test.entity.User;

public class App { 
	public static void main(String[] args) { 

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");
		EntityManager entityManager = factory.createEntityManager();
		
		//makeDummyData(entityManager); 
		
		// get list 
		Query query = entityManager.createQuery("from User"); 
		List list = query.getResultList(); 
				
		printList(list); 
		System.out.println("...ok."); 
				
		// get one
		//Object oneSample = entityManager.find(User.class, new Long(2));
		//printOne(oneSample);
		//System.out.println("...ok."); 
				
		entityManager.close();
		factory.close();
	} 
	
	// create dummy entity data. 
	public static void makeDummyData(EntityManager manager) { 
		EntityTransaction transaction = manager.getTransaction(); 
		transaction.begin(); 
		manager.persist(new User("tuyano", "syoda@tuyano.com")); 
		manager.persist(new User("hanako", "hanako@flower")); 
		manager.persist(new User("taro", "taro@yamada")); 
		manager.persist(new User("sachiko", "sachico@happy")); 
		manager.flush(); 
		transaction.commit(); 
	} 
	
	// print all entity. 
	public static void printList(List list) { 
		for (Object item : list) { 
			System.out.println(item); 
		} 
	}
	
	// print one entity. 
	public static void printOne(Object item) { 
		System.out.println(item); 
	}
}

