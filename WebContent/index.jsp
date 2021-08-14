<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<%@ include file = "/mainMenu.jsp" %>
<%
ResourceBundle rb = ResourceBundle.getBundle("Index", locale);
%>
<title><%=rb.getString("index.title")%></title>
</head>
<body>
	
	<div><%=rb.getString("index.page")%></div> 
	
</body>
</html>