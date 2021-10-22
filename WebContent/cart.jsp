<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.epam.jwd.apotheca.model.Drug, java.util.ResourceBundle, java.util.List, java.util.Locale,
    com.epam.jwd.apotheca.controller.action.CartAction"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%
Locale locale = null;
if (locale == null ) {
	locale = new Locale("en", "US");
}
ResourceBundle rb = ResourceBundle.getBundle("Drugs", locale);
%>

<style>
	input.error{
		background-color: #FFAAAA;
	}
</style>

<script type="text/javascript">
	
	function validateAmount(input) {
		
		result = false;
		if (input.value < 1 || input.value > 100) {
			input.className = "error";
			document.getElementById("errorStatus").innerHTML = "<label style='color:red;'>Value exceeds allowed range</label>";
		} else {
			input.className = "";
			document.getElementById("errorStatus").innerHTML = "";
			result = true;
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
	%>

	<c:if test="${(not empty action.cart) and (not empty action.products) }">
		<div style="width:50%" class="container">
			<div style="overflow: hidden" class="container" align="center">
				<div style="float: left">
					<%=rb.getString("drugs.records1")%>
					<%=currentPage * pageSize - pageSize + 1%>
					<%=rb.getString("drugs.records2")%>
					<%=currentPage * pageSize - pageSize + 1
			+ ((totalCount % pageSize != 0
					&& totalCount / pageSize * pageSize + 1 == currentPage * pageSize - pageSize + 1)
							? totalCount % pageSize
							: pageSize)
			- 1%>
					<%=rb.getString("drugs.records3")%>
					${action.totalCount}
				</div>
				<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				<div style="float: right">
					<div style="float: left">
						<%=rb.getString("drugs.records4")%>:&nbsp; <select name="pageSize"
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
						<%
							Integer pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
							request.setAttribute("pagesCount", pagesCount);
						%>
						<c:forEach var="displayPage" begin="1" end="${pagesCount}">
							<c:choose>
								<c:when test="${displayPage == (empty param.currentPage ? 1 : param.currentPage)}">${displayPage} &nbsp;</c:when>
								<c:otherwise>
									<a onclick="displayCart(${displayPage}, ${action.pageSize })"><u>${displayPage}</u></a>&nbsp;
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
			
	<table border = "1" style="width:50%; margin-top: 20px" class="container" align="center">
	<caption>Shopping Cart</caption>
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
						${action.invalidDrugs[d.key] }
						<tr 
								<c:if test="${not empty action.invalidDrugs[d.key] and 
											((not empty action.invalidDrugs[d.key]['absent']) or 
											 (not empty action.invalidDrugs[d.key]['prescription'])) }">
									bgcolor="LightPink"
									<c:if test="${not empty action.invalidDrugs[d.key] and (not empty action.invalidDrugs[d.key]['prescription']) }">
										title="${action.invalidDrugs[d.key]['prescription'] }"
									</c:if>
								</c:if>
						>
<%-- 							<td>${d}${action.invalidDrugs[d.key] }</td> --%>
							<td>${d.key.id}</td>
							<td>${d.key.name}</td>
							<td>${d.key.dose }</td>
							<td 
								<c:if test="${not empty action.invalidDrugs[d.key] and 
											 (not empty action.invalidDrugs[d.key]['price']) }">
									bgcolor="LightPink" title="${action.invalidDrugs[d.key]['price'] }"
								</c:if>
							>${d.key.price }</td>
							<td
								<c:if test="${not empty action.invalidDrugs[d.key] and (not empty action.invalidDrugs[d.key]['amount']) }">
									bgcolor="LightPink" title="${action.invalidDrugs[d.key]['amount'] }"
								</c:if>
							>${d.value }</td>
							<td><a onclick="updateShoppingCart(${d.key.id}, false);"><u>remove</u></a></td>
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
			<div id="totalAmount1"><font color="Blue">Total : </font><c:out value="${totalA }"/> <input type="submit" class="btn btn-primary" <c:if test="${action.cart.invalid }">disabled</c:if> value="<%=rb.getString("drugs.buy")%>"/></div>
		</div>
	</form>

</c:if>