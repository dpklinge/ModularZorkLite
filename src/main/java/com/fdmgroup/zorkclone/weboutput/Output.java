package com.fdmgroup.zorkclone.weboutput;

import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.commands.ListFetchUtil;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.webcontrollers.websockets.WebsocketOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Output {
	@Autowired
	private WebsocketOutput output;
	@Autowired
	private ListFetchUtil listUtil;
	public void outputToTarget(Player player, String string) {
		System.out.println("Message to target:");
		System.out.println(string);
		System.out.println("Target name: "+player.getDisplayName());
		if (Main.isWeb) {
			output.broadcastSpecific(string, player.getUser().getUsername());
		}
		System.out.println("Finished outputToTarget");
		

	}

	public void outputToRoomExceptTarget(Player player, Player fightable, String outputOther) {
		if (Main.isWeb) {
			List<String> usernames = listUtil.getListPlayers(player).stream().map(it -> it.getUser().getUsername()).filter(it -> !it.equalsIgnoreCase(player.getUser().getUsername())&&!it.equalsIgnoreCase(fightable.getUser().getUsername())).toList();
			output.broadcastSpecific(outputOther, usernames);
		}

	}

	public void outputToRoom(Player player, String string) {
		if (Main.isWeb) {
			List<String> usernames = listUtil.getListPlayers(player).stream().map(it -> it.getUser().getUsername()).toList();
			output.broadcastSpecific(string, usernames);
		}

	}

	public void outputToTargetRoom(Player player, Room room, String string) {
		if (Main.isWeb) {
		System.out.println("Outputting to room.");
		System.out.println("Room: "+room.getDisplayName());
		System.out.println("Message: "+string);
		System.out.println("Player: "+player.getName());
		List<String> usernames = new ArrayList<>();
		PlayerReader reader = Main.getPlayerReader();
		for (String playerName : room.getPlayersPresent()) {
			if (!playerName.equalsIgnoreCase(player.getName())) {
				usernames.add(reader.readPlayerByName(playerName).getUser().getUsername());
			}
		}
		System.out.println("List of users: "+usernames);
		output.broadcastSpecific(string, usernames);
		}
	}

	public void outputToAll(Player player, String string) {
		string = capitalize(string);
		if (Main.isWeb) {
			output.broadcastAll(string);
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
