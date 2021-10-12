<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.OrderManagerService,com.epam.jwd.apotheca.model.Order,java.util.List,java.util.Map,java.util.HashMap,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.controller.DrugManagerService"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file = "/mainMenu.jsp" %>
<%
ResourceBundle rb = ResourceBundle.getBundle("Orders", locale);
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
</head>
<body>

	<%=rb.getString("orders.page")%> "${action.user.name}"
	
				<c:forEach var="displayPage" begin="1" end="${action.pagesCount}">
					<c:choose>
						<c:when
							test="${displayPage == (empty action.currentPage ? 1 : action.currentPage)}">${displayPage} &nbsp;</c:when>
						<c:otherwise>
							<a href="/apotheca/orders.run?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${displayPage}">${displayPage}</a>&nbsp;</c:otherwise>
					</c:choose>
				</c:forEach>
	
	<table border = "1" style="width:50%" >
		<caption><%=rb.getString("orders.list")%></caption>
		<thead align ="center">
			<tr>
				<th>#</th>
				<th><%=rb.getString("orders.drug")%></th>
				<th><%=rb.getString("orders.amount")%></th>
				<th><%=rb.getString("orders.date")%></th>
			</tr>
		</thead>
		
		<tbody align ="center">
			<c:choose>
				<c:when test="${not empty action.orders}">
					<c:set var="classStyle" value="even"/>
					<c:forEach items="${action.orders}" var="o">
						<c:forEach items="${o.drugs}" var="d">
							<tr class="${classStyle }">
								<td><c:out value="${o.id}" /></td>
								<td><c:out value="${d.key.name} | ${d.key.dose}" /></td>
								<td><c:out value="${d.value }" /></td>
								<td><c:out value="${o.date}" /></td>
							</tr>
						</c:forEach>
						<c:choose>
							<c:when test="${classStyle eq 'even' }">
								<c:set var="classStyle" value="odd"/>
							</c:when>
							<c:otherwise>
								<c:set var="classStyle" value="even"/>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr><td colspan="6"><%=rb.getString("orders.absence")%></td></tr>
				</c:otherwise>
			</c:choose>
		
		</tbody>
	</table>

</body>
</html>