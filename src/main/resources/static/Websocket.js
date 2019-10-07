var stompClient = null;
var thisGame;

function connect(gameId) {
	thisGame=JSON.parse(document.getElementById('gameJson').innerHTML);
    var socket = new SockJS('/MockitosHaven/game-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        //console.log('Connected: ' + frame);
        //console.log('Subscribing to: /gameData/'+thisGame.name);
        stompClient.subscribe('/gameData/removeLocatable/'+thisGame.name, function (locatable) {
            removeLocatable(JSON.parse(locatable.body));
        });
        stompClient.subscribe('/gameData/addLocatable/'+thisGame.name, function (object) {
        	addLocatable(JSON.parse(object.body));
        });
        stompClient.subscribe('/gameData/killChar/'+thisGame.name, function (killScreen) {
        	killChar(JSON.parse(killScreen.body));
        });
        stompClient.subscribe('/gameData/updateLocatable/'+thisGame.name, function (locatable) {
            updateLocatable(JSON.parse(locatable.body));
        });
        stompClient.subscribe('/gameData/updateInventory/'+thisGame.name, function (locatable) {
        	console.log("websocket call to updateInventory")
            updateInventory(JSON.parse(locatable.body));
        });
        stompClient.subscribe('/gameData/updateMap/'+thisGame.name+'/'+characterId, function (map) {
            updateMap(JSON.parse(map.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    //console.log("Disconnected");
}

function removeLocatable(locatable){
	//console.log("Removing locatable "+locatable);
	locatables.delete(locatable.identifier);
	
}

function updateLocatable(locatable){
	if(locatable.coordinate.mapId==map.id){
		//console.log("Updating "+locatable.identifier+" : "+locatable);
		locatables.set(locatable.identifier,locatable);
		if(locatable.identifier == characterId){
			updateCharacter(locatable);
		}
	}
}
function addLocatable(object){
	//console.log("Updating "+object.identifier+" : "+object);
	Loader.loadImage(object.identifier, '/MockitosHaven/images/'+object.imageName);
	locatables.set(object.identifier,object);
	if(object.identifier == characterId){
		//console.log("Updating self")
		updateCharacter(object);
	}
}

function updateMap(message) {
	//console.log("Doing websocket update movement");
	//console.log(message);
	//console.log("Locatables before update: "+map.locatables);
	locatables = new Map();
	for(let i = 0; i<message.length;i++){
		locatables.set(message[i].identifier, message[i]);
	}
	//console.log("Locatables after update: "+map.locatables);
}

function killChar(killScreen){
	//console.log('Character killed');
	locatables.delete(killScreen.player.identifier);
	if(killScreen.player.identifier == characterId){
		//console.log('Character killed was you!');
		$('#killScreenStats').html(killScreen.player.displayName+' was killed by '+killScreen.killer.identifier+' on floor '+map.id);
		$('#killScreen').css("display", "block");
		$('#killScreen').css("color", "red");
		updateCharacter(killScreen.player);
		
		//console.log('????');
	}else{
		//console.log('Killed: '+killScreen.player.displayName+' and you are '+ map.locatables.get(characterId).displayName);
	}
}

function updateCharacter(character){
	//console.log("Updating character")
	$('#name').html(character.displayName);
	$('#health').html(character.stats.health);
	$('#mana').html(character.stats.mana);
	$('#speed').html(character.stats.speed);
	$('#strength').html(character.stats.strength);
	$('#dexterity').html(character.stats.dexterity);
	$('#luck').html(character.stats.luck);
	$('#intelligence').html(character.stats.intelligence);
	$('#observation').html(character.stats.observation);
	$('#level').html(character.stats.level);
	$('#exp').html(character.stats.experience);
	$('#expToGo').html(character.stats.experienceToNextLevel);
}

function addToInventory(item){
	console.log('Adding item to inventory');
	let imageToAppend = '<div id="'+item.identifier+'" onclick="itemClick(this.getAttribute(\'id\'))"><img src="/MockitosHaven/images/'+item.imageName+'" title="'+item.displayName+'"/></div>';
	console.log('Trying to append: '+imageToAppend);
	$('#inventory').append(imageToAppend);
}

function itemClick(itemName){
	//console.log('Item '+itemName+' clicked')
	if($('#'+itemName).hasClass('equipped')){
		unequipItem(itemName);
	}else{
		equipItem(itemName);
	}
}

function unequipItem(itemName){
	//console.log('Unequipping item');
	$.ajax({
		type:"POST",
		url:"/MockitosHaven/unequipItem/"+thisGame.name,
		dataType: 'json',
	    contentType: "application/json",
		data:JSON.stringify({'item':locatables.get(characterId).inventory.items[itemName], 'character':locatables.get(characterId)})
	});
		
}
function equipItem(itemName){
	//console.log('Equipping item');
	$.ajax({
		type:"POST",
		url:"/MockitosHaven/equipItem/"+thisGame.name,
		dataType: 'json',
	    contentType: "application/json",
		data:JSON.stringify({'item':locatables.get(characterId).inventory.items[itemName], 'character':locatables.get(characterId)})
	});
}

function updateEquipStatus(item, status){
	if(status){
		console.log('Updating equipment status to equipped')
		$('#'+item.identifier).addClass("equipped");
	}else{
		console.log('Updating equipment status to unequipped')
		$('#'+item.identifier).removeClass("equipped");
	}	
}

function updateMap(newMap){
	document.getElementById('tileMap').innerHTML = JSON.stringify(newMap);
	tileMapId = newMap.id;
	loadTileMap();
}

function updateInventory(locatable){
	
	console.log("updating inventory of "+locatable.identifier);
	if(locatable.identifier == characterId){
		console.log("Emptying inventory")
		$('#inventory').empty();
		console.log("Repopulating inventory - "+locatable.inventory.items)
		for (var key in locatable.inventory.items) {
               addToInventory(locatable.inventory.items[key])
               
               console.log("Does locatable equipment check work? Slot: "+locatable.inventory.items[key].slot);
              
               for(var innerKey in locatable.equipment){
            	   console.log("comparing: "+locatable.equipment[innerKey].identifier+" "+locatable.inventory.items[key].identifier)
            	   if(locatable.equipment[innerKey].identifier==locatable.inventory.items[key].identifier){
            		   console.log("Item is equipped")
            		   updateEquipStatus(locatable.inventory.items[key], true);
            	   }
               }

        }
	}
}