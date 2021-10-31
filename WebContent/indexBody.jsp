<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.text.SimpleDateFormat, java.util.*"%>
<div align="center"><%=rb.getString("index.page")%></div> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">

function fillDaySelect() {
	
	//alert("select");
	var year = document.getElementById('Year');
	var month = document.getElementById('Month');
	var day = document.getElementById('Day');
	var yearOpt = year.options[year.selectedIndex];
	var monthOpt = month.options[month.selectedIndex];
	console.log(monthOpt);
	if (monthOpt.id == '02'){
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
			if ( (monthOpt.value < "08" && Number(monthOpt.value) % 2 == 1)||(monthOpt.value >= "08" && Number(monthOpt.value) % 2 == 0) ) {
				dayOpt.hidden = false;
			} else {
				dayOpt.hidden = true;
			}
	}
}

function setExpieryDate(){}

</script>
	
<div align="center">	
	<%
		String expieryDate = "2020" + "/" + "2" + "/" + "29";
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		format.setLenient(false);
		java.util.Date date = format.parse(expieryDate);
		out.print(date);
	%>
</div>
<div align="center"><p><%=rb.getString("index.welcome")%></p></div>
<select id="Year" name="year" onchange="fillDaySelect();setExpieryDate()">
	<c:forEach begin="2021" end="2024" var="a">
		<option id="${a }" value="${a }" <c:if test="${action.cart.year eq a }">selected</c:if>>${a }</option>
	</c:forEach>
</select> 
<%

Map<String, String> sortedMonthNames = new TreeMap<String, String>();

Map<String, Integer> monthNames = GregorianCalendar.getInstance().getDisplayNames(Calendar.MONTH, Calendar.LONG, locale);

for ( Map.Entry<String, Integer> e : monthNames.entrySet() ) {
	int v = e.getValue() + 1;
	sortedMonthNames.put( v < 10 ? ("0" + v) : String.valueOf(v),
			              e.getKey().toLowerCase() );
}
 
request.setAttribute("monthNames", sortedMonthNames);
request.setAttribute("year", "2022");
request.setAttribute("month", "02");
request.setAttribute("day", "13");
%>
<select id="Month" name="month" onchange="fillDaySelect();setExpieryDate()" >
	<c:forEach var="m" items="${monthNames }">
		<option id="${m.key }" value="${m.key}"
		<c:if test="${month eq m.key  }">selected</c:if>
		>${m.value }</option>	
	</c:forEach>
</select> 
<select id="Day" name="day" onchange="setExpieryDate();">
	<c:forEach var="d" begin="1" end="28">
		<option value="${d }" 
		<c:if test="${day eq d }">selected</c:if>
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