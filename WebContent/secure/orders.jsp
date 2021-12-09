<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.OrderManagerService,com.epam.jwd.apotheca.model.Order,java.util.List,java.util.Map,java.util.HashMap,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.controller.DrugManagerService,
	com.epam.jwd.apotheca.controller.action.Orders"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file = "/mainMenu.jsp" %>
<%
ResourceBundle rb = ResourceBundle.getBundle("properties/Orders", locale);
%>
<title><%=rb.getString("orders.title")%> "${action.user.name}"</title>
<style>
	.even {
		background-color: LightBlue;
	}
	.odd {
		background-color: LightGreen;
	}
</style>

<c:set var="baseURL" value="/apotheca/orders.run"/>

</head>
<body>

	<p class="container" align="center"><%=rb.getString("orders.page")%> "${action.user.name}"</p>
	
	<%
		Orders bean = (Orders)request.getAttribute("action");
	
		Integer totalCount = bean.getTotalCount();
		int pageSize = bean.getPageSize();
		int currentPage = bean.getCurrentPage();
	%>

		<div class="container" style="width: 70%;">

			<%@ include file = "/pagination.jsp" %>
		
			<table border = "1" style=" margin-top: 20px"  align="center"  class="container">
			<caption><%=rb.getString("orders.list")%></caption>
			<thead align ="center">
				<tr>
					<th>#</th>
					<th><%=rb.getString("orders.drug")%></th>
					<th>dose</th>
					<th><%=rb.getString("orders.amount")%></th>
					<th><%=rb.getString("orders.date")%></th>
				</tr>
			</thead>
			<tbody align ="center">
				<c:choose>
					<c:when test="${not empty action.drugsInfo}">
						<c:set var="classStyle" value="even"/>
						<c:set var="currId" value="0"/>
						<c:forEach items="${action.drugsInfo }" var="d">
							<c:if test="${ not (currId  eq d.id) }">
								<c:set var="currId" value="${d.id }"/>
								<c:choose>
									<c:when test="${classStyle eq 'even' }">
										<c:set var="classStyle" value="odd"/>
									</c:when>
									<c:otherwise>
										<c:set var="classStyle" value="even"/>
									</c:otherwise>
								</c:choose>
							</c:if>
								<tr class="${classStyle }">
									<td>${d.id}</td>
									<td>${d.name }</td>
									<td>${d.dose }</td>
									<td>${d.amount }</td>
									<td>${d.date }</td>
								</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr><td colspan="6"><%=rb.getString("orders.absence")%></td></tr>
					</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	
		</div>

</body>
</html>