<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="/MockitosHaven/main.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script>
	var previousFocus = null;
	function openPasswordBox(gameId) {
		if (previousFocus != null) {
			$('#' + previousFocus + 'password').css("display", "none");
			$('#' + previousFocus + 'submit').css("display", "none");
		}
		previousFocus = gameId
		$('#' + gameId + 'password').css("display", "block");
		$('#' + gameId + 'submit').css("display", "block");
	}
	function joinGame(gameId) {

		let pass = $('#' + gameId + 'password').val();
		$.ajax({
			url : "test/" + gameId + "&" + pass,
			success : function(result) {
				if (result == "valid") {
					window.location.href = "game/" + gameId;
				} else {
					alert("That password was not correct!")
				}
			}

		});
	}
</script>
</head>
<body>
	<%@ include file="header.jsp"%>
	<form action="createGame" method="POST">
		<p>${ error }</p>
		<p>Create new game</p>
		<p>Game name:</p>
		<input type="text" name="name">
		<p>Game password:</p>
		<input type="password" name="password"> <input type="submit"
			value="submit">
	</form>
	<br />
	<h1>Active games:</h1>
	<div id="games">
		<c:forEach var="game" items="${ games }">
			<div class="centeredContent column ">
				<p id="${ game.name }" class="textGlow"
					onclick="openPasswordBox('${ game.name }')">${ game.name }</p>
				<input type="password" class="hidden quarterwide" id="${ game.name }password"
					placeholder="Game password" autofocus>
				<p></p>
				 <input type="submit"
					class="hidden quarterwide" onclick="joinGame('${ game.name }')"
					id="${ game.name }submit">
			</div>
		</c:forEach>

	</div>
</body>
</html>