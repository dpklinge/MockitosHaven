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
	<script>
	$(document).click(function(event) { 
		  $target = $(event.target);
		  if(!$target.closest('#menuButton').length) {
			hideDropDown();
		  }        
		});
	
	function displayDropDown(){
		console.log('Show');
			let dropDown = $('#menu');
			if($(dropDown).css('display')=='none'){
				let menuIcon = $('#menuSquare');
				$(dropDown).css('display','flex');
				$(menuIcon).css('background-color', 'burlywood');
				$(menuIcon).css('color', 'black');
				$(menuIcon).css('box-shadow', '0 0 10px 3px NavajoWhite');
				
			}else{
				hideDropDown();
				}
		}
		function hideDropDown(){
			console.log('Hide');
			
			let dropDown = $('#menu');
			
			if($(dropDown).css('display')=='flex'){
				let menuIcon = $('#menuSquare');
				$(dropDown).css('display','none');
				$(menuIcon).css('background-color', 'black');
				$(menuIcon).css('color', 'burlywood');
				$(menuIcon).css('box-shadow', '');
			}
		}
	</script>
</head>
<body>
<header class="flexRow spaceApart">
	<div>
		<img>
	</div>
	<h1 class="title blackGlow center">Mockito's Haven</h1>
	<div class="flexRow">
		<div class="flexColumn" id="menuButton" onclick="displayDropDown()">
			<p id="menuSquare">Menu</p>
			<div id="menu" class="flexColumn">
			<a href="/MockitosHaven/home" class="menuItem"><p>Home</p></a>
			<a href="/MockitosHaven/login" class="menuItem"><p>Login</p></a>
			<a href="/MockitosHaven/lobby" class="menuItem"><p>Lobby</p></a>
			</div>
		</div>
		
	</div>
</header>

	

</body>
</html>