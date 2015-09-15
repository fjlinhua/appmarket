<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    Exception e = (Exception) request.getAttribute("ex");
%>
<html>
<head>
<title>出错啦</title>
</head>
<body>
    <H2>异常类型：<%=e.getClass().getSimpleName()%></H2>
    <hr />
    <P>
        <strong>错误描述：</strong><%=e.getMessage()%>
    </P>
    
    <P>
        <strong>详细信息：</strong>
    </P>
    <pre>
    <%
        e.printStackTrace(new java.io.PrintWriter(out));
    %>
    </pre>
</body>
</html>