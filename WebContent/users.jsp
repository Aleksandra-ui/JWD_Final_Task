<ul>
	<c:forEach var="u" items="${users }">
		<li>name: ${u.name} | password: ${u.password} | role: ${u.role.name }</li>				
	</c:forEach>
</ul> 
