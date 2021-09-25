<%@page import="com.epam.jwd.apotheca.controller.DrugManagerService"%>
<%@page import="com.epam.jwd.apotheca.dao.api.UserDAO"%>
<%@page import="com.epam.jwd.apotheca.controller.UserManagerService,com.epam.jwd.apotheca.model.User,com.epam.jwd.apotheca.controller.RecipeManagerService,
java.util.List, com.epam.jwd.apotheca.model.Recipe, com.epam.jwd.apotheca.model.Drug, java.util.ResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file = "/mainMenu.jsp" %>  
<%
ResourceBundle rb = ResourceBundle.getBundle("Recipes", locale);
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title><%=rb.getString("recipes.list")%></title>
</head>
<body>
		<form method="GET">
		<label for="doctor"><%=rb.getString("recipes.prompt") %></label>
		<input id="doctor" name="doctor"></input>
		</form>
		<%
			DrugManagerService drugService = DrugManagerService.getInstance();
			String doctorName = request.getParameter("doctor");
			if ( doctorName != null ) {
				User doctor = userService.getUser(doctorName);
				if ( doctor == null || doctor.getRole().getId() != UserDAO.ROLE_DOCTOR ) {
		%>
					<p><%=rb.getString("recipes.message")%></p>
		<%			
				} else {
					RecipeManagerService recipeService = (RecipeManagerService) application.getAttribute("recipeService");
					List<Recipe> recipes = recipeService.findByDoctor(doctor);
					request.setAttribute("recipes", recipes);
		%>
				<table border = "1" style="width:50%" >
					<caption><%=rb.getString("recipes.list")%></caption>
					<thead align ="center">
						<tr>
							<th>#</th>
							<th><%=rb.getString("recipes.client")%></th>
							<th><%=rb.getString("recipes.drug")%></th>
							<th><%=rb.getString("recipes.dose")%></th>
							<th><%=rb.getString("recipes.date")%></th>
						</tr>
					</thead>
					<tbody align ="center">
						<c:choose>
							<c:when test="${not empty recipes}">
								<%
								for ( Recipe r : recipes ) {
									for ( Integer drugId : r.getDrugIds() ) {
								%>
									<tr bgcolor="LightGreen">
										<td><%=r.getId() %></td>
										<%
										User client = userService.getUser(r.getUserId());
										Drug drug = drugService.getDrug(drugId);
										%>
										<td><%=client.getName() %></td>
										<td><%=drug.getName() %></td>
										<td><%=drug.getDose() %></td>
										<td><%=r.getExpieryDate() %></td>
									</tr>
								<%
									}
								}
								%>
							</c:when>
							<c:otherwise>
								<tr><td colspan="6">no records found</td></tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
		<%
				}
			} 
		%>
		
</body>
</html>