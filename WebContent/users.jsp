Session page visited: <%=visitsSession%> times
<%-- Session page visited: <%=request.getParameter("visitsSession")%> times --%>

<ul>
	<%
 	for (User u : service.getUsers()) {
	%>
	<li><%=u.getName()%></li>
	<%
 	}
	%>
</ul> 
