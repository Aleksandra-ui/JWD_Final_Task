<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.DrugManagerService,com.epam.jwd.apotheca.model.Drug,java.util.List,java.util.ArrayList,java.util.Map,java.util.HashMap,com.epam.jwd.apotheca.model.Order,java.sql.Date,com.epam.jwd.apotheca.controller.OrderManagerService,
	com.epam.jwd.apotheca.controller.UserManagerService"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%@ include file = "/mainMenu.jsp" %>
	
	 <c:if test="${ empty sessionScope.user }">
    	<c:redirect url="/drugs.jsp"/>
     </c:if>

	<table border="1" style="width: 50%">
		<caption>Bought drugs</caption>
		<thead align="center">
			<tr>
				<th>name</th>
				<th>dose</th>
				<th>amount</th>
				<th>price</th>
			</tr>
		</thead>

		<tbody align="center">
		
			<%
			
				User user = (User) session.getAttribute("user");%>

			<% 
				DrugManagerService drugService = (DrugManagerService)application.getAttribute("drugService");
				OrderManagerService orderService = (OrderManagerService)application.getAttribute("orderService");
				String[] drugIdsStr = request.getParameter("drugIds").split(",");
				List<Drug> drugs = drugService.getDrugs(drugIdsStr); 
				Map<Drug, Integer> amountsById = new HashMap<Drug, Integer>();
	
				request.setAttribute("drugsList", drugs);
				
				String[] amountsStr = request.getParameter("amounts").split(",");
				for ( int i = 0; i < drugIdsStr.length; i ++ ) {
					amountsById.put(drugs.get(i), Integer.valueOf(amountsStr[i]));
				}
				request.setAttribute("amountsById", amountsById);
				
				Integer total = 0;
				for ( Drug d : drugs ) {
					total += d.getPrice() * amountsById.get(d); 
				}
				
				orderService.buy(user.getId(), amountsById);
				
			%>
			
			<c:choose>
				<c:when test="${not empty drugsList}">
					<c:forEach items="${drugsList}" var="d">
						<tr
							bgcolor=<c:out value="${not d.prescription ? 'LightGreen' : 'LightPink'}"/>>
							<td><c:out value="${d.name}" /></td>
							<td><c:out value="${d.dose }" /></td>
							<td><c:out value="${amountsById[d]}" /></td>
							<td><c:out value="${d.price }" /></td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="6">no records found</td>
					</tr>
				</c:otherwise>
			</c:choose>

		</tbody>
	</table>
	
	total: <%= total %>

</body>
</html>