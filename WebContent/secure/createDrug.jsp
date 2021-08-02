<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "com.epam.jwd.apotheca.controller.DrugManagerService,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.model.User,com.epam.jwd.apotheca.controller.UserManagerService"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>add drug</title>
</head>
<body>

	<%@ include file = "/mainMenu.jsp" %>
	
	<%
		DrugManagerService service = (DrugManagerService) application.getAttribute("drugService");
		String drugName = request.getParameter("drugName");
		Integer quantity = request.getParameter("quantity") == null ? null : Integer.valueOf(request.getParameter("quantity"));
		Integer price = request.getParameter("price") == null ? null : Integer.valueOf(request.getParameter("price"));
		Double dose =  request.getParameter("dose") == null ? null : Double.valueOf( request.getParameter("dose"));
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
			<div>drug added</div>
		<%
		} else {
		%>
			<div>cannot add drug</div>
	<%
		}
	} else if (request.getParameter("check") != null){
		%>
			<div>fill out all the fields</div>
		<%	
	}
	%>
	
		<form action="/apotheca/secure/createDrug.jsp" method="post">
		<label for = "drugName">name</label> :
		<input type = "text" name = "drugName"/>
		<br/>
		<label for = "quantity">quantity</label> :
		<input type = "text" name = "quantity"/>
		<br/>
		<label for = "price">price</label> :
		<input type = "text" name = "price"/>
		<br/>
		<label for = "dose">dose</label> :
		<input type = "text" name = "dose"/>
		<br/>
		<label for = "prescription">prescripted</label> :
		<input type = "checkbox" name = "prescription" value="on"/>
		<br/>
		<input type = "submit" name = "check" value = "add"/>
		</form>
		<a href = "/apotheca/drugs.jsp">list of drugs</a>
	
</body>
</html>