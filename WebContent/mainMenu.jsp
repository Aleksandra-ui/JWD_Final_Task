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
	
	li {
		margin-left: 10px; 
		margin-right: 10px; 
	}
	
</style>


	<%
		UserManagerService userService = (UserManagerService)application.getAttribute("userService");
 		request.setAttribute("canPrescribe", userService.canPrescribe((User)session.getAttribute("user")));
 		request.setAttribute("canAddDrugs", userService.canAddDrugs((User)session.getAttribute("user")));
	%>

	<ul>
		<li class="menu-item"><a href="/apotheca/drugs.jsp">List of drugs</a></li>
		<c:if test="${ canPrescribe }">
			<li class="menu-item"><a href="/apotheca/secure/recipe.jsp">Prescribe recipe</a></li>
		</c:if> 
		<c:if test="${not empty sessionScope.user }">
			<li class="menu-item"><a href="/apotheca/secure/orders.jsp">Your orders</a></li>
		</c:if> 
		<c:if test="${canAddDrugs}">
			<li class="menu-item"><a href="/apotheca/drugs.jsp">Create drug</a></li>
		</c:if> 
		<li class="menu-item">
			<c:choose>
				<c:when test="${not empty sessionScope.user}"><font color = "blue">logged user: ${sessionScope.user.name}</font></c:when>
				<c:otherwise>Please <a href="/apotheca/logonPage.jsp">logon</a></c:otherwise>
			</c:choose>
		</li>
		<li class="last-item">
			<c:if test="${not empty sessionScope.user}"><a href="/apotheca/logonPage.jsp?logoff=1">Log off</a></c:if>
		</li>
	</ul>
	<br/>