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

<script type="text/javascript">
	
	function saveLang() {
		//works
		select = document.getElementById("lang");
		selectedOpt = select.options[select.selectedIndex];
		localStorage.setItem("lang", selectedOpt.id);
	}
	
	function setLang() {
		
		optId = LocalStorage.getItem("lang");
		document.getElementById(optId).setAttribute("selected","selected");
		alert("successfully changed lang");
	}
	
	function f() {
		document.getElementById("zh").setAttribute("selected","selected");
	}

</script>

	<form method="POST">
		<select name="locale" id="lang" onchange="saveLang()">
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
 		
 		Map<String,String[]> langMap = new HashMap<String,String[]>(); 
 		langMap.put("zh", new String[]{"zh","CHINESE"});
 		langMap.put("en", new String[]{"en","US"});
 		
 		Cookie cookie = null;
 		if (langMap.containsKey(request.getParameter("locale"))) {
 			String[] value = langMap.get(request.getParameter("locale"));
			locale = new Locale(value[0], value[1]);
			if ( request.getCookies()!= null) {
				for (Cookie c : request.getCookies() ) {
					if ("lang".equals(c.getName())) {
						if ( !value[0].equals(c.getValue())) {
							c.setValue(value[0]);
						}
						cookie = c;
					}
				}
			}
			if ( cookie == null ) {
				cookie = new Cookie("lang", value[0]);
			}
			response.addCookie(cookie);
			
 		} 
 		
 		if ( cookie == null ) {
 			for ( Cookie c : request.getCookies() ) {
 	 			if ("lang".equals(c.getName())) {
 	 				cookie = c;
 	 			}
 	 		}
 		}
 		
 		if (cookie != null) {
 			String[] value = langMap.get(cookie.getValue());
			locale = locale ==null ? new Locale(value[0], value[1]) : locale;
 		} else {
 			locale = new Locale("en", "US");
 			response.addCookie(new Cookie("lang", "en"));
 		}
 		
//  		else if ("english".equals(request.getParameter("locale"))) {
//  			locale = new Locale("en", "US");
//  			Cookie cookie = new Cookie("lang", "en");
// 			response.addCookie(cookie);
//  		} else {
//  			if (request.getCookies() != null) {
//  				lang = request.getCookies()[request.getCookies().length - 1].getValue();
//  			} else {
//  				lang = "en";
//  			}
//  			if ("en".equals(lang)) {
//  				locale = new Locale("en", "US");
//  			} else {
//  				locale = new Locale("zh", "CHINESE");
//  			}
//  		}
		
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