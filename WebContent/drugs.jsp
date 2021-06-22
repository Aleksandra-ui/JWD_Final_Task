<%@page import="com.epam.jwd.apotheca.controller.DrugManagerService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.ResourceBundle, java.util.List, com.epam.jwd.apotheca.model.Drug, com.epam.jwd.apotheca.model.User" %>
<%--     <%@ taglib uri="" prefix="c" %> --%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=ResourceBundle.getBundle("Drugs").getString("drugs.list") %></title>
</head>
<body>
	
	<%=ResourceBundle.getBundle("Drugs").getString("drugs.welcome") %>
	<a href="/apotheca/index.jsp">home</a>
	
	<%
		DrugManagerService service = (DrugManagerService) application.getAttribute("drugService");
		List<Drug> drugs = service.getDrugs();
		int pageSize = request.getParameter("pageSize") == null ? 5 : Integer.valueOf(request.getParameter("pageSize"));
		int currentPage = request.getParameter("currentPage") == null ? 1 : Integer.valueOf(request.getParameter("currentPage"));
	%>
	
	<div>
		<div>records' number: <%= drugs.size()%></div>
		<div><select name="pagesize" onChange = "this.options[this.selectedIndex].value && (window.location = this.options[this.selectedIndex].value);">
			<option <%= pageSize == 5 ? "selected='true'" : ""  %>  value="/apotheca/drugs.jsp?pageSize=5">5</option>
			<option <%= pageSize == 10 ? "selected='true'" : ""  %> value="/apotheca/drugs.jsp?pageSize=10" >10</option>
			<option <%= pageSize == 20 ? "selected='true'" : ""  %> value="/apotheca/drugs.jsp?pageSize=20" >20</option>
		</select></div>
	<div>
	<%
		Integer pagesCount = drugs.size() / pageSize + ((drugs.size() % pageSize) == 0 ? 0 : 1);
		
		for ( int displayPage = 1; displayPage < pagesCount + 1; displayPage ++ ) {
		
			if ( displayPage == currentPage ) {
			%>
				<%= displayPage %>
			<%
			} else {
			
		%>
				<a href = "/apotheca/drugs.jsp?pageSize=<%= pageSize %>&currentPage=<%=displayPage%>"><%=displayPage %></a>&nbsp;
			
		<%
			}
		}
		%>
		</div>
	</div>
	<table border = "1" >
		<caption>List of drugs</caption>
		<thead align ="center">
			<tr>
				<th>#</th>
				<th>name</th>
				<th>dose</th>
				<th>quantity</th>
				<th>price</th>
				<th>prescription</th>
			</tr>
		</thead>
		
		<tbody align ="center">
		<%
			List<Drug> visibleDrugs = service.getDrugs(pageSize * (currentPage - 1) , pageSize );
			request.setAttribute("drugsList", visibleDrugs); //аналог 71 строки
// 				for ( Drug d : visibleDrugs ){
		%>
<%-- 		<c:set scope="request" var="drugsList" value="${visibleDrugs}"/> --%>
			<c:choose>
				<c:when test="${not empty drugsList}">
					<c:forEach items="${drugsList}" var="d">

						<tr bgcolor=<c:out value="${d.prescription ? 'red' : 'blue'}"/>>
							<%-- 			<tr  bgcolor="<%=d.isPrescription() ? "red" : "blue" %>"> --%>
							<td><c:out value="${d.id}" /></td>
							<%-- 				<td><%=d.getId() %></td> --%>
							<td><c:out value="${d.name}" /></td>
							<%-- 				<td><%=d.getName() %></td> --%>
							<td><c:out value="${d.dose }" /></td>
							<%--  				<td><%=d.getDose() %></td> --%>
							<td><c:out value="${d.quantity }" /></td>
							<%-- 				<td><%=d.getQuantity() %></td> --%>
							<td><c:out value="${d.price }" /></td>
							<%-- 				<td><%=d.getPrice() %></td> --%>
							<td><c:out value="${d.prescription ? 'yes' : 'no'}" /></td>
							<%-- 				<td><%=d.isPrescription() ? "yes" : "no" %></td> --%>
						</tr>

					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr><td colspan="6">no records found</td></tr>
				</c:otherwise>
			</c:choose>
		
		</tbody>
	</table>
	
	<%
	List<String> a = new java.util.ArrayList<String>();
	a.add("a");
	a.add("b");
	a.add("c");
	request.setAttribute("a", a);
	%>
	<c:forEach items = "${a}" var="i" begin="0" end="2" > 
		<c:out value="${i} "></c:out>
	</c:forEach>
	
	<c:forEach items="${visibleDrugs}" var="drug" begin="0" end="${visibleDrugs.size}">
		<c:out value="${drug}" default="---" ></c:out>
<%-- 		<c:out value="${drug.name}" default="---" ></c:out> --%>
		<c:out value="<br/>" ></c:out>
	</c:forEach>
	
	
	<%
	User user = (User)session.getAttribute("user");
	if (user != null && "doctor".equalsIgnoreCase(user.getRole())){
	%>
	<a href="/apotheca/secure/createDrug.jsp">create drug</a>
	<%
	}
	%>
</body>
</html>