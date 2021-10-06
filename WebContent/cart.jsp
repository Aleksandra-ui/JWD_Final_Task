<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.epam.jwd.apotheca.model.Drug, java.util.ResourceBundle, java.util.List, java.util.Locale,
    com.epam.jwd.apotheca.controller.action.CartAction"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%
Locale locale = null;
if (locale == null ) {
	locale = new Locale("en", "US");
}
ResourceBundle rb = ResourceBundle.getBundle("Drugs", locale);
%>
<title><%=rb.getString("drugs.list")%></title>
</head>

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

<body>
	<%
		CartAction bean = (CartAction)request.getAttribute("action");
	
		Integer totalCount = bean.getTotalCount();
		int pageSize = bean.getPageSize();
		int currentPage = bean.getCurrentPage();
	%>

	<div>
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
		<table border="1" style="width: 50%">
			<caption><%=rb.getString("drugs.list")%></caption>
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
						<c:forEach items="${action.products}" var="d">

							<tr>
								<td><c:out value="${d.key.id}" /></td>
								<td><c:out value="${d.key.name}" /></td>
								<td><c:out value="${d.key.dose }" /></td>
								<td><c:out value="${d.key.price }" /></td>
								<td><c:out value="${d.value }" /></td>
								<td><a href="removeFromCart.run?drugId=${d.key.id }">remove</a></td>
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
		
		<div id = "errorStatus"></div>

		<form action="drugsBill.run" method="POST">
			<div id = "div" <c:if test="${fn:length(action.cart.products) == 0}">style="display:none"</c:if>>
				<label for="total"><%=rb.getString("drugs.total")%></label>
				<input id="total" readonly></input>
				<input type="submit" value="<%=rb.getString("drugs.buy")%>"/>
			</div>
		</form>
		
		<c:out value="${action.pageSize }"/> 
		<c:out value="${action.totalCount }"></c:out> 
		<c:out value="${fn:length(action.products) }"></c:out> 
		
	</div>
</body>
</html>