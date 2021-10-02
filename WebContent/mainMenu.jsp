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

	<%
	
		UserManagerService userService = (UserManagerService)application.getAttribute("userService");
		request.setAttribute("canPrescribe", userService.canPrescribe((User)session.getAttribute("user")));
		request.setAttribute("canAddDrugs", userService.canAddDrugs((User)session.getAttribute("user")));
	
 		Locale locale = null;
 		
 		Map<String,String[]> langMap = new HashMap<String,String[]>(); 
 		langMap.put("zh", new String[]{"zh","CHINESE"});
 		langMap.put("en", new String[]{"en","US"});
 		
 		Cookie cookie = null;
 		//if user clicked "change language"
 		if (langMap.containsKey(request.getParameter("locale"))) {
 			String[] value = langMap.get(request.getParameter("locale"));
 			//setting locale
			locale = new Locale(value[0], value[1]);
			if ( request.getCookies()!= null ) {
				for (Cookie c : request.getCookies() ) {
					if ("lang".equals(c.getName())) {
						if ( !value[0].equals(c.getValue()) ) {
							c.setValue(value[0]);
						}
						cookie = c;
					}
				}
			}
			if ( cookie == null ) {
				cookie = new Cookie("lang", value[0]);
			}
			//setting certain value into cookie
			response.addCookie(cookie);
 		} 
 		
 		//if page was uploaded without clicking "change language"
 		Cookie[] cookies = request.getCookies();
 		if ( cookie == null && cookies != null ) {
 			//searching for locale in cookies
 			for ( Cookie c : cookies ) {
 	 			if ("lang".equals(c.getName())) {
 	 				cookie = c;
 	 			}
 	 		}
 		}
 		
 		//if we have already set locale, we do not change it. otherwise, we take the value from cookie
 		if (cookie != null) {
 			String[] value = langMap.get(cookie.getValue());
			locale = locale == null ? new Locale(value[0], value[1]) : locale;
		//if user uploaded the page for the first time
 		} else {
 			locale = new Locale("en", "US");
 			response.addCookie(new Cookie("lang", "en"));
 		}
		
		ResourceBundle rb0 = ResourceBundle.getBundle("Menu", locale);
		
	%>
	
	<form method="POST">
		<select name="locale" id="lang">
			<option id="en" value ="en">english</option>
			<option id="zh" value ="zh">chinese</option>
		</select>
		<input type="submit" value="<%=rb0.getString("menu.locale")%>" >
	</form>

	<ul>
		<li class="menu-item"><a href="/apotheca/drugs.run"><%=rb0.getString("menu.drugs")%></a></li>
		<c:if test="${ canPrescribe }">
			<li class="menu-item"><a href="/apotheca/recipe.run"><%=rb0.getString("menu.prescribe")%></a></li>
		</c:if> 
		<c:if test="${ canPrescribe }">
			<li class="menu-item"><a href="/apotheca/prescribedRecipes.run"><%=rb0.getString("menu.recipes")%></a></li>
		</c:if> 
		<c:if test="${not empty sessionScope.user }">
			<li class="menu-item"><a href="/apotheca/secure/orders.jsp"><%=rb0.getString("menu.orders")%></a></li>
		</c:if> 
		<c:if test="${canAddDrugs}">
			<li class="menu-item"><a href="/apotheca/secure/createDrug.jsp"><%=rb0.getString("menu.create")%></a></li>
		</c:if> 
		<li class="menu-item">
			<c:choose>
				<c:when test="${not empty sessionScope.user}"><font color = "blue"><%=rb0.getString("menu.user")%>: ${sessionScope.user.name}</font></c:when>
				<c:otherwise><%=rb0.getString("menu.logon1")%> <a href="/apotheca/logon.run"><%=rb0.getString("menu.logon2")%></a></c:otherwise>
			</c:choose>
		</li>
		<li class="last-item">
			<c:if test="${not empty sessionScope.user}"><a href="/apotheca/logon.run?logoff=1"><%=rb0.getString("menu.logoff")%></a></c:if>
		</li>
	</ul>
	<br/>