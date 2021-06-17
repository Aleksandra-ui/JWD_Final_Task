<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import = "com.epam.jwd.apotheca.controller.UserManagerService, com.epam.jwd.apotheca.model.User, java.util.List, com.epam.jwd.apotheca.dao.UserDAO,
	java.util.Calendar"%>

<!DOCTYPE html>
<html>
<!-- <head> -->
<!-- <meta charset="ISO-8859-1"> -->
<!-- <title>Apotheca</title> -->
<!-- </head> -->
<%@ include file="head.html" %>
<body>
	
	<%-- String userName = request.getParameter("name");
	String userPass = request.getParameter("pass"); --%>
	<%
	Integer visitsSession = (Integer) session.getAttribute("visits");
	if (visitsSession == null) {
		visitsSession = 0;
	}
	UserDAO dao=null;
	//UserManagerService service = (UserManagerService) session.getAttribute("service");
	UserManagerService service = (UserManagerService) application.getAttribute("service");
/* 	if (service == null) {
		service = new UserManagerService();
		session.setAttribute("service", service);
	} */
	dao = service.getUserDAO();
	visitsSession++;
	session.setAttribute("visits", visitsSession);
	System.out.println(dao);
	List<User> users = dao.getUsers();
	String userName = request.getParameter("name");
	String userPass = request.getParameter("pass");
	String userLogoff = request.getParameter("logoff");
	String register = request.getParameter("register");
	
	%>
	<%=userName%> <%=userPass%> <%=userLogoff%>
	<% 
	User user = (User)session.getAttribute("user");
	
	if ( user == null ) {
		if (  "1".equals(register) ){
			if (dao.hasUser(userName)){
				
			} else {
				user = new User();
				user.setName(userName);
				user.setPassword(userPass);
				user.setRole("client");
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
	<div>
	<%
	if (user != null) {
	%>
		<p><font color = "blue">logged user: <%=user.getName()%></font></p>
	<% 
	} else if ( userName != null){
	%>
		<p><font color = "red">User <%=userName%> is absent. Do you want to <a href="/apotheca/a.jsp?register=1&name=<%= userName %>&pass=<%= userPass%>">register</a>?</font></p>
	<%
	}
	%>
	</div>
	
	<%@ include file="status.html" %>
	
	<%--logged user: <%=userName%>
	<br />
	password: <%=userPass%> --%>
	
	<% if ( user == null ) {%>
	<p>please login</p>
	<form action="/apotheca/a.jsp" method="post">
		name: <input name="name" type="text" id="name" value = "user"/> <br />
		password: <input name="pass" type="password" id="pass" value = "password"/> <br>
		<input type="submit" value = "login"/>
	</form>
	<%} else{%>
	<form action="/apotheca/a.jsp" method="post">
		<input type="hidden" name = "logoff" value="1"/>
		<input type="submit" value = "logoff"/>
	</form>
	<%} %>
	<br />
<%-- 	Session page visited: <%=visitsSession %> times --%>
<!-- 	<ul> -->
<%-- 	<% for (User u : dao.getUsers()) {%> --%>
<%-- 		<li><%=u.getName() %></li> --%>
<%-- 	<%} %> --%>
<!-- 	</ul> -->
<%-- 	<jsp:include page="users.jsp"> --%>
<%-- 		<jsp:param value="<%=visitsSession %>" name="visitsSession"/> --%>
<%-- 	</jsp:include> --%>
<%@ include file = "users.jsp" %>
<br/>
<a href="/apotheca/index.jsp">home</a>
	<br/>
	<%=Calendar.getInstance().getTime()%>
</body>
</html>