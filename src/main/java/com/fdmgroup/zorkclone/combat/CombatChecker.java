package com.fdmgroup.zorkclone.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.Actor;
import com.fdmgroup.zorkclone.actors.Behaviour;
import com.fdmgroup.zorkclone.actors.io.ActorReader;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.rooms.Direction;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;
import com.fdmgroup.zorkclone.weboutput.Output;

public class CombatChecker {
	public static List<Actor> aggressors = new ArrayList<>();
	public static HashMap<Actor, CombatTimer> timers = new HashMap<>();
	private static ActorReader reader = Main.getActorReader(Main.savedGamePath);
	private static RoomReader roomReader = Main.getRoomReader(Main.savedGamePath);

	public static boolean leaveRoom(Player player, Room newRoom, Direction direction) {
		List<Actor> removeList = new ArrayList<>();
		player.setCurrentRoom(roomReader.readRoom(player.getCurrentRoom().getName()));
		Room oldRoom = player.getCurrentRoom();
		if (direction != null) {
			Output.outputToRoom(player,
					player.getDisplayName() + " leaves the room heading " + Direction.getEnumString(direction) + ".");
		}
		player.getCurrentRoom().getPlayersPresent().remove(player.getName());
		roomReader = Main.getRoomReader(Main.savedGamePath);
		roomReader.writeRoom(oldRoom);
		for (Actor actor : aggressors) {
			actor = reader.readActor(actor.getName());
			if (!actor.getBehaviours().contains(Behaviour.FOLLOW)
					|| !oldRoom.getActorsPresent().contains(actor.getName())||newRoom==null) {
				timers.get(actor).stopAttackThread(actor);
				removeList.add(actor);
			} else if (actor.getBehaviours().contains(Behaviour.FOLLOW) && !actor.getIsDead()) {

				player.getCurrentRoom().getActorsPresent().remove(actor.getName());
				newRoom.getActorsPresent().add(actor.getName());
				if (Main.isWeb) {
					Output.outputToTarget(player, Output.capitalize(Output.needsAThe(actor.getDisplayName())
							+ actor.getDisplayName() + " follows you into the next room!"));
					Output.outputToRoom(player,Output.capitalize(Output.needsAThe(actor.getDisplayName()) + actor.getDisplayName()
							+ " follows " + player.getDisplayName() + " out of the room!"));
					Output.outputToTargetRoom(player,newRoom, Output.capitalize(Output.needsAThe(actor.getDisplayName())
							+ actor.getDisplayName() + " follows " + player.getDisplayName() + " into the room!"));
				}
				String followText = actor.getDisplayName() + " follows you into the next room!";
				System.err.println(followText.substring(0, 1).toUpperCase() + followText.substring(1));
				roomReader.writeRoom(player.getCurrentRoom());
				roomReader.writeRoom(newRoom);
			}
		}
		for (Actor actor : removeList) {
			aggressors.remove(actor);
			timers.remove(actor);
			checkOtherPlayers(player, actor, oldRoom);
		}
		return true;
	}

	private static void checkOtherPlayers(Player player, Actor actor, Room oldRoom) {
		//TODO transfer aggression
	}

	public static void enterRoom(Player player, Room newRoom) {
		System.out.println("Entering room(1).");
		System.out.println("Entering room(2). Player: "+player.getDisplayName());
		System.out.println("Entering room(3). Room: "+newRoom.getDisplayName());
		newRoom=roomReader.readRoom(newRoom.getName());
		if (!player.getIsDead()) {
			String otherOutput = player.getName() + " enters the room!";
			Output.outputToTargetRoom(player,newRoom,otherOutput);
		}
		newRoom.getPlayersPresent().add(player.getName());
		
		roomReader.writeRoom(newRoom);
		player.setCurrentRoom(newRoom);
		for (String actor : newRoom.getActorsPresent()) {
			Actor thisActor = reader.readActor(actor);
			if (thisActor.getBehaviours().contains(Behaviour.AGGRESSIVE) && (!aggressors.contains(thisActor))
					&& !thisActor.getIsDead()) {
				CombatTimer timer = new CombatTimer(thisActor, player);
				timer.startAttackThread(thisActor);
				timers.put(thisActor, timer);
				aggressors.add(thisActor);
			}
		}

	}

	public static boolean canLeaveRoom(Room currentRoom, Direction direction) {

		return true;
	}

	public static void addAggressor(Actor actor, Player player) {
		CombatTimer timer = new CombatTimer(actor, player);
		timer.startAttackThread(actor);
		timers.put(actor, timer);
		aggressors.add(actor);
	}

}
