<%@page import="com.epam.jwd.apotheca.controller.DrugManagerService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.ResourceBundle, java.util.List, com.epam.jwd.apotheca.model.Drug, com.epam.jwd.apotheca.model.User" %>
<%--     <%@ taglib uri="" prefix="c" %> --%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
     <c:if test="${ not empty sessionScope.user and not (fn:toUpperCase(sessionScope.user.role) eq 'DOCTOR')}">
    	<c:redirect url="/drugs.jsp"/>
     </c:if>
		<c:out value="${fn:toUpperCase(sessionScope.user)}"></c:out>
		<c:out value="${fn:toUpperCase(sessionScope.user.role)}"></c:out>
		<c:out value="${not (fn:toUpperCase(sessionScope.user.role) eq 'DOCTOR')}"></c:out>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=ResourceBundle.getBundle("Drugs").getString("drugs.list") %></title>
</head>

<script type="text/javascript">
	drugIds = new Array(); 
// 	a = window.location.href; //link to current page
// 	paramLine = a.substring(a.indexOf('?') + 1);
// 	params = paramLine.split("&");
// 	const DRUG_ID = "drugIds=";
// 	for ( param of params ) {
// 		if (param.startsWith(DRUG_ID)) {
// 			drugIds = param.substring(param.indexOf(DRUG_ID) + DRUG_ID.length + 1).split(","); //array of ids of chosen drugs
// 		}
// 	}
</script>

<body onload="readDrugs();fillWithDrugIds();displayParams();">

	<script type="text/javascript">
	
	
	
	function readDrugs() {
	
	// 	drugIds = new Array(); 
		a = window.location.search; //link to current page
		//alert("search: " + a);
		paramLine = a.substr(1);
		params = paramLine.split("&");
		const DRUG_ID = "drugIds=";
		//alert(params);
		for ( param of params ) {
			if (param.startsWith(DRUG_ID)) {
				//alert( param.substr(param.indexOf(DRUG_ID) + DRUG_ID.length ) );
				drugIds = param.substring(param.indexOf(DRUG_ID) + DRUG_ID.length ).split(","); //array of ids of chosen drugs
			}
		}
	}
	
	
	function showId (drugId) {
		
		found = false;
		idx = drugIds.indexOf(drugId.value);
		recipe = document.getElementById("ListBox1");
		
		
		if (drugId.checked) {
			if (idx == -1) {
				drugIds.push(drugId.value);
				var opt = document.createElement("option");
				opt.text = drugId.value; //document.getElementById("TextBox4").value;
				opt.id = "drug" + drugId.value;
				recipe.options.add(opt);
				recipe.style.display = 'inline-block';
			}
			
		} else {
			if (idx != -1) {
				drugIds.splice(idx, 1);//deleting 1 element
				opt = document.getElementById("drug" + drugId.value);
				
				recipe.removeChild(opt);
				if ( recipe.options.length == 0){
					recipe.style.display = 'none';
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
		
		return anchor.href && (anchor.href = newHref); //window.location трансформир в window.location.href
		
	}
	
	function fillWithDrugIds(){
		
	
			//document.getElementById("ListBox1").style.display="inline"
			//visibility: hidden
		
		for ( id of drugIds ) {
			var opt = document.createElement("option");
			opt.text = id; //document.getElementById("TextBox4").value;
			opt.id = "drug" + id;
		    //opt.value =  drugIds[0];//document.getElementById("TextBox4").value;
		    document.getElementById("ListBox1").options.add(opt);
		}
		
		
		 
		//xxx = document.getElementById("drug2")
		//document.getElementById("ListBox1").removeChild(xxx)
		
	}
	
	function displayParams() {
        params = window.location.href;
        //alert(params);
        params = "" + params.substring(params.indexOf('?') + 1);
        pp = params.split('&');
        //alert(pp);
        retVal = [];
        for (i = 0; i < pp.length; i++) {
            keyVal = pp[i].split("=");
            //alert("Key = " + keyVal[0] + "\nValue = " + keyVal[1]);
            retVal.push(keyVal[0] + " : " + keyVal[1]);
        }
 		//alert("RetVal " + retVal);
        
    }
	
// 	function changeSelectVisibility() {
		
// 		var select = document.getElementById("ListBox1");
// 		alert(drugIds.length);
		
// 		select.style.display = drugIds.length == 0 ? 'none' : 'inline'; 
		
// 	}
	
	
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
			<select name="pageSize" onChange = "changePageSize(this);"> <!-- проверяем есть ли значение в первой части end-a -->
				<option  ${param.pageSize  == 5 ? "selected='true'" : "" } value="/apotheca/secure/recipe.jsp?pageSize=5">5</option>
				<option  ${param.pageSize  == 10 ? "selected='true'" : "" } value="/apotheca/secure/recipe.jsp?pageSize=10" >10</option>
				<option  ${param.pageSize  == 20 ? "selected='true'" : "" } value="/apotheca/secure/recipe.jsp?pageSize=20" >20</option>
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
				<c:otherwise><a href = "/apotheca/secure/recipe.jsp?pageSize=${empty param.pageSize ? 5 : param.pageSize}&currentPage=${displayPage}" onclick="changeURL(this);">${displayPage}</a>&nbsp;</c:otherwise>
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
			</tr>
		</thead>
		
		<tbody align ="center">
		<%
			List<Drug> visibleDrugs = service.getDrugs(pageSize * (currentPage - 1) , pageSize );
			request.setAttribute("drugsList", visibleDrugs); //analogue of line 73 
// 				for ( Drug d : visibleDrugs ){
		%>
<%-- 			<jsp:useBean id="drugList" beanName="visibleDrugs" type="List<Drug>" scope="request"/> --%>
<%--  		    <c:set scope="request" var="drugsList" value="${visibleDrugs}"/> --%>
			<c:choose>
				<c:when test="${not empty drugsList}">
					<c:forEach items="${drugsList}" var="d">

						<tr bgcolor=<c:out value="${not d.prescription ? 'LightGreen' : 'LightPink'}"/>>
							<td><c:out value="${d.id}" /></td>
							<td><c:out value="${d.name}" /></td>
							<td><c:out value="${d.dose }" /></td>
							<td><c:out value="${d.quantity }" /></td>
							<td><c:out value="${d.price }" /></td>
							<td>
								<c:if test="${d.prescription}">
									<c:set var="present" value="false"/>
<%-- 									<c:set var="ids" value="${param.drugIds}"/> --%>
									<c:set var="ids" value="${fn:split(param.drugIds,',')}"/>
									<c:set var="idStr" >${d.id}</c:set>
									<c:forEach var="id" items="${ids}">
										<c:if test="${id == idStr}">
											<c:set var="present" value="true"/>
										</c:if>
									</c:forEach>
<%-- 									<c:out value="${param.drugIds}" /> --%>
									<input type="checkbox" value="${d.id}" name="drug" onchange="showId(this);"
										   <c:out value="${present ? 'checked' : ''}"/>/> 
									
									
									
									&nbsp; <!-- this указывает на объект checkbox -->
								</c:if>
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
	
	<select multiple id="ListBox1" style="display:none">
<%-- 	<select multiple id="ListBox1" style=" display: "${empty drugIds ? 'none' : 'inline'}"" > --%>
		
<!-- 		<option id="1">---a---</option> -->
<!-- 		<option id="2">---b---</option> -->
<!-- 		<option id="3">---c---</option> -->
<!-- 		<option id="4">---d---</option> -->
	</select>
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
	User user = (User)session.getAttribute("user");
	if (user != null && "doctor".equalsIgnoreCase(user.getRole())){
	%>
	<a href="/apotheca/secure/createDrug.jsp">create drug</a>
	<%
	}
	%>
</body>
</html>