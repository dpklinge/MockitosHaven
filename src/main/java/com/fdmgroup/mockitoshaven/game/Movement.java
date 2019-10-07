package com.fdmgroup.mockitoshaven.game;

import com.fdmgroup.mockitoshaven.game.character.GameCharacter;
import com.fdmgroup.mockitoshaven.game.dungeon.Coordinate;
import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;

public class Movement {
	private GameCharacter character;
	private Coordinate target;
	private TileMap tileMap;
	public GameCharacter getCharacter() {
		return character;
	}
	public void setCharacter(GameCharacter character) {
		this.character = character;
	}
	public Coordinate getTarget() {
		return target;
	}
	public void setTarget(Coordinate target) {
		this.target = target;
	}
	public TileMap getTileMap() {
		return tileMap;
	}
	public void setTileMap(TileMap tileMap) {
		this.tileMap = tileMap;
	}
	
	
	

}
