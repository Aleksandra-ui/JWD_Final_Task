<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.RecipeManagerService, com.epam.jwd.apotheca.controller.UserManagerService, com.epam.jwd.apotheca.controller.DrugManagerService, com.epam.jwd.apotheca.model.Recipe, java.util.Arrays, java.util.List,
    java.util.stream.Collectors, java.sql.Date, java.util.ArrayList, java.text.SimpleDateFormat, java.text.ParseException, com.epam.jwd.apotheca.model.Drug, com.epam.jwd.apotheca.model.User" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<c:if test="${ empty param.recipeDrugIds }">
    	<c:redirect url="/secure/recipe.jsp"/>
     </c:if>
	
	the recipe was prescribed to <%=request.getParameter("clientName")%> by ${ sessionScope.doctorName } and is valid through <%=request.getParameter("day") %> <%=request.getParameter("month") %>
	<%=request.getParameter("year") %>
	
	<%
		RecipeManagerService service = new RecipeManagerService();
		UserManagerService uService = new UserManagerService();
		DrugManagerService dService = new DrugManagerService();
		
		Recipe recipe = new Recipe();
		List<Integer> drugIds = new ArrayList<Integer>();
		Integer userId = uService.getUser(request.getParameter("clientName")).getId();
		recipe.setUserId(userId);
 		String[] strings = request.getParameter("recipeDrugIds").split(",");
 		for ( String drug : strings ){
 			if ( ! drug.equals("") ) {
 				drugIds.add(Integer.valueOf(drug));	
 			}
 		}
 		recipe.setDrugIds(drugIds);
		recipe.setDoctorId(Integer.valueOf(request.getParameter("doctorId")));
		String expieryDate = request.getParameter("year") + "/" + request.getParameter("month") + "/" + request.getParameter("day");
		System.out.println(expieryDate);
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	    java.util.Date utilDate = format.parse(expieryDate);
	    Date sqlDate = new Date(utilDate.getTime());
		recipe.setExpieryDate(sqlDate);
		service.addRecipe(recipe);
		
		List<Drug> drugs = new ArrayList<Drug>();
		for ( Integer id : drugIds ) {
			drugs.add(dService.getDrug(id));
		}
		request.setAttribute("drugs", drugs);
		
		User doctor = uService.getUser(Integer.valueOf(request.getParameter("doctorId")));
		String doctorName = doctor.getName();
		session.setAttribute("doctorName", doctorName);
		
	%>

	<table border="1" style="width: 50%">
		<caption>Drugs in recipe</caption>
		<thead align="center">
			<tr>
				<th>#</th>
				<th>name</th>
				<th>dose</th>
				<th>quantity</th>
				<th>price</th>
			</tr>
		</thead>

		<tbody align="center">
					<c:forEach items="${drugs}" var="d">
						<tr bgcolor="LightPink">
							<td><c:out value="${d.id}" /></td>
							<td><c:out value="${d.name}" /></td>
							<td><c:out value="${d.dose }" /></td>
							<td><c:out value="${d.quantity }" /></td>
							<td><c:out value="${d.price }" /></td>
						</tr>
					</c:forEach>
		</tbody>
	</table>
	
	<a href="recipe.jsp">List of recipe</a>

</body>
</html>