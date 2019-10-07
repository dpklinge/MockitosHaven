package com.fdmgroup.mockitoshaven.game.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fdmgroup.mockitoshaven.dataaccess.MockitosHavenDao;
import com.fdmgroup.mockitoshaven.game.Stats;
import com.fdmgroup.mockitoshaven.game.character.NonPlayerCharacter;
import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;
@Component
public class CharacterGenerator {
	private List<NonPlayerCharacter> dbMonsters = new ArrayList<>();
	private Random random = new Random();
	private static long id = 0;
	
	//TODO remove test block
	
	@Bean
	public NonPlayerCharacter getNpc() {
		return new NonPlayerCharacter();
	}
	
	
	
	@Autowired
	public CharacterGenerator(MockitosHavenDao dao) {
		super();
		dbMonsters = dao.findAllObjects(NonPlayerCharacter.class);
		System.out.println("Character generator constructor, monsters: "+dbMonsters);
	}

	public NonPlayerCharacter generateRandomCharacter(int level) {
		NonPlayerCharacter template = dbMonsters.get(random.nextInt(dbMonsters.size()));
		NonPlayerCharacter monster = getNpc();
		monster.setIdentifier(template.getIdentifier()+id++);
		monster.setBehaviour(template.getBehaviour());
		monster.setStats(Stats.copyStats(template.getStats()));
		monster.setDisplayName(template.getDisplayName());
		monster.setImageName(template.getImageName());
		return modifyByLevel(monster,level);
	}

	private NonPlayerCharacter modifyByLevel(NonPlayerCharacter nonPlayerCharacter, int level) {
		return nonPlayerCharacter;
	}

	public NonPlayerCharacter generateRandomCharacter(TileMap map, int level) {
		return generateRandomCharacter(level);
	}

}
