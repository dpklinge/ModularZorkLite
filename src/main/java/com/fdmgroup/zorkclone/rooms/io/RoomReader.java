package com.fdmgroup.zorkclone.rooms.io;

import com.fdmgroup.zorkclone.rooms.Room;

public interface RoomReader {
	public Room readRoom(String roomId);
	public Room writeRoom(Room room);
	public Room deleteRoom(String roomId);

}
