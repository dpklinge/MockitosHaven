package com.fdmgroup.mockitoshaven.game.logic;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fdmgroup.mockitoshaven.game.dungeon.Coordinate;
import com.fdmgroup.mockitoshaven.game.dungeon.Locatable;
import com.fdmgroup.mockitoshaven.game.dungeon.Tile;
import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;

@Component
public class SpaceLogic {
	private static Random random = new Random();
	private static Logger logger = LogManager.getLogger();
	public static double getDistanceBetween(Locatable npc, Locatable pc) {
		return getDistanceBetween(npc.getCoordinate(), pc.getCoordinate());

	}

	public static double getDistanceBetween(Coordinate coord1, Coordinate coord2) {
		return Math.sqrt(Math.pow(coord2.getX() - coord1.getX(), 2) + Math.pow(coord2.getY() - coord1.getY(), 2));

	}

	public static Coordinate nextStepBetweenPoints(Coordinate start, Coordinate end, TileMap map) {
		logger.trace("Checking if path exist between " + start + " and " + end);
//		map.consoleDisplay();
		// setup for A*
		HashMap<Coordinate, Coordinate> parentMap = new HashMap<>();
		HashSet<Coordinate> visited = new HashSet<Coordinate>();
		Map<Coordinate, Double> distances = new HashMap<>();

		Queue<Coordinate> priorityQueue = initQueue(start);

		// enque StartNode, with distance 0
		distances.put(start, 0.0);
		priorityQueue.add(start);
		Coordinate current = null;

		while (!priorityQueue.isEmpty()) {
			current = priorityQueue.remove();

			if (!visited.contains(current)) {
				visited.add(current);
				// if last element in PQ reached
				if (current.equals(end))
					return reconstructPath(parentMap, start, end);

				Set<Coordinate> neighbors = getNeighbors(current, map);
				logger.trace("Neighbors: "+neighbors);
				for (Coordinate neighbor : neighbors) {
					if (!visited.contains(neighbor)) {
						double predictedDistance = getDistanceBetween(neighbor, end);
						double neighborDistance = getDistanceBetween(neighbor, current);
						double totalDistance = getDistanceBetween(current, start) + neighborDistance
								+ predictedDistance;
						if (distances.get(neighbor) == null || totalDistance < distances.get(neighbor)) {
							distances.put(neighbor, totalDistance);
							parentMap.put(neighbor, current);
							priorityQueue.add(neighbor);
						}
					}
				}
			}
		}
		return null;
	}

	private static Set<Coordinate> getNeighbors(Coordinate current, TileMap map) {
		Set<Coordinate> neighbors = new HashSet<>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {

				Coordinate co = new Coordinate(current.getX() + i, current.getY() + j, map.getId());
				if (map.isInBounds(co)) {
					if (map.getTile(co).isNavigable() && !co.equals(current)) {
						logger.trace("Adding neighbor to "+current+": "+co);
						neighbors.add(co);
					}
				}
			}
		}
		return neighbors;
	}

	private static Coordinate reconstructPath(HashMap<Coordinate, Coordinate> parentMap, Coordinate start,
			Coordinate goal) {
		Coordinate current = goal;
		Coordinate previous = null;
		while (parentMap.get(current) != null) {
			previous = current;
			current = parentMap.get(current);
		}
		return previous;
	}

	public static PriorityQueue<Coordinate> initQueue(Coordinate start) {
		return new PriorityQueue<>(10, new Comparator<Coordinate>() {
			public int compare(Coordinate x, Coordinate y) {
				if (getDistanceBetween(x, start) < getDistanceBetween(y, start)) {
					return -1;
				}
				if (getDistanceBetween(x, start) > getDistanceBetween(y, start)) {
					return 1;
				}
				return 0;
			};
		});
	}
	public static Coordinate getRoomMidpoint(int x, int x2, int y, int y2, TileMap map) {
		Coordinate midPoint = getRectangleMidpoint(x, x2, y, y2);
		midPoint.setMapId(map.getId());
		return midPoint;
	}

	private static Coordinate getRectangleMidpoint(int x, int x2, int y, int y2) {

		return new Coordinate(x + (x2 - x) / 2, y + (y2 - y) / 2, null);
	}

	public static Tile randomAdjacentTile(Coordinate coordinate, TileMap map) {
		Set<Coordinate> openNeighbors = getNeighbors(coordinate, map);
		Object[] coordinates=openNeighbors.toArray();
		
		return map.getTile((Coordinate) coordinates[random.nextInt(coordinates.length)]);
	}

}
