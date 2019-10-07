<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="/MockitosHaven/main.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="/MockitosHaven/webjars/jquery/jquery.min.js"></script>
<script src="/MockitosHaven/webjars/sockjs-client/sockjs.min.js"></script>
<script src="/MockitosHaven/webjars/stomp-websocket/stomp.min.js"></script>
<script src="/MockitosHaven/game.js"></script>
<script src="/MockitosHaven/DynamicMap.js"></script>
<script src="/MockitosHaven/Websocket.js"></script>

</head>
<body onload="connect(); loadCharacter(); loadTileMap(); ">
	<div id="game">
		<canvas id="gameCanvas" width="800" height="800"></canvas>
		<p id="tileMap" class="hidden">${ tileMap }</p>
		<p id="character" class="hidden">${ character }</p>
		<p id="gameJson" class="hidden">${ gameJson }</p>
		<p id="movement" class="hidden"></p>
	</div>
	<div id="killScreen" class="centeredContent">
		<p id="killScreenStats"></p>
		<a href="/MockitosHaven/lobby"><button>Return to lobby</button></a>
	</div>
	<div id="stats" class="centeredContent goldShadow">
		<p><span id="name"></span></p>
		<p>HP: <span id="health"></span></p>
		<p>MP: <span id="mana"></span></p>
		<p>SPD: <span id="speed"></span></p>
		<p>STR: <span id="strength"></span></p>
		<p>OBS: <span id="observation"></span></p>
		<p>LCK: <span id="luck"></span>
		<p>INT: <span id="intelligence"></span></p>
		<p>DEX: <span id="dexterity"></span></p>
		<p>LVL: <span id="level"></span></p>
		<p>EXP: <span id="exp"></span>/<span id="expToGo"></span></p>
	</div>
	<div id="inventory" class="centeredContent goldShadow">
	
	</div>
</body>
</html>