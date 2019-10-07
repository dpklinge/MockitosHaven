package com.fdmgroup.mockitoshaven.game.generators;

import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;

@Component
public class RoomGenerator {
	private Logger logger=  LogManager.getLogger();
	private Random random = new Random();

	public List<Room> generateRoom(TileMap map, List<Room> rooms) {
			logger.trace("Generating room");
	        int width = map.getStyle().getMinRoomSize() + random.nextInt(map.getStyle().getMaxRoomSize() - map.getStyle().getMinRoomSize() + 1);
	        int height = map.getStyle().getMinRoomSize() + random.nextInt(map.getStyle().getMaxRoomSize() - map.getStyle().getMinRoomSize() + 1);
	        int x = random.nextInt(map.getTileMap().size() - width - 4) + 2;
	        int y = random.nextInt(map.getTileMap().get(0).size() - height - 4) + 2;
	      

	        Room newRoom = new Room( width, height,x, y, map);
	 
	        boolean failed = false;
	        for (Room room: rooms) {
	            if (intersects(room, newRoom)) {
	            	logger.trace("Room intersect, room failed");
	                failed = true;
	                break;
	            }
	        }
	        if (failed) {
	        	logger.trace("Generating backup room");
	            return generateRoom(map, rooms);
	        }else { 
	        	logger.trace("Room success adding to integration queue");
	        	rooms.add(newRoom);
	        	return rooms;
	        }
	        
		
	}
	
	private boolean intersects(Room room1, Room room2) {
        return (room1.getX() <= room2.getX2() && room1.getX2() >= room2.getX() &&
        		room1.getY() <= room2.getY2() &&  room1.getY2() >= room2.getY() );
    }

}
