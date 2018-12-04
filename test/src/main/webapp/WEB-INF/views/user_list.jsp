<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%
	List list = (List)request.getAttribute("list");
	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/resources/lib/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		//alert("ready!");
	});
</script>
</head>
<body>
	member_list<br>
<%
	if (list != null)
	{
		for (Object item : list) { 
			out.println(item + "<br>"); 
		} 
	}
%>
</body>
</html>