<%@page import="com.epam.jwd.apotheca.controller.DrugManagerService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.ResourceBundle,java.util.List,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.model.User,com.epam.jwd.apotheca.controller.UserManagerService,com.epam.jwd.apotheca.dao.api.UserDAO,java.util.stream.Collectors,java.util.stream.Stream,java.util.function.Predicate,java.util.ArrayList,com.epam.jwd.apotheca.controller.action.RecipeCommand" %>
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

<script type="text/javascript">
	drugIds = new Array(); 
</script>

<script type="text/javascript">

function readDrugs() {
	
		a = window.location.search; 
		paramLine = a.substr(1);
		params = paramLine.split("&");
		const DRUG_ID = "drugIds=";
		for ( param of params ) {
			if (param.startsWith(DRUG_ID)) {
				drugIds = param.substring(param.indexOf(DRUG_ID) + DRUG_ID.length ).split(","); //array of ids of chosen drugs
			}
		}
	}

function getDrugIds() {
	
	drugIdLine = "";
	if (drugIds.length > 0) {
		drugIdLine =  "drugIds=";
		for ( id of drugIds ) {
			if (id != ""){
				drugIdLine += id + ",";
			}
		}
		drugIdLine = drugIdLine.substring(0,drugIdLine.length-1);
	}
	
	return drugIdLine;
	
}

function changePageSize (select) {
	
	drugIdLine = getDrugIds();
	
	return select.options[select.selectedIndex].value && (window.location = select.options[select.selectedIndex].value + ((drugIdLine != "") ? "&" + drugIdLine : "" )); 
	
}

function changeURL(anchor) {
	
	drugIdLine = getDrugIds();
	
	newHref = anchor.href + ((drugIdLine != "") ? "&" + drugIdLine : "" );
	
	return anchor.href && (anchor.href = newHref); //if (anchor.href) {anchor.href = newHref;}. window.location transforms into window.location.href
	
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

function removeOptionsSelected()
{
  var select = document.getElementById('ListBox1');
  var i;
  
  for (i = select.length - 1; i>=0; i--) {
    if (select.options[i].selected) {
    	optId = select.options[i].id.substr("selectedDrug".length);
    	idx = drugIds.indexOf(optId);
    	if ( idx != -1 ) {
    		drugIds.splice(idx, 1);	
    	}
    	optId = "drug" + optId;
    	checkbox = document.getElementById(optId);
    	if ( checkbox != null ) {
    		checkbox.checked = false;
    	}
    	select.remove(i);
    }
  }
  changeSelectVisibility();
}

function fillDaySelect() {
	
	var year = document.getElementById('Year');
	var month = document.getElementById('Month');
	var day = document.getElementById('Day');
	var yearOpt = year.options[year.selectedIndex];
	var monthOpt = month.options[month.selectedIndex];
	if (monthOpt.id == 'february'){
		var dayOpt = document.getElementById('30');
		dayOpt.hidden = true;
		var dayOpt = document.getElementById('31');
		dayOpt.hidden = true;
		var dayOpt = document.getElementById('29');
		if (yearOpt.id == '2024'){
			dayOpt.hidden = false;
		} else {
			dayOpt.hidden = true;
		}
	} else {
			var dayOpt = document.getElementById('29');
			dayOpt.hidden = false;
			var dayOpt = document.getElementById('30');
			dayOpt.hidden = false;
			var dayOpt = document.getElementById('31');
			if ( (Number(monthOpt.value) < "8" && Number(monthOpt.value) % 2 == 1)||(Number(monthOpt.value) >= "8" && Number(monthOpt.value) % 2 == 0) ) {
				dayOpt.hidden = false;
			} else {
				dayOpt.hidden = true;
			}
	}
}

function gatherDrugIds() {
	var select = document.getElementById("ListBox1");
    var drugsContainer = select.getElementsByTagName('option');
    var hiddenValue = "";
  
    for (var idx = 0; idx < drugsContainer.length; idx++) {
    	hiddenValue += drugsContainer[idx].id.substr(12); 
    	   if (idx < (drugsContainer.length -1))  {
               hiddenValue += ",";
           }
    }
   
    document.getElementById("selectedIds").value = hiddenValue; 
}


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
	}
	
}

function displayCart() {
	
	var xmlhttp = new XMLHttpRequest();
	//функция которая вызывается когда завершилась загрузка
	xmlhttp.onreadystatechange = function() {
		//this это объект xmlhttp
		if (this.readyState == 4 && this.status == 200) {
			displayShoppingCart(this);
	  	}
	};
	
	xmlhttp.open("GET", "displayRecipeCart.run", true);	
	xmlhttp.send();
	
}

</script>

<body onload="displayCart();readDrugs();">

	<%=rb.getString("drugs.welcome")%>
	
	<%
		RecipeCommand bean = (RecipeCommand)request.getAttribute("action");
				int pageSize = bean.getPageSize();
				int currentPage = bean.getCurrentPage();
	%>
	
	<div>
		<div style="overflow: hidden">
			<div style="float: left"><%=rb.getString("drugs.records1")%> <%= currentPage*pageSize - pageSize + 1 %> <%=rb.getString("drugs.records2")%> <%= currentPage*pageSize - pageSize + 1 + ( (bean.getTotalCount() % pageSize != 0 && bean.getTotalCount() / pageSize * pageSize + 1 == currentPage*pageSize - pageSize + 1)? bean.getTotalCount() % pageSize : pageSize ) - 1 %> <%=rb.getString("drugs.records3")%> <%= bean.getTotalCount()%></div>
			<span style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<div style="float: left">
				<%=rb.getString("drugs.records4")%>:&nbsp;
				<select name="pageSize" onChange = "changePageSize(this);"> <!-- checking if there is a value in the first part of an and -->
					<option  ${param.pageSize  == 5 ? "selected='true'" : "" } value="/apotheca/recipe.run?pageSize=5">5</option>
					<option  ${param.pageSize  == 10 ? "selected='true'" : "" } value="/apotheca/recipe.run?pageSize=10" >10</option>
					<option  ${param.pageSize  == 20 ? "selected='true'" : "" } value="/apotheca/recipe.run?pageSize=20" >20</option>
				</select>
			</div>
			<span style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<div style="float: none">
				<%
					Integer pagesCount = bean.getTotalCount() / pageSize + ((bean.getTotalCount() % pageSize) == 0 ? 0 : 1);
					request.setAttribute("pagesCount", pagesCount);
				%>
				<c:forEach var="displayPage" begin="1" end="${pagesCount}">
					<c:choose>
						<c:when test="${displayPage == (empty param.currentPage ? 1 : param.currentPage)}">${displayPage} &nbsp;</c:when>
						<c:otherwise><a href = "/apotheca/recipe.run?pageSize=${empty param.pageSize ? 5 : param.pageSize}&currentPage=${displayPage}" onclick="changeURL(this);">${displayPage}</a>&nbsp;</c:otherwise>
					</c:choose>
				</c:forEach>
			</div>
		</div>
		<table border = "1" style="width:50%" >
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
	</div>

	<div id = "shoppingCart"></div>

</body>
</html>