package com.fdmgroup.zorkclone.input;

import java.io.BufferedReader;
import java.io.IOException;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.Actor;
import com.fdmgroup.zorkclone.actors.io.ActorInput;
import com.fdmgroup.zorkclone.actors.io.ActorReader;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.io.ItemInput;
import com.fdmgroup.zorkclone.items.io.ItemReader;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.rooms.io.RoomInput;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;

public class EditInput {
	private BufferedReader reader;

	public EditInput(BufferedReader reader) {
		super();
		this.reader = reader;
	}

	public void doEdit() {
		MenuInput menu = new MenuInput(reader);

		while (true) {
			String userChoice = menu.editMenu();
			if (userChoice.equals("ROOM")) {
				roomEditSelection();
			} else if (userChoice.equals("ITEM")) {
				itemEditSelection();
			} else if (userChoice.equals("ACTOR")) {
				actorEditSelection();
			} else if (userChoice.equals("QUIT")) {
				return;
			}
		}

	}

	

	private void roomEditSelection() {
		while (true) {
			System.out.println("Which room would you like to edit?");
			try {
				String roomName = reader.readLine();
				RoomReader reader = Main.getRoomReader(Main.baseGamePath);
				Room room = reader.readRoom(roomName);
				editRoom(room);
				return;

			} catch (Exception e) {
				System.out.println("That room does not seem to exist... Did you mispell it?");
			}
		}

	}
	
	private void itemEditSelection() {
		while (true) {
			System.out.println("Which item would you like to edit?");
			try {
				String itemName = reader.readLine();
				ItemReader reader = Main.getItemReader(Main.baseGamePath);
				Item item = reader.readItem(itemName);
				editItem(item);
				return;

			} catch (Exception e) {
				System.out.println("That room does not seem to exist... Did you mispell it?");
			}
		}
	}

	private void actorEditSelection() {
		while (true) {
			System.out.println("Which actor would you like to edit?");
			try {
				String actorName = reader.readLine();
				ActorReader reader = Main.getActorReader(Main.baseGamePath);
				Actor actor = reader.readActor(actorName);
				editActor(actor);
				return;

			} catch (Exception e) {
				System.out.println("That actor does not seem to exist... Did you mispell it?");
			}
		}

	}

	private void editRoom(Room room) {
		MenuInput menu = new MenuInput(reader);
		RoomInput roomInput = new RoomInput(reader);
		while (true) {
			room.displayDetails();
			String choice = menu.editRoomMenu();
			if (choice.toUpperCase().equals("QUIT")) {
				return;
			}
			try {
				roomInput.editHub(choice, room);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
	
	private void editItem(Item item) {
		MenuInput menu = new MenuInput(reader);
		ItemInput itemInput = new ItemInput(reader);
		while (true) {
			item.displayDetails();
			String choice = menu.editItemMenu(item);
			if (choice.toUpperCase().equals("QUIT")) {
				return;
			}
			try {
				itemInput.editHub(choice, item);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
	}

	private void editActor(Actor actor) {
		MenuInput menu = new MenuInput(reader);
		ActorInput actorInput = new ActorInput(reader);
		while (true) {
			actor.displayDetails();
			String choice = menu.editActorMenu();
			if (choice.toUpperCase().equals("QUIT")) {
				return;
			}
			try {
				actorInput.editHub(choice, actor);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	
}
