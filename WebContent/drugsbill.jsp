<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.DrugManagerService, com.epam.jwd.apotheca.model.Drug, java.util.List, java.util.ArrayList, java.util.Map, java.util.HashMap"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

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
				DrugManagerService drugService = (DrugManagerService)application.getAttribute("drugService");
				List<Drug> drugs = new ArrayList<Drug>();
				List<String> drugIds = new ArrayList<String>();
				String[] drugIdsStr = request.getParameter("drugIds").split(",");
				Map<Drug, String> amountsById = new HashMap<Drug, String>();
		
				for ( String drugId : drugIdsStr ) {
					drugIds.add(drugId);
				}
				for ( String id : drugIds ) {
					drugs.add(drugService.getDrug(Integer.valueOf(id)));
				}
				request.setAttribute("drugsList", drugs);
				
				List<String> amounts = new ArrayList<String>();
				String[] amountsStr = request.getParameter("amounts").split(",");
				for ( String amount : amountsStr ) {
					amounts.add(amount);
				}
				
				for ( int i = 0; i < drugIds.size(); i ++ ) {
					amountsById.put(drugs.get(i), amounts.get(i));
				}
				request.setAttribute("amountsById", amountsById);
				
			%>
			<%=request.getAttribute("drugsList") %>
			<%= request.getParameter("amounts") %>
			<%= request.getParameter("drugIds") %>
			<c:choose>
				<c:when test="${not empty drugsList}">
					<c:forEach items="${drugsList}" var="d">
						<tr
							bgcolor=<c:out value="${not d.prescription ? 'LightGreen' : 'LightPink'}"/>>
							<td><c:out value="${d.name}" /></td>
							<td><c:out value="${d.dose }" /></td>
							<td>
								<c:set var="idStr">${d.id }</c:set>
								<c:out value="${amountsById[d]}" />
							</td>
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

</body>
</html>