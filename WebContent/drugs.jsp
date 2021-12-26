<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.action.Drugs, java.util.ResourceBundle" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
		<%@ include file = "/mainMenu.jsp" %>
		
		<%
		
			Drugs bean = (Drugs)request.getAttribute("action");
			int totalCount = bean.getTotalCount();
			int pageSize = bean.getPageSize();
			int currentPage = bean.getCurrentPage();
			ResourceBundle rb = ResourceBundle.getBundle("properties/Drugs", locale);
		
		%>
		<title><%=rb.getString("drugs.list")%></title>
	</head>

	<style>
		input.error{
			background-color: #FFAAAA;
		}
	</style>

	<c:set var="baseURL" value="/apotheca/drugs.run"/>

	<script type="text/javascript">
		
		function validateAmount(input) {
			
			control = document.getElementById("buyButton");
			result = false;
			if (input.value < 1 || input.value > 100) {
				if (control) {
					control.setAttribute("disabled", true);
				}
				input.className = "error";
				document.getElementById("errorStatus").innerHTML = "<label style='color:red;'>Value exceeds allowed range</label>";
			} else {
				if (control) {
					control.setAttribute("disabled", false);
				}
				input.className = "";
				document.getElementById("errorStatus").innerHTML = "";
				result = true;
			}
			
			return result;
			
		}
		
		function updateShoppingCart( drugId, add, amountControlId ) {
			
			if ( add ) {
				control = document.getElementById(amountControlId);
				var amount = 1;
				if ( control ) {
					amount = document.getElementById(amountControlId).value;	
				}
			}
			
			var xmlhttp = new XMLHttpRequest();
			//function that is called when the uploading has finished
			xmlhttp.onreadystatechange = function() {
				//"this" points to xmlhttp object
				if (this.readyState == 4 && this.status == 200) {
					var element = document.getElementById("drug" + drugId);
					if ( element ) {
						element.checked = add;  
					}
					displayShoppingCart(this);
			  	}
			};
			if ( add ) {
				xmlhttp.open("GET", "addToCart.run?drugId=" + drugId + "&amount=" + amount, true);	
			} else {
				xmlhttp.open("GET", "removeFromCart.run?drugId=" + drugId, true);
			}
			xmlhttp.send();
				
		}
		
		function displayShoppingCart(xml) {
			
			if ( xml.responseText.indexOf("Please login") != -1 ) {
				window.location = "/apotheca/logon.run";
			} else {
				document.getElementById("shoppingCart").innerHTML = xml.responseText;
			}
			
		}
		
		function displayCart(currentPage, pageSize) {
			
			var xmlhttp = new XMLHttpRequest();
			xmlhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					displayShoppingCart(this);
			  	}
			};
			
			currentPage = currentPage ? currentPage : 1;
			pageSize = pageSize ? pageSize : 5;
			
			xmlhttp.open("GET", "displayCart.run?currentPage=" + currentPage + "&pageSize=" + pageSize, true);	
			xmlhttp.send();
			
		}
		
	</script>

	<body onload="displayCart();">
		
		<div class="container" style="width: 70%;">	
			<%@ include file = "/pagination.jsp" %>
			<table border = "1" style="margin-top: 20px" class="container" align="center">
				<caption><%=rb.getString("drugs.list")%></caption>
				<thead align="center">
					<tr>
						<th><a href="/apotheca/drugs.run?pageSize=${empty action.pageSize ? 5 : action.pageSize}&sortColumn=id" class="text-dark" style="text-decoration: none">#&nbsp;<img alt="" src="asc.png" width="10px"/></a></th>
						<th><a href="/apotheca/drugs.run?pageSize=${empty action.pageSize ? 5 : action.pageSize}&sortColumn=name" class="text-dark" style="text-decoration: none"><%=rb.getString("drugs.name")%>&nbsp;<img alt="" src="asc.png" width="10px"/></a></th>
						<th><%=rb.getString("drugs.dose")%></th>
						<th><%=rb.getString("drugs.quantity")%></th>
						<th><%=rb.getString("drugs.price")%></th>
						<th><%=rb.getString("drugs.prescription")%></th> 
						<th><%=rb.getString("drugs.date")%></th>
						<th><%=rb.getString("drugs.cart")%></th>
					</tr>
				</thead>
				<tbody align="center">
					<c:choose>
						<c:when test="${not empty action.drugs}">
							<c:forEach items="${action.drugs}" var="d">
								<c:set var="present" value="false"/>
								<c:set var="amount" value="0"/>
								
								<c:forEach var="p" items="${action.cart.products}">
									<c:if test="${p.key.id == d.id}">
										<c:set var="present" value="true"/>
										<c:set var="amount" value="${p.value }"/>
									</c:if>
								</c:forEach>
								
								<tr bgcolor="${not d.prescription ? 'LightGreen' : 'LightBlue'}">
									<td>${d.id}</td>
									<td>${d.name}</td>
									<td>${d.dose }</td>
									<td>${d.quantity }</td>
									<td>${d.price }</td>
									<td>
										<c:if test="${d.prescription}"><%=rb.getString("drugs.yes")%></c:if>
										<c:if test="${not d.prescription}"><%=rb.getString("drugs.no")%></c:if>
									</td>
									<td>
										<c:if test="${not empty action.drugsFromRecipe[d.id] }">
											${action.drugsFromRecipe[d.id] }
										</c:if>
									</td>
									<td>
										<c:choose>
											<c:when test="${(not d.prescription)}">
												<input type="checkbox" id="drug${d.id}" value="${d.id}" name="drug" onchange="updateShoppingCart(${d.id}, this.checked, 'cartAmount${d.id}');"
												<c:if test="${d.quantity eq 0 }">disabled</c:if>
												<c:out value="${present ? 'checked' : ''}"/>/>
											</c:when>
											<c:otherwise><%=rb.getString("drugs.requirement") %></c:otherwise> 
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="6"><%=rb.getString("drugs.absence")%></td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
				
			<div id = "shoppingCart" class="container" align="center"></div>
		</div>
			
	</body>
</html>