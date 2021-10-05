<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "com.epam.jwd.apotheca.controller.DrugManagerService,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.model.User,com.epam.jwd.apotheca.controller.UserManagerService"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@ include file = "/mainMenu.jsp" %>
<%
ResourceBundle rb = ResourceBundle.getBundle("CreateDrug", locale);
%>
<title><%=rb.getString("create.title")%></title>
</head>
<body>

	<c:choose>
		<c:when test="${action.success}">
			<div><%=rb.getString("create.success")%></div>
		</c:when>
		<c:otherWise>
			<div><%=rb.getString("create.fail")%></div>
		</c:otherWise>
	</c:choose>
	
		<form action="/apotheca/createDrug.run" method="post">
		<label for = "drugName"><%=rb.getString("create.name")%></label> :
		<input type = "text" name = "drugName"/>
		<br/>
		<label for = "quantity"><%=rb.getString("create.quantity")%></label> :
		<input type = "text" name = "quantity"/>
		<br/>
		<label for = "price"><%=rb.getString("create.price")%></label> :
		<input type = "text" name = "price"/>
		<br/>
		<label for = "dose"><%=rb.getString("create.dose")%></label> :
		<input type = "text" name = "dose"/>
		<br/>
		<label for = "prescription"><%=rb.getString("create.prescripted")%></label> :
		<input type = "checkbox" name = "prescription" value="on"/>
		<br/>
		<input type = "submit" name = "check" value = "<%=rb.getString("create.add")%>"/>
		</form>
		<a href = "/apotheca/drugs.run"><%=rb.getString("create.list")%></a>
	
</body>
</html>