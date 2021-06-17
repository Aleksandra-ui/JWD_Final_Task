<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import = "com.epam.jwd.apotheca.model.User, java.util.Map, java.util.HashMap, java.util.Map.Entry" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%=application.getAttribute("xxx")%> : <%=application.getAttribute("zzz")%>
	<br/>
	<%
	User user = (User)session.getAttribute("user");
	if (user != null) {
	%>
		<p><font color = "blue">logged user: <%=user.getName()%></font></p>
	<% } else {
	%>
		<p><font color = "red">no users logged</font></p>
	<%
	}
	Map<String, Integer> visits = (Map<String, Integer>)session.getAttribute("userVisits");
	if ( visits == null ){
		visits = new HashMap<String, Integer>();
	}
	if ( user != null ){
		int count = visits.getOrDefault(user.getName(), 0);
		count++;
		visits.put(user.getName(), count);
	}
	session.setAttribute("userVisits", visits);
	%>
	<hr/><br/>
	<ul>
	<%
	for ( Entry e : visits.entrySet() ){
	%>	
	<li><%=e.getKey()%> : <%=e.getValue() %></li>
	<%
	}
	%>
	</ul>
	<br/>
	<a href="/apotheca/index.jsp">home</a>
	<br/>
	<a href="/apotheca/a.jsp">logon</a>
</body>
</html>