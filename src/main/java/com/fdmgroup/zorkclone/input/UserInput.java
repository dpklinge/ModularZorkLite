package com.fdmgroup.zorkclone.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import com.fdmgroup.zorkclone.actors.Actor;
import com.fdmgroup.zorkclone.actors.io.ActorInput;
import com.fdmgroup.zorkclone.commands.CommandParser;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.io.ItemInput;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.rooms.io.RoomInput;

public class UserInput {
	private BufferedReader reader;

	private BufferedReader getReader() {
		if (!Objects.isNull(reader)) {
			return reader;
		} else {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			return reader;
		}
	}

	public String[] getCommand() {
		String[] commands = {};
		reader = getReader();
		boolean isValid = false;
		while (!isValid) {
			try {
				String commandLine = reader.readLine();
				commands = commandLine.split(" ");

				for (int i = 0; i < commands.length; i++) {

					commands[i] = commands[i];

				}

				CommandParser parser = new CommandParser();
				String command = parser.validateFirstCommand(commands);
				if (Objects.isNull(command) || command.length()<1) {
					System.out.println("You forgot to enter a command, dummy!");
					System.out.println("Type 'help' for help. Duh.");
					System.out.println("And hit enter when you're done typing it.");
					System.out.println("Pretty sure I needed to explain that to you.");
					System.out.println("Also, the single letter commands are shortcuts to make typing easier- e.g., L==LOOK");
				} else if (command.equals("Bad command")) {
					System.out.println("I'm sorry, I don't know how to do that.");
				} else {
					isValid = true;
				}

			} catch (IOException e) {
				System.err.println("Reader input failed?");
				e.printStackTrace();
			}

		}

		return commands;
	}

	public String askNewGame(Player player) {
		MenuInput menu = new MenuInput(getReader());

		return menu.askNewGame(player);

	}

	public String getType() {
		MenuInput menu = new MenuInput(getReader());

		return menu.getType();

	}

	public Room getRoom() {
		RoomInput room =new RoomInput(getReader());
		return room.getRoom();

	}

	public Item getItem() {
		ItemInput item = new ItemInput(getReader());
		return item.getItem();
	}

	

	public Actor getActor() {
		ActorInput actor = new ActorInput(getReader());
		
		return actor.getActor();
	}

	public void doEdit() {
		EditInput edit = new EditInput(getReader());
		edit.doEdit();
		
	}
}
