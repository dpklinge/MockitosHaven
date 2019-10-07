package com.fdmgroup.mockitoshaven.game.dungeon;

import java.util.HashMap;
import java.util.Map;

public enum MapStyle {
	CAVERN(EnvironmentType.DIRT, EnvironmentType.DIRT, EnvironmentType.DIRT, 8, 15), SQUARE_ROOMS(EnvironmentType.ROCK,
			EnvironmentType.ROCK, EnvironmentType.ROCK, 5, 10);
	private Map<TileType, EnvironmentType> tileTypeMapping = new HashMap<>();
	private int minRoomSize;
	private int maxRoomSize;

	private MapStyle(EnvironmentType openSpace, EnvironmentType walls,EnvironmentType stairs, int minRoomSize, int maxRoomSize) {
		tileTypeMapping.put(TileType.OPEN, openSpace);
		tileTypeMapping.put(TileType.WALL, walls);
		tileTypeMapping.put(TileType.UPSTAIRS, stairs);
		tileTypeMapping.put(TileType.DOWNSTAIRS, stairs);
		this.minRoomSize = minRoomSize;
		this.maxRoomSize = maxRoomSize;
	}

	public Map<TileType, EnvironmentType> getTileTypeMapping() {
		return tileTypeMapping;
	}

	public void setTileTypeMapping(Map<TileType, EnvironmentType> tileTypeMapping) {
		this.tileTypeMapping = tileTypeMapping;
	}

	public int getMinRoomSize() {
		return minRoomSize;
	}

	public void setMinRoomSize(int minRoomSize) {
		this.minRoomSize = minRoomSize;
	}

	public int getMaxRoomSize() {
		return maxRoomSize;
	}

	public void setMaxRoomSize(int maxRoomSize) {
		this.maxRoomSize = maxRoomSize;
	}

}
