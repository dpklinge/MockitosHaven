package com.fdmgroup.mockitoshaven.game.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fdmgroup.mockitoshaven.game.generators.TileMapGenerator;

public class Dungeon {
	private Map<String, TileMap> maps = new HashMap<>();
	private List<String> mapIdsByIndex = new ArrayList<>();
	
	public boolean addTileMap(TileMap map) {
		if(maps.containsKey(map.getId())) {
			return false;
		}
		maps.put(map.getId(), map);
		mapIdsByIndex.add(map.getId());
		return true;
	}
	
	public TileMap getTileMapById(String id) {
		return maps.get(id);
	}
	public TileMap getTileMapByIndex(int index) {
		return maps.get(mapIdsByIndex.get(index));
	}

	public Map<String, TileMap> getMaps() {
		return maps;
	}

	public void setMaps(Map<String, TileMap> maps) {
		this.maps = maps;
	}

	public List<String> getMapIdsByIndex() {
		return mapIdsByIndex;
	}

	public void setMapIdsByIndex(List<String> mapIdsByIndex) {
		this.mapIdsByIndex = mapIdsByIndex;
	}

	@Override
	public String toString() {
		return "Dungeon [maps=" + maps + ", mapIdsByIndex=" + mapIdsByIndex + "]";
	}

	public TileMap getNextLevelMap(TileMapGenerator generator,String mapId) {
		int index = mapIdsByIndex.indexOf(mapId)+1;
		if(mapIdsByIndex.size()<=index) {
			addTileMap(generator.generateRandomLevel(index+1));
		}
		return getTileMapByIndex(index);
	}

	public TileMap getPreviousLevelMap(String mapId) {
		TileMap map = getTileMapById(mapId);
		int index = mapIdsByIndex.indexOf(map.getId())-1;
		if(index<0) {
			return null;
		}
		return getTileMapByIndex(index);
	}
	
	
	


}
