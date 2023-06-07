package com.fdmgroup.zorkclone;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.zorkclone.actors.Actor;
import com.fdmgroup.zorkclone.actors.io.ActorReader;
import com.fdmgroup.zorkclone.commands.CommandProcessor;
import com.fdmgroup.zorkclone.input.UserInput;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.io.ItemReader;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;
@Component
public class ZorkController {
	@Autowired
	private CommandProcessor processor;
	public void loadRooms() {
		System.out.println("Loading rooms");
		Path path = Paths.get(Main.baseGamePath+"/rooms/");
		Path newPath = Paths.get(Main.savedGamePath+"/rooms/");
		System.out.println("Loading from: "+path.toString());
		System.out.println("To: "+newPath.toString());
		File dir = new File(path.toString());
		File[] directoryListing = dir.listFiles();
		RoomReader reader= Main.getRoomReader(Main.savedGamePath);
		if (directoryListing != null) {
			for (File child : directoryListing) {

				try {
					byte[] data = Files.readAllBytes(child.toPath());
					Files.write(Paths.get(newPath.toString() + "/" + child.getName()), data);
					reader.readRoom(FilenameUtils.removeExtension(child.getName()));
				} catch (IOException x) {
					System.err.println("Problem creating file - check document paths");
					System.err.println(x);
				}
			}
		} else {

		}
	}

	public void loadItems() {
		System.out.println("Loading items");
		Path path = Paths.get(Main.baseGamePath+"/items/");
		Path newPath = Paths.get(Main.savedGamePath+"/items/");
		File dir = new File(path.toString());
		File[] directoryListing = dir.listFiles();
		ItemReader reader= Main.getItemReader(Main.savedGamePath);
		if (directoryListing != null) {
			for (File child : directoryListing) {

				try {
					byte[] data = Files.readAllBytes(child.toPath());
					Files.write(Paths.get(newPath.toString() + "/" + child.getName()), data);
					reader.readItem(FilenameUtils.removeExtension(child.getName()));
				} catch (IOException x) {
					System.err.println("Problem creating file - check document paths");
					x.printStackTrace();
				}
			}
		}
	}

	public void loadActors() {
		System.out.println("Loading actors");
		Path path = Paths.get(Main.baseGamePath+"/actors/");
		Path newPath = Paths.get(Main.savedGamePath+"/actors/");
		File dir = new File(path.toString());
		File[] directoryListing = dir.listFiles();
		ActorReader reader= Main.getActorReader(Main.savedGamePath);
		if (directoryListing != null) {
			for (File child : directoryListing) {

				try {
					byte[] data = Files.readAllBytes(child.toPath());

					Files.write(Paths.get(newPath.toString() + "/" + child.getName()), data);
					reader.readActor(FilenameUtils.removeExtension(child.getName()));
				} catch (IOException x) {
					System.err.println("Problem creating file - check document paths");
					System.err.println(x);
				}
			}
		} 
	}

	public void startGame(Room room, Player player) {
		player.setCurrentRoom(room);
		UserInput input = new UserInput();
		String[] commands;
		boolean loopContinue = true;
		while (loopContinue) {

			commands = input.getCommand();
			player=processor.processCommand(commands, player);
		}
	}

	public Player newGame() {
		loadRooms();
		loadItems();
		loadActors();
		Player player = new Player();
		PlayerReader reader = Main.getPlayerReader();
		reader.writePlayer(player);
		return player;
				
	}

	public void createMode() {
		boolean continueLoop = true;
		UserInput input = new UserInput();
		while (continueLoop) {

			String type = input.getType();
			if (type.equals("ROOM")) {
				Room room = input.getRoom();
				if (room != null) {
					RoomReader reader = Main.getRoomReader(Main.baseGamePath);
					reader.writeRoom(room);
					RoomReader liveReader = Main.getRoomReader(Main.savedGamePath);
					liveReader.writeRoom(room);
				}

			} else if (type.equals("ITEM")) {
				Item item = input.getItem();
				if (item != null) {
					ItemReader reader = Main.getItemReader(Main.baseGamePath);
					reader.writeItem(item);
					ItemReader liveReader = Main.getItemReader(Main.savedGamePath);
					liveReader.writeItem(item);
				}

			} else if (type.equals("ACTOR")) {
				Actor actor = input.getActor();
				if (actor != null) {
					ActorReader reader = Main.getActorReader(Main.baseGamePath);
					reader.writeActor(actor);
					ActorReader liveReader = Main.getActorReader(Main.savedGamePath);
					liveReader.writeActor(actor);
				}
			} else if (type.equals("EDIT")) {
				input.doEdit();
			} else {
				break;
			}
		}
	}
}
