<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>add drug</title>
</head>
<body>
	<form action="/apotheca/secure/createDrug.jsp">
		<label for = "drugName">name</label> : <input type = "text" name = "drugName"/><input type = "submit"/>
	</form>
	<a href = "/apotheca/drugs.jsp">list of drugs</a>
</body>
</html>