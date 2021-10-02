<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.text.SimpleDateFormat"%>
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
	
	<%
		String expieryDate = "2020" + "/" + "2" + "/" + "29";
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		format.setLenient(false);
		java.util.Date date = format.parse(expieryDate);
		out.print(date);
	%>
	
</body>
</html>