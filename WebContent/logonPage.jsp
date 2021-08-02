<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import = "com.epam.jwd.apotheca.controller.UserManagerService,com.epam.jwd.apotheca.model.User,java.util.List,java.util.Calendar,com.epam.jwd.apotheca.model.Role,com.epam.jwd.apotheca.dao.api.UserDAO"%>

<!DOCTYPE html>
<html>
<%@ include file="head.html" %>
<body>

	<%
	Integer visitsSession = (Integer) session.getAttribute("visits");
	if (visitsSession == null) {
		visitsSession = 0;
	}

	UserManagerService service = (UserManagerService) application.getAttribute("userService");

	visitsSession++;
	System.out.println(service.getUserDAO());
	List<User> users = service.getUsers();
	String userName = request.getParameter("name");
	String userPass = request.getParameter("pass");
	String userLogoff = request.getParameter("logoff");
	String register = request.getParameter("register");

	User user = (User)session.getAttribute("user");
	
	if ( user == null ) {
		if (  "1".equals(register) ){
			if (service.hasUser(userName)){
				
			} else {
				user = new User();
				user.setName(userName);
				user.setPassword(userPass);
				Role role = new Role();
				role.setId(UserDAO.ROLE_CLIENT);
				role.setName("client");
				role.setPermission(UserDAO.PERM_CLIENT);
				user.setRole(role);
				if (!service.createUser(user, session)){
					user=null;
				}
			}
		} else {
			boolean found = false;
			for ( User u : users ) {
				if ( u.getName().equalsIgnoreCase(userName) && u.getPassword().equalsIgnoreCase(userPass) ) {
					found = true;
					user = u;
					session.setAttribute("user",u);
					break;
				}
			}
		}
	} if ("1".equals(userLogoff)) {
		session.removeAttribute("user");
		user = null;
	}
		
	%>
	
	<%@ include file = "/mainMenu.jsp" %>
	
	<div>
		<c:if test="${empty sessionScope.user }">
			<c:if test="${not empty param.name }">
				<p><font color = "red">User ${param.name } is absent.
				Do you want to <a href="/apotheca/logonPage.jsp?register=1&name=${ param.name }&pass=${ param.pass }">register</a>?</font></p>
			</c:if>
			<p>please login</p>
			<form action="/apotheca/logonPage.jsp" method="post">
				<label for="name">name: </label><input name="name" type="text" id="name" value = "user"/> <br />
				<label for="name">password: </label><input name="pass" type="password" id="pass" value = "password"/> <br>
				<input type="submit" value = "login"/>
			</form>
		</c:if>
	</div>

	<br />

<%
	request.setAttribute("users", service.getUsers());
%>
<%@ include file = "users.jsp" %>

</body>
</html>