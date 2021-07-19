<%@page import="com.epam.jwd.apotheca.controller.UserManagerService, com.epam.jwd.apotheca.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<style type="text/css">
	.menu-item {
		float: left;
	}
	
	.last-item {
		float: none;
	}
	
	ul {
		list-style-type: none;
	}
	
</style>


	<%
		UserManagerService userService = (UserManagerService)application.getAttribute("userService");
 		request.setAttribute("canPrescribe", userService.canPrescribe((User)session.getAttribute("user")));
 		request.setAttribute("canAddDrugs", userService.canAddDrugs((User)session.getAttribute("user")));
	%>

	<ul>
		<li class="menu-item">&nbsp;<a href="drugs.jsp">List of drugs</a>&nbsp;</li>
		<c:if test="${not empty sessionScope.user && canPrescribe }">
			<li class="menu-item">&nbsp;<a href="secure/recipe.jsp">Prescribe recipe</a>&nbsp;</li>
		</c:if> 
		<c:if test="${not empty sessionScope.user }">
			<li class="menu-item">&nbsp;<a href="secure/orders.jsp">Your orders</a>&nbsp;</li>
		</c:if> 
		<c:if test="${not empty sessionScope.user && canAddDrugs}">
			<li class="menu-item">&nbsp;<a href="drugs.jsp">Create drug</a>&nbsp;</li>
		</c:if> 
		<li class="menu-item">&nbsp;${not empty sessionScope.user ? sessionScope.user.name  : 'please logon'}&nbsp;</li>
		<li class="last-item">&nbsp;<a href="logonPage.jsp">Logon page</a>&nbsp;</li>
	</ul>