<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.DrugManagerService,com.epam.jwd.apotheca.model.Drug,java.util.List,java.util.ArrayList,java.util.Map,java.util.HashMap,com.epam.jwd.apotheca.model.Order,java.sql.Date,com.epam.jwd.apotheca.controller.OrderManagerService,
	com.epam.jwd.apotheca.controller.UserManagerService"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%@ include file = "/mainMenu.jsp" %>
	
	 <c:if test="${ empty sessionScope.user }">
    	<c:redirect url="/drugs.jsp"/>
     </c:if>
		
			<%
					
				User user = (User) session.getAttribute("user");
				DrugManagerService drugService = DrugManagerService.getInstance();
				OrderManagerService orderService = (OrderManagerService)application.getAttribute("orderService");
				String[] drugIdsStr = request.getParameter("drugIds").split(",");
				List<Drug> drugs = drugService.getDrugs(drugIdsStr); 
				Map<Drug, Integer> amountsById = new HashMap<Drug, Integer>();
	
				request.setAttribute("drugsList", drugs);
				
				String[] amountsStr = request.getParameter("amounts").split(",");
				for ( int i = 0; i < drugIdsStr.length; i ++ ) {
					amountsById.put(drugs.get(i), Integer.valueOf(amountsStr[i]));
				}
				request.setAttribute("amountsById", amountsById);
							
				orderService.buy(user.getId(), amountsById);

			%>
			
			 
    		<c:redirect url="/drugsBill.jsp">
    			<c:param name="drugIds">${param.drugIds }</c:param>
    			<c:param name="amounts">${param.amounts }</c:param>
    		</c:redirect>
     		

</body>
</html>