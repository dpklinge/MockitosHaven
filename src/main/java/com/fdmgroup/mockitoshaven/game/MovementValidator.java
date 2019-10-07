package com.fdmgroup.mockitoshaven.game;

import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fdmgroup.mockitoshaven.game.character.GameCharacter;
import com.fdmgroup.mockitoshaven.game.character.NonPlayerCharacter;
import com.fdmgroup.mockitoshaven.game.character.PlayerCharacter;
import com.fdmgroup.mockitoshaven.game.dungeon.Coordinate;
import com.fdmgroup.mockitoshaven.game.dungeon.Locatable;
import com.fdmgroup.mockitoshaven.game.dungeon.Tile;
import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;
import com.fdmgroup.mockitoshaven.game.items.Item;
import com.fdmgroup.mockitoshaven.game.logic.CombatLogic;


public class MovementValidator {
	private static Logger logger = LogManager.getLogger();
	public static boolean attemptMove(Game game, TileMap map, Coordinate target, GameCharacter character) {
		if(target==null) {
			return false;
		}
		if(map.getTile(target).isNavigable()) {
			logger.trace("Navigable tile - "+target);
			for(Locatable locatable : map.getLocatablesByCoordinate(target)) {
				logger.trace("Locatable at target destination: "+locatable);
				if(locatable instanceof GameCharacter){
					logger.trace("Tile coordinate contained character");
					GameCharacter targetChar = (GameCharacter) locatable;
					//TODO if eliminating to allow a confusion effect, must handle multithread problems
					
					if(targetChar.getIdentifier().equals(character.getIdentifier())) {
						logger.warn("Tried to move onto self?");
						logger.info(map.getLocatablesByCoordinate(character.getCoordinate()));
						logger.info(map.getLocatablesByCoordinate(target));
						return false;
					}
					CombatLogic.performPhysicalAttack(character, targetChar);
					if(targetChar.getStats().getHealth()<=0) {
							logger.trace("Target "+targetChar.getIdentifier() +" killed by "+character.getIdentifier());
//						logger.trace("Map locatables before kill:"+map.getLocatables());
						character.addKillExperience(targetChar);
						
//						logger.trace("After kill: "+map.getLocatables());
						for(Entry<String, Item> item : targetChar.getInventory().getItems().entrySet()) {
							item.getValue().setCoordinate(targetChar.getCoordinate());
							map.addLocatable(item.getValue());
							game.updateLocatable(item.getValue());
						}
						map.removeLocatable(targetChar);
						game.removeLocatable(map, targetChar);
						game.updateLocatable(character);
						if(targetChar instanceof PlayerCharacter) {
							logger.trace("Player "+targetChar.getIdentifier()+" was killed");
							game.killPlayer((PlayerCharacter) targetChar, character);
						}
						
						return true;
					}
					game.updateLocatable(targetChar);
					game.updateLocatable(character);
					return false;
				}
			}
			map.moveLocatable(character,target);
			game.updateLocatable(character);
			return true;
		}
		logger.trace("Tile innavigable");
		return false;
	}

}
