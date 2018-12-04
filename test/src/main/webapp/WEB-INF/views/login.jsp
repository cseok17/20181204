<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

<script type="text/javascript"> 
	function btn_join() {
		//var user_id = document.querySelector('#user_id');
		var user_id = $("#user_id").val();
		var user_pswd = $("#user_pswd").val();	

		var data=new Object(); 
		data.id = user_id; 
		data.pswd = user_pswd;
		
		$.ajax({ 
	        type:"post", 
	        contentType : "application/json", 
	        url:"/test/login_join", 
	        async: false, 
	        data : JSON.stringify(data) , 
        	success: function(result){ 
	     		alert("가입이 성공하였습니다.");
	       }, 
	        error: function(xhr, status, error) { 
	        	alert("가입 중 오류가 발생하였습니다.");
	        }   
	    });
	}
	
	function btn_login() {
		var user_id = $("#user_id").val();
		var user_pswd = $("#user_pswd").val();	

		var data=new Object(); 
		data.id = user_id; 
		data.pswd = user_pswd;
		
		$.ajax({ 
	        type:"post", 
	        contentType : "application/json", 
	        url:"/test/login_action", 
	        async: false, 
	        data : JSON.stringify(data) , 
        	success: function(result){ 
	     		if (result.res_code == "00000") {
	     			alert("로그인을 성공하였습니다.\n장소검색 화면으로 이동합니다.");
	     			location.href = "/test/addr";
        		} else {
        			alert("로그인에 실패하였습니다.");
        		}	
	       }, 
	        error: function(xhr, status, error) { 
	        	alert("로그인 중 오류가 발생하였습니다.");
	        }   
	    });
	}
</script>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>test project</title>
	</head>

	<% pageContext.setAttribute("newLine", "\n"); %>

	<body>
		<div style="text-align:center;">
			<h1>Login</h1>
	    	<form method="post">
	    		<table border="0" width="300" style="margin:auto;margin-top: 30px;">
					<tr>
						<td>id</td>
						<td><input type="text" id="user_id" name="user_id"></td>
					</tr>
					<tr>
						<td>password</td>
						<td><input type="password" id="user_pswd" name="user_pswd"></td>
					</tr>
				</table>
	    		<div style="margin-top: 25px;">
	    			<input type="button" style="margin-right:20px" value=" 확인 " onclick="btn_login();">
	    			<input type="button" value=" 가입" onclick="btn_join();">
	    		</div>
			</form>
		</div>
	</body>
</html>
