var map;
var context;
var charImage;
var characterId;
var displayName;
var tileSize = 50;
var width;
var height;
var movementBuildupSpeed=4*tileSize;
var locatables=new Map();
var canMove = true;




function setScreenDimensions(){
	width = window.innerWidth+tileSize;
	height = window.innerHeight+tileSize;
	let canvas = document.getElementById('gameCanvas');
	canvas.setAttribute("width", width);
	canvas.setAttribute("height", height);
}

function loadCharacter(){
	let character = JSON.parse(document.getElementById('character').innerHTML);
	characterId=character.identifier;
	while(character.displayName==null||character.displayName==""){
		character.displayName=window.prompt("Welcome to the dungeon! Please enter your character's name.","");
	}
	updateInventory(character);
	$.ajax({
		type:"POST",
		url:"/MockitosHaven/changeName/"+thisGame.name,
		dataType: 'json',
	    contentType: "application/json",
		data:JSON.stringify(character)});
	updateCharacter(character);
	
	charImage = new Image(tileSize, tileSize);
	charImage.src='/MockitosHaven/images/'+character.imageName;
	
	
	charTileX = character.coordinate.x;
	charTileY = character.coordinate.y;
	tileMapId = character.coordinate.mapId;
	//console.log(character);
}

function loadTileMap(){
	
	setScreenDimensions();
	if(context==null){
		context = document.getElementById('gameCanvas').getContext('2d');
	}
	map = JSON.parse(document.getElementById('tileMap').innerHTML);
	Game.run(context);
	xOffset=Math.floor(Math.floor(width/(tileSize)/2));
	yOffset=Math.floor(Math.floor(height/(tileSize)/2));
	locatables=new Map();
	for (let i = 0;i< map.allLocatables.length;i++){
		if(map.allLocatables[i].hasOwnProperty('imageName')){
			locatables.set(map.allLocatables[i].identifier,map.allLocatables[i])
			Loader.loadImage(map.allLocatables[i].identifier, '/MockitosHaven/images/'+map.allLocatables[i].imageName);
		}
	}
}

Game.init = function() {
	 Keyboard.listenForEvents(
		        [Keyboard.GET,
		        Keyboard.LEFT,
		        Keyboard.NUMLEFT,
		        Keyboard.RIGHT,
		        Keyboard.NUMRIGHT,
		        Keyboard.UP,
		        Keyboard.NUMUP,
		        Keyboard.DOWN,
		        Keyboard.NUMDOWN,
		        Keyboard.NUMDOWNLEFT,
		        Keyboard.NUMDOWNRIGHT,
		        Keyboard.NUMUPLEFT,
		        Keyboard.NUMUPRIGHT, 
		        Keyboard.DOWNSTAIRS,
		        Keyboard.UPSTAIRS]);
		    this.tileAtlas = Loader.getImage('tiles');
		    setScreenDimensions();
}


function getTileNumber(envType, tileType){
	let num=1;
	if(tileType!="OPEN"){
		num++;
	}
	if(envType=="ROCK"){
		num+=2;
	}
	return num;
}

Game.load = function() {
	return [ Loader.loadImage('tiles', '/MockitosHaven/tiles/basicTiles.jpg') ];
};

Game.render = function () {
	context.clearRect( 0 , 0 , width , height );
	renderMap(this);

};


function renderLocatables(x, y, xTranslate, yTranslate, game){
	let renderQueue=[];
	locatables.forEach(function(value, key, map){
		if(value.coordinate.x == x && value.coordinate.y == y){
			if(value.hasOwnProperty("amount")){
				renderQueue.unshift(value);
			}else{
				renderQueue.push(value);
			}
			
		}
	});
	for(let i=0;i<renderQueue.length;i++){
		game.ctx.drawImage(Loader.getImage(renderQueue[i].identifier),
				   0,
				   0,
				   tileSize,
				   tileSize,
				   xTranslate,
				   yTranslate, 
				   tileSize,
				   tileSize);
		if(renderQueue[i])
	}
				

}

function renderMap(game){
	let chara = locatables.get(characterId);
	for(var c=chara.coordinate.x-xOffset; c<=chara.coordinate.x+xOffset; c++){
		if(c>=0 && c<map.tileMap.length){
			for (var r = chara.coordinate.y-yOffset; r <= chara.coordinate.y+yOffset; r++) {
				if(r>=0 && r<map.tileMap[0].length){
					let tileSourceX = getTileSourceX(map.tileMap[c][r]);
					let tileSourceY = getTileSourceY(map.tileMap[c][r]);
					let xTranslate = coordinateToPixels(c-chara.coordinate.x+xOffset);
					let yTranslate = coordinateToPixels(r-chara.coordinate.y+yOffset);
					game.ctx.drawImage(game.tileAtlas, // image
							tileSourceX*tileSize, // source x
							tileSourceY*tileSize, // source y
							tileSize, // source width
							tileSize, // source height
							xTranslate, // target x
							yTranslate, // target y
							tileSize, // target width
							tileSize // target height
							);
					renderLocatables(c, r, xTranslate, yTranslate, game);
					
				}
			}
		}
	}
}
function getTileSourceX(tile){
	if(tile.tileType=="OPEN"){
		return 0;
	}else if(tile.tileType=="WALL"){
		return 1;
	}else if(tile.tileType=="DOWNSTAIRS"){
		return 2;
	}else if(tile.tileType=="UPSTAIRS"){
		return 3;
	}
}
function getTileSourceY(tile){
	if(tile.envType=="DIRT"){
		return 0;
	}else if(tile.envType=="ROCK"){
		return 1;
	}
}
function coordinateToPixels(x){
	let xPixels = x*tileSize;
	return xPixels;
}

Game.update = function (delta) {
    var dirx = 0;
    var diry = 0;
    if (Keyboard.isDown(Keyboard.LEFT)) { dirx = -1; }
    if (Keyboard.isDown(Keyboard.NUMLEFT)) { dirx = -1; }
    if (Keyboard.isDown(Keyboard.RIGHT)) { dirx = 1; }
    if (Keyboard.isDown(Keyboard.NUMRIGHT)) { dirx = 1; }
    if (Keyboard.isDown(Keyboard.UP)) { diry = -1; }
    if (Keyboard.isDown(Keyboard.NUMUP)) { diry = -1; }
    if (Keyboard.isDown(Keyboard.DOWN)) { diry = 1; }
    if (Keyboard.isDown(Keyboard.NUMDOWN)) { diry = 1; }
    if (Keyboard.isDown(Keyboard.NUMUPLEFT)) { diry = -1; dirx=-1; }
    if (Keyboard.isDown(Keyboard.NUMUPRIGHT)) { diry = -1; dirx=1; }
    if (Keyboard.isDown(Keyboard.NUMDOWNLEFT)) { diry = 1; dirx=-1; }
    if (Keyboard.isDown(Keyboard.NUMDOWNRIGHT)) { diry = 1; dirx=1; }
    

    move(dirx, diry);
};

function move ( dirx, diry) {
	if(canMove&&(dirx!=0 || diry!=0)){
		attemptMove(dirx, diry);
		canMove=false;
		window.setTimeout(function(){canMove=true;}, locatables.get(characterId).actionDelay*1000)
	}
};

function attemptMove(dirx, diry){
	let chara = locatables.get(characterId);
	//console.log("Attempting move to: "+dirx+" "+diry)
	let targetX = chara.coordinate.x;
	let targetY = chara.coordinate.y;
		targetX += dirx;
		targetY += diry;
	let attemptedMovement=fillMovement(targetX, targetY, chara);
	$.ajax({
		type:"POST",
		url:"/MockitosHaven/attemptMove",
		dataType: 'json',
	    contentType: "application/json",
		data:JSON.stringify(attemptedMovement),
		success:function(data){
			//console.log("Successful ajax movement:"+data)
			return data;
		}	
	});
}

function fillMovement(x, y, character){
	let movement={
			character:character,
			target:{
				x:x,
				y:y,
				mapId:map.id
			},
			tileMap:map	
	};
	return movement;
}
