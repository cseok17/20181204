package com.kcs.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Keyword {
	/*
	@Id 
	@Column 
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private long id; 
	*/
	@Id
	@Column(length=50, nullable=false) 
	//@GeneratedValue(strategy=GenerationType.AUTO)
	private String keyword; 
	
	@Column(length=100, nullable=true) 
	private int count; 
	
	public String getKeyword() { 
		return keyword; 
	} 
	
	public void setKeyword(String keyword) { 
		this.keyword = keyword; 
	} 
	
	public int getCount() { 
		return count; 
	} 
	
	public void setCount(int count) { 
		this.count = count; 
	} 
	/*
	public long getId() { 
		return id; 
	} 
	*/
	public Keyword() { 
		super(); 
	} 
	
	public Keyword(String keyword, int count) { 
		this(); 
		this.keyword = keyword; 
		this.count = count; 
	} 
	/*
	@Override
	public String toString() { 
		return "\"keyword\":\"" + keyword + "\",\"count\":\"" + count + "\""; 
	}
	*/
}
