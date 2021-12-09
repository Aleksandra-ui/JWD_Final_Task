<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.text.SimpleDateFormat, java.util.GregorianCalendar, java.util.Calendar"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<%@ include file = "/mainMenu.jsp" %>
		<%
			ResourceBundle rb = ResourceBundle.getBundle("properties/Index", locale);
		%>
		<title><%=rb.getString("index.title")%></title>
	</head>
	<body>
		<div style="justify-content: center; width: 80%" class="container">	
			<div align="center"><p><%=rb.getString("index.welcome")%></p></div>
		</div>
		<div align="center">A.Mayarovskaya, 2021</div>
	</body>
</html>