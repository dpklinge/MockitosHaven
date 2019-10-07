
	
	INSERT INTO TABLE stats (id,health,stamina,mana,magical_power,
	physical_power,strength,observation,luck,intelligence,
	dexterity,speed,character_level,experience,experience_to_next_level,
	experience_value,experience_increment)
	VALUES('SparkSwordStats', 0, 0, 3, 3, 
	2, 2, 0, 0, 2, 
	2, 1, 0, 0, 0,
	0, 0 );
	
	INSERT INTO TABLE stats (id,health,stamina,mana,magical_power,
	physical_power,strength,observation,luck,intelligence,
	dexterity,speed,character_level,experience,experience_to_next_level,
	experience_value,experience_increment)
	VALUES('LiverAxeStats', 0, 0, 0, 0, 
	3, 2, 0, 0, 0, 
	0, -1, 0, 0, 0,
	0, 0 );
	
	INSERT INTO TABLE item 
	(identifier, display_name, amount, image_name)
	VALUES
	('SparkSword', 'Sword of Sparks', 1, 'sparksword.png');
	
	INSERT INTO TABLE item_classification
	(item_identifier, classification)
	VALUES
	('SparkSword', 'WEAPON');
	INSERT INTO TABLE item_classification
	(item_identifier, classification)
	VALUES
	('SparkSword', 'UNIQUE');
	INSERT INTO TABLE equipable_item
	(slot, identifier, stats_id)
	VALUES
	('ONEHANDEDWEAPON', 'SparkSword', 'SparkSwordStats');
	
	INSERT INTO TABLE item 
	(identifier, display_name, amount, image_name)
	VALUES
	('LiverAxe', 'Axe of Liver', 1, 'meataxe.png');
	
	INSERT INTO TABLE item_classification
	(item_identifier, classification)
	VALUES
	('LiverAxe', 'WEAPON');
	INSERT INTO TABLE equipable_item
	(slot, identifier, stats_id)
	VALUES
	('ONEHANDEDWEAPON', 'LiverAxe', 'LiverAxeStats');
	
	