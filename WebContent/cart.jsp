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

	function changePageSize (select) {
	
		return select.options[select.selectedIndex].value && (window.location = select.options[select.selectedIndex].value ); 
	
	}
	
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
		<div style="margin-top: 20px; margin-bottom: 20px">
			<span style="align-content: center; align-self: center;">Shopping Cart</span>
				<div style="overflow: hidden">
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
					<span style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<div style="float: left">
						<%=rb.getString("drugs.records4")%>:&nbsp; <select name="pageSize"
							onChange="changePageSize(this);">
							<option
								${(empty param.pageSize or param.pageSize == 5) ? "selected='true'" : "" }
								value="/apotheca/addToCart.run?pageSize=5">5</option>
							<option
								${(not empty param.pageSize and param.pageSize  == 10) ? "selected='true'" : "" }
								value="/apotheca/addToCart.run?pageSize=10">10</option>
							<option
								${(not empty param.pageSize and param.pageSize  == 20) ? "selected='true'" : "" }
								value="/apotheca/addToCart.run?pageSize=20">20</option>
						</select>
					</div>
					<span style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<div style="float: none">
						<%
						Integer pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
						request.setAttribute("pagesCount", pagesCount);
						%>
		
						<c:forEach var="displayPage" begin="1" end="${pagesCount}">
							<c:choose>
								<c:when
									test="${displayPage == (empty param.currentPage ? 1 : param.currentPage)}">${displayPage} &nbsp;</c:when>
								<c:otherwise>
									<a
										href="/apotheca/addToCart.run?pageSize=${empty param.pageSize ? 5 : param.pageSize}&currentPage=${displayPage}" onclick="changeURL(this)">${displayPage}</a>&nbsp;</c:otherwise>
							</c:choose>
						</c:forEach>
		
					</div>
				</div>
			<table border="1" style="width: 50%; margin-top:20px">
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
	
								<tr>
									<td><c:out value="${d.key.id}" /></td>
									<td><c:out value="${d.key.name}" /></td>
									<td><c:out value="${d.key.dose }" /></td>
									<td><c:out value="${d.key.price }" /></td>
									<td><c:out value="${d.value }" /></td>
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
			
			<div id = "errorStatus"></div>
		</div>
			<form action="drugsBill.run" method="POST">
				<div id = "div" <c:if test="${empty action.cart.products}">style="display:none"</c:if>>
					<div id="totalAmount1"><font color="Blue">Total : </font><c:out value="${totalA }"/> <input type="submit" class="btn btn-primary" value="<%=rb.getString("drugs.buy")%>"/></div>
				</div>
			</form>
		
		</c:if>