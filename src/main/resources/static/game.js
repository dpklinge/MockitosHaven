var Loader = {
	images : {}
};

Loader.loadImage = function(key, src) {
	var img = new Image();

	var d = new Promise(function(resolve, reject) {
		img.onload = function() {
			this.images[key] = img;
			resolve(img);
		}.bind(this);

		img.onerror = function() {
			reject('Could not load image: ' + src);
		};
	}.bind(this));

	img.src = src;
	return d;
};

Loader.getImage = function(key) {
	return (key in this.images) ? this.images[key] : null;
};

//
// Keyboard handler
//

var Keyboard = {};

Keyboard.GET = 71;
Keyboard.LEFT = 37;
Keyboard.NUMLEFT = 100;
Keyboard.RIGHT = 39;
Keyboard.NUMRIGHT = 102;
Keyboard.UP = 38;
Keyboard.NUMUP = 104;
Keyboard.DOWN = 40;
Keyboard.NUMDOWN = 98;
Keyboard.NUMDOWNLEFT = 97;
Keyboard.NUMDOWNRIGHT = 99;
Keyboard.NUMUPLEFT = 103;
Keyboard.NUMUPRIGHT = 105;
Keyboard.DOWNSTAIRS = 190;
Keyboard.UPSTAIRS = 188;

Keyboard._keys = {};

Keyboard.listenForEvents = function(keys) {
	window.addEventListener('keydown', this._onKeyDown.bind(this));
	window.addEventListener('keyup', this._onKeyUp.bind(this));

	keys.forEach(function(key) {
		this._keys[key] = false;
	}.bind(this));
}

Keyboard._onKeyDown = function(event) {
	console.log(event.keyCode);
	var keyCode = event.keyCode;
	if (keyCode in this._keys) {
		//console.log("Keycode in keys: "+event.keyCode);
		event.preventDefault();
		this._keys[keyCode] = true;
	}
};

Keyboard._onKeyUp = function(event) {
	//console.log(event.keyCode);
	var keyCode = event.keyCode;
	if (keyCode in this._keys) {
		//console.log('KeyCode in keys')
		event.preventDefault();
		this._keys[keyCode] = false;
		if(keyCode==Keyboard.GET){
			console.log('Get key logged')
			pickupItem();
		}else if(keyCode==Keyboard.DOWNSTAIRS){
			//console.log('Downstairs key logged')
			goDownstairs();
		}else if(keyCode==Keyboard.UPSTAIRS){
			goUpstairs();
		}
		
	}
};
function pickupItem(){
	console.log("Doing pickup");
	$.ajax({
		type:"POST",
		url:"/MockitosHaven/pickupItems/"+thisGame.name,
		dataType: 'json',
	    contentType: "application/json",
		data:JSON.stringify(locatables.get(characterId))
	});
	
}

function goDownstairs(){

	$.ajax({
		type:"POST",
		url:"/MockitosHaven/goDownstairs/"+thisGame.name,
		dataType: 'json',
	    contentType: "application/json",
		data:JSON.stringify(locatables.get(characterId))
	});
	
}

function goUpstairs(){
	console.log("Going upstairs")
	$.ajax({
		type:"POST",
		url:"/MockitosHaven/goUpstairs/"+thisGame.name,
		dataType: 'json',
	    contentType: "application/json",
		data:JSON.stringify(locatables.get(characterId))
	});
	
}

Keyboard.isDown = function(keyCode) {
	if (!keyCode in this._keys) {
		throw new Error('Keycode ' + keyCode + ' is not being listened to');
	}
	return this._keys[keyCode];
};

//
// Game object
//

var Game = {};

Game.run = function(context) {
	this.ctx = context;
	this._previousElapsed = 0;

	var p = this.load();
	Promise.all(p).then(function(loaded) {
		this.init();
		window.requestAnimationFrame(this.tick);
	}.bind(this));
};

Game.tick = function(elapsed) {
	window.requestAnimationFrame(this.tick);

	// clear previous frame
	this.ctx.clearRect(0, 0, 512, 512);

	// compute delta time in seconds -- also cap it
	var delta = (elapsed - this._previousElapsed) / 1000.0;
	delta = Math.min(delta, 0.25); // maximum delta of 250 ms
	this._previousElapsed = elapsed;

	this.update(delta);
	this.render();
}.bind(Game);

// override these methods to create the demo
Game.init = function() {
};
Game.update = function(delta) {
};
Game.render = function() {
};

window.onload = function() {
	var context = document.getElementById('gameCanvas').getContext('2d');
	Game.run(context);
};
