package com.fdmgroup.zorkclone.commands;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.combat.CombatChecker;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.rooms.Direction;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;
import com.fdmgroup.zorkclone.weboutput.Output;

public class DirectionalCommands {
	public boolean isDirectional(String command) {
		command = command.toUpperCase();
		if (command.equals("NORTH") || command.equals("EAST") || command.equals("SOUTH") || command.equals("WEST")
				|| command.equals("NORTHEAST") || command.equals("SOUTHEAST") || command.equals("SOUTHWEST")
				|| command.equals("NORTHWEST") || command.equals("UP") || command.equals("DOWN") || command.equals("N")
				|| command.equals("E") || command.equals("S") || command.equals("W") || command.equals("NE")
				|| command.equals("NW") || command.equals("SE") || command.equals("SW") || command.equals("U")
				|| command.equals("D")) {
			return true;
		}

		return false;

	}

	public void doDirection(Player player,String[] commands) {
		String command = commands[0].toUpperCase();
		if (command.equals("N")) {
			command = "NORTH";
		} else if (command.equals("S")) {
			command = "SOUTH";
		} else if (command.equals("E")) {
			command = "EAST";
		} else if (command.equals("W")) {
			command = "WEST";
		} else if (command.equals("NE")) {
			command = "NORTHEAST";
		} else if (command.equals("NW")) {
			command = "NORTHWEST";
		} else if (command.equals("SE")) {
			command = "SOUTHEAST";
		} else if (command.equals("SW")) {
			command = "SOUTHWEST";
		} else if (command.equals("U")) {
			command = "UP";
		} else if (command.equals("D")) {
			command = "DOWN";
		}

		if (player.getCurrentRoom().getDirections().contains(Direction.valueOf(command))) {
			if (CombatChecker.canLeaveRoom(player.getCurrentRoom(), Direction.valueOf(command))) {
				RoomReader reader = Main.getRoomReader(Main.baseGamePath);
				Room newRoom =reader.readRoom((String) player.getCurrentRoom().getMapping().get(Direction.valueOf(command)));
				CombatChecker.leaveRoom(player, newRoom, Direction.valueOf(command));
				Output.outputToTarget(player,"You travel " + Direction.getEnumString(Direction.valueOf(command)) + ".");
				CombatChecker.enterRoom(player, newRoom);
				Output.outputToTarget(player,player.getCurrentRoom().displayRoom(player));
				
				

			}
		} else {
			Output.outputToTarget(player,"You can't go that direction!");
		}

	}


}
