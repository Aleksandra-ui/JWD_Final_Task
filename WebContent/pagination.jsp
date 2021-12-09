<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.OrderManagerService,com.epam.jwd.apotheca.model.Order,java.util.List,java.util.Map,java.util.HashMap,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.controller.DrugManagerService,
	com.epam.jwd.apotheca.controller.action.Orders, java.util.ResourceBundle"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<style>
	#popUpNavigate{
		position: relative;
		left: 23px;
	}
</style>

<c:set var="sortCol" value="${empty param.sortColumn ? '' : 'sortColumn=name' }"/>
<c:set var="order" value="${empty param.sortOrder ? '' : (param.sortOrder eq 'asc' ? 'sortOrder=asc' : 'sortOrder=desc') }"/>

<c:if test='${not empty sortCol }'><c:set var="sortLine" value="&${sortCol }&${order}"/></c:if>

<script type="text/javascript">

canNavigate = false;

	function changePageSize (select) {
		
		return select.options[select.selectedIndex].value && (window.location = select.options[select.selectedIndex].value); 
	
	}
	
	function changeCurrentPage ( input, baseURL) {
	
		if ( canNavigate) {
			console.log(input.value);
			url = baseURL + "?pageSize=" + ${empty action.pageSize ? 5 : action.pageSize} + "&currentPage=" + input.value;
			url = url + "&${sortLine }";
			console.log(url);
			return isPageValid(input.value) && (window.location = url); 
		}
	}
	
	function isPageValid (page) {
		
		return page ? true : false; 
	
	}
	
	function showHideInput(popUpId, goButton, registerFunction) {
		var div = document.getElementById(popUpId);
		if ( div.style.visibility=="hidden" ) {
			div.style.visibility="visible";
			document.getElementById(goButton).addEventListener("click", registerFunction);
		} else {
			div.style.visibility="hidden";
			document.getElementById(goButton).removeEventListener("click", registerFunction);
		}
	}
	
	
	function registerEvent(e) {

		canNavigate = true;
		myInput = document.getElementById("goToPage");
		changeCurrentPage(myInput, '${baseURL}');

	}
	
	function registerCartEvent(e) {

		canNavigate = true;
		myInput = document.getElementById("goToCartPage");
		changeDynamicPage(myInput);

	}
	
	function changeDynamicPage (input) {
		console.log(input.value);
		cartPageSize = document.getElementById("cartPageSize");
		return isDynamicPageValid(input.value) &&
				displayCart(input.value, ${empty cartPageSize ? 5 : cartPageSize.options[cartPageSize.selectedIndex].value}); 
	
	}
	

	
	function isDynamicPageValid (page) {
		
		return page ? true : false; 
	
	}
	

</script>

<%
ResourceBundle rbp = ResourceBundle.getBundle("properties/Pagination", locale);
%>
	
<%-- 	<c:if test="${action.pagesCount > 3 }"> --%>
		<div id="popUpNavigate" style="visibility: hidden; float: right; margin-right: 25px; margin-bottom: 10px;">
			<input type="number" min="1" max="${action.pagesCount }" id="goToPage" value="${action.currentPage }" />
			<button  id="navigateButton" style="display: block; margin-left: 15px; float: right;"><%= rbp.getString("pagination.go2") %></button>  
		</div>
		<script>
			document.getElementById("navigateButton").addEventListener("click", registerEvent);
		</script>
<%-- 	</c:if> --%>
	
<!-- <div  class="container"> -->
	<div style="overflow: hidden"  class="container" align="center">
		<div style="float: left">
			<%=rbp.getString("pagination.items1")%>
			<%=currentPage * pageSize - pageSize + 1%>
			<%=rbp.getString("pagination.items2")%>
			<%=currentPage * pageSize - pageSize + 1 + ((totalCount % pageSize != 0 && totalCount / pageSize * pageSize + 1 == currentPage * pageSize - pageSize + 1)
					? totalCount % pageSize : pageSize) - 1%>
			<%=rbp.getString("pagination.items3")%>
			${action.totalCount}
		</div>
		<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<div style="float: right">
			<div style="float: left">
				<%=rbp.getString("pagination.items4")%> 
				<select name="pageSize"
				onChange="changePageSize(this);">
					<option
					${(empty action.pageSize or action.pageSize == 5) ? "selected='true'" : "" }
					value="${baseURL }?pageSize=5${sortLine}">5</option>
					<option
					${(not empty action.pageSize and action.pageSize  == 10) ? "selected='true'" : "" }
					value="${baseURL }?pageSize=10${sortLine}">10</option>
					<option
					${(not empty action.pageSize and action.pageSize  == 20) ? "selected='true'" : "" }
					value="${baseURL }?pageSize=20${sortLine}">20</option>
				</select>
			</div>
			<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<div style="float: right">

				<%--Button "previous", shown when current page > 1--%>
				<c:if test="${action.currentPage != 1 }">
					<span>
						<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${action.currentPage - 1}${sortLine}">&lt;</a>&nbsp;
					</span>
				</c:if>
					
				<%--First page--%>
				
				<c:choose>
					<c:when test="${empty action.currentPage or action.currentPage eq 1 }">
						1&nbsp;
					</c:when>
					<c:otherwise>
						<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=1${sortLine}">1</a>&nbsp;
					</c:otherwise>
				</c:choose>
				
				<%--spacer after first page --%>
				<c:if test="${not (empty action.currentPage or action.currentPage < 4) }">
					<span>...&nbsp;</span>
				</c:if>
				
				<%--previous page --%>
				<c:if test="${(not empty action.currentPage) and (action.currentPage > 2) and (action.pagesCount > 3) }">
					<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${action.currentPage - 1}${sortLine}">${action.currentPage - 1}</a> &nbsp;
				</c:if>
				
				<%--current page --%>
				<c:if test="${(not empty action.currentPage) and ( action.currentPage > 1 ) and (action.currentPage < action.pagesCount)}">
					${action.currentPage} &nbsp;
				</c:if>
				
				<%--next page --%>
				<c:if test="${(not empty action.currentPage) and (action.currentPage < action.pagesCount - 1) and (action.pagesCount > 3) }">
					<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${action.currentPage + 1}${sortLine}">${action.currentPage + 1}</a> &nbsp;
				</c:if>
				
				<%--spacer before last page --%>
				<c:if test="${action.currentPage < action.pagesCount - 2 }">
					<span>...&nbsp;</span>
				</c:if>
				
				<%--Button "last" --%>
				<c:if test="${ action.pagesCount > 1 }">
					<c:choose>
						<c:when test="${not empty action.currentPage and (action.currentPage eq action.pagesCount) }">
							${ action.pagesCount}&nbsp;
						</c:when>
						<c:otherwise>
							<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${ action.pagesCount}${sortLine}">${ action.pagesCount}</a> &nbsp;
						</c:otherwise>
					</c:choose>
				</c:if>
				
				<%--Button "next", shown when current page < total pages count--%>
				<c:if test="${(empty action.currentPage and action.pagesCount > 1 ) or (action.currentPage < action.pagesCount) }">
					<span>
						<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${action.currentPage + 1}${sortLine}">&gt;</a> &nbsp;
					</span>
				</c:if>
				
				<c:if test="${action.pagesCount > 3 }">
					<button id="pageButton" onclick="showHideInput('popUpNavigate', 'navigateButton', registerEvent);"><%=rbp.getString("pagination.go1")%></button>
				</c:if>
				
			</div>
		</div>
	</div>
<!-- </div> -->