	INSERT INTO TABLE stats 
	(id,health,stamina,mana,magical_power,
	physical_power,strength,observation,luck,intelligence,
	dexterity,speed,character_level,experience,experience_to_next_level,
	experience_value,experience_increment)
	VALUES
	('JunitStats', 10, 5, 0, 0,
	2, 2, 5, 2, 1,
	5, 5, 1, 0, 10,
	10, 100 );
	
	INSERT INTO TABLE game_character 
	(identifier, display_name, image_name,inventory_id, stats_id)
	VALUES
	('Junit', 'Junit Test','UnitTest.png',null, 'JunitStats'  );