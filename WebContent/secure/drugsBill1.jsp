<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.DrugManagerService,com.epam.jwd.apotheca.model.Drug,java.util.List,java.util.ArrayList,java.util.Map,java.util.HashMap,com.epam.jwd.apotheca.model.Order,java.sql.Date,com.epam.jwd.apotheca.controller.OrderManagerService,
	com.epam.jwd.apotheca.controller.action.DrugsBill, com.epam.jwd.apotheca.controller.UserManagerService"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file = "/mainMenu.jsp" %>
<%
ResourceBundle rb = ResourceBundle.getBundle("Drugs", locale);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=rb.getString("drugs.title")%></title>

</head>
<c:set var="baseURL" value="/apotheca/drugsBill.run"/>
<body>

	<%
		DrugsBill bean = (DrugsBill)request.getAttribute("action");
	
		Integer totalCount = bean.getTotalCount();
		int pageSize = bean.getPageSize();
		int currentPage = bean.getCurrentPage();
	%>
	
	<c:choose>

		<c:when test="${not empty action.errorMessages}">
			<div id="errorMessages" class="container" align="center">
				<c:forEach items="${action.errorMessages}" var="message">
		   			<font color="red"><c:out value="${ message}"/></font>
		   			<br/>
		   		</c:forEach>
		   	</div>
		   	<div class="container" align="center">
		   		Please re-check your shopping cart at page <a href="/apotheca/drugs.run">"List of drugs"</a>.
		   	</div>
		</c:when>

		<c:otherwise>
<!-- 			<div class="container" style="width:50%"> -->
<!-- 				<div style="overflow: hidden" class="container" align="center"> -->
<!-- 					<div style="float: left"> -->
<!-- 						records from&nbsp; -->
<%-- 						<%=currentPage * pageSize - pageSize + 1%> --%>
<%-- 						<%=rb.getString("drugs.records2")%> --%>
<%-- 						<%=currentPage * pageSize - pageSize + 1 --%>
<%-- 								: pageSize) - 1%> --%>
<%-- 						<%=rb.getString("drugs.records3")%> --%>
<%-- 						${action.totalCount} --%>
<!-- 					</div> -->
<!-- 					<span style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> -->
<!-- 					<div style="float: right"> -->
<!-- 						<div style="float: left"> -->
<%-- 							<%=rb.getString("drugs.records4")%>:&nbsp; <select name="pageSize" --%>
<!-- 							onChange="changePageSize(this);"> -->
<!-- 								<option -->
<%-- 								${(empty action.pageSize or action.pageSize == 5) ? "selected='true'" : "" } --%>
<!-- 								value="/apotheca/drugsBill.run?pageSize=5">5</option> -->
<!-- 								<option -->
<%-- 								${(not empty action.pageSize and action.pageSize  == 10) ? "selected='true'" : "" } --%>
<!-- 								value="/apotheca/drugsBill.run?pageSize=10">10</option> -->
<!-- 								<option -->
<%-- 								${(not empty action.pageSize and action.pageSize  == 20) ? "selected='true'" : "" } --%>
<!-- 								value="/apotheca/drugsBill.run?pageSize=20">20</option> -->
<!-- 							</select> -->
<!-- 						</div> -->
<!-- 						<span style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> -->
<!-- 						<div style="float: right"> -->
<%-- 							<c:forEach var="displayPage" begin="1" end="${action.pagesCount}"> --%>
<%-- 								<c:choose> --%>
<%-- 									<c:when test="${displayPage == (empty action.currentPage ? 1 : action.currentPage)}">${displayPage} &nbsp;</c:when> --%>
<%-- 									<c:otherwise> --%>
<%-- 										<a href="/apotheca/drugsBill.run?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${displayPage}">${displayPage}</a>&nbsp; --%>
<%-- 									</c:otherwise> --%>
<%-- 								</c:choose> --%>
<%-- 							</c:forEach> --%>
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->

			<%@ include file = "/pagination.jsp" %>
		
			<table border = "1" style="width:50%; margin-top: 20px" class="container" align="center">
				<caption><%=rb.getString("drugs.caption")%></caption>
				<thead align="center">
					<tr>
						<th><%=rb.getString("drugs.name")%></th>
						<th>id</th>
						<th><%=rb.getString("drugs.dose")%></th>
						<th><%=rb.getString("drugs.amount")%></th>
						<th><%=rb.getString("drugs.price")%></th>
					</tr>
				</thead>
		
				<tbody align="center">
				
					<c:choose>
						<c:when test="${not empty action.order}">
							<c:forEach items="${action.drugs}" var="d">
								<tr
									bgcolor=<c:out value="${not d.key.prescription ? 'LightGreen' : 'LightBlue'}"/>>
									
									<td><c:out value="${d.key.name}" /></td>
									<td><c:out value="${d.key.id}" /></td>
									<td><c:out value="${d.key.dose }" /></td>
									<td><c:out value="${d.value}" /></td>
									<td><c:out value="${d.key.price }" /></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="6"><%=rb.getString("drugs.absence")%></td>
							</tr>
						</c:otherwise>
					</c:choose>
		
				</tbody>
			</table>
			
			<p class="container" align="center"><%=rb.getString("drugs.total")%>: ${action.total }</p>
		</c:otherwise>
		
	</c:choose>

</body>
</html>