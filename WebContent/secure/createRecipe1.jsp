<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.RecipeManagerService,com.epam.jwd.apotheca.controller.UserManagerService,com.epam.jwd.apotheca.controller.DrugManagerService,com.epam.jwd.apotheca.model.Recipe,java.util.Arrays,java.util.List,java.util.stream.Collectors,java.sql.Date,java.util.ArrayList,java.text.SimpleDateFormat,java.text.ParseException,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.model.User,
    com.epam.jwd.apotheca.controller.action.CreateRecipe" %>
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
<script type="text/javascript">

	function changePageSize (select) {
		
		return select.options[select.selectedIndex].value && (window.location = select.options[select.selectedIndex].value); 
	
	}

</script>
<body>
     
    <div id = "errorMessages" class="container" align="center">
   		<c:forEach items="${action.errorMessages}" var="message">
   			<font color="red"><c:out value="${ message}"/></font>
   			<br/>
   		</c:forEach>
    </div>
	
		<%
		CreateRecipe bean = (CreateRecipe)request.getAttribute("action");
	
		Integer totalCount = bean.getTotalCount();
		int pageSize = bean.getPageSize();
		int currentPage = bean.getCurrentPage();
		%>
	
	<c:if test="${empty action.errorMessages}">
		<div class="container" align="center">
			<font color="blue">
				<%=rb.getString("create.message1")%> ${action.client.name} <%=rb.getString("create.message2")%> ${ action.user.name } <%=rb.getString("create.message3")%> ${action.expieryDate}
			</font>
		</div>
	
		<div style="width:50%" class="container">
			<div style="overflow: hidden" class="container" align="center">
				<div style="float: left">
					<%out.print("records from");%>
					${ action.currentPage * action.pageSize - action.pageSize + 1}
					<%out.print("to");%>
					<%=currentPage * pageSize - pageSize + 1
					+ ((totalCount % pageSize != 0
					&& totalCount / pageSize * pageSize + 1 == currentPage * pageSize - pageSize + 1)
							? totalCount % pageSize
							: pageSize)
					- 1%>
					<%out.print("of");%>
					${action.totalCount}
				</div>
				<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				<div style="float: right">
					<div style="float: left">
				 		items per page:&nbsp; <select name="pageSize"
						onChange="changePageSize(this);">
							<option
							${(empty action.pageSize or action.pageSize == 5) ? "selected='true'" : "" }
							value="/apotheca/createRecipe.run?pageSize=5&currentPage=1">5</option>
							<option
							${(not empty action.pageSize and action.pageSize  == 10) ? "selected='true'" : "" }
							value="/apotheca/createRecipe.run?pageSize=10&currentPage=1">10</option>
							<option
							${(not empty action.pageSize and action.pageSize  == 20) ? "selected='true'" : "" }
							value="/apotheca/createRecipe.run?pageSize=20&currentPage=1">20</option>
						</select>
					</div>
					<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<div style="float: right">
						<c:forEach var="displayPage" begin="1" end="${action.pagesCount}">
							<c:choose>
								<c:when test="${displayPage == (empty action.currentPage ? 1 : action.currentPage)}">${displayPage} &nbsp;</c:when>
								<c:otherwise><a href="/apotheca/createRecipe.run?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${displayPage}">${displayPage}</a>&nbsp;</c:otherwise>
							</c:choose>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	
		<table border = "1" style="width:50%; margin-top: 20px" class="container" align="center">
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
	</c:if>
	
</body>
</html>