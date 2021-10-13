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