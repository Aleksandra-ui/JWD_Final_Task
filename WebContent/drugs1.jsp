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

	drugIds = new Array(); 

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
	
	function setAmount() {
	for ( id of drugIds ) {
		amount = document.getElementById("amount" + id);
		if ( amount != null ) {
			amount.removeAttribute("disabled");
			amountValue = localStorage.getItem("amount" + id);
			if ( amountValue != null ) {
				amount.value = amountValue;
			} else {
				amount.value = 1;
			}
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
	
	return anchor.href && (anchor.href = newHref);
	
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
	
	function gatherAmounts() {
	
	var select = document.getElementById("ListBox1");
	var drugsContainer = select.getElementsByTagName('option');
	var amounts = document.getElementById("amounts");
	var drugIdsStr = document.getElementById("selectedIds").value.split(',');
	
	var listStr = "";
	
	for ( var i = 0; i < drugIdsStr.length; i ++ ) {
		
		listStr += drugsContainer[i].text.substring(drugsContainer[i].text.lastIndexOf('|') + 1, drugsContainer[i].text.length).trim();
		if ( i < (drugIdsStr.length - 1) ) {
			listStr += ",";
		}
	
	}
	
	amounts.value = listStr;
	
	}
	
	function printTotal() {
	
	var select = document.getElementById("ListBox1");
	var drugsContainer = select.getElementsByTagName('option');
	var total = 0;
	var input = document.getElementById("total");
	
	for (var idx = 0; idx < drugsContainer.length; idx++) {
		
		var i = drugsContainer[idx].text.lastIndexOf('|');
		amount = drugsContainer[idx].text.substr(i + 1);
		
		priceStr = drugsContainer[idx].text.substring(0, i);
		i = priceStr.lastIndexOf('|');
		priceStr = priceStr.substring(i + 1);
		total = total + Number(priceStr) * Number(amount);
	}
	
	input.value = total;
	
	}
	
	function printAmount(drugId) {
	
	var option = document.getElementById("selectedDrug" + drugId);
	var amount = document.getElementById("amount" + drugId);
	option.text = option.text.substring(0, option.text.lastIndexOf('|') + 1) + " " + String(amount.value);
	}
	
	function saveAmount(input) {
	
	localStorage.setItem(input.id, input.value);
	
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

</script>

<body onload="readDrugs();setAmount();printTotal();">
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
				<%
// 				RecipeManagerService recipeService = (RecipeManagerService) application.getAttribute("recipeService");
// 				User user = (User) session.getAttribute("user");
// 				Map<Integer, Date> drugsFromRecipe = new HashMap<Integer, Date>();
// 				if (user != null) {
// 					List<Recipe> recipesForUser = recipeService.findByUser(user);
// 					for (Recipe recipe : recipesForUser) {
// 						Date expieryDate = recipe.getExpieryDate();
// 						for (Integer drugId : recipe.getDrugIds()) {
// 							drugsFromRecipe.put(drugId, expieryDate);
// 						}
// 					}
// 				}
				//request.setAttribute("drugsFromRecipe", drugsFromRecipe);
				%>
				
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
								<td><input type="number" value=0 disabled
									id="amount${d.id}" onchange="if (validateAmount(amount${d.id})){printAmount(${d.id });printTotal()}" onkeyup="saveAmount(this);"/></td>
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
												onchange="addRemoveFromCart(this, ${d.id});showId(this);"
												<c:out value="${present ? 'checked' : ''}"/> />
											<input type="hidden" id="checkbox${d.id}" value="${d.name}&nbsp;|&nbsp;${d.dose}&nbsp;|&nbsp;${d.price}"/>
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

		<form action="drugsBill.run" method="POST">
			<div id = "div" <c:if test="${fn:length(param.drugIds) == 0}">style="display:none"</c:if>>
<!-- 				<select multiple id="ListBox1"> -->
<%-- 					<% --%>
 					<!-- request.setAttribute("allDrugs", drugService.getDrugs()); -->
<%-- 					%> --%>
<%-- 					<c:forEach items="${allDrugs}" var="drug"> --%>
<%-- 						<c:set var="idStr">${drug.id}</c:set> --%>
<%-- 						<c:forEach items="${fn:split(param.drugIds,',')}" var="aDrug"> --%>
<%-- 							<c:if test="${aDrug == idStr}"> --%>
<%-- 								<option id="selectedDrug${idStr}" value="${idStr}"> --%>
<%-- 									${drug.name}&nbsp;|&nbsp;${drug.dose}&nbsp;|&nbsp;${drug.price}&nbsp;|&nbsp;<script>document.write(localStorage.getItem("amount" + ${drug.id}) != null ? localStorage.getItem("amount" + ${drug.id}) : 1);</script> --%>
<!-- 								</option> -->
<%-- 							</c:if> --%>
<%-- 						</c:forEach> --%>
<%-- 					</c:forEach> --%>
<!-- 				</select> -->
				<button id="Submit1" type="button" onclick="removeOptionsSelected();printTotal()"
					<c:if test="${fn:length(param.drugIds) == 0}">style="display:none"</c:if>><%=rb.getString("drugs.delete")%></button>
				<input hidden="true" id="selectedIds"  name="drugIds" id="hiddenInput"  />
				<label for="total"><%=rb.getString("drugs.total")%></label>
				<input id="total" readonly></input>
				<input hidden="true" name="amounts" id="amounts"/>
				<input type="submit" value="<%=rb.getString("drugs.buy")%>" onclick="gatherDrugIds();gatherAmounts()"/>
			</div>
			
		</form>
		
	</div>
</body>
</html>