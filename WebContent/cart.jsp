<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.epam.jwd.apotheca.model.Drug, java.util.ResourceBundle, java.util.List, java.util.Locale,
    com.epam.jwd.apotheca.controller.action.CartAction, java.util.Map, java.util.HashMap"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%-- <%@include file = "/mainMenu.jsp" %> --%>
<%
	Locale locale = null;
	
	Map<String,String[]> langMap = new HashMap<String,String[]>(); 
	langMap.put("zh", new String[]{"zh","CHINESE"});
	langMap.put("en", new String[]{"en","US"});
	
	Cookie cookie = null;
	locale =(Locale) session.getAttribute("locale");
	if (locale==null){
		locale=Locale.getDefault();
	}
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

	session.setAttribute("locale", locale);
	ResourceBundle rb = ResourceBundle.getBundle("Drugs", locale);
	
%>

<style>
	input.error{
		background-color: #FFAAAA;
	}
	
	#popUpGo{
		position: relative;
		left: 20px;
	}
</style>

<script type="text/javascript">
	
	function validateAmount(input) {
		
		result = false;
		console.log(input.value);
		if (input.value){
		if (input.value < 1 || input.value > 100) {
			input.className = "error";
			document.getElementById("errorStatus").innerHTML = "<label style='color:red;'>Value exceeds allowed range</label>";
		} else {
			input.className = "";
			document.getElementById("errorStatus").innerHTML = "";
			result = true;
		}
		}
		console.log("input " + input.id + ", value " + input.value + ", class " + input.className);
		return result;
		
	}
	



</script>

	<%
		CartAction bean = (CartAction)request.getAttribute("action");
	
		Integer totalCount = bean.getTotalCount();
		int pageSize = bean.getPageSize();
		int currentPage = bean.getCurrentPage();
		Integer pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
		request.setAttribute("pagesCount", pagesCount);
	%>

	<c:if test="${(not empty action.cart) and (not empty action.products) }">
	
		<div id="popUpGo" style="visibility: hidden; float: right; margin-right: 25px">
			<input type="number" min="1" max="${pagesCount }" id="goToCartPage" value="${action.currentPage }" />
			<button  id="cartButton" style="display: block; margin-left: 15px; float: right;">go</button>  
		</div>
		<script>
			
		</script>
	
		<div  class="container">
			<div style="overflow: hidden" align="center">
				<div style="float: left">
					<%=rb.getString("drugs.items1")%>
					<%=currentPage * pageSize - pageSize + 1%>
					<%=rb.getString("drugs.items2")%>
					<%=currentPage * pageSize - pageSize + 1
			+ ((totalCount % pageSize != 0
					&& totalCount / pageSize * pageSize + 1 == currentPage * pageSize - pageSize + 1)
							? totalCount % pageSize
							: pageSize)
			- 1%>
					<%=rb.getString("drugs.items3")%>
					${action.totalCount}
				</div>
				<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				<div style="float: right">
					<div style="float: left">
						<%=rb.getString("drugs.items4")%> <select name="pageSize"
						onChange="displayCart( 1, this.options[this.selectedIndex].value );">
							<option
							${(empty param.pageSize or param.pageSize == 5) ? "selected='true'" : "" }
							value="5">5</option>
							<option
							${(not empty param.pageSize and param.pageSize  == 10) ? "selected='true'" : "" }
							value="10">10</option>
							<option
							${(not empty param.pageSize and param.pageSize  == 20) ? "selected='true'" : "" }
							value="20">20</option>
						</select>
					</div>
					<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<div style="float: right">
<%-- 						<c:forEach var="displayPage" begin="1" end="${pagesCount}"> --%>
<%-- 							<c:choose> --%>
<%-- 								<c:when test="${displayPage == (empty param.currentPage ? 1 : param.currentPage)}">${displayPage} &nbsp;</c:when> --%>
<%-- 								<c:otherwise> --%>
<%-- 									<a onclick="displayCart(${displayPage}, ${action.pageSize })" style="text-decoration: underline;">${displayPage}</a>&nbsp; --%>
<%-- 								</c:otherwise> --%>
<%-- 							</c:choose> --%>
<%-- 						</c:forEach> --%>
				<%--Button "previous", shown when current page > 1--%>
				<c:if test="${action.currentPage != 1 }">
					<span>
						<a onclick="displayCart(${action.currentPage - 1}, ${empty action.pageSize ? 5 : action.pageSize})" style="text-decoration: underline;"
						 >&lt;</a>&nbsp;
					</span>
				</c:if>
					
				<%--First page--%>
				<c:choose>
					<c:when test="${empty action.currentPage or action.currentPage eq 1 }">
						1&nbsp;
					</c:when>
					<c:otherwise>
						<a onclick="displayCart(1, ${empty action.pageSize ? 5 : action.pageSize})" style="text-decoration: underline;"
						>1</a>&nbsp;
					</c:otherwise>
				</c:choose>
				
				<%--spacer after first page --%>
				<c:if test="${not (empty action.currentPage or action.currentPage < 4) }">
					<span>...&nbsp;</span>
				</c:if>
				
				<%--previous page --%>
				<c:if test="${(not empty action.currentPage) and (action.currentPage > 2) and (pagesCount > 2) }">
					<a onclick="displayCart(${action.currentPage - 1}, ${empty action.pageSize ? 5 : action.pageSize})" style="text-decoration: underline;"
					>${action.currentPage - 1}</a> &nbsp;
				</c:if>
				
				<%--current page --%>
				<c:if test="${(not empty action.currentPage) and ( action.currentPage > 1 ) and (action.currentPage < pagesCount)}">
					${action.currentPage} &nbsp;
				</c:if>
				
				<%--next page --%>
				<c:if test="${(not empty action.currentPage) and (action.currentPage < pagesCount - 1) and (pagesCount > 2) }">
					<a onclick="displayCart(${action.currentPage + 1}, ${empty action.pageSize ? 5 : action.pageSize})" style="text-decoration: underline;"
					 >${action.currentPage + 1}</a> &nbsp;
				</c:if>
				
				<%--spacer before last page --%>
				<c:if test="${action.currentPage < pagesCount - 2 }">
					<span>...&nbsp;</span>
				</c:if>
				
				<%--Button "last" --%>
				<c:if test="${ pagesCount > 1 }">
					<c:choose>
						<c:when test="${not empty action.currentPage and (action.currentPage eq pagesCount) }">
							${ pagesCount}&nbsp;
						</c:when>
						<c:otherwise>
							<a onclick="displayCart(${pagesCount}, ${empty action.pageSize ? 5 : action.pageSize})" style="text-decoration: underline;"
							>${ pagesCount}</a> &nbsp;
						</c:otherwise>
					</c:choose>
				</c:if>
				
				<%--Button "next", shown when current page < total pages count--%>
				<c:if test="${(empty action.currentPage and pagesCount > 1 ) or (action.currentPage < pagesCount) }">
					<span>
						<a onclick="displayCart(${action.currentPage + 1}, ${empty action.pageSize ? 5 : action.pageSize})" style="text-decoration: underline;"
						>&gt;</a> &nbsp;
					</span>
				</c:if>
				
				<c:if test="${pagesCount > 3 }">
					<button id="cartPageButton" onclick="showHideInput('popUpGo', 'cartButton', registerCartEvent);" >go to page</button>
<%-- 					<input hidden type="number" min="1" max="${pagesCount }" id="goToPage" onkeyup="changeDynamicPage(this);" value="${action.currentPage }" /> --%>
				</c:if>
				
					</div>
				</div>
			</div>
		</div>
			
	<table border = "1" style="margin-top: 20px" class="container" align="center">
	<caption><%= rb.getString("drugs.table") %></caption>
		<thead align="center">
			<tr>
				<th>#</th>
				<th><%=rb.getString("drugs.name")%></th>
				<th><%=rb.getString("drugs.dose")%></th>
				<th><%=rb.getString("drugs.price")%></th>
				<th><%=rb.getString("drugs.amount")%></th>
				<th><%=rb.getString("drugs.cart")%></th>
			</tr>
		</thead>
		<tbody align="center">
			<c:choose>
				<c:when test="${not empty action.cart}">
					<c:set var="totalA" value="0"></c:set>
					<c:forEach items="${action.products}" var="d">
						<tr 
							<c:if test="${not empty action.invalidDrugs[d.key] and 
										((not empty action.invalidDrugs[d.key]['absent']) or 
										 (not empty action.invalidDrugs[d.key]['prescription'])) }">
										bgcolor="LightPink"
							</c:if>
							<c:if test="${not empty action.invalidDrugs[d.key] and (not empty action.invalidDrugs[d.key]['prescription']) }">
										title="${action.invalidDrugs[d.key]['prescription'] }"
							</c:if>
								
						>
							<td>${d.key.id}</td>
							<td>${d.key.name}</td>
							<td>${d.key.dose }</td>
							<td 
								<c:if test="${not empty action.invalidDrugs[d.key] and 
											 (not empty action.invalidDrugs[d.key]['price']) }">
									bgcolor="LightPink" title="${action.invalidDrugs[d.key]['price'] }"
								</c:if>
							>${d.key.price }</td>
							<td>
								<input type="number" min="1" max="100"
									id="cartAmount${d.key.id}"
									<c:if test="${not empty action.invalidDrugs[d.key] and (not empty action.invalidDrugs[d.key]['amount']) }">
										class="error" title="${action.invalidDrugs[d.key]['amount'] }"
									</c:if>
									onchange="onkeyup();"
									onkeyup="if (validateAmount(this)){updateShoppingCart(${d.key.id}, true, 'cartAmount${d.key.id}');}"
								 	value="${d.value }"/>
							</td>
							<td><a onclick="updateShoppingCart(${d.key.id}, false);" style="text-decoration: underline;">remove</a></td>
						</tr>
						<c:set var="totalA" value="${totalA + d.key.price * d.value }"></c:set>
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

	<form action="drugsBill.run" method="POST">
		<div id = "div" <c:if test="${empty action.cart.products}">style="display:none"</c:if> class="container" align="center">
			<div id="totalAmount1"><font color="Blue"><%= rb.getString("drugs.total") %> </font><c:out value="${totalA }"/> <input id="buyButton" type="submit" class="btn btn-primary" <c:if test="${action.cart.invalid }">disabled</c:if> value="<%=rb.getString("drugs.buy")%>"/></div>
		</div>
	</form>

</c:if>