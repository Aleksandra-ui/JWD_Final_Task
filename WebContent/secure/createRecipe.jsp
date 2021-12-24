<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.action.CreateRecipe" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<%@ include file = "/mainMenu.jsp" %>
		<%
			ResourceBundle rb = ResourceBundle.getBundle("properties/CreateRecipe", locale);
		%> 
		<title><%=rb.getString("create.title")%></title>
	</head>
	<c:set var="baseURL" value="/apotheca/createRecipe.run"/>

	<body>
     
	    <div id = "errorMessages" class="container" align="center">
	   		<c:forEach items="${action.errorMessages}" var="message">
	   			<font color="red"><c:out value="${message}"/></font>
	   			<br/>
	   		</c:forEach>
	    </div>
	
		<%
			CreateRecipe bean = (CreateRecipe)request.getAttribute("action");
			int totalCount = bean.getTotalCount();
			int pageSize = bean.getPageSize();
			int currentPage = bean.getCurrentPage();
		%>
	
		<c:if test="${empty action.errorMessages}">
		
			<div class="container" style="width: 70%;">	
			
				<div class="container" align="center">
					<font color="blue">
						<%=rb.getString("create.message1")%> ${action.client.name} <%=rb.getString("create.message2")%> ${action.user.name } <%=rb.getString("create.message3")%> ${action.expieryDate}
					</font>
				</div>
	
				<%@ include file = "/pagination.jsp" %>
			
				<table border = "1" style="margin-top: 20px" class="container" align="center">
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
							<tr bgcolor="LightBlue">
								<td>${d.id}</td>
								<td>${d.name}</td>
								<td>${d.dose }</td>
								<td>${d.quantity }</td>
								<td>${d.price }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<div  class="container" align="center"><a href="recipe.run"><%=rb.getString("create.link")%></a></div>
				
			</div>
			
		</c:if>
	
	</body>
</html>