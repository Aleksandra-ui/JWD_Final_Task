<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import = "com.epam.jwd.apotheca.controller.UserManagerService,com.epam.jwd.apotheca.model.User,java.util.List,java.util.Calendar,com.epam.jwd.apotheca.model.Role,com.epam.jwd.apotheca.dao.api.UserDAO"%>

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
	ResourceBundle rb = ResourceBundle.getBundle("LogonPage", locale);
%>
<title><%=rb.getString("logon.title")%></title>
<script type="text/javascript">

	function applyAction(userId) {
		
		var select = document.getElementById("userAction" + userId);
		console.log(select.selectedIndex);
		console.log(select.options[select.selectedIndex].value);
		window.location = select.options[select.selectedIndex].value;
		
	}

</script>
</head>
<body>

	<div align="center" class="container" style="width: 40%">
		<c:if test="${empty action.user }">
			<c:if test="${not empty param.name }">
				<p><font color = "red"><%=rb.getString("logon.message1")%> ${param.name } <%=rb.getString("logon.message2")%>
				 <a href="/apotheca/logon.run?register=1&name=${ param.name }&pass=${ param.pass }"><%=rb.getString("logon.message3")%></a>?</font></p>
			</c:if>
			<p align="center"><%=rb.getString("logon.prompt")%></p>
			<form action="/apotheca/logon.run" method="post">
				<label for="name" class="form-label"><%=rb.getString("logon.name")%> </label><input class="form-control" name="name" type="text" id="name" value = "user"/> <br />
				<label for="name" class="form-label"><%=rb.getString("logon.password")%> </label><input class="form-control" name="pass" type="password" id="pass" value = "password"/> <br>
				<input type="submit" class="btn btn-primary" value="<%=rb.getString("logon.login")%>"/>
			</form>
		</c:if>
	</div>

	<br/>

	<div id="messages">
		<c:forEach items="${action.messages }" var="message">
			${message }
			<br/>
		</c:forEach>
	</div>
	
	<c:set var="isAdmin" value="${not empty action.user and action.user.role.name eq 'admin' }"/>
	<c:if test="${isAdmin}">
		<c:if test="${not empty action.users }">
			<ul>
				<c:forEach var="u" items="${action.users }">
					<li>
						<%=rb.getString("logon.name")%>: ${u.name} | <%=rb.getString("logon.role")%>: ${u.role.name }
						<c:if test="${ not (u.role.name eq 'admin') }">
							<select id="userAction${u.id }">
								<option value="deleteUser.run?userId=${u.id }" selected>delete</option>
								<option value="logon.run?logoff=1">log off</option>
							</select>
							<a onclick="applyAction(${u.id });"><u>apply action</u></a>
<!-- 							<form action="deleteUser.run"> -->
<%-- 								<input hidden name="userId" value="${u.id }" /> --%>
<!-- 								<input type="submit" value="delete user"/> -->
<!-- 							</form> -->
						</c:if>
					</li>	
					<br/>			
				</c:forEach>			
			</ul> 
		</c:if>
	</c:if>

</body>
</html>