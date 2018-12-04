package com.kcs.test.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DbManager {
	
	EntityManagerFactory factory;
	EntityManager entityManager;
	
	public DbManager() {
		factory = Persistence.createEntityManagerFactory("test");
		entityManager = factory.createEntityManager();
	}
	
	public EntityManager getEntityManager()
	{
		if (this.entityManager == null)
		{
			factory = Persistence.createEntityManagerFactory("test");
			entityManager = factory.createEntityManager();
		}
		return this.entityManager;
	}
	
	public void closeEntityManager()
	{
		try
		{
			if (entityManager != null)
			{
				entityManager.close();
			}
			
			if (factory != null)
			{
				factory.close();
			}
		}catch(Exception e)
		{
			try
			{
				if (entityManager != null)
				{
					entityManager.close();
				}
				
				if (factory != null)
				{
					factory.close();
				}
			}catch(Exception e1)
			{
				
			}
		}
	}
}
