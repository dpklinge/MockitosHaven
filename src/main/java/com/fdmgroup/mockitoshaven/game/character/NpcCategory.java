package com.fdmgroup.mockitoshaven.game.character;

public enum NpcCategory {
	UNIT_TEST("UnitTest");
	private String name;
	private NpcCategory(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
