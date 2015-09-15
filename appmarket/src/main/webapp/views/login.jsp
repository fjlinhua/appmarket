<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" class="no-js">
<head>
	<meta charset="utf-8">
	<title>安卓应用市场登录</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="author" content="">
	
	<!-- CSS -->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/supersized.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
	
	<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
	<!--[if lt IE 9]>
	    <script src="<%=request.getContextPath()%>/js/html5.js"></script>
	<![endif]-->
</head>
<body>
	<div class="page-container">
	    <h1>安卓应用市场登录</h1>
	    <form action="login.html" method="post">
	        <input type="text" name="userName" class="username" value="${userName}" placeholder="请输入您的用户名！">
	        <input type="password" name="password" class="password" value="${password}" placeholder="请输入您的用户密码！">
	        <button type="submit" class="submit_button">登录</button>
	        <div class="error"><span>+</span></div>
	    </form>
	    <div class="connect">
            <p><font color="#FF0000">${errorMsg}</font></p>
        </div>
	</div>
	
	<!-- Javascript -->
	<script src="<%=request.getContextPath()%>/js/jquery-1.8.2.min.js" ></script>
	<script src="<%=request.getContextPath()%>/js/supersized.3.2.7.min.js" ></script>
	<script src="<%=request.getContextPath()%>/js/supersized-init.js" ></script>
	<script src="<%=request.getContextPath()%>/js/scripts.js" ></script>
</body>
</html>