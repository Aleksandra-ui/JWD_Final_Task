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

<script type="text/javascript">

	function changePageSize (select) {
		
		return select.options[select.selectedIndex].value && (window.location = select.options[select.selectedIndex].value); 
	
	}

</script>

</head>
<body>

	<p class="container" align="center"><%=rb.getString("orders.page")%> "${action.user.name}"</p>
	
	<%
		Orders bean = (Orders)request.getAttribute("action");
	
		Integer totalCount = bean.getTotalCount();
		int pageSize = bean.getPageSize();
		int currentPage = bean.getCurrentPage();
	%>
	
	<div style="width:50%" class="container">
		<div style="overflow: hidden" class="container" align="center">
			<div style="float: left">
				records from&nbsp;
				<%=currentPage * pageSize - pageSize + 1%>
				to
				<%=currentPage * pageSize - pageSize + 1 + ((totalCount % pageSize != 0 && totalCount / pageSize * pageSize + 1 == currentPage * pageSize - pageSize + 1)
						? totalCount % pageSize : pageSize) - 1%>
				of&nbsp;
				${action.totalCount}
			</div>
			<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<div style="float: right">
				<div style="float: left">
					records per page:&nbsp; <select name="pageSize"
						onChange="changePageSize(this);">
						<option
							${(empty action.pageSize or action.pageSize == 5) ? "selected='true'" : "" }
							value="/apotheca/orders.run?pageSize=5">5</option>
						<option
							${(not empty action.pageSize and action.pageSize  == 10) ? "selected='true'" : "" }
							value="/apotheca/orders.run?pageSize=10">10</option>
						<option
							${(not empty action.pageSize and action.pageSize  == 20) ? "selected='true'" : "" }
							value="/apotheca/orders.run?pageSize=20">20</option>
					</select>
				</div>
				<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				<div style="float: right">
					<c:forEach var="displayPage" begin="1" end="${action.pagesCount}">
						<c:choose>
							<c:when
								test="${displayPage == (empty action.currentPage ? 1 : action.currentPage)}">${displayPage} &nbsp;</c:when>
							<c:otherwise>
								<a href="/apotheca/orders.run?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${displayPage}">${displayPage}</a>&nbsp;</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
	
	<table border = "1" style="width:50%; margin-top: 20px" class="container" align="center">
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
		<%-- 				<c:when test="${not empty action.orders}"> --%>
<%-- 					<c:set var="classStyle" value="even"/> --%>
<%-- 					<c:forEach items="${action.orders}" var="o"> --%>
<%-- 						<c:forEach items="${o.drugs}" var="d"> --%>
<%-- 							<tr class="${classStyle }"> --%>
<%-- 								<td><c:out value="${o.id}" /></td> --%>
<%-- 								<td><c:out value="${d.key.name} | ${d.key.dose}" /></td> --%>
<%-- 								<td><c:out value="${d.value }" /></td> --%>
<%-- 								<td><c:out value="${o.date}" /></td> --%>
<!-- 							</tr> -->
<%-- 						</c:forEach> --%>
<%-- 						<c:choose> --%>
<%-- 							<c:when test="${classStyle eq 'even' }"> --%>
<%-- 								<c:set var="classStyle" value="odd"/> --%>
<%-- 							</c:when> --%>
<%-- 							<c:otherwise> --%>
<%-- 								<c:set var="classStyle" value="even"/> --%>
<%-- 							</c:otherwise> --%>
<%-- 						</c:choose> --%>
<%-- 					</c:forEach> --%>
<%-- 				</c:when> --%>
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

</body>
</html>