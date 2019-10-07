package com.fdmgroup.zorkclone;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fdmgroup.zorkclone.actors.io.ActorReader;
import com.fdmgroup.zorkclone.actors.io.InMemoryActorReader;
import com.fdmgroup.zorkclone.actors.io.JsonActorReader;
import com.fdmgroup.zorkclone.combat.CombatChecker;
import com.fdmgroup.zorkclone.effects.effectio.EffectClassReader;
import com.fdmgroup.zorkclone.effects.effectio.EffectReader;
import com.fdmgroup.zorkclone.input.UserInput;
import com.fdmgroup.zorkclone.items.io.InMemoryItemReader;
import com.fdmgroup.zorkclone.items.io.ItemReader;
import com.fdmgroup.zorkclone.items.io.JsonItemReader;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.InMemoryPlayerReader;
import com.fdmgroup.zorkclone.player.io.JsonPlayerReader;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.rooms.io.InMemoryRoomReader;
import com.fdmgroup.zorkclone.rooms.io.JsonRoomReader;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;
import com.fdmgroup.zorkclone.user.JsonUserReader;
import com.fdmgroup.zorkclone.user.UserReader;

public class Main {
	public static boolean isWeb = true;
	
	public static Path baseGamePath ;
	public static Path savedGamePath ;
	public static Path userPath ;
	public static String startRoom = "StartRoom";
	
	//Initializing filepaths
	static{
		System.out.println("Entering static initializer block");
		Logger logger = LoggerFactory.getLogger("FileLogger");
		Class<Main> thisClass= Main.class;
		ClassLoader loader = thisClass.getClassLoader();
		System.out.println("Attempting pattern match");
		
		String newGamePattern = "SourceFiles/";
		URL newGamePathURL = loader.getResource(newGamePattern);
		baseGamePath = Paths.get(newGamePathURL.getPath().substring(1));
		logger.debug("Targeted old file path: "+baseGamePath);
		System.out.println("Targeted old file path: "+baseGamePath);
		
		String saveGamePattern = "SavedFiles/";
		URL savedGamePathURL = loader.getResource(saveGamePattern);
		savedGamePath = Paths.get(savedGamePathURL.getPath().substring(1));
		logger.debug("Targeted new file path: "+savedGamePath.toString());
		System.out.println("Targeted new file path: "+savedGamePath.toString());
		
		String userGamePattern = "users/";
		URL userPathURL = loader.getResource(userGamePattern);
		userPath = Paths.get(userPathURL.getPath().substring(1));
		logger.debug("Targeted user file path: "+userPath.toString());
		System.out.println("Targeted user file path: "+userPath.toString());
		
	}
	public static RoomReader getRoomReader(Path path) {
		if (isWeb) {
			return new InMemoryRoomReader();
		} else {
			return new JsonRoomReader(path);
		}

	}

	public static ActorReader getActorReader(Path path) {
		if (isWeb) {
			return new InMemoryActorReader();
		} else {
			return new JsonActorReader(path);
		}

	}

	public static ItemReader getItemReader(Path path) {
		if (isWeb) {
			return new InMemoryItemReader();
		} else {
			return new JsonItemReader(path);
		}
	}

	public static PlayerReader getPlayerReader() {
		if (isWeb) {
			return new InMemoryPlayerReader();
		} else {
			return new JsonPlayerReader();
		}
	}

	public static UserReader getUserReader() {
		return new JsonUserReader();
	}

	public static EffectReader getEffectReader() {
		return new EffectClassReader();
	}

	public static void main(String[] args) {
		isWeb = false;
		Player player;
		baseGamePath = Paths.get("src/main/resources/SourceFiles");
		savedGamePath = Paths.get("src/main/resources/SavedFiles");
		userPath = Paths.get("src/main/resources/users/");

		PlayerReader playerReader = getPlayerReader();
		player = playerReader.getPlayer("System", "me");
		if (player == null) {
			player = new Player();
			playerReader.writePlayer(player);
		}

		ZorkController controller = new ZorkController();
		UserInput input = new UserInput();
		String menuResult = input.askNewGame(player);
		Room room = null;

		if (menuResult.equals("NEWGAME")) {
			player = controller.newGame();
			RoomReader reader = getRoomReader(Main.baseGamePath);
			room = reader.readRoom(startRoom);
			CombatChecker.enterRoom(player, room);
			System.out.println(room.displayRoom(player));
			controller.startGame(room, player);
		} else if (menuResult.equals("CONTINUE")) {
			RoomReader reader = getRoomReader(Main.savedGamePath);
			room = reader.readRoom(player.getCurrentRoom().getName());
			CombatChecker.enterRoom(player, room);
			System.out.println(room.displayRoom(player));
			controller.startGame(room, player);
		} else if (menuResult.equals("CREATORMODE")) {
			controller.createMode();
		} else {

		}

	}

}
