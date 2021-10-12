<%@page import="com.epam.jwd.apotheca.controller.DrugManagerService"%>
<%@page import="com.epam.jwd.apotheca.dao.api.UserDAO"%>
<%@page import="com.epam.jwd.apotheca.controller.UserManagerService,com.epam.jwd.apotheca.model.User,com.epam.jwd.apotheca.controller.RecipeManagerService,
java.util.List, com.epam.jwd.apotheca.model.Recipe, com.epam.jwd.apotheca.model.Drug, java.util.ResourceBundle, com.epam.jwd.apotheca.controller.action.PrescribedRecipes"%>
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
<style>
	.even {
		background-color: LightBlue;
	}
	.odd {
		background-color: LightGreen;
	}
</style>
</head>
<script type="text/javascript">

	function changePageSize (select) {
		
		return select.options[select.selectedIndex].value && (window.location = select.options[select.selectedIndex].value); 
	
	}

</script>
<body>
	
		<%
		PrescribedRecipes bean = (PrescribedRecipes)request.getAttribute("action");
	
		Integer totalCount = bean.getTotalCount();
		int pageSize = bean.getPageSize();
		int currentPage = bean.getCurrentPage();
		%>

	<div>
		<div style="overflow: hidden">
			<div style="float: left">
				records from&nbsp;
				<%=currentPage * pageSize - pageSize + 1%>
				&nbsp;to&nbsp;
				<%=currentPage * pageSize - pageSize + 1
		+ ((totalCount % pageSize != 0
				&& totalCount / pageSize * pageSize + 1 == currentPage * pageSize - pageSize + 1)
						? totalCount % pageSize
						: pageSize)
		- 1%>
				&nbsp;of&nbsp;
				${action.totalCount}
			</div>
			<span style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<div style="float: left">
				&nbsp;items per page&nbsp;:&nbsp; <select name="pageSize"
					onChange="changePageSize(this);">
					<option
						${(empty action.pageSize or action.pageSize == 5) ? "selected='true'" : "" }
						value="/apotheca/prescribedRecipes.run?pageSize=5">5</option>
					<option
						${(not empty action.pageSize and action.pageSize  == 10) ? "selected='true'" : "" }
						value="/apotheca/prescribedRecipes.run?pageSize=10">10</option>
					<option
						${(not empty action.pageSize and action.pageSize  == 20) ? "selected='true'" : "" }
						value="/apotheca/prescribedRecipes.run?pageSize=20">20</option>
				</select>
			</div>
			<span style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<div style="float: none">
				
				<c:forEach var="displayPage" begin="1" end="${action.pagesCount}">
					<c:choose>
						<c:when
							test="${displayPage == (empty action.currentPage ? 1 : action.currentPage)}">${displayPage} &nbsp;</c:when>
						<c:otherwise>
							<a href="/apotheca/prescribedRecipes.run?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${displayPage}">${displayPage}</a>&nbsp;</c:otherwise>
					</c:choose>
				</c:forEach>

			</div>
		</div>
	
		<table border = "1" style="width:50%" >
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
		
</body>
</html>