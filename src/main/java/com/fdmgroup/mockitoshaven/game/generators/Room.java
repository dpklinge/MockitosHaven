package com.fdmgroup.mockitoshaven.game.generators;

import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.mockitoshaven.game.dungeon.Coordinate;
import com.fdmgroup.mockitoshaven.game.dungeon.MapStyle;
import com.fdmgroup.mockitoshaven.game.dungeon.Tile;
import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;
import com.fdmgroup.mockitoshaven.game.dungeon.TileType;

public class Room {
	private int height, width, x, y, x2, y2;
	private List<List<Tile>> tiles = new ArrayList<>();
	
	
	public Room(int width, int height, int x, int y, TileMap map) {
		super();
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		x2=x+width;
		y2=y+height;
		for(int i = 0; i<width;i++) {
			tiles.add(new ArrayList<>());
			for(int j=0; j<height; j++) {
				tiles.get(i).add(new Tile(new Coordinate(x+i, y+j, map.getId()), map.getStyle().getTileTypeMapping().get(TileType.OPEN), TileType.OPEN));
			}
		}
	}
	
	
	public int getX2() {
		return x2;
	}


	public void setX2(int x2) {
		this.x2 = x2;
	}


	public int getY2() {
		return y2;
	}


	public void setY2(int y2) {
		this.y2 = y2;
	}


	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public List<List<Tile>> getTiles() {
		return tiles;
	}
	public void setTiles(List<List<Tile>> tiles) {
		this.tiles = tiles;
	}


	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + x2;
		result = prime * result + y;
		result = prime * result + y2;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		if (x != other.x)
			return false;
		if (x2 != other.x2)
			return false;
		if (y != other.y)
			return false;
		if (y2 != other.y2)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Room [height=" + height + ", width=" + width + ", x=" + x + ", y=" + y + ", x2=" + x2 + ", y2=" + y2
				+ ", tiles=" + tiles + "]";
	}
	
	
	
	

}
