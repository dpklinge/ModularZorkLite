package com.fdmgroup.zorkclone.rooms.io;

import java.util.HashMap;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.rooms.Room;

public class InMemoryRoomReader implements RoomReader {
	private static HashMap<String, Room> rooms = new HashMap<>();
	
	@Override
	public Room readRoom(String roomId) {
		if(rooms.containsKey(roomId)){
			return rooms.get(roomId);
		}else{
			RoomReader reader = new JsonRoomReader(Main.baseGamePath);
			Room room = reader.readRoom(roomId);
			rooms.put(roomId, room);
			return room;
		}
	}

	@Override
	public synchronized Room writeRoom(Room room) {
		rooms.put(room.getName(), room);
		return room;
	}

	public static HashMap<String, Room> getRooms() {
		return rooms;
	}

	@Override
	public Room deleteRoom(String roomId) {
		return rooms.remove(roomId);
	}

	



}
