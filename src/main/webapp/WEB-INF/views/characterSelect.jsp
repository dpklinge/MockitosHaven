<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
	function submit(form){
		form.submit();
	}

</script>
<%@ page import="com.fdmgroup.mockitoshaven.game.character.CharacterClass" %>
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="flexRow">
		<c:set var="classes" value="<%=CharacterClass.values()%>"/>
		<c:forEach var="clazz" items="${ classes }">
			<form onclick="submit(this)" class="characterSelect flexColumn goldShadow" action="/MockitosHaven/characterSelect/${ currentGame.name }"
				method="POST">
				<p>${ clazz.name }</p>
				<img src="/MockitosHaven/images/${ clazz.imageName }"/>
				<p>${ clazz.description }</p>
				<input type="text" class="hidden" name="clazz" value="${ clazz }"/>	
			</form>
		</c:forEach>
	</div>
</body>
</html>