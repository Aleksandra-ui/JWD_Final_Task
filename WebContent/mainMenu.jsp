<%@page import="com.epam.jwd.apotheca.controller.UserManagerService, com.epam.jwd.apotheca.model.User, java.util.Locale, java.util.ResourceBundle,
java.util.Map,java.util.HashMap"%>
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

	<form method="POST">
		<select name="locale" id="lang">
			<option id="en" value ="en">english</option>
			<option id="zh" value ="zh">chinese</option>
		</select>
		<input type="submit" value="change language" >
	</form>

	<%
	
		UserManagerService userService = (UserManagerService)application.getAttribute("userService");
		request.setAttribute("canPrescribe", userService.canPrescribe((User)session.getAttribute("user")));
		request.setAttribute("canAddDrugs", userService.canAddDrugs((User)session.getAttribute("user")));
	
		Locale locale = null;
		Cookie cookie = null;
		Map<String,String> lang = new HashMap<String,String>();
		lang.put("en", "US");
		lang.put("zh", "CHINESE");
		
		//if user clicked "change language"
		if (lang.containsKey(request.getParameter("locale"))){
			String value = lang.get(request.getParameter("locale"));
			//setting locale
			locale = new Locale(request.getParameter("locale"),value);
			//setting cookie
			for (Cookie c : request.getCookies()){
				if ("lang".equals(c.getName())){
					if (! request.getParameter("locale").equals(c.getValue())) {
						c.setValue(request.getParameter("locale"));
					}
					cookie = c;
				}
			}
			//if cookie is not found
			if (cookie == null){
				cookie = new Cookie("lang",request.getParameter("locale"));
			}
			//if cookie with such name already exists, it will be replaced
			response.addCookie(cookie);
		} else {
		//if user refreshes page without clicking "change language"
			for (Cookie c : request.getCookies()) {
				if ("lang".equals(c.getName())){
					cookie = c;
				}
			}
			//if cookie is found
			if (cookie != null) {
				String value = lang.get(cookie.getValue());
				locale = new Locale(cookie.getValue(), value);
			} else {
			//if cookie is not found
				locale = new Locale("en", "US");
				cookie = new Cookie("lang","en");
				response.addCookie(cookie);
			}
		}
		
	%>

	<ul>
		<li class="menu-item"><a href="/apotheca/drugs.jsp">List of drugs</a></li>
		<c:if test="${ canPrescribe }">
			<li class="menu-item"><a href="/apotheca/secure/recipe.jsp">Prescribe recipe</a></li>
		</c:if> 
		<c:if test="${ canPrescribe }">
			<li class="menu-item"><a href="/apotheca/secure/prescribedRecipes.jsp">Prescribed recipes</a></li>
		</c:if> 
		<c:if test="${not empty sessionScope.user }">
			<li class="menu-item"><a href="/apotheca/secure/orders.jsp">Your orders</a></li>
		</c:if> 
		<c:if test="${canAddDrugs}">
			<li class="menu-item"><a href="/apotheca/secure/createDrug.jsp">Create drug</a></li>
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