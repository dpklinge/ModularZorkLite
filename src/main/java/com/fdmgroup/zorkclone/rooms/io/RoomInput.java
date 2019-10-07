package com.fdmgroup.zorkclone.rooms.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.io.ActorReader;
import com.fdmgroup.zorkclone.input.MenuInput;
import com.fdmgroup.zorkclone.items.io.ItemReader;
import com.fdmgroup.zorkclone.rooms.Direction;
import com.fdmgroup.zorkclone.rooms.Room;

public class RoomInput {
	private BufferedReader reader;

	public RoomInput(BufferedReader reader) {
		super();
		this.reader = reader;
	}

	public Room getRoom() {
		try {
			Room room = new Room();
			room = getId(room);
			if (room == null) {
				return null;
			}

			room.displayDetails();
			room = getDisplayName(room);
			if (room == null) {
				return null;
			}

			room.displayDetails();
			room = getDescription(room);
			if (room == null) {
				return null;
			}

			room.displayDetails();
			room = getItems(room);
			if (room == null) {
				return null;
			}

			room.displayDetails();
			room = getActors(room);
			if (room == null) {
				return null;
			}

			room.displayDetails();
			room = getExits(room);

			return room;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public void editHub(String choice, Room room) throws IOException {
		if (choice.equals("ID")) {
			room = getId(room);
		} else if (choice.equals("NAME")) {
			room = getDisplayName(room);
		} else if (choice.equals("DISPLAY")) {
			room = getDescription(room);
		} else if (choice.equals("ITEMS")) {
			room = getItems(room);
		} else if (choice.equals("ACTORS")) {
			room = getActors(room);
		} else if (choice.equals("EXITS")) {
			room = getExits(room);
		}
		RoomReader reader = Main.getRoomReader(Main.baseGamePath);
		reader.writeRoom(room);

	}

	private Room getExits(Room room) throws IOException {
		List<Direction> directions = new ArrayList<>();
		Map<Direction, String> mapping = new HashMap<>();
		while (true) {
			System.out.println("Please enter an exit direction, or type 'q' to skip.");
			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				room.setDirections(directions);
				room.setMapping(mapping);
				return room;
			} else {
				try {
					Direction direction = Direction.valueOf(input.toUpperCase());
					System.out.println("Please enter the id name of the room located in the " + direction
							+ " direction, or type 'q' to cancel current mapping.");
					input = reader.readLine();
					if (input.equals("q") || input.equals("Q")) {
						continue;
					} else {
						directions.add(direction);
						mapping.put(direction, input);
					}

				} catch (Exception e) {
					System.out.println("I'm sorry, that was not a valid direction.");
					continue;
				}

			}
		}
	}

	private Room getActors(Room room) throws IOException {

		List<String> actors = new ArrayList<>();
		while (true) {
			System.out.println("Please enter the name of an actor in this room, or type 'q' to skip.");
			// Specifically, you can use duplicate actors, but they share a file
			// and consequently a state. Hit one and you hit them all.
			System.out.println("Note that this game does not currently support duplicate actors.");
			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				room.setActorsPresent(actors);
				return room;
			} else {
				try {
					ActorReader reader = Main.getActorReader(Main.baseGamePath);
					reader.readActor(input);
					actors.add(input);
				} catch (Exception e) {
					System.out.println("That actor does not exist! Feel free to skip and edit it in later.");
				}

			}
		}
	}

	private Room getItems(Room room) throws IOException {

		List<String> items = new ArrayList<>();
		while (true) {
			System.out.println("Please enter the name of an item in this room, or type 'q' to skip.");
			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				room.setItemsPresent(items);
				return room;
			} else {
				try {
					ItemReader reader = Main.getItemReader(Main.baseGamePath);
					reader.readItem(input);
					items.add(input);
				} catch (Exception e) {
					System.out.println("That item does not exist! Feel free to skip and edit it in later.");
				}
			}
		}

	}

	private Room getDescription(Room room) throws IOException {
		System.out.println("Please enter room description, or type 'q' to cancel.");

		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			room.setDescription(input);
		}
		return room;
	}

	private Room getDisplayName(Room room) throws IOException {
		System.out.println("Please enter the display name of the room, or type 'q' to cancel.");

		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			room.setDisplayName(input);
		}
		return room;
	}

	private Room getId(Room room) throws IOException {
		MenuInput menu = new MenuInput(reader);
		System.out.println("Please enter the id name of the room, or type 'q' to cancel.");

		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			Path path = Paths.get(Main.baseGamePath.toString(), "/rooms/" + input + ".txt");
			File file = path.toFile();
			if (file.exists()) {
				if (!menu.confirmOverride(input)) {
					return null;
				}
			}
			room.setName(input);
			return room;
		}

	}
}
