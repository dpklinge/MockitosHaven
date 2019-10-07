
	
	INSERT INTO stats (identifier, health, stamina, mana, magical_power,
	physical_power, strength, observation, luck, intelligence,
	dexterity, speed, character_level, experience, experience_to_next_level,
	experience_value, experience_increment)
	VALUES('SparkSwordStats', 0, 0, 3, 3, 
	2, 2, 0, 0, 2, 
	2, 1, 0, 0, 0,
	0, 0 );
	
	INSERT INTO item 
	(identifier, display_name, amount, image_name)
	VALUES
	('SparkSword', 'Sword of Sparks', 1, 'sparksword.png');
	
	INSERT INTO item_classification
	(item_identifier, classification)
	VALUES
	('SparkSword', 'WEAPON');
	INSERT INTO item_classification
	(item_identifier, classification)
	VALUES
	('SparkSword', 'UNIQUE');
	INSERT INTO equipable_item
	(slot, identifier, stats_identifier)
	VALUES
	('ONEHANDEDWEAPON', 'SparkSword', 'SparkSwordStats');
	
	INSERT INTO stats (identifier,health,stamina,mana,magical_power,
	physical_power,strength,observation,luck,intelligence,
	dexterity,speed,character_level,experience,experience_to_next_level,
	experience_value,experience_increment)
	VALUES('LiverAxeStats', 0, 0, 0, 0, 
	3, 2, 0, 0, 0, 
	0, -1, 0, 0, 0,
	0, 0 );
	
	INSERT INTO item 
	(identifier, display_name, amount, image_name)
	VALUES
	('LiverAxe', 'Axe of Liver', 1, 'meataxe.png');
	
	INSERT INTO item_classification
	(item_identifier, classification)
	VALUES
	('LiverAxe', 'WEAPON');
	INSERT INTO equipable_item
	(slot, identifier, stats_identifier)
	VALUES
	('ONEHANDEDWEAPON', 'LiverAxe', 'LiverAxeStats');
	
	INSERT INTO stats 
	(identifier,health, maxHealth,stamina,mana,magical_power,
	physical_power,strength,observation,luck,intelligence,
	dexterity,speed,character_level,experience,experience_to_next_level,
	experience_value,experience_increment)
	VALUES
	('JunitStats', 10,10,  5, 0, 0,
	2, 2, 5, 2, 1,
	5, 5, 1, 0, 10,
	10, 100 );
	
	INSERT INTO game_character 
	(identifier, display_name, image_name,inventory_identifier, stats_identifier)
	VALUES
	('Junit', 'Junit Test','UnitTest.png',null, 'JunitStats'  );


	INSERT INTO non_player_character
	(identifier, behaviour)
	VALUES
	('Junit', 'AGGRESSIVE');
	
	INSERT INTO stats 
	(identifier,health,stamina,mana,magical_power,
	physical_power,strength,observation,luck,intelligence,
	dexterity,speed,character_level,experience,experience_to_next_level,
	experience_value,experience_increment, maxHealth)
	VALUES
	('NullPointerStats', 15, 5, 0, 0,
	1, 1, 10, 5, 0,
	5, 10, 1, 0, 10,
	30, 100, 15);
	
	INSERT INTO game_character 
	(identifier, display_name, image_name,inventory_identifier, stats_identifier)
	VALUES
	('NullPointer', 'Null Pointer','null.png',null, 'NullPointerStats'  );


	INSERT INTO non_player_character
	(identifier, behaviour)
	VALUES
	('NullPointer', 'AGGRESSIVE');
	
	
	INSERT INTO stats (identifier, health, stamina, mana, magical_power,
	physical_power, strength, observation, luck, intelligence,
	dexterity, speed, character_level, experience, experience_to_next_level,
	experience_value, experience_increment, maxHealth)
	VALUES('HealthPotStats', 10, 0, 0, 0, 
	0, 0, 0, 0, 0, 
	0, 0, 0, 0, 0,
	0, 0 ,0);
	
	INSERT INTO item 
	(identifier, display_name, amount, image_name)
	VALUES
	('HealthPot', 'Potion of Restore Health', 1, 'healthPot.png');
	
	INSERT INTO item_classification
	(item_identifier, classification)
	VALUES
	('HealthPot', 'CONSUMABLE');
	INSERT INTO item_classification
	(item_identifier, classification)
	VALUES
	('HealthPot', 'POTION');
	INSERT INTO consumable
	(duration, identifier, stats_identifier)
	VALUES
	(10001, 'HealthPot', 'HealthPotStats');