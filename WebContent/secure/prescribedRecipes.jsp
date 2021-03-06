<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.action.PrescribedRecipes" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file = "/mainMenu.jsp" %>    
<%
	ResourceBundle rb = ResourceBundle.getBundle("properties/Recipes", locale);
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%=rb.getString("recipes.list")%></title>
		<style>
			.even {
				background-color: LightBlue;
			}
			.odd {
				background-color: LightGreen;
			}
		</style>
	</head>
	<c:set var="baseURL" value="/apotheca/prescribedRecipes.run"/>

	<body>
	
		<%
			PrescribedRecipes bean = (PrescribedRecipes)request.getAttribute("action");
		
			int totalCount = bean.getTotalCount();
			int pageSize = bean.getPageSize();
			int currentPage = bean.getCurrentPage();
		%>

		<div class="container" style="width: 70%;">		
			
			<%@ include file = "/pagination.jsp" %>
			
			<table border = "1" style="margin-top: 20px" class="container" align="center" >
				<caption><%=rb.getString("recipes.list")%></caption>
				<thead align ="center">
					<tr>
						<th>id</th>
						<th><%=rb.getString("recipes.client")%></th>
						<th><%=rb.getString("recipes.drug")%></th>
						<th><%=rb.getString("recipes.dose")%></th>
						<th><%=rb.getString("recipes.date")%></th>
					</tr>
				</thead>
				<tbody align ="center">
					<c:choose>
						<c:when test="${not empty action.recipeInfo}">
							<c:set var="classStyle" value="even"/>
							<c:set var="currId" value="0"/>
							<c:forEach items="${action.recipeInfo }" var="r">
								<c:if test="${ not (currId  eq r.id) }">
									<c:set var="currId" value="${r.id }"/>
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
										<td>${r.id}</td>
										<td>${r.name }</td>
										<td>${r.drug }</td>
										<td>${r.dose }</td>
										<td>${r.date }</td>
									</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr><td colspan="6">no records found</td></tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
			
		</div>
		
	</body>
</html>