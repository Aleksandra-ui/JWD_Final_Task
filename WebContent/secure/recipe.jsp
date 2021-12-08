<%@page import="com.epam.jwd.apotheca.controller.DrugManagerService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.ResourceBundle,java.util.List,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.model.User,com.epam.jwd.apotheca.controller.UserManagerService,com.epam.jwd.apotheca.dao.api.UserDAO,java.util.stream.Collectors,java.util.stream.Stream,java.util.function.Predicate,java.util.ArrayList,com.epam.jwd.apotheca.controller.action.RecipeDrugs" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>   
    
<%@ include file = "/mainMenu.jsp" %>    
          
<c:if test="${ not canPrescribe }">
<c:redirect url="/drugs.jsp"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%
ResourceBundle rb = ResourceBundle.getBundle("Drugs", locale);
%>
<title><%=rb.getString("drugs.list")%></title>
</head>

<c:set var="baseURL" value="/apotheca/recipe.run"/>

<script type="text/javascript">

function updateRecipeCart( drugId, add ) {
	
	var xmlhttp = new XMLHttpRequest();
	//функция которая вызывается когда завершилась загрузка
	xmlhttp.onreadystatechange = function() {
		//this это объект xmlhttp
		if (this.readyState == 4 && this.status == 200) {
			var element = document.getElementById("drug" + drugId);
			if ( element ) {
				element.checked = add;  
			}
			displayShoppingCart(this);
	  	}
	};
	if ( add ) {
		xmlhttp.open("GET", "addToRecipeCart.run?drugId=" + drugId, true);	
	} else {
		xmlhttp.open("GET", "removeFromRecipeCart.run?drugId=" + drugId, true);
	}
	xmlhttp.send();
		
}

function displayShoppingCart(xml) {
	
	if ( xml.responseText.indexOf("Please login") != -1 ) {
		window.location = "/apotheca/logon.run";
	} else {
		document.getElementById("shoppingCart").innerHTML = xml.responseText;
		fillDaySelect();
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
	
	xmlhttp.open("GET", "displayRecipeCart.run?currentPage=" + currentPage + "&pageSize=" + pageSize, true);	
	xmlhttp.send();
	
}

function setClientName(select) {
	
		var opt = select.options[select.selectedIndex];
		var clientName = opt.text;
	
		var xmlhttp = new XMLHttpRequest();
		
		xmlhttp.open("GET", "setClientName.run?clientName=" + clientName, true);	
		xmlhttp.send();
	
}

function setClientName(select) {
	
	var opt = select.options[select.selectedIndex];
	var clientName = opt.text;

	var xmlhttp = new XMLHttpRequest();
	
	xmlhttp.open("GET", "setClientName.run?clientName=" + clientName, true);	
	xmlhttp.send();

}

function setExpieryDate(select) {
	
	var yearSelect = document.getElementById("Year");
	var monthSelect = document.getElementById("Month");
	var daySelect = document.getElementById("Day");
	
	var year = yearSelect.options[yearSelect.selectedIndex].value;
	var month = monthSelect.options[monthSelect.selectedIndex].value;
	var day = daySelect.options[daySelect.selectedIndex].value;
	
	var xmlhttp = new XMLHttpRequest();
	
	xmlhttp.onreadystatechange = function() {
		//this это объект xmlhttp
		if (this.readyState == 4 && this.status == 200) {
			displayShoppingCart(this);
	  	}
	};
	
	xmlhttp.open("GET", "setExpieryDate.run?year=" + year + "&month=" + month + "&day=" + day, true);	
	xmlhttp.send();
	
}

function fillDaySelect() {
	
	//alert("select");
	var year = document.getElementById('Year');
	var month = document.getElementById('Month');
	var day = document.getElementById('Day');
	var yearOpt = year.options[year.selectedIndex];
	var monthOpt = month.options[month.selectedIndex];
	console.log(monthOpt, yearOpt, day, month, year);
	var dayOpt;
	if (monthOpt.id == 'month02'){
		dayOpt = document.getElementById('30');
		dayOpt.hidden = true;
		dayOpt = document.getElementById('31');
		dayOpt.hidden = true;
		dayOpt = document.getElementById('29');
		if (yearOpt.id == '2024'){
			dayOpt.hidden = false;
		} else {
			dayOpt.hidden = true;
		}
	} else {
			dayOpt = document.getElementById('29');
			console.log(dayOpt);
			dayOpt.hidden = false;
			dayOpt = document.getElementById('30');
			console.log(dayOpt);
			dayOpt.hidden = false;
			dayOpt = document.getElementById('31');
			console.log(dayOpt);
			if ( (monthOpt.value < "08" && Number(monthOpt.value) % 2 == 1)||(monthOpt.value >= "08" && Number(monthOpt.value) % 2 == 0) ) {
				dayOpt.hidden = false;
			} else {
				dayOpt.hidden = true;
			}
	}
}


function updateUser( clientId ) {
	
	var xmlhttp = new XMLHttpRequest();
	//функция которая вызывается когда завершилась загрузка
	xmlhttp.onreadystatechange = function() {
		//this это объект xmlhttp
		if (this.readyState == 4 && this.status == 200) {
			displayShoppingCart(this);
	  	}
	};
	
	xmlhttp.open("GET", "setClientName.run?clientId=" + clientId, true);	
	
	xmlhttp.send();
		
}

</script>

<body onload="displayCart()">

	<%
	RecipeDrugs bean = (RecipeDrugs)request.getAttribute("action");
			int pageSize = bean.getPageSize();
			int currentPage = bean.getCurrentPage();
			int totalCount = bean.getTotalCount();
	%>	

	<div class="container" style="width: 70%;">	
		
		<%@ include file = "/pagination.jsp" %>
		
		<table border = "1" style="margin-top: 20px" class="container" align="center" >
			<caption><%=rb.getString("drugs.list")%></caption>
			<thead align ="center">
				<tr>
					<th>#</th>
					<th><%=rb.getString("drugs.name")%></th>
					<th><%=rb.getString("drugs.dose")%></th>
					<th><%=rb.getString("drugs.quantity")%></th>
					<th><%=rb.getString("drugs.price")%></th>
					<th><%=rb.getString("drugs.add")%></th>
				</tr>
			</thead>
			<tbody align ="center">
				<c:choose>
					<c:when test="${not empty action.drugs}">
						<c:forEach items="${action.drugs}" var="d">
							<tr bgcolor="LightBlue">
								<td><c:out value="${d.id}" /></td>
								<td><c:out value="${d.name}" /></td>
								<td><c:out value="${d.dose }" /></td>
								<td><c:out value="${d.quantity }" /></td>
								<td><c:out value="${d.price }" /></td>
								<td>
									<c:set var="present" value="false"/>
									<c:forEach var="drug" items="${action.cart.drugs }">
										<c:if test="${drug.id == d.id}">
											<c:set var="present" value="true"/>
										</c:if>
									</c:forEach>
									<input type="checkbox" id="drug${d.id}" value="${d.id}" name="drug" onchange="updateRecipeCart(${d.id}, this.checked);"
									   <c:out value="${present ? 'checked' : ''}"/>/> 
									<input type="hidden" id="checkbox${d.id}" value="${d.name}&nbsp;|&nbsp;${d.dose}"/>
								&nbsp; 
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr><td colspan="6">no records found</td></tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		
		<div id = "shoppingCart"></div>
		
		</div>

</body>
</html>