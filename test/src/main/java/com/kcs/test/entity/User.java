package com.kcs.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	/*
	@Id 
	@Column 
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private long id; 
	*/
	@Id
	@Column(length=50, nullable=false) 
	//@GeneratedValue(strategy=GenerationType.AUTO)
	private String user_id; 
	
	@Column(length=100, nullable=true) 
	private String pswd; 
	
	public String getUser_id() { 
		return user_id; 
	} 
	
	public void setUser_id(String user_id) { 
		this.user_id = user_id; 
	} 
	
	public String getPswd() { 
		return pswd; 
	} 
	
	public void setPswd(String pswd) { 
		this.pswd = pswd; 
	} 
	/*
	public long getId() { 
		return id; 
	} 
	*/
	public User() { 
		super(); 
	} 
	
	public User(String user_id, String pswd) { 
		this(); 
		this.user_id = user_id; 
		this.pswd = pswd; 
	} 
	
	@Override
	public String toString() { 
		return "\"user_id\":\"" + user_id + "\",\"pswd\":\"" + pswd + "\""; 
	}
	
}
