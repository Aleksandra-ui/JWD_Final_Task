<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.epam.jwd.apotheca.controller.OrderManagerService,com.epam.jwd.apotheca.model.Order,java.util.List,java.util.Map,java.util.HashMap,com.epam.jwd.apotheca.model.Drug,com.epam.jwd.apotheca.controller.DrugManagerService,
	com.epam.jwd.apotheca.controller.action.Orders"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script type="text/javascript">

	function changePageSize (select) {
		
		return select.options[select.selectedIndex].value && (window.location = select.options[select.selectedIndex].value); 
	
	}
	
	function changeCurrentPage (input, baseURL) {
		console.log(input.value);
		url = baseURL + "?pageSize=" + ${empty action.pageSize ? 5 : action.pageSize} + "&currentPage=" + input.value;
		console.log(url);
		return isPageValid(input.value) && (window.location = url); 
	
	}
	
	function isPageValid (page) {
		
		return page ? true : false; 
	
	}

</script>
	
<div style="width:50%" class="container">
	<div style="overflow: hidden" class="container" align="center">
		<div style="float: left">
			items from&nbsp;
			<%=currentPage * pageSize - pageSize + 1%>
			to
			<%=currentPage * pageSize - pageSize + 1 + ((totalCount % pageSize != 0 && totalCount / pageSize * pageSize + 1 == currentPage * pageSize - pageSize + 1)
					? totalCount % pageSize : pageSize) - 1%>
			of&nbsp;
			${action.totalCount}
		</div>
		<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<div style="float: right">
			<div style="float: left">
				items per page:&nbsp; <select name="pageSize"
				onChange="changePageSize(this);">
					<option
					${(empty action.pageSize or action.pageSize == 5) ? "selected='true'" : "" }
					value="${baseURL }?pageSize=5">5</option>
					<option
					${(not empty action.pageSize and action.pageSize  == 10) ? "selected='true'" : "" }
					value="${baseURL }?pageSize=10">10</option>
					<option
					${(not empty action.pageSize and action.pageSize  == 20) ? "selected='true'" : "" }
					value="${baseURL }?pageSize=20">20</option>
				</select>
			</div>
			<span style="float: none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<div style="float: right">

				<%--Button "previous", shown when current page > 1--%>
				<c:if test="${action.currentPage != 1 }">
					<span>
						<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${action.currentPage - 1}">&lt;</a>&nbsp;
					</span>
				</c:if>
					
				<%--First page--%>
				
				<c:choose>
					<c:when test="${empty action.currentPage or action.currentPage eq 1 }">
						1&nbsp;
					</c:when>
					<c:otherwise>
						<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=1">1</a>&nbsp;
					</c:otherwise>
				</c:choose>
				
				<%--spacer after first page --%>
				<c:if test="${not (empty action.currentPage or action.currentPage < 4) }">
					<span>...&nbsp;</span>
				</c:if>
				
				<%--previous page --%>
				<c:if test="${(not empty action.currentPage) and (action.currentPage > 2) and (action.pagesCount > 3) }">
					<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${action.currentPage - 1}">${action.currentPage - 1}</a> &nbsp;
				</c:if>
				
				<%--current page --%>
				<c:if test="${(not empty action.currentPage) and ( action.currentPage > 1 ) and (action.currentPage < action.pagesCount)}">
					${action.currentPage} &nbsp;
				</c:if>
				
				<%--next page --%>
				<c:if test="${(not empty action.currentPage) and (action.currentPage < action.pagesCount - 1) and (action.pagesCount > 3) }">
					<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${action.currentPage + 1}">${action.currentPage + 1}</a> &nbsp;
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
							<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${ action.pagesCount}">${ action.pagesCount}</a> &nbsp;
						</c:otherwise>
					</c:choose>
				</c:if>
				
				<%--Button "next", shown when current page < total pages count--%>
				<c:if test="${(empty action.currentPage and action.pagesCount > 1 ) or (action.currentPage < action.pagesCount) }">
					<span>
						<a href="${baseURL }?pageSize=${empty action.pageSize ? 5 : action.pageSize}&currentPage=${action.currentPage + 1}">&gt;</a> &nbsp;
					</span>
				</c:if>
				
				<c:if test="${action.pagesCount > 3 }">
					<input type="number" min="1" max="${action.pagesCount }" id="goToPage" onkeyup="changeCurrentPage(this, '${baseURL}');" value="${action.currentPage }" />
				</c:if>
				
			</div>
		</div>
	</div>
</div>