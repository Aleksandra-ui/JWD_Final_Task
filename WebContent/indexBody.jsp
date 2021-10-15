<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.text.SimpleDateFormat"%>
<div align="center"><%=rb.getString("index.page")%></div> 
	
<div align="center">	
	<%
		String expieryDate = "2020" + "/" + "2" + "/" + "29";
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		format.setLenient(false);
		java.util.Date date = format.parse(expieryDate);
		out.print(date);
	%>
</div>
<div align="center"><p><%=rb.getString("index.welcome")%></p></div>