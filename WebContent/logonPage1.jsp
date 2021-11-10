<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import = "com.epam.jwd.apotheca.controller.UserManagerService,com.epam.jwd.apotheca.model.User,java.util.List,java.util.Calendar,com.epam.jwd.apotheca.model.Role,com.epam.jwd.apotheca.dao.api.UserDAO,
	com.epam.jwd.apotheca.controller.action.Logon, com.epam.jwd.apotheca.controller.action.DeleteUser, com.epam.jwd.apotheca.controller.action.ChangeUserRole"%>

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
<style>
	.even {
		background-color: LightBlue;
	}
	.odd {
		background-color: LightGreen;
	}
</style>
<script type="text/javascript">

	function applyAction(userId) {
		
		var select = document.getElementById("userAction" + userId);
		console.log(select.selectedIndex);
		console.log(select.options[select.selectedIndex].value);
		window.location = select.options[select.selectedIndex].value;
		
	}

</script>
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
				<label for="name" class="form-label"><%=rb.getString("logon.name")%> </label><input class="form-control" name="name" type="text" id="name" value = "user"/> <br />
				<label for="name" class="form-label"><%=rb.getString("logon.password")%> </label><input class="form-control" name="pass" type="password" id="pass" value = "password"/> <br>
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
	
	<%
	Integer totalCount;
	int pageSize;
	int currentPage;
	if ( request.getAttribute("action") instanceof Logon ) {
		Logon bean = (Logon)request.getAttribute("action");
		totalCount = bean.getTotalCount();
		pageSize = bean.getPageSize();
		currentPage = bean.getCurrentPage();
	} else if ( request.getAttribute("action") instanceof DeleteUser ) {
		DeleteUser bean = (DeleteUser)request.getAttribute("action");
		totalCount = bean.getTotalCount();
		pageSize = bean.getPageSize();
		currentPage = bean.getCurrentPage();
	} else {
		ChangeUserRole bean = ( ChangeUserRole )request.getAttribute("action");
		totalCount = bean.getTotalCount();
		pageSize = bean.getPageSize();
		currentPage = bean.getCurrentPage();
	}
		
	%>
	
	<c:set var="isAdmin" value="${not empty action.user and action.user.role.name eq 'admin' }"/>
	
	<c:if test="${isAdmin and not empty action.users }">	

		<div class="container" style="width: 70%;">	

			<%@ include file = "/pagination.jsp" %>
		
			<table border = "1" style="margin-top: 20px; margin-bottom: 20px" class="container" align="center">
			<thead align ="center">
				<tr>
					<th><%=rb.getString("logon.name")%></th>
					<th><%=rb.getString("logon.role")%></th>
					<th>action</th>
					<th>apply</th>
				</tr>
			</thead>
			<tbody align ="center">
				<c:set var="classStyle" value="even"/>
				<c:set var="currId" value="0"/>
				<c:forEach var="u" items="${action.users }">
					<c:if test="${ not (currId  eq u.id) }">
						<c:set var="currId" value="${u.id }"/>
						<c:choose>
							<c:when test="${classStyle eq 'even' }">
								<c:set var="classStyle" value="odd"/>
							</c:when>
							<c:otherwise>
								<c:set var="classStyle" value="even"/>
							</c:otherwise>
						</c:choose>
					</c:if>
					<tr class="${classStyle }">
						<td>${u.name}</td>
						<td>${u.role.name}</td>
						<td>
							<c:if test="${ not (u.role.name eq 'admin') }">
								<select id="userAction${u.id }">
									<option value="deleteUser.run?userId=${u.id }" selected>delete</option>
									<option value="changeUserRole.run?userId=${u.id }&role=pharmacist">change role to pharmacist</option>
									<option value="changeUserRole.run?userId=${u.id }&role=doctor">change role to doctor</option>
									<option value="changeUserRole.run?userId=${u.id }&role=client">change role to client</option>
								</select>
							</c:if>
						</td>
						<td>
							<c:if test="${ not (u.role.name eq 'admin') }">
								<a onclick="applyAction(${u.id });"><u>apply action</u></a>
							</c:if>
						</td>
					</tr>			
				</c:forEach>			
			</table>
			
		</div>
		
	</c:if>

</body>
</html>