package com.fdmgroup.mockitoshaven.game.dungeon;

public enum TileType {
	OPEN, WALL, DOOR, HOLE, SHOP, SHRINE, UPSTAIRS, DOWNSTAIRS;

	public static boolean getDefaultNavigability(TileType tileType) {
		switch (tileType) {
		case OPEN:
			return true;
		case WALL:
			return false;
		case HOLE:
			return false;
		case SHOP:
			return true;
		case SHRINE:
			return true;
		case DOOR:
			return false;
		case UPSTAIRS:
			return true;
		case DOWNSTAIRS:
			return true;

		}
		return false;
	}

}
