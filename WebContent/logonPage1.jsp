<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import = "com.epam.jwd.apotheca.controller.UserManagerService,com.epam.jwd.apotheca.model.User,java.util.List,java.util.Calendar,com.epam.jwd.apotheca.model.Role,com.epam.jwd.apotheca.dao.api.UserDAO"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	
<%@ include file = "/mainMenu.jsp" %>
<%
	ResourceBundle rb = ResourceBundle.getBundle("LogonPage", locale);
%>
<title><%=rb.getString("logon.title")%></title>
</head>
<body>

	<div>
		<c:if test="${empty action.user }">
			<c:if test="${not empty param.name }">
				<p><font color = "red"><%=rb.getString("logon.message1")%> ${param.name } <%=rb.getString("logon.message2")%>
				 <a href="/apotheca/logon.run?register=1&name=${ param.name }&pass=${ param.pass }"><%=rb.getString("logon.message3")%></a>?</font></p>
			</c:if>
			<p><%=rb.getString("logon.prompt")%></p>
			<form action="/apotheca/logon.run" method="post">
				<label for="name"><%=rb.getString("logon.name")%> </label><input name="name" type="text" id="name" value = "user"/> <br />
				<label for="name"><%=rb.getString("logon.password")%> </label><input name="pass" type="password" id="pass" value = "password"/> <br>
				<input type="submit" value = "<%=rb.getString("logon.login")%>"/>
			</form>
		</c:if>
	</div>

	<br/>

<ul>
	<c:forEach var="u" items="${action.users }">
		<li><%=rb.getString("logon.name")%>: ${u.name} | <%=rb.getString("logon.role")%>: ${u.role.name }</li>				
	</c:forEach>
</ul> 


</body>
</html>