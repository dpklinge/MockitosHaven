package com.fdmgroup.mockitoshaven.game.generators;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.mockitoshaven.game.dungeon.Coordinate;
import com.fdmgroup.mockitoshaven.game.dungeon.MapStyle;
import com.fdmgroup.mockitoshaven.game.dungeon.Tile;
import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;
import com.fdmgroup.mockitoshaven.game.dungeon.TileType;
import com.fdmgroup.mockitoshaven.game.logic.SpaceLogic;

@Component("roomMapGenerator")
public class RoomBasedTileMapGenerator extends TileMapGenerator {
	private Logger logger = LogManager.getLogger();
	@Autowired
	private RoomGenerator roomGenerator;

	@Autowired
	public RoomBasedTileMapGenerator(CharacterGenerator monsterGenerator, ItemGenerator itemGenerator) {
		super(monsterGenerator, itemGenerator);
	}

	@Override
	public TileMap generateRandomLevel(int depth) {
		logger.trace("Creating map level");
		TileMap map = new TileMap();
		map.setLevel(depth);
		map.setId("" + getTileMapId());

		MapStyle style = MapStyle.values()[getRandom().nextInt(MapStyle.values().length)];
		map.setStyle(style);

		logger.trace("Filling map with solids");
		fillSolid(map);
		generateRooms(map);

		logger.trace("Populating map");
		populate(map, depth);
		logger.trace("Generating entry");
		generateEntryPoint(map);
		logger.trace("Generating exit");
		generateExitPoint(map);

		logger.trace("returning map");
		map.consoleDisplay();
		return map;
	}

	private void generateRooms(TileMap map) {
		int mapSize = ((getxSize() - 2) * (getySize() - 2));
		logger.trace("Mapsize: " + mapSize);
		int maxRoomSize = (int) (Math.pow(map.getStyle().getMaxRoomSize(), 2));
		logger.trace("Expected max room size: " + maxRoomSize);
		int maxRooms = (int) (mapSize / maxRoomSize / 1.5);
		int minRooms = (int) (mapSize / maxRoomSize / 2);
		logger.trace("Minrooms: " + minRooms);
		logger.trace("MaxRooms: " + maxRooms);
		int roomNumber = getRandom().nextInt(maxRooms - minRooms) + minRooms;
		logger.trace("Number of rooms to generate: " + roomNumber);
		List<Room> rooms = new ArrayList<>();
		for (int i = 0; i < roomNumber; i++) {
			rooms = roomGenerator.generateRoom(map, rooms);
		}
		fillRooms(rooms, map);
		connectRooms(rooms, map);

	}

	private void connectRooms(List<Room> rooms, TileMap map) {
		logger.trace("Connecting rooms");
		for (Room room : rooms) {
			logger.trace("Room in connectRooms: " + room);
			logger.trace("Finding nearest room");
			Room nearestRoom = findNearestRoom(room, rooms, map);
			logger.trace("NearestRoom in connectRooms: " + nearestRoom);
			logger.trace("Checking for path to nearest");
			if (SpaceLogic.nextStepBetweenPoints(new Coordinate(room.getX(), room.getY(), map.getId()),
					new Coordinate(nearestRoom.getX(), nearestRoom.getY(), map.getId()), map) == null) {
				logger.trace("No path found! Creating corridor");
				makeCorridor(room, nearestRoom, map);
				logger.trace("Checking if path was successfuly created to nearest");
				if (SpaceLogic.nextStepBetweenPoints(new Coordinate(room.getX(), room.getY(), map.getId()),
						new Coordinate(nearestRoom.getX(), nearestRoom.getY(), map.getId()), map) == null) {
					throw new IllegalStateException("Path invalid");
				}
			}
		}
		// Connect stragglers not caught in first pass
		logger.trace("Beginning second sweep - current map: ");
//		map.consoleDisplay();
		for (Room room : rooms) {
			logger.trace("Finding closest unconnected room to "+room);
			Room nearestRoom = findNearestUnconnectedRoom(room, rooms, map);
			while (nearestRoom != null) {
				
					// If this one is null, something is wrong with findNearestUnconnectedRoom
					Room tempRoom = findNearestUnconnectedRoom(nearestRoom, rooms, map);
					makeCorridor(tempRoom, nearestRoom, map);
					logger.trace("Finding closest unconnected room to "+room);
					nearestRoom = findNearestUnconnectedRoom(room, rooms, map);
			}
		}
	}

	private Room findNearestUnconnectedRoom(Room room, List<Room> rooms, TileMap map) {
		double nearestDistance = Double.MAX_VALUE;
		Room nearestRoom = null;
		Coordinate roomMidPoint = SpaceLogic.getRoomMidpoint(room.getX(), room.getX2(), room.getY(), room.getY2(), map);
		for (Room otherRoom : rooms) {
			if (!otherRoom.equals(room)) {

				Coordinate otherRoomMidPoint = SpaceLogic.getRoomMidpoint(otherRoom.getX(), otherRoom.getX2(),
						otherRoom.getY(), otherRoom.getY2(), map);
				Coordinate path = SpaceLogic.nextStepBetweenPoints(roomMidPoint, otherRoomMidPoint, map);
				logger.trace("Path? "+path);
				if (path == null) {
					double tempDistance = SpaceLogic.getDistanceBetween(roomMidPoint, otherRoomMidPoint);
					if (tempDistance < nearestDistance) {
						nearestDistance = tempDistance;
						nearestRoom = otherRoom;
					}
				}
			}
		}
		return nearestRoom;
	}

	private void makeCorridor(Room room, Room nearestRoom, TileMap map) {

		int potentialDistanceFromNearestPoint = 5;
		int xStart;
		int xEnd;
		int yStart;
		int yEnd;
		logger.trace("Making corridor");
		logger.trace("Room : " + room);
		logger.trace("Other room : " + nearestRoom);

		if (room.getX() > nearestRoom.getX2()) {
			logger.trace("X > oX2");
			if (room.getY() > nearestRoom.getY2()) {
				logger.trace("Y > oY2");
				if (getRandom().nextBoolean()) {
					logger.trace("Randomly picked Y side to initiate");
					xStart = room.getX();
					yStart = room.getY() + getRandom().nextInt(potentialDistanceFromNearestPoint);
				} else {
					logger.trace("Randomly picked X side to initiate");
					xStart = room.getX() + getRandom().nextInt(potentialDistanceFromNearestPoint);
					yStart = room.getY();
				}
				if (getRandom().nextBoolean()) {
					logger.trace("Randomly picked oY side to initiate");
					xEnd = nearestRoom.getX2();
					yEnd = nearestRoom.getY2() - getRandom().nextInt(potentialDistanceFromNearestPoint);
				} else {
					logger.trace("Randomly picked oX side to initiate");
					xEnd = nearestRoom.getX2() - getRandom().nextInt(potentialDistanceFromNearestPoint);
					yEnd = nearestRoom.getY2();
				}
			} else if (room.getY2() < nearestRoom.getY()) {
				logger.trace("Y < oY2");
				if (getRandom().nextBoolean()) {
					logger.trace("Randomly picked Y side to initiate");
					xStart = room.getX();
					yStart = room.getY2() - getRandom().nextInt(potentialDistanceFromNearestPoint);
				} else {
					logger.trace("Randomly picked X side to initiate");
					xStart = room.getX() + getRandom().nextInt(potentialDistanceFromNearestPoint);
					yStart = room.getY2();
				}
				if (getRandom().nextBoolean()) {
					logger.trace("Randomly picked oY side to initiate");
					xEnd = nearestRoom.getX2();
					yEnd = nearestRoom.getY() + getRandom().nextInt(potentialDistanceFromNearestPoint);
				} else {
					logger.trace("Randomly picked oX side to initiate");
					xEnd = nearestRoom.getX2() - getRandom().nextInt(potentialDistanceFromNearestPoint);
					yEnd = nearestRoom.getY();
				}
			} else {
				logger.trace("y and oy overlap");
				xStart = room.getX();
				yStart = getRandom().nextInt(room.getY2() - room.getY()) + room.getY();
				xEnd = nearestRoom.getX2();
				yEnd = getRandom().nextInt(nearestRoom.getY2() - nearestRoom.getY()) + nearestRoom.getY();
			}
		} else if (room.getX2() < nearestRoom.getX()) {
			logger.trace("X2<=oX");
			if (room.getY() > nearestRoom.getY2()) {
				logger.trace("Y>oY2");
				if (getRandom().nextBoolean()) {
					logger.trace("Randomly picked Y side to initiate");

					xStart = room.getX2();
					yStart = room.getY() + getRandom().nextInt(potentialDistanceFromNearestPoint);
				} else {
					logger.trace("Randomly picked X side to initiate");

					xStart = room.getX2() - getRandom().nextInt(potentialDistanceFromNearestPoint);
					yStart = room.getY();
				}
				if (getRandom().nextBoolean()) {
					logger.trace("Randomly picked oY side to initiate");

					xEnd = nearestRoom.getX();
					yEnd = nearestRoom.getY2() - getRandom().nextInt(potentialDistanceFromNearestPoint);
				} else {
					logger.trace("Randomly picked oX side to initiate");

					xEnd = nearestRoom.getX() + getRandom().nextInt(potentialDistanceFromNearestPoint);
					yEnd = nearestRoom.getY2();
				}
			} else if (room.getY2() < nearestRoom.getY()) {
				logger.trace("Y2< oY");
				if (getRandom().nextBoolean()) {
					logger.trace("Randomly picked Y side to initiate");
					xStart = room.getX2();
					yStart = room.getY2() - getRandom().nextInt(potentialDistanceFromNearestPoint);
				} else {
					logger.trace("Randomly picked X side to initiate");
					xStart = room.getX2() - getRandom().nextInt(potentialDistanceFromNearestPoint);
					yStart = room.getY2();
				}
				if (getRandom().nextBoolean()) {
					logger.trace("Randomly picked oY side to initiate");
					xEnd = nearestRoom.getX();
					yEnd = nearestRoom.getY() + getRandom().nextInt(potentialDistanceFromNearestPoint);
				} else {
					logger.trace("Randomly picked oX side to initiate");
					xEnd = nearestRoom.getX() + getRandom().nextInt(potentialDistanceFromNearestPoint);
					yEnd = nearestRoom.getY();
				}
			} else {
				logger.trace("y and oy overlap");
				xStart = room.getX2();
				yStart = getRandom().nextInt(room.getY2() - room.getY()) + room.getY();
				xEnd = nearestRoom.getX();
				yEnd = getRandom().nextInt(nearestRoom.getY2() - nearestRoom.getY()) + nearestRoom.getY();
			}
		} else {
			logger.trace("X and oX overlap");
			if (room.getY() < nearestRoom.getY()) {
				logger.trace("y<oY");
				xStart = getRandom().nextInt(room.getX2() - room.getX()) + room.getX();
				yStart = room.getY2();
				xEnd = getRandom().nextInt(nearestRoom.getX2() - nearestRoom.getX()) + nearestRoom.getX();
				yEnd = nearestRoom.getY();
			} else {
				logger.trace("y>oY");
				xStart = getRandom().nextInt(room.getX2() - room.getX()) + room.getX();
				yStart = room.getY();
				xEnd = getRandom().nextInt(nearestRoom.getX2() - nearestRoom.getX()) + nearestRoom.getX();
				yEnd = nearestRoom.getY2();
			}

		}
		Coordinate roomTunnelStart = new Coordinate(xStart, yStart, map.getId());
		Coordinate nearestRoomTunnelEnd = new Coordinate(xEnd, yEnd, map.getId());

		int kinkPoint = 0;
		int xDistance = Math.abs(xEnd - xStart);
		int yDistance = Math.abs(yEnd - yStart);
		boolean startXDirection = true;
		if (xDistance > yDistance) {
			kinkPoint = getRandom().nextInt(xDistance);
		} else {
			kinkPoint = getRandom().nextInt(yDistance);
			startXDirection = false;
		}
		int xProgress = 0;
		int yProgress = 0;
		logger.trace("Building corridor");
		logger.trace("distance x = " + xDistance + " y = " + yDistance);
		logger.trace("Kink point : " + kinkPoint);
		logger.trace("Start point: " + roomTunnelStart);
		logger.trace("End point: " + nearestRoomTunnelEnd);
		if (startXDirection) {
			logger.trace("X distance was longer - starting horizontal");
			while (xProgress <= xDistance) {

				Coordinate corridor = new Coordinate(
						(int) (roomTunnelStart.getX() + xProgress * Math.signum(xEnd - xStart)),
						(int) (roomTunnelStart.getY() + yProgress * Math.signum(yEnd - yStart)), map.getId());
				logger.trace("Flipping tile: " + corridor);
				map.setTile(corridor,
						new Tile(corridor, map.getStyle().getTileTypeMapping().get(TileType.OPEN), TileType.OPEN));

				if (xProgress >= kinkPoint && yProgress < yDistance) {
					yProgress++;
				} else {
					xProgress++;
				}
			}
		} else {
			logger.trace("Y distance was longer - starting vertical");
			while (yProgress <= yDistance) {

				Coordinate corridor = new Coordinate(
						(int) (roomTunnelStart.getX() + xProgress * Math.signum(xEnd - xStart)),
						(int) (roomTunnelStart.getY() + yProgress * Math.signum(yEnd - yStart)), map.getId());
				logger.trace("Flipping tile: " + corridor);
				map.setTile(corridor,
						new Tile(corridor, map.getStyle().getTileTypeMapping().get(TileType.OPEN), TileType.OPEN));
				if (yProgress >= kinkPoint && xProgress < xDistance) {
					xProgress++;
				} else {
					yProgress++;
				}
			}
		}

	}

	private Room findNearestRoom(Room room, List<Room> rooms, TileMap map) {
		double nearestDistance = Double.MAX_VALUE;
		Room nearestRoom = null;
		Coordinate roomMidPoint = SpaceLogic.getRoomMidpoint(room.getX(), room.getX2(), room.getY(), room.getY2(),map);
		for (Room otherRoom : rooms) {
			if (!otherRoom.equals(room)) {
				Coordinate otherRoomMidPoint = SpaceLogic.getRoomMidpoint(otherRoom.getX(), otherRoom.getX2(),
						otherRoom.getY(), otherRoom.getY2(), map);
				double tempDistance = SpaceLogic.getDistanceBetween(roomMidPoint, otherRoomMidPoint);
				if (tempDistance < nearestDistance) {
					nearestDistance = tempDistance;
					nearestRoom = otherRoom;
				}
			}
		}
		return nearestRoom;
	}

	private void fillRooms(List<Room> rooms, TileMap map) {
		for (Room room : rooms) {
			logger.trace("Filling room: " + room);
			logger.trace("In map  size " + map.getTileMap().size() + " x " + map.getTileMap().get(0).size());
			for (List<Tile> row : room.getTiles()) {
				for (Tile tile : row) {
					map.setTile(tile.getCoordinate(), tile);
				}
			}
		}

	}

	private void fillSolid(TileMap map) {
		List<List<Tile>> tiles = map.getTileMap();
		for (int i = 0; i < getySize(); i++) {
			tiles.add(new ArrayList<>());

			for (int j = 0; j < getxSize(); j++) {
				tiles.get(i).add(new Tile(new Coordinate(i, j, map.getId()),
						map.getStyle().getTileTypeMapping().get(TileType.WALL), TileType.WALL));
			}

		}

	}

}
