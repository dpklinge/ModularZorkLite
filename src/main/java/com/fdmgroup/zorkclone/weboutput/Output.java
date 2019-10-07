package com.fdmgroup.zorkclone.weboutput;

import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.commands.GetLists;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.webcontrollers.websockets.OutputEndpoint;

public class Output {
	public static void outputToTarget(Player player, String string) {
		System.out.println("Message to target:");
		System.out.println(string);
		System.out.println("Target name: "+player.getDisplayName());
		if (Main.isWeb) {
			OutputEndpoint.broadcastSingle(OutputEndpoint.users.get(player.getUser()), "<br/>" + string);
		}
		System.out.println("Finished outputToTarget");
		

	}

	public static void outputToRoomExceptTarget(Player player, Player fightable, String outputOther) {
		if (Main.isWeb) {
			GetLists getLists = new GetLists();
			List<Player> playerList = getLists.getListPlayers(player);
			for (Player playerTarget : playerList) {
				if (!fightable.getName().equalsIgnoreCase(playerTarget.getName())) {
					outputToTarget(playerTarget, outputOther);
				}

			}
		}

	}

	public static void outputToRoom(Player player, String string) {
		if (Main.isWeb) {
			GetLists getLists = new GetLists();
			List<Player> playerList = getLists.getListPlayers(player);
			for (Player playerTarget : playerList) {
				outputToTarget(playerTarget, string);
			}
		}

	}

	public static void outputToTargetRoom(Player player, Room room, String string) {
		System.out.println("Outputting to room.");
		System.out.println("Room: "+room.getDisplayName());
		System.out.println("Message: "+string);
		System.out.println("Player: "+player.getName());
		List<Player> playerList = new ArrayList<>();
		PlayerReader reader = Main.getPlayerReader();
		for (String playerName : room.getPlayersPresent()) {

			if (!playerName.equalsIgnoreCase(player.getName())) {

				playerList.add(reader.readPlayerByName(playerName));
			}
		}
		System.out.println("List of players: "+playerList);
		if (Main.isWeb) {
			for (Player playerTarget : playerList) {
				Output.outputToTarget(playerTarget, string);
			}
		}
	}

	public static void outputToAll(Player player, String string) {
		string = capitalize(string);
		if (Main.isWeb) {
			OutputEndpoint.broadcast(string);
		}
		System.out.println(string);

	}

	public static String capitalize(String string) {
		if(!string.isEmpty()){
		string = string.substring(0, 1).toUpperCase() + string.substring(1);
		}else{
			System.out.println("Warning- attempted to capitalize empty string");
		}
		return string;
	}

	public static String needsAThe(String string) {
		if (Character.isUpperCase(string.charAt(0))) {

			return "";
		} else {

			return "the ";

		}
	}
}
