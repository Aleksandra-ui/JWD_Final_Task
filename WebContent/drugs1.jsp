<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.action.Drugs, com.epam.jwd.apotheca.model.Drug, java.util.ResourceBundle, java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@ include file = "/mainMenu.jsp" %>
<%
ResourceBundle rb = ResourceBundle.getBundle("Drugs", locale);
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
		// console.log("input " + input.id + ", value " + input.value + ", class " + input.className);
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
		//функция которая вызывается когда завершилась загрузка
		xmlhttp.onreadystatechange = function() {
			//this это объект xmlhttp
			if (this.readyState == 4 && this.status == 200) {
				var element = document.getElementById("drug" + drugId);
				if ( element ) {
					element.checked = add;  
				}
				/*code block for enabling/disabling input in drugs table
				element = document.getElementById(amountControlId);
				if ( element ) {
					element.disabled = ! add;  
				}
				*/
				
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
		//функция которая вызывается когда завершилась загрузка
		xmlhttp.onreadystatechange = function() {
			//this это объект xmlhttp
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
	<%
		Drugs bean = (Drugs)request.getAttribute("action");
	
		Integer totalCount = bean.getTotalCount();
		int pageSize = bean.getPageSize();
		int currentPage = bean.getCurrentPage();
	%>

<%-- 
	<div style="width:50%" class="container">
		<div style="overflow: hidden" class="container" align="center">
			<div style="float: left">
				<%=rb.getString("drugs.records1")%>
				<%=currentPage * pageSize - pageSize + 1%>
				<%=rb.getString("drugs.records2")%>
				<%=currentPage * pageSize - pageSize + 1 + ((totalCount % pageSize != 0 && totalCount / pageSize * pageSize + 1 == currentPage * pageSize - pageSize + 1)
						? totalCount % pageSize : pageSize) - 1%>
				<%=rb.getString("drugs.records3")%>
				${action.totalCount}&nbsp;&nbsp;
			</div>
			<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<div style="float: right">
				<div style="float: left">
					<%=rb.getString("drugs.records4")%>:&nbsp; <select name="pageSize"
						onChange="changePageSize(this);" >
						<option
							${(empty action.pageSize or action.pageSize == 5) ? "selected='true'" : "" }
							value="/apotheca/drugs.run?pageSize=5">5</option>
						<option
							${(not empty action.pageSize and action.pageSize  == 10) ? "selected='true'" : "" }
							value="/apotheca/drugs.run?pageSize=10">10</option>
						<option
							${(not empty action.pageSize and action.pageSize  == 20) ? "selected='true'" : "" }
							value="/apotheca/drugs.run?pageSize=20">20</option>
					</select>
				</div>
				<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				<div style="float: right">
					<c:forEach var="displayPage" begin="1" end="${action.pagesCount}">
						<c:choose>
							<c:when
								test="${displayPage == (empty action.currentPage ? 1 : action.currentPage)}">${displayPage} &nbsp;</c:when>
							<c:otherwise>
								<a href="/apotheca/drugs.run?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${displayPage}">${displayPage}</a>&nbsp;</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
--%>
	
<%@ include file = "/pagination.jsp" %>
	<table border = "1" style="width:50%; margin-top: 20px" class="container" align="center">
		<caption><%=rb.getString("drugs.list")%></caption>
		<thead align="center">
			<tr>
				<th>#</th>
				<th><%=rb.getString("drugs.name")%></th>
				<th><%=rb.getString("drugs.dose")%></th>
				<th><%=rb.getString("drugs.quantity")%></th>
				<th><%=rb.getString("drugs.price")%></th>
				<th><%=rb.getString("drugs.prescription")%></th>
 				<%--th><%=rb.getString("drugs.amount")%></th--%> 
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
						<c:forEach  var="p" items="${action.cart.products}">
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
							<%-- <td> 
 								<input type="number"  
 								<c:choose> 
 									<c:when test="${present }">value=${amount }</c:when> 
 									<c:otherwise>value=0 disabled</c:otherwise> 
 								</c:choose> 
 								id="amount${d.id}" onchange="if (validateAmount(this)){onkeyup();}" onkeyup="updateShoppingCart(${d.id}, document.getElementById('drug${d.id}').checked, 'amount${d.id}')"/> 
 							</td> --%>
							<!-- expiery date -->
							<td>
								<c:if test="${not empty action.drugsFromRecipe[d.id] }">
									<c:out value="${action.drugsFromRecipe[d.id] }" />
								</c:if>
							</td>
							<td>
								
								<c:choose>
									<c:when test="${(not d.prescription)}">
										<input type="checkbox" id="drug${d.id}" value="${d.id}" name="drug"
											onchange="/*addRemoveFromCart(this, ${d.id });*/
													  updateShoppingCart(${d.id}, this.checked, 'cartAmount${d.id}');"
													  <c:if test="${d.quantity eq 0 }">disabled</c:if>
											<c:out value="${present ? 'checked' : ''}"/> />
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
		
	<!-- 	element for displaying validator errors -->
 	<!-- div id = "errorStatus" class="container" align="center"></div -->
	
	<div id = "shoppingCart" class="container" align="center"></div>
		
</body>
</html>