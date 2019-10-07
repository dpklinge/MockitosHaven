package com.fdmgroup.mockitoshaven;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fdmgroup.mockitoshaven.game.dungeon.Coordinate;
import com.fdmgroup.mockitoshaven.game.dungeon.EnvironmentType;
import com.fdmgroup.mockitoshaven.game.dungeon.Tile;
import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;
import com.fdmgroup.mockitoshaven.game.dungeon.TileType;
import com.fdmgroup.mockitoshaven.game.logic.SpaceLogic;

public class PathTest {

	private static Tile getOpenTile(int x, int y){
		return new Tile(new Coordinate(x,y,"0"), EnvironmentType.DIRT, TileType.OPEN);
	}
	
	private static Tile getWallTile(int x, int y){
		return new Tile(new Coordinate(x,y,"0"), EnvironmentType.DIRT, TileType.WALL);
	}
	
	private static TileMap map;
	
	
	@BeforeClass
	public static void setup() {
		map=new TileMap();
		List<Tile> row1 = new ArrayList<>();
		List<Tile> row2 = new ArrayList<>();
		List<Tile> row3 = new ArrayList<>();
		List<Tile> row4 = new ArrayList<>();
		List<Tile> row5 = new ArrayList<>();
		for(int i=0;i<5;i++) {
			row1.add(getWallTile(0, i));
			row3.add(getWallTile(2, i));
			row5.add(getWallTile(4, i));
		}
		row2.add(getWallTile(1,0));
		row2.add(getOpenTile(1,1));
		row2.add(getOpenTile(1,2));
		row2.add(getOpenTile(1,3));
		row2.add(getWallTile(1,4));
		row4.add(getWallTile(3,0));
		row4.add(getOpenTile(3,1));
		row4.add(getOpenTile(3,2));
		row4.add(getOpenTile(3,3));
		row4.add(getWallTile(3,4));
		
			map.getTileMap().add(row1);
			map.getTileMap().add(row2);
			map.getTileMap().add(row3);
			map.getTileMap().add(row4);
			map.getTileMap().add(row5);
			map.setId("0");
		
	}
	
	@Test
	public void testPathingReturnsCorrectNextStep() {
		Coordinate co1 = new Coordinate(1,1,"0");
		Coordinate co2 = new Coordinate(1,3, "0");
		Coordinate coExpected = new Coordinate(1,2,"0");
		
		Coordinate result = SpaceLogic.nextStepBetweenPoints(co1, co2, map);
		assertEquals(coExpected, result);
	}
	@Test
	public void testPathingReturnsNullWhenNoNextStep() {
		Coordinate co1 = new Coordinate(1,1,"0");
		Coordinate co2 = new Coordinate(3,1, "0");
		
		Coordinate result = SpaceLogic.nextStepBetweenPoints(co1, co2, map);
		assertEquals(null, result);
	}
	
	
	
}
