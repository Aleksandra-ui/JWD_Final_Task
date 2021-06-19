<%@page import="com.epam.jwd.apotheca.controller.DrugManagerService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.ResourceBundle, java.util.List, com.epam.jwd.apotheca.model.Drug, com.epam.jwd.apotheca.model.User" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=ResourceBundle.getBundle("Drugs").getString("drugs.list") %></title>
</head>
<body>
	<%=ResourceBundle.getBundle("Drugs").getString("drugs.welcome") %>
	<a href="/apotheca/index.jsp">home</a>
	<table border = "1" >
		<caption>List of drugs</caption>
		<thead align ="center">
			<tr>
				<th>name</th>
				<th>dose</th>
				<th>quantity</th>
				<th>price</th>
				<th>prescription</th>
			</tr>
		</thead>
		
		<tbody align ="center">
		<%
			DrugManagerService service = (DrugManagerService) application.getAttribute("drugService");
			List<Drug> drugs = service.getDrugs();
			if (drugs.size() != 0) {
				for ( Drug d : drugs ){
		%>
			<tr  bgcolor="<%=d.isPrescription() ? "red" : "blue" %>">
				<td><%=d.getName() %></td>
				<td><%=d.getDose() %></td>
				<td><%=d.getQuantity() %></td>
				<td><%=d.getPrice() %></td>
				<td><%=d.isPrescription() ? "yes" : "no" %></td>
			</tr>
			<!-- <tr>
				<td>aspirin</td>
				<td>2</td>
				<td>10</td>
				<td>500</td>
				<td>no</td>
			</tr> -->
			
			
		<%
				}
		} else {
		%>
			<tr><td>no records found</td></tr>
		<%
		}
		%>
		
		
		</tbody>
	</table>
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