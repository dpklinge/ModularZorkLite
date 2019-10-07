package com.fdmgroup.zorkclone.rooms.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.zorkclone.rooms.Room;

public class JsonRoomReader implements RoomReader {
	private ObjectMapper mapper = new ObjectMapper();

	{
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	private Path storagePath;

	public JsonRoomReader(Path storagePath) {
		super();
		this.storagePath = storagePath;
	}

	@Override
	public Room readRoom(String roomId) {

		Room room = null;
		try {
			room = mapper.readValue(new File((storagePath + "\\rooms\\" + roomId + ".txt")), Room.class);
		} catch (JsonParseException |JsonMappingException e) {
			System.err.println("Json read failed");
		} catch (IOException e) {
			System.err.println("Failed to read room from file path: " + (storagePath + "\\rooms\\" + roomId + ".txt"));
		}
		return room;
	}

	@Override
	public Room writeRoom(Room room) {
		try {
			mapper.writeValue(new File(storagePath + "\\rooms\\" + room.getName() + ".txt"), room);
		} catch (JsonGenerationException e) {
			System.err.println("JSON failed");
		} catch (JsonMappingException e) {
			System.err.println("JSON failed");
		} catch (IOException e) {
			System.err.println("Failed room write on path " + storagePath + "\\rooms\\" + room.getName() + " not found!");
		}
		return room;
	}

	@Override
	public Room deleteRoom(String roomId) {
		// TODO
		return null;
	}

	

}
