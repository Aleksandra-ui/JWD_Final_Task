<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "com.epam.jwd.apotheca.controller.DrugManagerService,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.model.User,com.epam.jwd.apotheca.controller.UserManagerService"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@ include file = "/mainMenu.jsp" %>
<%
ResourceBundle rb = ResourceBundle.getBundle("CreateDrug", locale);
%>
<title><%=rb.getString("create.title")%></title>
</head>
<body>

	
	<%
		DrugManagerService service = DrugManagerService.getInstance();
		String drugName = request.getParameter("drugName");
		Integer quantity = (request.getParameter("quantity") == null || request.getParameter("quantity").equals("")) ? null : Integer.valueOf(request.getParameter("quantity"));
		Integer price = (request.getParameter("price") == null || request.getParameter("price").equals("")) ? null : Integer.valueOf(request.getParameter("price"));
		Double dose =  (request.getParameter("dose") == null || request.getParameter("dose").equals("")) ? null : Double.valueOf( request.getParameter("dose"));
		Boolean prescription =  request.getParameter("prescription") == null ? Boolean.FALSE : Boolean.TRUE;

		if (drugName != null && quantity != null && price != null && dose != null) {
			Drug drug = new Drug();
			drug.setName(drugName);
			drug.setQuantity(quantity);
			drug.setPrice(price);
			drug.setDose(dose);
			drug.setPrescription(prescription);
			if (service.addDrug(drug)){
	%>
			<div><%=rb.getString("create.success")%></div>
		<%
		} else {
		%>
			<div><%=rb.getString("create.fail")%></div>
	<%
		}
	} else if (request.getParameter("check") != null){
		%>
			<div><%=rb.getString("create.prompt")%></div>
		<%	
	}
	%>
	
		<form action="/apotheca/secure/createDrug.jsp" method="post">
		<label for = "drugName"><%=rb.getString("create.name")%></label> :
		<input type = "text" name = "drugName"/>
		<br/>
		<label for = "quantity"><%=rb.getString("create.quantity")%></label> :
		<input type = "text" name = "quantity"/>
		<br/>
		<label for = "price"><%=rb.getString("create.price")%></label> :
		<input type = "text" name = "price"/>
		<br/>
		<label for = "dose"><%=rb.getString("create.dose")%></label> :
		<input type = "text" name = "dose"/>
		<br/>
		<label for = "prescription"><%=rb.getString("create.prescripted")%></label> :
		<input type = "checkbox" name = "prescription" value="on"/>
		<br/>
		<input type = "submit" name = "check" value = "<%=rb.getString("create.add")%>"/>
		</form>
		<a href = "/apotheca/drugs.jsp"><%=rb.getString("create.list")%></a>
	
</body>
</html>