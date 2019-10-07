<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="/MockitosHaven/main.css">
</head>
<body>
	<%@ include file="header.jsp"%>
	<a href="lobby"><h1 class="title textGlow centered">Welcome to
			the Mockito's Haven!</h1></a>
	<div class="flexRow fullWidth goldShadow">
		<div class="goldShadow halfWidth">
			<h2 class="textGlow title">Update news!</h2>
			<div class="flexColumn">
				<div>
					<p class="textGlow title">News 1</p>
					<p>In the latest news, this exists and has a few working
						features!</p>
				</div>
				<div>
					<p class="textGlow title">News 2</p>
					<p>Before that, it didn't have so many features.</p>
				</div>
				<div>
					<p class="textGlow title">News 3</p>
					<p>Before that, it didn't exist!</p>
				</div>
			</div>
		</div>
		<div class="goldShadow halfWidth">
			<h2 class="textGlow title">Contributors:</h2>
			<div class="flexColumn">
				<p>Daniel Klingensmith</p>
				<p>George Hall</p>
				<p>Holden Wright</p>
				<p>Alexander Perrotti</p>
				<p>Franco Savariyar</p>
			</div>
		</div>
	</div>
	<div class="fullWidth flexCenter">
		<h2 class="textGlow title">Future features:</h2>
		<p>-multiplayer</p>
		<p>-actually having a game</p>
	</div>
</body>
</html>