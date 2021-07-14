<%@page import="com.epam.jwd.apotheca.controller.DrugManagerService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.ResourceBundle, java.util.List, com.epam.jwd.apotheca.model.Drug, com.epam.jwd.apotheca.model.User, com.epam.jwd.apotheca.controller.UserManagerService,
    com.epam.jwd.apotheca.controller.RecipeManagerService, com.epam.jwd.apotheca.model.Recipe, java.util.Map, java.util.HashMap, java.sql.Date" %>
<%--     <%@ taglib uri="" prefix="c" %> --%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=ResourceBundle.getBundle("Drugs").getString("drugs.list") %></title>
</head>
<body onload="readDrugs();">

	<script>
	
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
	 		recipe = document.getElementById("ListBox1");
			button = document.getElementById("Submit1");
	 		div = document.getElementById("div");
	 		
			if (drugId.checked) {
				if (idx == -1) {
					drugIds.push(drugId.value);
					var opt = document.createElement("option");
					var drugName = document.getElementById("checkbox" + drugId.value);
					opt.text = drugName.value; //document.getElementById("TextBox4").value;
					opt.id = "selectedDrug" + drugId.value;
					recipe.options.add(opt);
//	 				recipe.style.display = 'inline-block';
					button.style.display = 'inline-block';
//	 				button.style.display = 'inline-block';
					div.style.display = 'inline-block';
					
				}
				
			} else {
				if (idx != -1) {
					drugIds.splice(idx, 1);//deleting 1 element
					opt = document.getElementById("selectedDrug" + drugId.value);
					
					recipe.removeChild(opt);
					if ( recipe.options.length == 0){
//	 					recipe.style.display = 'none';
						button.style.display = 'none';
						div.style.display = 'none';
					}
					
				}
				
			}
			
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
	
	</script>
	
	<%=ResourceBundle.getBundle("Drugs").getString("drugs.welcome") %>
	<a href="/apotheca/index.jsp">home</a>
	
	<%
		DrugManagerService service = (DrugManagerService) application.getAttribute("drugService");
		List<Drug> drugs = service.getDrugs();
		int pageSize = request.getParameter("pageSize") == null ? 5 : Integer.valueOf(request.getParameter("pageSize"));
		int currentPage = request.getParameter("currentPage") == null ? 1 : Integer.valueOf(request.getParameter("currentPage"));
	%>
	
	<div>
	<div style="overflow: hidden">
		<div style="float: left">Records: from <%= currentPage*pageSize - pageSize + 1 %> to <%= currentPage*pageSize - pageSize + 1 + ( (drugs.size() % pageSize != 0 && drugs.size() / pageSize * pageSize + 1 == currentPage*pageSize - pageSize + 1)? drugs.size() % pageSize : pageSize ) - 1 %> of <%= drugs.size()%> </div>
		<span style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<div style="float: left">
			records per page:&nbsp;
			<select name="pageSize" onChange = "this.options[this.selectedIndex].value && (window.location = this.options[this.selectedIndex].value);">
			<option ${(empty param.pageSize or param.pageSize == 5) ? "selected='true'" : "" } value="/apotheca/drugs.jsp?pageSize=5">5</option>
			<option ${(not empty param.pageSize and param.pageSize  == 10) ? "selected='true'" : "" } value="/apotheca/drugs.jsp?pageSize=10" >10</option>
			<option ${(not empty param.pageSize and param.pageSize  == 20) ? "selected='true'" : "" } value="/apotheca/drugs.jsp?pageSize=20" >20</option>
			</select>
		</div>
		<span style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<div style="float: none">
		<%
		Integer pagesCount = drugs.size() / pageSize + ((drugs.size() % pageSize) == 0 ? 0 : 1);
		request.setAttribute("pagesCount", pagesCount);
		%>
		
<%-- 		<c:set scope="request" var="pagesCount" value="${drugs.size() / pageSize + ((drugs.size() % pageSize) == 0 ? 0 : 1)}"/> --%>
		
<%-- 		<c:out value="${param.pageSize}"/> --%>
<%-- 		<c:set scope="request" var="currentPage" value="${empty param.currentPage ? 1 : param.currentPage}"/> --%>
<%-- 		<c:set scope="request" var="pageSize" value="${empty param.pageSize ? 5 : param.pageSize}"/> --%>
		
		<c:forEach var="displayPage" begin="1" end="${pagesCount}">
			<c:choose>
				<c:when test="${displayPage == (empty param.currentPage ? 1 : param.currentPage)}">${displayPage} &nbsp;</c:when>
				<c:otherwise><a href = "/apotheca/drugs.jsp?pageSize=${empty param.pageSize ? 5 : param.pageSize}&currentPage=${displayPage}">${displayPage}</a>&nbsp;</c:otherwise>
			</c:choose>
		</c:forEach>
		
		</div>
	</div>
	<table border = "1" style="width:50%" >
		<caption>List of drugs</caption>
		<thead align ="center">
			<tr>
				<th>#</th>
				<th>name</th>
				<th>dose</th>
				<th>quantity</th>
				<th>price</th>
				<th>prescription</th>
				<th>amount</th>
				<th>expiery date</th>
				<th>add to cart</th>
			</tr>
		</thead>
		
		<tbody align ="center">
		<%
			List<Drug> visibleDrugs = service.getDrugs(pageSize * (currentPage - 1) , pageSize );
			request.setAttribute("drugsList", visibleDrugs); 
			RecipeManagerService recipeService = (RecipeManagerService)application.getAttribute("recipeService"); 
			User user = (User)session.getAttribute("user");
			Map<Integer, Date> drugsFromRecipe = new HashMap<Integer, Date>();
			if ( user != null ) {
				List<Recipe> recipesForUser = recipeService.findByUser(user);
				for ( Recipe recipe : recipesForUser ) {
					Date expieryDate = recipe.getExpieryDate();
					for ( Integer drugId : recipe.getDrugIds() ) {
						drugsFromRecipe.put(drugId, expieryDate);
					}
				}
			}
			request.setAttribute("drugsFromRecipe", drugsFromRecipe);
		%>
<%-- 			<jsp:useBean id="drugList" beanName="visibleDrugs" type="List<Drug>" scope="request"/> --%>
<%--  		    <c:set scope="request" var="drugsList" value="${visibleDrugs}"/> --%>
			<c:choose>
				<c:when test="${not empty drugsList}">
					<c:forEach items="${drugsList}" var="d">

						<tr bgcolor=<c:out value="${not d.prescription ? 'LightGreen' : 'LightPink'}"/>>
							<td><c:out value="${d.id}" /></td>
							<td><c:out value="${d.name}" /></td>
							<%-- 				<td><%=d.getName() %></td> --%>
							<td><c:out value="${d.dose }" /></td>
							<td><c:out value="${d.quantity }" /></td>
							<td><c:out value="${d.price }" /></td>
							<td><c:out value="${d.prescription ? 'yes' : 'no'}" /></td>
							<%-- 				<td><%=d.isPrescription() ? "yes" : "no" %></td> --%>
							<td><input type="number" value=0 disabled id="amount${d.id}"/></td>
							<td><c:if test="${not empty drugsFromRecipe[d.id] }" >
								<c:out value="${drugsFromRecipe[d.id] }"/>
							</c:if></td>
							<td><input type="checkbox" onchange="addRemoveFromCart(this, ${d.id});showId(this);"/></td>
						</tr>

					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr><td colspan="6">no records found</td></tr>
				</c:otherwise>
			</c:choose>
		
		</tbody>
	</table>

	<select multiple id="ListBox1">
		<%
		DrugManagerService drugService = (DrugManagerService)application.getAttribute("drugService");
		request.setAttribute("allDrugs", drugService.getDrugs());
		%>
		<c:forEach items="${allDrugs}" var="drug">
			<c:set var="idStr">${drug.id}</c:set>
			<c:forEach items="${fn:split(param.drugIds,',')}" var="aDrug">
				<c:if test="${aDrug == idStr}">
					<option id="selectedDrug${idStr}" value="${idStr}">${drug.name}&nbsp;|&nbsp;${ drug.dose }</option>
				</c:if>
			</c:forEach>
		</c:forEach>
	</select>
	<button id="Submit1" type="button" onclick="removeOptionsSelected();"
		<c:if test="${fn:length(param.drugIds) == 0}">style="display:none"</c:if>>delete</button>

	</div>
	
	<%
	List<String> a = new java.util.ArrayList<String>();
	a.add("a");
	a.add("b");
	a.add("c");
	request.setAttribute("a", a);
	%>
	<c:forEach items = "${a}" var="i" begin="0" end="2" > 
		<c:out value="${i} "></c:out>
	</c:forEach>
	
	<c:forEach items="${visibleDrugs}" var="drug" begin="0" end="${visibleDrugs.size}">
		<c:out value="${drug}" default="---" ></c:out>
<%-- 		<c:out value="${drug.name}" default="---" ></c:out> --%>
		<c:out value="<br/>" ></c:out>
	</c:forEach>
	
	
	<%
	UserManagerService uService = (UserManagerService)application.getAttribute("userService");
	if (user != null && uService.canPrescribe(user)){
	%>
	<a href="/apotheca/secure/createDrug.jsp">create drug</a>
	<%
	}
	%>
</body>
</html>
