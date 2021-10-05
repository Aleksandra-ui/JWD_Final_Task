<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.OrderManagerService,com.epam.jwd.apotheca.model.Order,java.util.List,java.util.Map,java.util.HashMap,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.controller.DrugManagerService"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file = "/mainMenu.jsp" %>
<%
ResourceBundle rb = ResourceBundle.getBundle("Orders", locale);
%>
<title><%=rb.getString("orders.title")%> "${sessionScope.user.name}"</title>
</head>
<body>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<%=rb.getString("orders.page")%> "${sessionScope.user.name}"

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
					<c:forEach items="${action.orders}" var="o">
						<c:forEach items="${o.drugs}" var="d">
							<tr bgcolor="LightGreen">
								<td><c:out value="${o.id}" /></td>
								<td><c:out value="${d.key.name} | ${d.key.dose}" /></td>
								<td><c:out value="${d.value }" /></td>
								<td><c:out value="${o.date}" /></td>
							</tr>
						</c:forEach>
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