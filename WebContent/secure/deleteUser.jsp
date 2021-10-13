<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User deletion</title>
</head>
<body>
	<c:choose>
		<c:when test="${action.deleted}">
			User ${action.userToDelete.name } was deleted.
		</c:when>
		<c:otherwise>
			User ${action.userToDelete.name } can't be deleted.
		</c:otherwise>
	</c:choose>
</body>
</html>