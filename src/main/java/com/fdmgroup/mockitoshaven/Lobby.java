package com.fdmgroup.mockitoshaven;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fdmgroup.mockitoshaven.game.Game;
import com.fdmgroup.mockitoshaven.game.dungeon.TileType;
import com.fdmgroup.mockitoshaven.game.generators.TileMapGenerator;

@Component
public class Lobby {
	private Map<String, Game> games = new TreeMap<>();
	@Autowired
	@Qualifier("roomMapGenerator")
	private TileMapGenerator generator;
	@Autowired 
	private ApplicationContext context;

	public Map<String, Game> getGames() {
		return games;
	}

	public Game getGame(String name) {
		return games.get(name);
	}

	public boolean deleteGame(Game game) {
		return games.remove(game.getName(), game);
	}
	
	

	public Game createGame(String name, String password) {
		if (!games.containsKey(name)) {
			Game game = context.getBean(Game.class);
					game.setName(name);
					game.setPassword(password);
					game.initialize(generator);
			games.put(name, game);
			return game;
		}
		return null;
	}

	public TileMapGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(TileMapGenerator generator) {
		this.generator = generator;
	}

	

}
