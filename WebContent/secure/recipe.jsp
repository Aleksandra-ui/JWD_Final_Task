<%@page import="com.epam.jwd.apotheca.controller.DrugManagerService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.ResourceBundle, java.util.List, com.epam.jwd.apotheca.model.Drug, com.epam.jwd.apotheca.model.User, com.epam.jwd.apotheca.controller.UserManagerService" %>
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

<body onload="readDrugs();fillDaySelect()">

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
		button = document.getElementById("Submit1");
		
		
		if (drugId.checked) {
			if (idx == -1) {
				drugIds.push(drugId.value);
				var opt = document.createElement("option");
				
				var drugName = document.getElementById("checkbox" + drugId.value);
				//alert(drugName.value);
				
				opt.text = drugName.value; //document.getElementById("TextBox4").value;
				opt.id = "selectedDrug" + drugId.value;
				recipe.options.add(opt);
				recipe.style.display = 'inline-block';
				button.style.display = 'inline-block';
			}
			
		} else {
			if (idx != -1) {
				drugIds.splice(idx, 1);//deleting 1 element
				opt = document.getElementById("selectedDrug" + drugId.value);
				
				recipe.removeChild(opt);
				if ( recipe.options.length == 0){
					recipe.style.display = 'none';
					button.style.display = 'none';
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
		
		return anchor.href && (anchor.href = newHref); //if (anchor.href) {anchor.href = newHref;}. window.location transforms into window.location.href
		
	}
	
	function fillWithDrugIds(){
		
		
			//document.getElementById("ListBox1").style.display="inline"
			//visibility: hidden
			
		if ( drugIds.length > 0 ) {
			document.getElementById("ListBox1").style.display="inline-block";
		}	
			
		alert(drugIds +"!!!");	
		
		for ( id of drugIds ) {
			var opt = document.createElement("option");
			//opt.text = id; //document.getElementById("TextBox4").value;
			
			var drugName = document.getElementById("checkbox" + id);
			alert(drugName);
			
			opt.text = drugName.value;
			
			opt.id = "selectedDrug" + id;
		    //opt.value =  drugIds[0];//document.getElementById("TextBox4").value;
		    document.getElementById("ListBox1").options.add(opt);
		}
		
		
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
	
	function changeSelectVisibility() {
		
		var select = document.getElementById("ListBox1");
		var button = document.getElementById("Submit1");
		select.style.display = (drugIds.length == 0) ? 'none' : 'inline-block'; 
		button.style.display = (drugIds.length == 0) ? 'none' : 'inline-block'; 
		
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
		
		//alert("select");
		var year = document.getElementById('Year');
		var month = document.getElementById('Month');
		var day = document.getElementById('Day');
		var yearOpt = year.options[year.selectedIndex];
		var monthOpt = month.options[month.selectedIndex];
		//alert(monthOpt);
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
				if (monthOpt.value == '31') {
					dayOpt.hidden = false;
				} else {
					dayOpt.hidden = true;
				}
		}
	}
	
	function gatherDrugIds() {
	    drugsContainer = document.getElementByID("ListBox1").options;
	    Array recipeDrugIds
	    
	    for (idx = 0; idx < drugsContainer.length; idx++) {
	    	
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
									<input type="checkbox" id="drug${d.id}" value="${d.id}" name="drug" onchange="showId(this);"
										   <c:out value="${present ? 'checked' : ''}"/>/> 
									<input type="hidden" id="checkbox${d.id}" value="${d.name}&nbsp;|&nbsp;${d.dose}"/>
									
									
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
	
<!-- <!-- 	<script type="text/javascript"> --> 
<!-- // 	 fillWithDrugIds(); -->
<!-- <!-- 	</script> -->

	<%
	UserManagerService userService = (UserManagerService) application.getAttribute("userService");
	List<User> clients = userService.getClients();
	User doctor = (User)session.getAttribute("user");
	Integer doctorId = doctor.getId();
	%>

	<form action = "createRecipe.jsp" method="POST">
		<input hidden="true" name="doctorId" value=<%= doctorId%> />
		
		
<%-- 		<input hidden="true" name="drugs" value="${fn:split(param.drugIds,',')[0]}" /> --%>
		<select multiple id="ListBox1" 
			<c:if test="${fn:length(param.drugIds) == 0}">style="display:none"</c:if>>
			<%
			request.setAttribute("allDrugs", service.getDrugs());
			%>
			<c:forEach items="${allDrugs}" var="drug">
				<c:set var="idStr">${drug.id}</c:set>
				<c:forEach items="${fn:split(param.drugIds,',')}" var="aDrug">
					<c:if test="${aDrug == idStr}">
						<option id="selectedDrug${idStr}">${drug.name}&nbsp;|&nbsp;${ drug.dose }</option>
					</c:if>
				</c:forEach>
			</c:forEach>
		</select>
		<button id="Submit1" type="submit" onclick="  removeOptionsSelected()"
			<c:if test="${fn:length(param.drugIds) == 0}">style="display:none"</c:if>>delete</button>
	
		<input  value=${ param.drugIds} />
		<input hidden="true" id="hiddenInput" name="drugs" value=${ param.drugIds} />
		<select id="ListBoxUsers" name="clientName">
		<%
	 	for (User u : userService.getClients()) {
		%>
		<option><%=u.getName()%></option>
		<%
	 	}
		%>
		</select>
		
		<select id="Year" name="year" onchange="fillDaySelect()">
			<option id="2021">2021</option>
			<option id="2022">2022</option>
			<option id="2023">2023</option>
			<option id="2024">2024</option>	
		</select> 
		<select id="Month" name="month" onchange="fillDaySelect()" >
			<option id="january"  value="31">january</option>
			<option id="february">february</option>
			<option id="march"  value="31">march</option>
			<option id="april">april</option>	
			<option id="may"  value="31">may</option>	
			<option id="june">june</option>	
			<option id="july"  value="31">july</option>	
			<option id="august"  value="31">august</option>	
			<option id="september">september</option>	
			<option id="october"  value="31">october</option>	
			<option id="november">november</option>	
			<option id="december" value="31">december</option>	
		</select> 
		<select id="Day" name="day" >
			<option>1</option>
			<option>2</option>
			<option>3</option>
			<option>4</option>	
			<option>5</option>	
			<option>6</option>	
			<option>7</option>	
			<option>8</option>	
			<option>9</option>	
			<option>10</option>	
			<option>11</option>	
			<option>12</option>	
			<option>13</option>	
			<option>14</option>	
			<option>15</option>	
			<option>16</option>	
			<option>17</option>	
			<option>18</option>	
			<option>19</option>	
			<option>20</option>	
			<option>21</option>	
			<option>22</option>	
			<option>23</option>	
			<option>24</option>	
			<option>25</option>	
			<option>26</option>	
			<option>27</option>	
			<option>28</option>	
			<option id="29" hidden="true">29</option>	
			<option id="30" hidden="true">30</option>	
			<option id="31" hidden="true">31</option>	
		</select>   
		<input type="submit" value="Submit" />
	</form>
	
	<%
	User user = (User)session.getAttribute("user");
	if (user != null && "pharmacist".equalsIgnoreCase(user.getRole())){
	%>
	<a href="/apotheca/secure/createDrug.jsp">create drug</a>
	<%
	}
	%>
</body>
</html>