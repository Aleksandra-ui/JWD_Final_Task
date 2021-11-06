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
	.error{
		background-color: #FFAAAA;
	}
</style>



<body onload="fillDaySelect();">

	<%
			RecipeCartAction bean = (RecipeCartAction)request.getAttribute("action");
		
			Integer totalCount = bean.getTotalCount();
			int pageSize = bean.getPageSize();
			int currentPage = bean.getCurrentPage();
	%>

	<c:if test="${(not empty action.errors) and (not empty action.errors['access']) }">
		<div class="error">You are not allowed to view this section. Your role is '${action.errors['access'] }', while 'doctor' is required.</div>
	</c:if>

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
							<c:forEach var="displayPage" begin="1" end="${action.pagesCount}">
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
		
			<form action="createRecipe.run" method="POST" class="container" align="center" style="margin-top: 20px">
				<div id = "div" <c:if test="${empty action.cart.drugs}">style="display:none"</c:if>>
					<input class="btn btn-primary" type="submit" <c:if test="${action.cart.invalid }">disabled</c:if> value="<%=rb.getString("drugs.recipe")%>"/>
					<select id="clientId" name="clientId" onchange="updateUser(this.value);">
						<c:if test="${empty action.cart.userId }"><option/></c:if>
						<c:forEach items="${action.clients}" var="client">
							<option value="${client.id }"
							<c:if test="${action.cart.userId eq client.id }">selected</c:if>
							>${client.name }</option>
						</c:forEach>
					</select>
					${action.cart.year} ${ action.errors } ${action.cart.month } ${action.cart.day}
					<select id="Year" name="year" onchange="setExpieryDate()">
					<c:forEach begin="2021" end="2024" var="a">
						<option id="${a }" value="${a }" <c:if test="${action.cart.year eq a }">selected</c:if>>${a }</option>
					</c:forEach>
					</select> 
					<select id="Month" name="month" onchange="setExpieryDate()" >
						<c:forEach var="m" items="${action.monthNames }">
							<option id="month${m.key }" value="${m.key}"
							<c:if test="${action.cart.month eq m.key  }">selected</c:if>
							>${m.value }</option>	
						</c:forEach>
					</select> 
					<select id="Day" name="day" onchange="setExpieryDate();">
						<c:forEach var="d" begin="1" end="28">
							<option value="${d }" 
							<c:if test="${action.cart.day eq ( ((d lt 10) ? '0' : '')+d) }">selected</c:if>
							>${(d lt 10) ? '0' : ''}${d}</option>  	
						</c:forEach>
						<option id="29" hidden="true" value="29" 
						<c:if test="${action.cart.day eq 29 }">selected</c:if>
						>29</option>	
						<option id="30" hidden="true" value="30"
						<c:if test="${action.cart.day eq 30 }">selected</c:if>
						>30</option>	
						<option id="31" hidden="true" value="31"
						<c:if test="${action.cart.day eq 31 }">selected</c:if>
						>31</option>	
					</select>   
				</div>
			</form>
		
		</c:if>
		
</body>