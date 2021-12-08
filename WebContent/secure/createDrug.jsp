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

	<c:if test="${not empty param }">
		<div  class="container" align="center">
			<c:choose>
				<c:when test="${action.success}">
					<font color="blue"><%=rb.getString("create.success")%></font>
				</c:when>
				<c:otherwise>
					<font color="red"><%=rb.getString("create.fail")%></font>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
	
	<div id = "errorMessages"  class="container" align="center">
   		<c:forEach items="${action.errorMessages}" var="message">
   			<font color="red"><c:out value="${ message}"/></font>
   			<br/>
   		</c:forEach>
    </div>
	
	<form action="/apotheca/createDrug.run" method="post"  class="container" align="center" style="width: 40%">
		<label for = "drugName" class="form-label"><%=rb.getString("create.name")%></label> :
		<input type = "text" name = "drugName" class="form-control"/>
		<br/>
		<label for = "quantity" class="form-label"><%=rb.getString("create.quantity")%></label> :
		<input type = "text" name = "quantity" class="form-control"/>
		<br/>
		<label for = "price" class="form-label"><%=rb.getString("create.price")%></label> :
		<input type = "text" name = "price" class="form-control"/>
		<br/>
		<label for = "dose" class="form-label"><%=rb.getString("create.dose")%></label> :
		<input type = "text" name = "dose" class="form-control"/>
		<br/>
		<label for = "prescription"  class="form-check-label"><%=rb.getString("create.prescripted")%></label> :
		<input type = "checkbox" name = "prescription" value="on"  class="form-check-input"/>
		<br/>
		<input class="btn btn-primary" type = "submit" name = "check" value = "<%=rb.getString("create.add")%>"/>
	 </form>
	 <div class="container" align="center"><a href = "/apotheca/drugs.run"><%=rb.getString("create.list")%></a></div>
		
		
	
</body>
</html>