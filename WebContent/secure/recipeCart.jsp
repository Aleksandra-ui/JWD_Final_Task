<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Locale, com.epam.jwd.apotheca.controller.action.RecipeCartAction, java.util.Map, java.util.HashMap, java.util.ResourceBundle" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%
	Locale locale = null;
	
	Map<String,String[]> langMap = new HashMap<String,String[]>(); 
	langMap.put("zh", new String[]{"zh","CHINESE"});
	langMap.put("en", new String[]{"en","US"});
	
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
	
	session.setAttribute("locale", locale);
	ResourceBundle rb = ResourceBundle.getBundle("properties/Drugs", locale);
%>

<style>
	.error{
		background-color: #FFAAAA;
	}
	
	#popUpGo{
		position: relative;
		left: 20px;
	}
</style>

<body onload="fillDaySelect();">

	<%
			RecipeCartAction bean = (RecipeCartAction)request.getAttribute("action");
		
			int totalCount = bean.getTotalCount();
			int pageSize = bean.getPageSize();
			int currentPage = bean.getCurrentPage();
	%>

	<c:if test="${(not empty action.errors) and (not empty action.errors['access']) }">
		<div class="error">You are not allowed to view this section. Your role is '${action.errors['access'] }', while 'doctor' is required.</div>
	</c:if>

	<c:if test="${(not empty action.cart) and (not empty action.drugs) }">
	
		<div id="popUpGo" style="visibility: hidden; float: right; margin-right: 25px">
			<input type="number" min="1" max="${action.pagesCount }" id="goToCartPage" value="${action.currentPage }" />
			<button id="cartButton" style="display: block; margin-left: 15px; float: right;">go</button>  
		</div>
		
		<div class="container">
			<div style="overflow: hidden" align="center">
				<span style="align-content: center; align-self: center;"><%= rb.getString("drugs.current") %></span>
				<div style="float: left">
					<%=rb.getString("drugs.items1")%>
					<%=currentPage * pageSize - pageSize + 1%>
					<%=rb.getString("drugs.items2")%>
					<%=currentPage * pageSize - pageSize + 1
					+ ((totalCount % pageSize != 0
					&& totalCount / pageSize * pageSize + 1 == currentPage * pageSize - pageSize + 1)
					? totalCount % pageSize : pageSize) - 1%>
					<%=rb.getString("drugs.items3")%>
					${action.totalCount}
				</div>
				<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				<div style="float: right">
					<div style="float: left">
						<%=rb.getString("drugs.items4")%>&nbsp;
						<select id="cartPageSize" name="pageSize" onChange="displayCart( 1, this.options[this.selectedIndex].value );">
							<option ${(empty param.pageSize or param.pageSize == 5) ? "selected='true'" : "" } value="5">5</option>
							<option ${(not empty param.pageSize and param.pageSize  == 10) ? "selected='true'" : "" } value="10">10</option>
							<option ${(not empty param.pageSize and param.pageSize  == 20) ? "selected='true'" : "" } value="20">20</option>
						</select>
					</div>
					<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<div style="float: right">

						<%--Button "previous", shown when current page > 1--%>
						<c:if test="${action.currentPage != 1 }">
							<span>
								<a onclick="displayCart(${action.currentPage - 1}, ${empty action.pageSize ? 5 : action.pageSize})" style="text-decoration: underline;" >&lt;</a>&nbsp;
							</span>
						</c:if>
							
						<%--First page--%>
						<c:choose>
							<c:when test="${empty action.currentPage or action.currentPage eq 1 }">
								1&nbsp;
							</c:when>
							<c:otherwise>
								<a onclick="displayCart(1, ${empty action.pageSize ? 5 : action.pageSize})" style="text-decoration: underline;" >1</a>&nbsp;
							</c:otherwise>
						</c:choose>
							
						<%--spacer after first page --%>
						<c:if test="${not (empty action.currentPage or action.currentPage < 4) }">
							<span>...&nbsp;</span>
						</c:if>
							
						<%--previous page --%>
						<c:if test="${(not empty action.currentPage) and (action.currentPage > 2) and (action.pagesCount > 2) }">
							<a onclick="displayCart(${action.currentPage - 1}, ${empty action.pageSize ? 5 : action.pageSize})" style="text-decoration: underline;" >${action.currentPage - 1}</a> &nbsp;
						</c:if>
							
						<%--current page --%>
						<c:if test="${(not empty action.currentPage) and ( action.currentPage > 1 ) and (action.currentPage < action.pagesCount)}">
							${action.currentPage} &nbsp;
						</c:if>
							
						<%--next page --%>
						<c:if test="${(not empty action.currentPage) and (action.currentPage < action.pagesCount - 1) and (action.pagesCount > 2) }">
							<a onclick="displayCart(${action.currentPage + 1}, ${empty action.pageSize ? 5 : action.pageSize})" style="text-decoration: underline;">${action.currentPage + 1}</a> &nbsp;
						</c:if>
							
						<%--spacer before last page --%>
						<c:if test="${action.currentPage < action.pagesCount - 2 }">
							<span>...&nbsp;</span>
						</c:if>
							
						<%--Button "last" --%>
						<c:if test="${action.pagesCount > 1 }">
							<c:choose>
								<c:when test="${not empty action.currentPage and (action.currentPage eq action.pagesCount) }">
									${ action.pagesCount}&nbsp;
								</c:when>
								<c:otherwise>
									<a onclick="displayCart(${action.pagesCount}, ${empty action.pageSize ? 5 : action.pageSize})" style="text-decoration: underline;" >${ action.pagesCount}</a> &nbsp;
								</c:otherwise>
							</c:choose>
						</c:if>
							
						<%--Button "next", shown when current page < total pages count--%>
						<c:if test="${(empty action.currentPage and action.pagesCount > 1 ) or (action.currentPage < action.pagesCount) }">
							<span>
								<a onclick="displayCart(${action.currentPage + 1}, ${empty action.pageSize ? 5 : action.pageSize})" style="text-decoration: underline;" >&gt;</a> &nbsp;
							</span>
						</c:if>
							
						<c:if test="${action.pagesCount > 3 }">
							<button id="cartPageButton" onclick="showHideInput('popUpGo', 'cartButton', registerCartEvent);">go to page</button>
						</c:if>

					</div>
				</div>
			</div>
		</div>
			
		<table border = "1" style="margin-top: 20px" class="container" align="center">
			<thead align="center">
				<tr>
					<th>#</th>
					<th><%=rb.getString("drugs.name")%></th>
					<th><%=rb.getString("drugs.dose")%></th>
					<th><%=rb.getString("drugs.price")%></th>
					<th><%=rb.getString("drugs.cart")%></th>
				</tr>
			</thead>

			<tbody align="center" <c:if test="${not empty action.errors['user'] }"> bgcolor="LightPink" title="${action.errors['user'] }" </c:if>
								  <c:if test="${not empty action.errors['date'] }"> bgcolor="LightPink" title="${action.errors['date'] }" </c:if>>
				<c:choose>
					<c:when test="${not empty action.cart}">
						<c:forEach items="${action.drugs}" var="d">
							
							<c:set var="present" value="false"/>
							<c:forEach items="${action.invalidDrugs}" var="i">
								<c:if test="${i eq d }"><c:set var="present" value="true"/></c:if>
							</c:forEach>
							
							<tr
								<c:if test="${present }">
									bgcolor="LightPink" title="this drug now does not require prescription"
								</c:if>>
								<td><c:out value="${d.id}" /></td>
								<td><c:out value="${d.name}" /></td>
								<td><c:out value="${d.dose }" /></td>
								<td><c:out value="${d.price }" /></td>
								<td><a onclick="updateRecipeCart(${d.id}, false);" style="text-decoration: underline;">remove</a></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="6"><%=rb.getString("drugs.absence")%></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		
		<div id = "errorStatus" class="container" align="center"></div>
		
		<form action="createRecipe.run" method="POST" class="container" align="center" style="margin-top: 20px">
			<div id = "div" <c:if test="${empty action.cart.drugs}">style="display:none"</c:if>>
				<input class="btn btn-primary" type="submit" <c:if test="${action.cart.invalid }">disabled</c:if> value="<%=rb.getString("drugs.recipe")%>"/>
				
				<select id="clientId" name="clientId" onchange="updateUser(this.value);">
					<c:if test="${empty action.cart.userId }"><option/></c:if>
					<c:forEach items="${action.clients}" var="client">
						<option value="${client.id }"
							<c:if test="${action.cart.userId eq client.id }">selected</c:if>>
							${client.name }
						</option>
					</c:forEach>
				</select>
					
				<select id="Year" name="year" onchange="setExpieryDate()">
					<c:forEach begin="2022" end="2024" var="a">
						<option id="${a }" value="${a }" <c:if test="${action.cart.year eq a }">selected</c:if>>${a }</option>
					</c:forEach>
				</select> 
				
				<select id="Month" name="month" onchange="setExpieryDate()" >
					<c:forEach var="m" items="${action.monthNames }">
						<option id="month${m.key }" value="${m.key}"
						<c:if test="${action.cart.month eq m.key  }">selected</c:if>>
							${m.value }
						</option>	
					</c:forEach>
				</select> 
				
				<select id="Day" name="day" onchange="setExpieryDate();">
					<c:forEach var="d" begin="1" end="28">
						<option value="${d }" 
						<c:if test="${action.cart.day eq ( ((d lt 10) ? '0' : '')+d) }">selected</c:if>>
							${(d lt 10) ? '0' : ''}${d}
						</option>  	
					</c:forEach>
					<option id="29" hidden="true" value="29" 
					<c:if test="${action.cart.day eq 29 }">selected</c:if>>
						29
					</option>	
					<option id="30" hidden="true" value="30"
					<c:if test="${action.cart.day eq 30 }">selected</c:if>>
						30
					</option>	
					<option id="31" hidden="true" value="31"
					<c:if test="${action.cart.day eq 31 }">selected</c:if>>
						31
					</option>	
				</select>   
			</div>
		</form>
		
	</c:if>
		
</body>