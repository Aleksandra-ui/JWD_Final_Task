<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.epam.jwd.apotheca.model.Drug, java.util.ResourceBundle, java.util.List, java.util.Locale,
    com.epam.jwd.apotheca.controller.action.RecipeCartAction"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%
	Locale locale = null;
	if (locale == null ) {
		locale = new Locale("en", "US");
	}
	ResourceBundle rb = ResourceBundle.getBundle("Drugs", locale);
%>

<style>
	input.error{
		background-color: #FFAAAA;
	}
</style>

<script type="text/javascript">
	
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

</script>

<body onload="fillDaySelect();">

	<%
		RecipeCartAction bean = (RecipeCartAction)request.getAttribute("action");
	
		Integer totalCount = bean.getTotalCount();
		int pageSize = bean.getPageSize();
		int currentPage = bean.getCurrentPage();
		
	%>

	<c:if test="${(not empty action.cart) and (not empty action.drugs) }">
		
		<div style="width:50%" class="container">
			<div style="overflow: hidden" class="container" align="center">
				<span style="align-content: center; align-self: center;">Current recipe</span>
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
					<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<div style="float: right">
						<div style="float: left">
							<%=rb.getString("drugs.records4")%>:&nbsp;<select name="pageSize"
							onChange="displayCart( 1, this.options[this.selectedIndex].value );">
								<option
								${(empty param.pageSize or param.pageSize == 5) ? "selected='true'" : "" }
								value="5">5</option>
								<option
								${(not empty param.pageSize and param.pageSize  == 10) ? "selected='true'" : "" }
								value="10">10</option>
								<option
								${(not empty param.pageSize and param.pageSize  == 20) ? "selected='true'" : "" }
								value="20">20</option>
							</select>
						</div>
						<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
						<div style="float: right">
							<%
							Integer pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
							request.setAttribute("pagesCount", pagesCount);
							%>
							<c:forEach var="displayPage" begin="1" end="${pagesCount}">
								<c:choose>
									<c:when
										test="${displayPage == (empty param.currentPage ? 1 : param.currentPage)}">${displayPage} &nbsp;</c:when>
									<c:otherwise>
										<a onclick="displayCart(${displayPage}, ${action.pageSize })" style="text-decoration: underline;">${displayPage}</a>&nbsp;
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
			
			<table border = "1" style="width:50%; margin-top: 20px" class="container" align="center">
				<thead align="center">
					<tr>
						<th>#</th>
						<th><%=rb.getString("drugs.name")%></th>
						<th><%=rb.getString("drugs.dose")%></th>
						<th><%=rb.getString("drugs.price")%></th>
						<th><%=rb.getString("drugs.cart")%></th>
					</tr>
				</thead>
	
				<tbody align="center" <c:if test="${not empty action.errors['user'] }">
									  	   bgcolor="LightPink" title="${action.errors['user'] }"
									  </c:if>
									  <c:if test="${not empty action.errors['date'] }">
									  	   bgcolor="LightPink" title="${action.errors['date'] }"
									  </c:if>
				>
					
					<c:choose>
						<c:when test="${not empty action.cart}">
							<c:forEach items="${action.drugs}" var="d">
								
								<c:set var="present" value="false"/>
								<c:forEach items="${action.invalidDrugs}" var="i">
									<c:if test="${i eq d }"><c:set var="present" value="true"/></c:if>
								</c:forEach>
								
								<tr
									<c:if test="${present }">
											  bgcolor="LightPink" title="this drug now does not require prescription"
									</c:if>
								>
									<td><c:out value="${d.id}" /></td>
									<td><c:out value="${d.name}" /></td>
									<td><c:out value="${d.dose }" /></td>
									<td><c:out value="${d.price }" /></td>
									<td><a onclick="updateRecipeCart(${d.id}, false);" style="text-decoration: underline;">remove</a></td>
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
			
			<div id = "errorStatus" class="container" align="center"></div>
		
		${action.errors }
			<form action="createRecipe.run" method="POST" class="container" align="center" style="margin-top: 20px">
				<div id = "div" <c:if test="${empty action.cart.drugs}">style="display:none"</c:if>>
					<input class="btn btn-primary" type="submit" <c:if test="${action.cart.invalid }">disabled</c:if> value="<%=rb.getString("drugs.recipe")%>"/>
					<select name="clientName">
					<c:forEach items="${action.clients}" var="client">
						<option>
							<c:out value="${client.name }"/>
						</option>
					</c:forEach>
					</select>
					<select id="Year" name="year" onchange="fillDaySelect()">
					<c:forEach begin="2022" end="2024" var="a">
						<option id="${a }">${a }</option>
					</c:forEach>
					</select> 
					<select id="Month" name="month" onchange="fillDaySelect()" >
						<option id="january"  value="01">january</option>
						<option id="february"  value="02">february </option>
						<option id="march"  value="03">march</option>
						<option id="april"  value="04">april</option>	
						<option id="may"  value="05">may</option>	
						<option id="june"  value="06">june</option>	
						<option id="july"  value="07">july</option>	
						<option id="august"  value="08">august</option>	
						<option id="september"  value="09">september</option>	
						<option id="october"  value="10">october</option>	
						<option id="november"  value="11">november</option>	
						<option id="december" value="12">december</option>	
					</select> 
					<select id="Day" name="day" >
						<c:forEach var="d" begin="1" end="28">
							<option>${(d lt 10) ? '0' : ''}${d}</option>  	
						</c:forEach>
						<option id="29" hidden="true">29</option>	
						<option id="30" hidden="true">30</option>	
						<option id="31" hidden="true">31</option>	
					</select>   
				</div>
			</form>
		
		</c:if>
		
</body>