<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" 
    import="com.epam.jwd.apotheca.controller.action.UserManagement"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User management</title>
<style>
	.even {
		background-color: LightBlue;
	}
	.odd {
		background-color: LightGreen;
	}
</style>
</head>
<body>

	<%@ include file = "/mainMenu.jsp" %>

	<%
		
			ResourceBundle rb = ResourceBundle.getBundle("LogonPage", locale);
			UserManagement bean = (UserManagement)request.getAttribute("action");
			int totalCount = bean.getTotalCount();
			int pageSize = bean.getPageSize();
			int currentPage = bean.getCurrentPage();

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