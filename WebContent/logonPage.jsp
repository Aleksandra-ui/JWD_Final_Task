<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<!-- Bootstrap CSS (jsDelivr CDN) -->
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-uWxY/CJNBR+1zjPWmfnSnVxwRheevXITnMqoEIeG1LJrdI0GlVs/9cVSyPYXdcSF" crossorigin="anonymous">
		<!-- Bootstrap Bundle JS (jsDelivr CDN) -->
		<script defer src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-kQtW33rZJAHjgefvhyyzcGF3C5TFyBQBA13V1RKPf4uH+bwyzQxZ6CmMZHmNBEfJ" crossorigin="anonymous"></script>
		<%@ include file = "/mainMenu.jsp" %>
		<%
			ResourceBundle rb = ResourceBundle.getBundle("properties/LogonPage", locale);
		%>
		<title><%=rb.getString("logon.title")%></title>
	</head>

	<c:set var="baseURL" value="/apotheca/logon.run"/>

	<body>

		<div align="center" class="container" style="width: 40%; <c:if test="${not empty action.user }">display: none</c:if>">
			<c:if test="${empty action.user }">
				<c:if test="${not empty param.name }">
					<p><font color = "red"><%=rb.getString("logon.message1")%> ${param.name } <%=rb.getString("logon.message2")%>
					 <a href="/apotheca/logon.run?register=1&name=${ param.name }&pass=${ param.pass }"><%=rb.getString("logon.message3")%></a>?</font></p>
				</c:if>
				<p align="center"><%=rb.getString("logon.prompt")%></p>
				<form action="/apotheca/logon.run" method="post">
					<label for="name" class="form-label"><%=rb.getString("logon.name")%> </label><input class="form-control" name="name" type="text" id="name"/> <br />
					<label for="name" class="form-label"><%=rb.getString("logon.password")%> </label><input class="form-control" name="pass" type="password" id="pass"/> <br>
					<input type="submit" class="btn btn-primary" value="<%=rb.getString("logon.login")%>"/>
				</form>
			</c:if>
		</div>
	
		<br/>
	
		<div id="messages" align="center" class="container">
			<c:forEach items="${action.messages }" var="message">
				<div>${message }</div>
			</c:forEach>
		</div>
		
	</body>
	
</html>