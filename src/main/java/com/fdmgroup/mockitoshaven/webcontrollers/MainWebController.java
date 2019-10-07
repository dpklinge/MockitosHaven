package com.fdmgroup.mockitoshaven.webcontrollers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.mockitoshaven.Lobby;
import com.fdmgroup.mockitoshaven.game.Game;
import com.fdmgroup.mockitoshaven.game.Movement;
import com.fdmgroup.mockitoshaven.game.MovementValidator;
import com.fdmgroup.mockitoshaven.game.character.CharacterClass;
import com.fdmgroup.mockitoshaven.game.character.GameCharacter;
import com.fdmgroup.mockitoshaven.game.character.PlayerCharacter;
import com.fdmgroup.mockitoshaven.game.dungeon.Locatable;
import com.fdmgroup.mockitoshaven.game.dungeon.Tile;
import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;
import com.fdmgroup.mockitoshaven.game.generators.TileMapGenerator;
import com.fdmgroup.mockitoshaven.game.items.Item;
import com.fdmgroup.mockitoshaven.game.items.ItemAction;

@Controller
public class MainWebController {
	@Autowired
	private Lobby lobby;

	@Autowired
	private ServletContext context;

	private Logger logger = LogManager.getLogger();

	private static ObjectMapper mapper = new ObjectMapper();

	public MainWebController() {
		logger.trace("Web controller initialized");
	}

	@GetMapping("/home")
	public String landingPage() {
		return "index";
	}

	@GetMapping("/lobby")
	public String lobby(Model model, HttpSession session) {
		// TODO: remove test code

		Map<String, Map<HttpSession, PlayerCharacter>> gameUserMap = (Map<String, Map<HttpSession, PlayerCharacter>>) context
				.getAttribute("gameUserMap");
		if (gameUserMap == null) {
			gameUserMap = new HashMap<>();
			context.setAttribute("gameUserMap", gameUserMap);
		}
		if (!gameUserMap.containsKey("Game2")) {
			Game game = lobby.createGame("Game2", "");
			gameUserMap.put("Game2", new HashMap<>());

		}

		model.addAttribute("error", session.getAttribute("error"));
		session.setAttribute("error", null);
		model.addAttribute("games", lobby.getGames().values());
		return "lobby";
	}

	@GetMapping("/test/{id}")
	@ResponseBody
	public String testGamePassword(@PathVariable String id) {
		String[] terms = id.split("&");
		if (terms.length == 2) {
			String name = terms[0];
			String password = terms[1];
			Game game = lobby.getGame(name);
			if (game != null && password.equalsIgnoreCase(game.getPassword())) {

				return "valid";
			}
		}
		if (terms.length == 1) {
			String name = terms[0];
			Game game = lobby.getGame(name);
			if (game != null && (game.getPassword() == null || game.getPassword().isEmpty())) {
				return "valid";
			}
		}
		return "invalid";

	}

	@GetMapping("/characterSelect/{id}")
	public String characterSelect(Model model, @PathVariable String id, HttpSession session) {
		model.addAttribute("classes", Arrays.asList(CharacterClass.values()));
		session.setAttribute("currentGame", lobby.getGame(id));
		return "characterSelect";
	}

	@PostMapping("/characterSelect/{id}")
	public String characterSelectPost(@PathVariable String id, @RequestParam String clazz, HttpSession session) {
		PlayerCharacter pc = addNewCharacter(lobby.getGame(id), CharacterClass.valueOf(clazz));
		Map<String, Map<HttpSession, PlayerCharacter>> gameUserMap = (Map<String, Map<HttpSession, PlayerCharacter>>) context
				.getAttribute("gameUserMap");
		gameUserMap.get(id).put(session, pc);
		return "redirect:/game/" + id;
	}

	@PostMapping("/createGame")
	public String createGame(Model model, HttpSession session, @RequestParam String name, @RequestParam String password)
			throws JsonProcessingException {

		Map<String, Map<HttpSession, PlayerCharacter>> gameUserMap = (Map<String, Map<HttpSession, PlayerCharacter>>) context
				.getAttribute("gameUserMap");
		if (gameUserMap == null) {
			gameUserMap = new HashMap<>();
			context.setAttribute("gameUserMap", gameUserMap);
		}

		if (gameUserMap.containsKey(name)) {
			session.setAttribute("error", "That game name is already taken!");
			return "redirect:lobby";
		}
		Game game = lobby.createGame(name, password);

		session.setAttribute("gameJson", mapper.writeValueAsString(game));
		logger.trace("Created game: " + game.getDungeon().getTileMapByIndex(0));
		gameUserMap.put(name, new HashMap<>());
		session.setAttribute("currentGame", game);
		logger.trace("In web controller, created game start coordinates:");
		logger.trace(game.getDungeon().getTileMapByIndex(0).getEntryPoint());
		return "redirect:/characterSelect/" + game.getName();
	}

	private PlayerCharacter addNewCharacter(Game game, CharacterClass clazz) {
		logger.trace("In addNewCharacter");

		PlayerCharacter character = PlayerCharacter.generateClass(clazz);
		TileMap map = game.getDungeon().getTileMapByIndex(0);
		game.pause(map.getId());
		Tile tile = map.findNearestOpenTile(map.getEntryPoint());
		character.setCoordinate(tile.getCoordinate());
		logger.trace("Character set, adding to dungeon");
		map.addLocatable(character);
		logger.trace("Sending message to front-end");
		game.addLocatable(character);
		return character;

	}

	@GetMapping("/game/{id}")
	public String getGame(Model model, @PathVariable String id, HttpSession session) throws JsonProcessingException {
		logger.trace("Entering game: " + id);
		Game game = lobby.getGame(id);
		logger.trace("Got game: " + game.getName());

		PlayerCharacter character;
		Map<String, Map<HttpSession, PlayerCharacter>> gameUserMap = (Map<String, Map<HttpSession, PlayerCharacter>>) context
				.getAttribute("gameUserMap");
		if (gameUserMap == null) {
			logger.info("First game in the system");
			gameUserMap = new HashMap<>();
			context.setAttribute("gameUserMap", gameUserMap);
		}
		if (!gameUserMap.containsKey(game.getName())) {
			session.setAttribute("error", "Game not found!");
			return "redirect:/lobby";
		}
		if (gameUserMap.get(game.getName()).containsKey(session)) {
			logger.trace("Character already existed");
			character = gameUserMap.get(game.getName()).get(session);
			if (character.getStats().getHealth() <= 0) {
				gameUserMap.get(game.getName()).remove(session);
				return "redirect:/characterSelect/" + game.getName();
			}
		} else {
			return "redirect:/characterSelect/" + id;
		}

		logger.debug("Setting currentGame");
		session.setAttribute("currentGame", game);
		logger.trace("Game: " + game);
		game.pause(character.getCoordinate().getMapId());
		session.setAttribute("gameJson", mapper.writeValueAsString(game));
		session.setAttribute("tileMap",
				mapper.writeValueAsString(game.getDungeon().getTileMapById(character.getCoordinate().getMapId())));
		session.setAttribute("characterObject", character);
		session.setAttribute("character", mapper.writeValueAsString(character));
		return "game";
	}

	@PostMapping(value = "/attemptMove", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String attemptMove(HttpSession session, @RequestBody Movement attemptedMovement) {
		Game game = (Game) session.getAttribute("currentGame");
		
		String gameId = game.getName();
		logger.trace("Move detected : " + attemptedMovement.getTarget());
		TileMap map = lobby.getGame(gameId).getDungeon().getTileMapById(attemptedMovement.getTarget().getMapId());
		if (!map.isActive()) {
			map.setActive(true);
			game.initializeEnemyActions(attemptedMovement.getTarget().getMapId());
		}
		attemptedMovement.setTileMap(map);
		
		logger.trace("Getting locatables");
		for (Locatable locatable : map.getLocatablesByCoordinate(attemptedMovement.getCharacter().getCoordinate())) {
			logger.trace("Locatable at location" + locatable);
			if (locatable.equals(attemptedMovement.getCharacter())) {
				logger.trace("Attempting movement");
				return "" + MovementValidator.attemptMove(game, map, attemptedMovement.getTarget(), (GameCharacter) locatable);
			}

		}

		logger.trace("Map does not contain character(probably dead):");
		logger.trace(map);
		logger.trace(attemptedMovement.getCharacter());

		return "false";
	}

	@PostMapping("/changeName/{gameId}")
	public void changeName(@RequestBody PlayerCharacter character, @PathVariable String gameId) {
		logger.trace("Changing name with character " + character);
		Game game = lobby.getGame(gameId);
		for (Locatable locatable : game.getDungeon().getTileMapById(character.getCoordinate().getMapId())
				.getAllLocatables()) {

			if (locatable instanceof PlayerCharacter) {
				logger.trace("Found in memory player: " + locatable);
				if (((PlayerCharacter) locatable).getIdentifier().equals(character.getIdentifier())) {
					logger.trace("IDs match");
					((PlayerCharacter) locatable).setDisplayName(character.getDisplayName());
					game.updateLocatable(character);
					return;
				}
			}
		}
	}

	@PostMapping("/pickupItems/{id}")
	public void pickupItems(@RequestBody PlayerCharacter character, @PathVariable String id) {
		logger.info("Picking up items at: " + character.getCoordinate());
		Game game = lobby.getGame(id);
		TileMap map = game.getDungeon().getTileMapById(character.getCoordinate().getMapId());
		GameCharacter chara = null;
		Set<Locatable> locatables = map.getLocatablesByCoordinate(character.getCoordinate());
		for (Locatable locatable : locatables) {
			if (locatable.getIdentifier().equals(character.getIdentifier())) {
				chara = (GameCharacter) locatable;
				break;
			}
		}
		for (Locatable locatable : locatables) {
			if (locatable instanceof Item) {
				logger.info("Added Item:" + locatable);
				game.addToInventory(chara, (Item) locatable);
				game.updateInventory(chara);
				break;
			}
		}

	}

	@PostMapping("/equipItem/{id}")
	public void equipItem(@RequestBody ItemAction action, @PathVariable String id) {
		logger.info("Using item: " + action.getItem());
		Game game = lobby.getGame(id);
		TileMap map = game.getDungeon().getTileMapById(action.getCharacter().getCoordinate().getMapId());
		for (Locatable locatable : map.getLocatablesByCoordinate(action.getCharacter().getCoordinate())) {
			if (locatable.getIdentifier().equals(action.getCharacter().getIdentifier())) {
				((GameCharacter) locatable).useItem(action.getItem().getIdentifier(), game);
				game.updateLocatable(locatable);
				game.updateInventory(locatable);
				break;
			}
		}
	}

	@PostMapping("/unequipItem/{id}")
	public void unequipItem(@RequestBody ItemAction action, @PathVariable String id) {
		Game game = lobby.getGame(id);
		TileMap map = game.getDungeon().getTileMapById(action.getCharacter().getCoordinate().getMapId());
		for (Locatable locatable : map.getLocatablesByCoordinate(action.getCharacter().getCoordinate())) {
			if (locatable.getIdentifier().equals(action.getCharacter().getIdentifier())) {

				((GameCharacter) locatable).unequipItem(action.getItem().getIdentifier());
				game.updateLocatable(locatable);
				game.updateInventory(locatable);
				break;
			}
		}
	}

	@PostMapping("/goDownstairs/{id}")
	public void goDownstairs(@RequestBody PlayerCharacter character, @PathVariable String id) {
		Game game = lobby.getGame(id);
		TileMapGenerator generator = lobby.getGenerator();
		String mapId = character.getCoordinate().getMapId();
		game.pause(mapId);
		TileMap oldMap = game.getDungeon().getTileMapById(mapId);
		TileMap newMap = game.getDungeon().getNextLevelMap(generator, mapId);
		for (Locatable locatable : oldMap.getLocatablesByCoordinate(character.getCoordinate())) {
			if (locatable.getIdentifier().equals(character.getIdentifier())) {
				synchronized (locatable) {
					if (locatable.getCoordinate().equals(oldMap.getExitPoint())) {
						logger.info("Moving character down");
						synchronized (oldMap) {
							game.removeLocatable(oldMap, locatable);
						}
						locatable.setCoordinate(newMap.findNearestOpenTile(newMap.getEntryPoint()).getCoordinate());
						synchronized (newMap) {

							newMap.addLocatable(locatable);
						}
						game.updateMap(newMap, locatable.getIdentifier());
						game.updateLocatable(locatable);
						game.initializeEnemyActions(newMap.getId());
					} else {
						logger.info("Character not on exit");
					}
					break;
				}
			}
		}
	}

	@PostMapping("/goUpstairs/{id}")
	public void goUpstairs(@RequestBody PlayerCharacter character, @PathVariable String id) {
		Game game = lobby.getGame(id);
		String mapId = character.getCoordinate().getMapId();
		game.pause(mapId);
		TileMap oldMap = game.getDungeon().getTileMapById(mapId);
		TileMap newMap = game.getDungeon().getPreviousLevelMap(mapId);
		if (newMap == null) {
			logger.warn("Previous level is null");
			return;
		}
		for (Locatable locatable : oldMap.getLocatablesByCoordinate(character.getCoordinate())) {
			if (locatable.getIdentifier().equals(character.getIdentifier())) {

				if (locatable.getCoordinate().equals(oldMap.getEntryPoint())) {
					logger.info("Moving character up");
					synchronized (oldMap) {
						game.removeLocatable(oldMap, locatable);
					}
					locatable.setCoordinate(newMap.findNearestOpenTile(newMap.getExitPoint()).getCoordinate());
					newMap.addLocatable(locatable);

					game.updateMap(newMap, locatable.getIdentifier());
					game.updateLocatable(locatable);
					game.initializeEnemyActions(newMap.getId());
				} else {
					logger.info("Character not on entrance");
				}
				break;
			}
		}
	}

}
