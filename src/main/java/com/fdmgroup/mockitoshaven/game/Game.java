package com.fdmgroup.mockitoshaven.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fdmgroup.mockitoshaven.game.character.GameCharacter;
import com.fdmgroup.mockitoshaven.game.character.NonPlayerCharacter;
import com.fdmgroup.mockitoshaven.game.character.PlayerCharacter;
import com.fdmgroup.mockitoshaven.game.dungeon.Dungeon;
import com.fdmgroup.mockitoshaven.game.dungeon.Locatable;
import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;
import com.fdmgroup.mockitoshaven.game.generators.TileMapGenerator;
import com.fdmgroup.mockitoshaven.game.items.Item;

@Component
@Scope("prototype")
public class Game implements Comparable<Game> {
	private String password;
	private String name;
	private Dungeon dungeon = new Dungeon();

	@JsonIgnore
	private Logger logger = LogManager.getLogger();
	

	@Autowired
	@JsonIgnore
	private SimpMessagingTemplate template;

	public SimpMessagingTemplate getTemplate() {
		return template;
	}

	public void setTemplate(SimpMessagingTemplate template) {
		this.template = template;
	}

	public void initialize(TileMapGenerator generator) {
		dungeon.addTileMap(generator.generateRandomLevel(1));
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Game(String name, String password, TileMapGenerator generator, SimpMessagingTemplate template) {
		super();
		this.password = password;
		this.name = name;
		this.template = template;
		dungeon.addTileMap(generator.generateRandomLevel(1));
	}

	public Game(String name, String password, File file, TileMapGenerator generator, SimpMessagingTemplate template) {
		super();
		this.password = password;
		this.name = name;
		this.template = template;
		dungeon.addTileMap(generator.generateFromJsonFile(file));
	}

	public Game() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public int compareTo(Game o) {
		return name.compareTo(o.name);
	}

	public void updateMap(TileMap map, String characterId) {
		logger.info("Updating map");
		template.convertAndSend("/gameData/updateMap/" + getName()+"/"+characterId, map);
		;

	}

	public void initializeEnemyActions(String mapId) {
		logger.trace("Initializing enemy actions");
		TileMap map = dungeon.getTileMapById(mapId);
		Game game = this;
		map.getNpcTimer().cancel();
		map.setNpcTimer(new Timer());

		for (Locatable locatable : map.getAllLocatables()) {
			if (locatable instanceof NonPlayerCharacter) {
				NonPlayerCharacter npc = (NonPlayerCharacter) locatable;
				
				TimerTask task = new TimerTask() {
					public void run() {
						// logger.trace("Running npc timer for "+npc.getIdentifier());
						if (npc.getStats().getHealth() > 0) {
							npc.executeBehaviour(game);
						} else {
							this.cancel();
						}
					}
				};
				map.getNpcTimer().scheduleAtFixedRate(task, 500, (int) (npc.getActionDelay() * 1000));
			}
		}
		map.setActive(true);

	}

	public void updateLocatable(Locatable targetChar) {
		getDungeon().getTileMapById(targetChar.getCoordinate().getMapId()).updateLocatable(targetChar);
		logger.trace("Updating locatable: " + targetChar);
		template.convertAndSend("/gameData/updateLocatable/" + getName(), targetChar);

	}

	public void addLocatable(PlayerCharacter character) {
		logger.trace("Adding locatable: " + character.getIdentifier());
		template.convertAndSend("/gameData/addLocatable/" + getName(), character);

	}

	public void killPlayer(PlayerCharacter character, GameCharacter killer) {
		logger.trace("Killing character: " + character.getIdentifier());
		template.convertAndSend("/gameData/killChar/" + getName(), new KillScreen(character, killer));

	}

	

	public void pause(String mapId) {
		dungeon.getTileMapById(mapId).getNpcTimer().cancel();
		dungeon.getTileMapById(mapId).setActive(false);
	}

	public void removeLocatable(TileMap map, Locatable target) {
		logger.trace("Removing locatable: " + target);
		map.removeLocatable(target);
		template.convertAndSend("/gameData/removeLocatable/" + getName(), target);

	}

	public void addToInventory(GameCharacter character, Item locatable) {
		if (character.addItem(locatable)) {
			removeLocatable(dungeon.getTileMapById(character.getCoordinate().getMapId()), locatable);
			updateLocatable(character);
		}

	}

	

	

	@Override
	public String toString() {
		return "Game [password=" + password + ", name=" + name + ", dungeon=" + dungeon + 
				", template=" + template + "]";
	}

	public void updateInventory(Locatable locatable) {
		logger.info("Updating inventory: " + locatable);
		template.convertAndSend("/gameData/updateInventory/" + getName(), locatable);
		
	}

}
