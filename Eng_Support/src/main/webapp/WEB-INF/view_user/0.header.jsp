<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title> Engineering Support </title>
	<style type="text/css">
		/*헤더*/
		#main_header>div {margin: 0 auto; width: 1500px;}
		#main_header>div>button {width: 100px; font-size: 20px;}
		#main_header>div>h1 {font-size: 50px}
		
		/*메인화면: 회원관리*/
		.member {height: 30px; }
		
		#greet{position: relative; left: 980px;  top: 6px; font-size: 20px;}
		#login {position: relative; left: 1350px; }
		#logout {position: relative; left: 1215px; bottom: 40px;}
		#mypage {position: relative; left: 1250px; bottom: 40px;}
		
		/*메인화면: 메뉴판*/
		
		#main_body>ul {
			overflow: hidden;
			list-style-type: none;
			background-color: #333;
			text-align: center;
			font-size: 25px;
			width: 1500px;
			color: white;
			margin: 30px auto;
			padding-inline-start: 0px;
		}
		
		#main_body>ul>li {float: left; width: 25%;}
		#main_body>ul>li a {display: block; padding: 14px 0px;}
		#main_body>ul>li a:hover {background-color: #111111;}
			
		

	</style>
	<script type="text/javascript">
		function log_in() {
			location.href="login.do";
			
			document.getElementById("login").style.display= "none";
			document.getElementById("logout").style.display= "block";
			document.getElementById("mypage").style.display= "block";
		}
		
		function log_out() {
			location.href="logout_member.do"
			
			document.getElementById("login").style.display= "block";
			document.getElementById("logout").style.display= "none";
			document.getElementById("mypage").style.display= "none";
		}
		
		function MyPage() {
			location.href="mypage.do?page=faq";
		}
		
		function Home_go(){
			location.href="home.do"
		}
		
		function Unit_go(){
			location.href="unit.do"
		}
		
		function Petro_go(){
			location.href="petro.do?unit=Intro"
		}
		
		function Cal_go(){
			location.href="cal.do?cal=Intro"
		}
		

		
	</script>
</head>
<body>
<header>
<div id="main_header" >
		<div>
			<h1>Engineering Support</h1>
		</div>
		<div class="member">
			<c:choose>
				<c:when test="${vo1.m_name ne null}">
					<p id="greet"> ${vo1.m_name}님 환영합니다.</p>
					<button id ="logout" onclick="log_out()">Logout </button> 			
					<button id ="mypage" onclick="MyPage()">MyPage </button> 
				</c:when>
				<c:otherwise>
					<button id ="login" onclick="log_in()">Login </button> 	
				</c:otherwise>
			</c:choose>	
			
		</div>
		
	</div>
</header>
<div id="main_body">
	<ul>
		<li><a onclick="Home_go()">Home</a></li>
		<li><a onclick="Unit_go()">Unit Converter</a></li>
		<li><a onclick="Petro_go()">Petroleum</a></li>
		<li><a onclick="Cal_go()">Calculation</a></li>
	</ul>
</div>
</body>
</html>