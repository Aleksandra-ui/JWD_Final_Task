<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.OrderManagerService,com.epam.jwd.apotheca.model.Order,java.util.List,java.util.Map,java.util.HashMap,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.controller.DrugManagerService,
	com.epam.jwd.apotheca.controller.action.Orders"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file = "/mainMenu.jsp" %>
<%
ResourceBundle rb = ResourceBundle.getBundle("Orders", locale);
%>
<title><%=rb.getString("orders.title")%> "${action.user.name}"</title>
<style>
	.even {
		background-color: LightBlue;
	}
	.odd {
		background-color: LightGreen;
	}
</style>

<c:set var="baseURL" value="/apotheca/orders.run"/>

</head>
<body>

	<p class="container" align="center"><%=rb.getString("orders.page")%> "${action.user.name}"</p>
	
	<%
		Orders bean = (Orders)request.getAttribute("action");
	
		Integer totalCount = bean.getTotalCount();
		int pageSize = bean.getPageSize();
		int currentPage = bean.getCurrentPage();
	%>
	
<!-- 		<div style="width:50%" class="container"> -->
<!-- 			<div style="overflow: hidden" class="container" align="center"> -->
<!-- 				<div style="float: left"> -->
<!-- 					records from&nbsp; -->
<%-- 					<%=currentPage * pageSize - pageSize + 1%> --%>
<!-- 					to -->
<%-- 					<%=currentPage * pageSize - pageSize + 1 + ((totalCount % pageSize != 0 && totalCount / pageSize * pageSize + 1 == currentPage * pageSize - pageSize + 1) --%>
<%-- 							? totalCount % pageSize : pageSize) - 1%> --%>
<!-- 					of&nbsp; -->
<%-- 					${action.totalCount} --%>
<!-- 				</div> -->
<!-- 				<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> -->
<!-- 				<div style="float: right"> -->
<!-- 					<div style="float: left"> -->
<!-- 						records per page:&nbsp; <select name="pageSize" -->
<!-- 						onChange="changePageSize(this);"> -->
<!-- 							<option -->
<%-- 							${(empty action.pageSize or action.pageSize == 5) ? "selected='true'" : "" } --%>
<%-- 							value="${baseURL }?pageSize=5">5</option> --%>
<!-- 							<option -->
<%-- 							${(not empty action.pageSize and action.pageSize  == 10) ? "selected='true'" : "" } --%>
<%-- 							value="${baseURL }?pageSize=10">10</option> --%>
<!-- 							<option -->
<%-- 							${(not empty action.pageSize and action.pageSize  == 20) ? "selected='true'" : "" } --%>
<%-- 							value="${baseURL }?pageSize=20">20</option> --%>
<!-- 						</select> -->
<!-- 					</div> -->
<!-- 					<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> -->
<!-- 					<div style="float: right"> -->
					
<%-- 					Old pagination --%>
<%-- <%-- 						<c:forEach var="displayPage" begin="1" end="${action.pagesCount}"> --%> 
<%-- <%-- 							<c:choose> --%> 
<%-- <%-- 								<c:when --%> 
<%-- <%-- 								test="${displayPage == (empty action.currentPage ? 1 : action.currentPage)}">${displayPage} &nbsp;</c:when> --%>
<%-- <%-- 								<c:otherwise> --%> 
<%-- <%-- 									<a href="/apotheca/orders.run?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${displayPage}">${displayPage}</a>&nbsp; --%> 
<%-- <%-- 								</c:otherwise> --%> 
<%-- <%-- 							</c:choose> --%> 
<%-- <%-- 						</c:forEach> --%> 

<%-- 						Button "previous", shown when current page > 1 --%>
<%-- 						<c:if test="${action.currentPage != 1 }"> --%>
<!-- 							<span> -->
<%-- 								<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${action.currentPage - 1}">&lt;</a>&nbsp; --%>
<!-- 							</span> -->
<%-- 						</c:if> --%>
							
<%-- 						First page --%>
						
<%-- 						<c:choose> --%>
<%-- 							<c:when test="${empty action.currentPage or action.currentPage eq 1 }"> --%>
<!-- 								1&nbsp; -->
<%-- 							</c:when> --%>
<%-- 							<c:otherwise> --%>
<%-- 								<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=1">1</a>&nbsp; --%>
<%-- 							</c:otherwise> --%>
<%-- 						</c:choose> --%>
						
<%-- 						spacer after first page --%>
<%-- 						<c:if test="${not (empty action.currentPage or action.currentPage < 4) }"> --%>
<!-- 							<span>...&nbsp;</span> -->
<%-- 						</c:if> --%>
						
<%-- 						previous page --%>
<%-- 						<c:if test="${(not empty action.currentPage) and (action.currentPage > 2) and (action.pagesCount > 3) }"> --%>
<%-- 							<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${action.currentPage - 1}">${action.currentPage - 1}</a> &nbsp; --%>
<%-- 						</c:if> --%>
						
<%-- 						current page --%>
<%-- 						<c:if test="${(not empty action.currentPage) and ( action.currentPage > 1 ) and (action.currentPage < action.pagesCount)}"> --%>
<%-- 							${action.currentPage} &nbsp; --%>
<%-- 						</c:if> --%>
						
<%-- 						next page --%>
<%-- 						<c:if test="${(not empty action.currentPage) and (action.currentPage < action.pagesCount - 1) and (action.pagesCount > 3) }"> --%>
<%-- 							<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${action.currentPage + 1}">${action.currentPage + 1}</a> &nbsp; --%>
<%-- 						</c:if> --%>
						
<%-- 						spacer before last page --%>
<%-- 						<c:if test="${action.currentPage < action.pagesCount - 2 }"> --%>
<!-- 							<span>...&nbsp;</span> -->
<%-- 						</c:if> --%>
						
<%-- 						Button "last" --%>
<%-- 						<c:if test="${ action.pagesCount > 1 }"> --%>
<%-- 							<c:choose> --%>
<%-- 								<c:when test="${not empty action.currentPage and (action.currentPage eq action.pagesCount) }"> --%>
<%-- 									${ action.pagesCount}&nbsp; --%>
<%-- 								</c:when> --%>
<%-- 								<c:otherwise> --%>
<%-- 									<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${ action.pagesCount}">${ action.pagesCount}</a> &nbsp; --%>
<%-- 								</c:otherwise> --%>
<%-- 							</c:choose> --%>
<%-- 						</c:if> --%>
						
<%-- 						Button "next", shown when current page < total pages count --%>
<%-- 						<c:if test="${(empty action.currentPage and action.pagesCount > 1 ) or (action.currentPage < action.pagesCount) }"> --%>
<!-- 							<span> -->
<%-- 								<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${action.currentPage + 1}">&gt;</a> &nbsp; --%>
<!-- 							</span> -->
<%-- 						</c:if> --%>
						
<%-- 						<c:if test="${action.pagesCount > 3 }"> --%>
<%-- 							<input type="number" min="1" max="${action.pagesCount }" id="goToPage" onkeyup="changeCurrentPage(this, '${baseURL}');" value="${action.currentPage }" /> --%>
<%-- 						</c:if> --%>
						
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->

		<div class="container" style="width: 70%;">

			<%@ include file = "/pagination.jsp" %>
		
			<table border = "1" style=" margin-top: 20px"  align="center"  class="container">
			<caption><%=rb.getString("orders.list")%></caption>
			<thead align ="center">
				<tr>
					<th>#</th>
					<th><%=rb.getString("orders.drug")%></th>
					<th>dose</th>
					<th><%=rb.getString("orders.amount")%></th>
					<th><%=rb.getString("orders.date")%></th>
				</tr>
			</thead>
			<tbody align ="center">
				<c:choose>
					<c:when test="${not empty action.drugsInfo}">
						<c:set var="classStyle" value="even"/>
						<c:set var="currId" value="0"/>
						<c:forEach items="${action.drugsInfo }" var="d">
							<c:if test="${ not (currId  eq d.id) }">
								<c:set var="currId" value="${d.id }"/>
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
									<td>${d.id}</td>
									<td>${d.name }</td>
									<td>${d.dose }</td>
									<td>${d.amount }</td>
									<td>${d.date }</td>
								</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr><td colspan="6"><%=rb.getString("orders.absence")%></td></tr>
					</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	
		</div>

</body>
</html>