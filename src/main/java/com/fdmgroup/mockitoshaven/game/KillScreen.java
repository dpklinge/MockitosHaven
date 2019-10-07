package com.fdmgroup.mockitoshaven.game;

import com.fdmgroup.mockitoshaven.game.character.GameCharacter;
import com.fdmgroup.mockitoshaven.game.character.PlayerCharacter;

public class KillScreen {
	private PlayerCharacter player;
	private GameCharacter killer;
	public KillScreen(PlayerCharacter player, GameCharacter killer) {
		super();
		this.player = player;
		this.killer = killer;
	}
	public PlayerCharacter getPlayer() {
		return player;
	}
	public void setPlayer(PlayerCharacter player) {
		this.player = player;
	}
	public GameCharacter getKiller() {
		return killer;
	}
	public void setKiller(GameCharacter killer) {
		this.killer = killer;
	}
	
	
	

}
