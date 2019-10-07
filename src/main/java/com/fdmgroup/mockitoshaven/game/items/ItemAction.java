package com.fdmgroup.mockitoshaven.game.items;

import com.fdmgroup.mockitoshaven.game.character.GameCharacter;

public class ItemAction {
	private Item item;
	private GameCharacter character;
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public GameCharacter getCharacter() {
		return character;
	}
	public void setCharacter(GameCharacter character) {
		this.character = character;
	}
	
	

}
