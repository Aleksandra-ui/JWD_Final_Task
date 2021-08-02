<ul>
	<c:forEach var="u" items="${users }">
		<li>name: ${u.name} | role: ${u.role.name }</li>				
	</c:forEach>
</ul> 
