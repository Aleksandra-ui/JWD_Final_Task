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

<script type="text/javascript">
	
	function showId (drugId) {
	
	found = false;
	idx = drugIds.indexOf(drugId.value);
		select = document.getElementById("ListBox1");
	button = document.getElementById("Submit1");
		div = document.getElementById("div");
		
	if (drugId.checked) {
		if (idx == -1) {
			drugIds.push(drugId.value);
			
			document.getElementById("amount" + drugId.value).removeAttribute("disabled");
			if (localStorage.getItem("amount" + drugId.value) != null) {
				document.getElementById("amount" + drugId.value).value = localStorage.getItem("amount" + drugId.value);	
			} else {
				document.getElementById("amount" + drugId.value).value = 1;
			}
			
			var opt = document.createElement("option");
			var drugDescr = document.getElementById("checkbox" + drugId.value);
			opt.text = drugDescr.value + " | " + document.getElementById("amount" + drugId.value).value; 
			opt.id = "selectedDrug" + drugId.value;
			select.options.add(opt);
			button.style.display = 'inline-block';
			div.style.display = 'inline-block';	
			
			
		}
	} else {
		if (idx != -1) {
			drugIds.splice(idx, 1);//deleting 1 element
			opt = document.getElementById("selectedDrug" + drugId.value);
			select.removeChild(opt);
			if ( select.options.length == 0){
				button.style.display = 'none';
				div.style.display = 'none';
			}	
		}
	}
	printTotal();
	} 
	
	function addRemoveFromCart(checkbox, drugId) {
	
	amount = document.getElementById("amount" + drugId);
	
	if (checkbox.checked) {
		amount.removeAttribute("disabled");
		if ( amount.value == 0 ) {
			amount.value = 1;	
		}
	} else {
		amount.setAttribute("disabled", true);
	}
	
	}
	
	function changePageSize (select) {
	
		return select.options[select.selectedIndex].value && (window.location = select.options[select.selectedIndex].value); 
	
	}
	
	function displayParams() {
		params = window.location.href;
		params = "" + params.substring(params.indexOf('?') + 1);
		pp = params.split('&');
		retVal = [];
		for (i = 0; i < pp.length; i++) {
		    keyVal = pp[i].split("=");
		    retVal.push(keyVal[0] + " : " + keyVal[1]);
		}
	}
	
	function changeSelectVisibility() {
		var div = document.getElementById("div");
		div.style.display = (drugIds.length == 0) ? 'none' : 'inline-block'; 
	}
	
	
	function validateAmount(input) {
		
		result = false;
		if (input.value < 1 || input.value > 100) {
			input.className = "error";
			document.getElementById("errorStatus").innerHTML = "<label style='color:red;'>Value exceeds allowed range</label>";
		} else {
			input.className = "";
			document.getElementById("errorStatus").innerHTML = "";
			result = true;
		}
		console.log("input " + input.id + ", value " + input.value + ", class " + input.className);
		return result;
		
	}
	
	function updateShoppingCart( drugId, amountControlId ) {
		
		var amount = document.getElementById(amountControlId).value;
		var xmlhttp = new XMLHttpRequest();
		//функция которая вызывается когда завершилась загрузка
		xmlhttp.onreadystatechange = function() {
			//this это объект xmlhttp
		  if (this.readyState == 4 && this.status == 200) {
		    displayShoppingCart(this);
		  }
		};
		xmlhttp.open("GET", "addToCart.run?drugId=" + drugId + "&amount=" + amount, true);
		xmlhttp.send();
			
	}
	
	function displayShoppingCart(xml) {
		document.getElementById("shoppingCart").innerHTML = xml.responseText;
	}

</script>

<body>
	<%
		Drugs bean = (Drugs)request.getAttribute("action");
	
		Integer totalCount = bean.getTotalCount();
		int pageSize = bean.getPageSize();
		int currentPage = bean.getCurrentPage();
	%>

	<div>
		<div style="overflow: hidden">
			<div style="float: left">
				<%=rb.getString("drugs.records1")%>
				<%=currentPage * pageSize - pageSize + 1%>
				<%=rb.getString("drugs.records2")%>
				<%=currentPage * pageSize - pageSize + 1
		+ ((totalCount % pageSize != 0
				&& totalCount / pageSize * pageSize + 1 == currentPage * pageSize - pageSize + 1)
						? totalCount % pageSize
						: pageSize)
		- 1%>
				<%=rb.getString("drugs.records3")%>
				${action.totalCount}
			</div>
			<span style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<div style="float: left">
				<%=rb.getString("drugs.records4")%>:&nbsp; <select name="pageSize"
					onChange="changePageSize(this);">
					<option
						${(empty param.pageSize or param.pageSize == 5) ? "selected='true'" : "" }
						value="/apotheca/drugs.run?pageSize=5">5</option>
					<option
						${(not empty param.pageSize and param.pageSize  == 10) ? "selected='true'" : "" }
						value="/apotheca/drugs.run?pageSize=10">10</option>
					<option
						${(not empty param.pageSize and param.pageSize  == 20) ? "selected='true'" : "" }
						value="/apotheca/drugs.run?pageSize=20">20</option>
				</select>
			</div>
			<span style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<div style="float: none">
				<%
				Integer pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
				request.setAttribute("pagesCount", pagesCount);
				%>

				<c:forEach var="displayPage" begin="1" end="${pagesCount}">
					<c:choose>
						<c:when
							test="${displayPage == (empty param.currentPage ? 1 : param.currentPage)}">${displayPage} &nbsp;</c:when>
						<c:otherwise>
							<a
								href="/apotheca/drugs.run?pageSize=${empty param.pageSize ? 5 : param.pageSize}&currentPage=${displayPage}" onclick="changeURL(this)">${displayPage}</a>&nbsp;</c:otherwise>
					</c:choose>
				</c:forEach>

			</div>
		</div>
		<table border="1" style="width: 50%">
			<caption><%=rb.getString("drugs.list")%></caption>
			<thead align="center">
				<tr>
					<th>#</th>
					<th><%=rb.getString("drugs.name")%></th>
					<th><%=rb.getString("drugs.dose")%></th>
					<th><%=rb.getString("drugs.quantity")%></th>
					<th><%=rb.getString("drugs.price")%></th>
					<th><%=rb.getString("drugs.prescription")%></th>
					<th><%=rb.getString("drugs.amount")%></th>
					<th><%=rb.getString("drugs.date")%></th>
					<th><%=rb.getString("drugs.cart")%></th>
				</tr>
			</thead>

			<tbody align="center">
				<c:choose>
					<c:when test="${not empty action.drugs}">
						<c:forEach items="${action.drugs}" var="d">

							<tr
								bgcolor=<c:out value="${not d.prescription ? 'LightGreen' : 'LightBlue'}"/>>
								<td><c:out value="${d.id}" /></td>
								<td><c:out value="${d.name}" /></td>
								<td><c:out value="${d.dose }" /></td>
								<td><c:out value="${d.quantity }" /></td>
								<td><c:out value="${d.price }" /></td>
								<td>
									<c:if test="${d.prescription}"><%=rb.getString("drugs.yes")%></c:if>
									<c:if test="${not d.prescription}"><%=rb.getString("drugs.no")%></c:if>
								</td>
								<td>
									<input type="number" value=0 disabled
									id="amount${d.id}" onchange="if (validateAmount(amount${d.id})){}" onkeyup="updateShoppingCart(${d.id}, amount${d.id})"/>
								</td>
								<!-- expiery date -->
								<td>
									<c:if test="${not empty action.drugsFromRecipe[d.id] }">
										<c:out value="${action.drugsFromRecipe[d.id] }" />
									</c:if>
								</td>
								<td>
									<c:set var="present" value="false"/>
									<c:set var="ids" value="${fn:split(param.drugIds,',')}"/>
									<c:set var="idStr" >${d.id}</c:set>
									<c:forEach var="id" items="${ids}">
										<c:if test="${id == idStr}">
											<c:set var="present" value="true"/>
										</c:if>
									</c:forEach>
							
									<c:choose>
<%-- 										<c:when test="${(not d.prescription) or (not empty drugsFromRecipe[d.id])}"> --%>
										<c:when test="${(not d.prescription)}">
											<input type="checkbox" id="drug${d.id}" value="${d.id}" name="drug"
												onchange="updateShoppingCart(${d.id}, 'amount${d.id}')"
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
		
		<div id = "errorStatus"></div>
		
		<div id = "shoppingCart"></div>
		
	</div>
</body>
</html>