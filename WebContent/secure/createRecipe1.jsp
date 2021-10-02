<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.RecipeManagerService,com.epam.jwd.apotheca.controller.UserManagerService,com.epam.jwd.apotheca.controller.DrugManagerService,com.epam.jwd.apotheca.model.Recipe,java.util.Arrays,java.util.List,java.util.stream.Collectors,java.sql.Date,java.util.ArrayList,java.text.SimpleDateFormat,java.text.ParseException,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.model.User" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file = "/mainMenu.jsp" %>
<%
ResourceBundle rb = ResourceBundle.getBundle("CreateRecipe", locale);
%> 
<title><%=rb.getString("create.title")%></title>
</head>
<body>
	
	 <c:if test="${ empty param.recipeDrugIds }">
    	<c:redirect url="/recipe.run"/>
     </c:if>
     
    <div id = "errorMessages">
   		<c:forEach items="${action.errorMessages}" var="message">
   			<font color="red"><c:out value="${ message}"/></font>
   			<br/>
   		</c:forEach>
    </div>
	
	<c:if test="${empty action.errorMessages}">
		<font color="blue">
			<%=rb.getString("create.message1")%> ${param.clientName} <%=rb.getString("create.message2")%> ${ action.doctorName } <%=rb.getString("create.message3")%> ${param.day} ${param.month} ${param.year}
		</font>
	
		<table border="1" style="width: 50%">
			<caption><%=rb.getString("create.caption")%></caption>
			<thead align="center">
				<tr>
					<th>#</th>
					<th><%=rb.getString("create.name")%></th>
					<th><%=rb.getString("create.dose")%></th>
					<th><%=rb.getString("create.quantity")%></th>
					<th><%=rb.getString("create.price")%></th>
				</tr>
			</thead>
	
			<tbody align="center">
						<c:forEach items="${action.drugs}" var="d">
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
		<a href="recipe.run"><%=rb.getString("create.link")%></a>
	</c:if>
	
</body>
</html>