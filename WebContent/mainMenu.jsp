<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
import="com.epam.jwd.apotheca.controller.UserManagerService, com.epam.jwd.apotheca.model.User, java.util.Locale, java.util.ResourceBundle,
java.util.Map,java.util.HashMap" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Bootstrap CSS (jsDelivr CDN) -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-uWxY/CJNBR+1zjPWmfnSnVxwRheevXITnMqoEIeG1LJrdI0GlVs/9cVSyPYXdcSF" crossorigin="anonymous">
<!-- Bootstrap Bundle JS (jsDelivr CDN) -->
<script defer src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-kQtW33rZJAHjgefvhyyzcGF3C5TFyBQBA13V1RKPf4uH+bwyzQxZ6CmMZHmNBEfJ" crossorigin="anonymous"></script>
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
		
	Map<String,String[]> langMap = new HashMap<String,String[]>(); 
	langMap.put("zh", new String[]{"zh","CHINESE"});
	langMap.put("en", new String[]{"en","US"});
	Locale locale = null;
	Cookie cookie = null;
	
	locale = (Locale) session.getAttribute("locale");
	if (locale == null){
		locale = Locale.getDefault();
	}
	
	//if user clicked "change language"
	if (langMap.containsKey(request.getParameter("locale"))) {
		String[] value = langMap.get(request.getParameter("locale"));
		//setting locale
		locale = new Locale(value[0], value[1]);
		if ( request.getCookies() != null ) {
		for ( Cookie c : request.getCookies() ) {
			if ("lang".equals(c.getName())) {
				if ( ! value[0].equals(c.getValue()) ) {
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
	
	ResourceBundle rb0 = ResourceBundle.getBundle("properties/Menu", locale);
	session.setAttribute("locale", locale);
	
%>
		
<form method="POST" class="col-md-2" style="display: flex; margin-top: 20px; margin-left: 32px; margin-bottom: 20px">
	<select class="form-select" name="locale" id="lang" style="display: inline">
		<option id="en" value ="en">english</option>
		<option id="zh" value ="zh">chinese</option>
	</select>
	<input type="submit" style="display: inline" class ="btn btn-primary" value="<%=rb0.getString("menu.locale")%>">
</form>
	
<div class="container" align="center" style="width: 80%">
	<ul class="list-group-horizontal">
	
		<li class="menu-item">
			<c:choose>
				<c:when test="${action.name eq 'Drugs' }"><%=rb0.getString("menu.drugs")%></c:when>
				<c:otherwise><a href="/apotheca/drugs.run"><%=rb0.getString("menu.drugs")%></a></c:otherwise>
			</c:choose>
		</li>
		
		<c:if test="${ canPrescribe }">
			<li class="menu-item">
				<c:choose>
					<c:when test="${action.name eq 'RecipeDrugs' }"><%=rb0.getString("menu.prescribe")%></c:when>
					<c:otherwise><a href="/apotheca/recipe.run"><%=rb0.getString("menu.prescribe")%></a></c:otherwise>
				</c:choose>
			</li>
		</c:if> 
		
		<c:if test="${ canPrescribe }">
			<li class="menu-item">
				<c:choose>
					<c:when test="${action.name eq 'PrescribedRecipes' }"><%=rb0.getString("menu.recipes")%></c:when>
					<c:otherwise><a href="/apotheca/prescribedRecipes.run"><%=rb0.getString("menu.recipes")%></a></c:otherwise>
				</c:choose>
			</li>
		</c:if> 
		
		<c:if test="${not empty sessionScope.user }">
			<li class="menu-item">
				<c:choose>
					<c:when test="${action.name eq 'Orders' }"><%=rb0.getString("menu.orders")%></c:when>
					<c:otherwise><a href="/apotheca/orders.run"><%=rb0.getString("menu.orders")%></a></c:otherwise>
				</c:choose>
			</li>
		</c:if> 
		
		<c:if test="${canAddDrugs}">
			<li class="menu-item">
				<c:choose>
					<c:when test="${action.name eq 'CreateDrug' }"><%=rb0.getString("menu.create")%></c:when>
					<c:otherwise><a href="/apotheca/createDrug.run"><%=rb0.getString("menu.create")%></a></c:otherwise>
				</c:choose>
			</li>
		</c:if> 
		
		<c:set var="isAdmin" value="${not empty action.user and action.user.role.name eq 'admin' }"/>
		<c:if test="${isAdmin}">
			<li class="menu-item">
				<c:choose>
					<c:when test="${action.name eq 'UserManagement' }">Manage users</c:when>
					<c:otherwise><a href="/apotheca/userManagement.run">Manage users</a></c:otherwise>
				</c:choose>
			</li>
		</c:if> 
		
		<li class="menu-item">
			<c:choose>
				<c:when test="${not empty sessionScope.user}"><%=rb0.getString("menu.user")%>: ${sessionScope.user.name}</c:when>
				<c:otherwise><%=rb0.getString("menu.logon1")%> <a href="/apotheca/logon.run"><%=rb0.getString("menu.logon2")%></a></c:otherwise>
			</c:choose>
		</li>
		
		<li class="last-item">
			<c:if test="${not empty sessionScope.user}"><a href="/apotheca/logon.run?logoff=1"><%=rb0.getString("menu.logoff")%></a></c:if>
		</li>
		
	</ul>
</div>

<br/>