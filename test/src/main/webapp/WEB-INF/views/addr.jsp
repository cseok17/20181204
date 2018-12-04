<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript"> 
	$(document).ready(function(){
        
		if ($("#data_div").val() == "") {
			$("#search_div").hide();
			$("#map").hide();
		} else {
			$("#search_div").show();
		}	
		
	});
		
	function btn_search(gubun) {
		//var user_id = document.querySelector('#user_id');
		var keyword = $("#keyword").val();
		
		var data = new Object(); 
		data.keyword = keyword; 
		data.gubun = gubun;
		
		if (gubun == "back") {
			data.search_page = parseInt($("#curr_page").val()) - 1;
		} else if (gubun == "next") {
			data.search_page = parseInt($("#curr_page").val()) + 1;
		} else if (gubun == "curr") {
			$("#curr_page").val(1)
			data.search_page = parseInt($("#curr_page").val());
		} 
	
		$.ajax({ 
	        type:"post", 
	        contentType : "application/json", 
	        url:"/test/addr_action", 
	        async: false, 
	        dataType : 'json',
	        data : JSON.stringify(data), 
        	success: function(result){ 
        		var obj = JSON.parse(JSON.stringify(result));
        		
        		search_success(obj);
	       }, 
	        error: function(xhr, status, error) { 
	        	alert("장소검색 중 오류가 발생하였습니다.");
	        }   
	    });
	}
	
	function search_success(search_data) {
		var resdata = document.getElementById('data_div');
		var documents = search_data.documents;
		var meta = search_data.meta;
		var search_page = search_data.search_page;
		var html = "";

		$("#data_div").html("");
		
		/* 검색 결과 출력 */
		html += "<h3>count : "+ meta.total_count +"</h3>";
		html += "<table border=1 style='margin: auto;'>"

		for(var i = 0; i < documents.length; i ++) {
			html += "<tr>";
			html += "<td>" + documents[i].place_name + "</td>";
			html += "<td>" + documents[i].category_group_name + "</td>";
			html += "<td>" + documents[i].address_name + "</td>";
			html += "<td><input type='button' value='지도보기' onclick='search_map("+documents[i].x+","+documents[i].y +")'></td>";
			html += "</tr>";
		}

		html += "</table>";

		resdata.innerHTML = html;
		
		/* paging*/
		var totCount = meta.total_count;
	    var cntPerPage=15;
	    var cntFetch=cntPerPage;
	    var totPage;
	    var currPage=search_page;

	    totPage=Math.floor(totCount/cntPerPage);
        
        if (totCount%cntPerPage!=0) {
            totPage++;
        }
        
        if (totCount<cntPerPage) {
            cntFetch=totCount%cntPerPage;
        } else {
            cntFetch=cntPerPage;
        }
        
        if (totPage > 45) {
        	totPage = 45;
        }
        $("#page").html("Page "+currPage+" of "+totPage);
        $("#curr_page").val(currPage);
		
        $("#search_div").show();
        
        if(currPage==totPage) {
            $("#next").hide();
        } else {
            $("#next").show();
        }
        
        if(currPage==1) {
            $("#back").hide();
        } else {
            $("#back").show();
        }
	}
	
	function search_map(x, y){
		$("#map").show();
		
		var map_focus = $("#map").offset();
        $('html, body').animate({scrollTop : map_focus.top}, 400);
    
		var container = document.getElementById('map'); 
		var options = { 						//지도를 생성할 때 필요한 기본 옵션
			center: new daum.maps.LatLng(y, x), //지도의 중심좌표.
			level: 3 							//지도의 레벨(확대, 축소 정도)
		};

		var map = new daum.maps.Map(container, options); //지도 생성 및 객체 리턴
	}
	
	function btn_best() {
		var data = new Object(); 
		data.keyword_on = $("#keyword_on").val();

		if (data.keyword_on == "N") {
			$.ajax({ 
		        type:"post", 
		        contentType : "application/json", 
		        url:"/test/keyword_action", 
		        async: false, 
		        data : JSON.stringify(data) , 
	        	success: function(result){ 
	        		var obj = JSON.parse(JSON.stringify(result));
	        		keyword_success(obj);
		       }, 
		        error: function(xhr, status, error) { 
		        	alert("인기검색어 검색 중 오류가 발생하였습니다.");
		        }   
		    });
		} else if(data.keyword_on == "Y") {
			$("#keyword_div").html("");
			$("#keyword_div").hide();
		    $("#keyword_on").val("N");
		    $("#btn_keyword_on").val("인기검색어 보기");
		}
	}
	
	function keyword_success(keyword_data) {
		var reskdata = document.getElementById('keyword_div');
		var keyword = keyword_data.keyword;
		var html = "";
		$("#keyword_div").html("");
		
		/* 검색 결과 출력 */
		html += "<h3>인기검색어 Top10</h3>";
		html += "<table border=1 style='margin: auto;'>"

		for(var i = 0; i < keyword.length; i ++) {
			html += "<tr>";
			html += "<td>" + keyword[i].keyword + "</td>";
			html += "<td>" + keyword[i].count + "</td>";
			html += "</tr>";
		}

		html += "</table>";

		reskdata.innerHTML = html;
		
        $("#keyword_div").show();
        $("#keyword_on").val("Y");
        $("#btn_keyword_on").val("인기검색어 닫기");
	}
</script>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=021a09e7a854c22441a418a18e1820b3"></script>
		<title>test project</title>
	</head>

	<% pageContext.setAttribute("newLine", "\n"); %>

	<body>
		<div style="text-align:center;">
			<h1>키워드 주소 검색</h1>
	    	<form method="post">
	    		<div>
		    		<table border="0" width="500" style="margin: auto;margin-top: 30px;">
						<tr>
							<td>키워드</td>
							<td><input type="text" id="keyword" name="keyword"></td>
						</tr>
					</table>
	    		</div>
	    		<div style="margin-top: 25px;">
	    			<input type="button" style="margin-right:20px" value="키워드 검색 " onclick="btn_search('curr');">
	    			<input type="button" id="btn_keyword_on" value=" 인기검색어 보기" onclick="btn_best();">
	    			<input type="hidden" id="keyword_on" value="N">
	    		</div>
	    		<div id="keyword_div"></div>
	    		<div id="search_div">
		    		<div id="data_div"></div>
		    		<br>
		    		<input type="button" id="back" style="display: inline;" onclick="btn_search('back');" value="Back">
			        <p id="page" style="display: inline;"></p>
			        <input type="button" id="next" style="display: inline;" onclick="btn_search('next');" value="Next">
			    </div>    	        
		        <input id="curr_page" type="hidden" value="1">
		        <br><br>
		        <div id="map" style="width:500px;height:400px;margin:auto;"></div>
				<br><br>
				<p>.</p>
			</form>
		</div>
	</body>
</html>
