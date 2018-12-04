package com.kcs.test.controller;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class PswdController {
	
	private static final Logger logger = LoggerFactory.getLogger(PswdController.class);
	
	public String hash(String pswd) {		
		String hash_value = "";
		String hash_key = "kcstest"; 
		String str = pswd + hash_key;
		
		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
		  
			sh.update(str.getBytes()); 
		
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 

			for(int i = 0 ; i < byteData.length ; i++) {
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}

			hash_value = sb.toString();

		} catch(Exception e){
			hash_value = e.toString();
		}

		return hash_value;
	}
	
}
