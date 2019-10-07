package com.fdmgroup.mockitoshaven.game.dungeon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fdmgroup.mockitoshaven.game.character.GameCharacter;

public class TileMap {
	private List<List<Tile>> tileMap = Collections.synchronizedList(new ArrayList<>());
	private Map<Coordinate, Set<Locatable>> locatables = Collections.synchronizedMap(new HashMap<>());
	private Set<String> uniqueItemsGenerated = new HashSet<>();
	private String id;
	private Coordinate entryPoint;
	private Coordinate exitPoint;
	private int level;
	private MapStyle style;
	@JsonIgnore
	private Timer npcTimer = new Timer();
	@JsonIgnore
	private boolean active = false;

	public void addUniqueItemGenerated(String item) {
		uniqueItemsGenerated.add(item);
	}

	public Set<String> getUniqueItemsGenerated() {
		return uniqueItemsGenerated;
	}
	
	

	public MapStyle getStyle() {
		return style;
	}

	public void setStyle(MapStyle style) {
		this.style = style;
	}

	public void setUniqueItemsGenerated(Set<String> uniqueItemsGenerated) {
		this.uniqueItemsGenerated = uniqueItemsGenerated;
	}


	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void addLocatable(Locatable locatable) {
		if (locatables.get(locatable.getCoordinate()) == null) {
			Set<Locatable> locatableSet = Collections.synchronizedSet(new HashSet<>());
			locatables.put(locatable.getCoordinate(), locatableSet);
		}
		locatables.get(locatable.getCoordinate()).add(locatable);
	}

	public List<List<Tile>> getTileMap() {
		return tileMap;
	}

	public void setTileMap(List<List<Tile>> tileMap) {
		this.tileMap = tileMap;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Tile getTile(Coordinate coordinate) {
		return tileMap.get(coordinate.getX()).get(coordinate.getY());
	}
	
	public Tile setTile(Coordinate coordinate, Tile tile) {
		return tileMap.get(coordinate.getX()).set(coordinate.getY(), tile);
	}

	public Coordinate getEntryPoint() {
		return entryPoint;
	}

	public void setEntryPoint(Coordinate entryPoint) {
		this.entryPoint = entryPoint;
	}

	public Coordinate getExitPoint() {
		return exitPoint;
	}

	public void setExitPoint(Coordinate exitPoint) {
		this.exitPoint = exitPoint;
	}

	@Override
	public String toString() {
		return ("TileMap [locatables=" + locatables + ", id=" + id + ", entryPoint=" + entryPoint + ", exitPoint="
				+ exitPoint + "]").trim();
	}

	public Tile findNearestOpenTile(Coordinate target, Set<Coordinate> previous) {
		previous.add(target);
		boolean thisTile = checkIfOpen(target);
		if (thisTile) {
			return getTile(target);
		} else {
			int range = 0;
			while (!thisTile) {
				range++;
				Coordinate co = checkRangeForOpen(range, target, previous);
				if (co != null) {
					return getTile(co);
				}
			}
		}
		return null;

	}

	private Coordinate checkRangeForOpen(int range, Coordinate origin, Set<Coordinate> previous) {
		for (int i = -range; i <= range; i++) {
			for (int j = -range; j <= range; j++) {

				Coordinate co = new Coordinate(origin.getX() + i, origin.getY() + j, origin.getMapId());
				if (!previous.contains(co)) {
					previous.add(co);
					if (checkIfOpen(co)) {
						return co;
					}
				}
			}
		}
		return null;

	}

	public Set<Locatable> getLocatablesByCoordinate(Coordinate co) {

			if (locatables.get(co) == null) {
				locatables.put(co, Collections.synchronizedSet(new HashSet<>()));
			}
			return new HashSet<>(locatables.get(co));
	}

	public Set<Locatable> getAllLocatables() {
		Set<Locatable> locatables = new HashSet<>();
		synchronized (this.locatables) {
			Collection<Set<Locatable>> locatablesSets = this.locatables.values();

			for (Set<Locatable> locatableSet : locatablesSets) {

				for (Locatable locatable : locatableSet) {
					locatables.add(locatable);
				}

			}
		}

		return locatables;
	}

	public void removeLocatable(Locatable target) {
		locatables.get(target.getCoordinate()).remove(target);
	}

	private boolean checkIfOpen(Coordinate target) {
		if (getTile(target).isNavigable()) {
			for (Locatable locatable : getLocatablesByCoordinate(target)) {
				if (locatable instanceof GameCharacter) {
					if (locatable.getCoordinate().equals(target)) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public Tile findNearestOpenTile(Coordinate point) {
		return findNearestOpenTile(point, new HashSet<>());

	}

	public void moveLocatable(GameCharacter character, Coordinate coordinate) {
		removeLocatable(character);
		character.setCoordinate(coordinate);
		addLocatable(character);
	}

	public void consoleDisplay() {
		for(List<Tile> row : getTileMap()) {
			for(Tile tile: row) {
				System.out.print(tile.getTileType()==TileType.WALL?1:0);
			}
			System.out.println();
		}
		
	}

	public boolean isInBounds(Coordinate co) {
		
		return (co.getX()<getTileMap().size()&&co.getY()<getTileMap().get(0).size());
	}

	

	public Timer getNpcTimer() {
		return npcTimer;
	}

	public void setNpcTimer(Timer npcTimer) {
		this.npcTimer = npcTimer;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void updateLocatable(Locatable targetChar) {
		Set<Locatable> locatables = getLocatablesByCoordinate(targetChar.getCoordinate());
		locatables.remove(targetChar);
		locatables.add(targetChar);
		
	}
	
	
	
	


}
