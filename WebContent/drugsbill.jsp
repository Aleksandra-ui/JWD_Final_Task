<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

			<table border="1" style="width: 50%">
			<caption>Bought drugs</caption>
			<thead align="center">
				<tr>
					<th>name</th>
					<th>dose</th>
					<th>amount</th>
					<th>price</th>
				</tr>
			</thead>

			<tbody align="center">
<!-- 				<c:choose> -->
<%-- 					<c:when test="${not empty param.drugsFromRecipe}"> --%>
<%-- 						<c:forEach items="${param.drugsFromRecipe}" var="d"> --%>

<!-- 							<tr -->
<%-- 								bgcolor=<c:out value="${not d.prescription ? 'LightGreen' : 'LightPink'}"/>> --%>
<%-- 								<td><c:out value="${d.id}" /></td> --%>
<%-- 								<td><c:out value="${d.name}" /></td> --%>
<%-- 												<td><%=d.getName() %></td> --%>
<%-- 								<td><c:out value="${d.dose }" /></td> --%>
<%-- 								<td><c:out value="${d.quantity }" /></td> --%>
<%-- 								<td><c:out value="${d.price }" /></td> --%>
<%-- 								<td><c:out value="${d.prescription ? 'yes' : 'no'}" /></td> --%>
<%-- 												<td><%=d.isPrescription() ? "yes" : "no" %></td> --%>
<!-- 								<td><input type="number" value=0 disabled -->
<%-- 									id="amount${d.id}" /></td> --%>
<%-- 								<td><c:if test="${not empty drugsFromRecipe[d.id] }"> --%>
<%-- 										<c:out value="${drugsFromRecipe[d.id] }" /> --%>
<!-- 									</c:if></td> -->
<!-- 								<td> -->
<!-- 									<c:set var="present" value="false"/> -->
<%-- 									<c:set var="ids" value="${fn:split(param.drugIds,',')}"/> --%>
<%-- 									<c:set var="idStr" >${d.id}</c:set> --%>
<%-- 									<c:forEach var="id" items="${ids}"> --%>
<%-- 										<c:if test="${id == idStr}"> --%>
<!-- 											<c:set var="present" value="true"/> -->
<!-- 										</c:if> -->
<!-- 									</c:forEach> -->
<%-- 									<input type="checkbox" id="drug${d.id}" value="${d.id}" name="drug" --%>
<%-- 										onchange="addRemoveFromCart(this, ${d.id});showId(this);" --%>
<%-- 										<c:out value="${present ? 'checked' : ''}"/> /> --%>
<%-- 									<input type="hidden" id="checkbox${d.id}" value="${d.name}&nbsp;|&nbsp;${d.dose}&nbsp;|&nbsp;${d.quantity}&nbsp;|&nbsp;${d.price}"/> --%>
<!-- 								</td> -->
<!-- 							</tr> -->

<!-- 						</c:forEach> -->
<!-- 					</c:when> -->
<!-- 					<c:otherwise> -->
<!-- 						<tr> -->
<!-- 							<td colspan="6">no records found</td> -->
<!-- 						</tr> -->
<!-- 					</c:otherwise> -->
<!-- 				</c:choose> -->

			</tbody>
		</table>

</body>
</html>