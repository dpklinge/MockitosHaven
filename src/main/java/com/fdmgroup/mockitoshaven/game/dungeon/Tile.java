package com.fdmgroup.mockitoshaven.game.dungeon;

public class Tile {
	private Coordinate coordinate;
	private EnvironmentType envType;
	private TileType tileType;
	private boolean navigable;
	
	
	public Tile(Coordinate coordinate, EnvironmentType envType, TileType tileType) {
		super();
		this.coordinate = coordinate;
		this.envType = envType;
		this.tileType = tileType;
		navigable = TileType.getDefaultNavigability(tileType);
	}
	public Tile() {
		super();
	}
	public Coordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	public EnvironmentType getEnvType() {
		return envType;
	}
	public void setEnvType(EnvironmentType envType) {
		this.envType = envType;
	}
	public TileType getTileType() {
		return tileType;
	}
	public void setTileType(TileType tileType) {
		this.tileType = tileType;
		navigable = TileType.getDefaultNavigability(tileType);
	}
	public boolean isNavigable() {
		
		return navigable;
	}
	public void setNavigable(boolean navigable) {
		this.navigable = navigable;
	}
	@Override
	public String toString() {
		return "Tile [coordinate=" + coordinate + ", envType=" + envType + ", tileType=" + tileType + ", navigable="
				+ navigable + "]";
	}
	
	
	
	
}
