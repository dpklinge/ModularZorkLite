package com.fdmgroup.zorkclone.commands;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.math3.util.Precision;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.Actor;
import com.fdmgroup.zorkclone.actors.Behaviour;
import com.fdmgroup.zorkclone.actors.io.ActorReader;
import com.fdmgroup.zorkclone.combat.Combat;
import com.fdmgroup.zorkclone.combat.CombatChecker;
import com.fdmgroup.zorkclone.combat.Fightable;
import com.fdmgroup.zorkclone.effects.Effect;
import com.fdmgroup.zorkclone.effects.Effectables;
import com.fdmgroup.zorkclone.effects.effectio.EffectReader;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.ItemType;
import com.fdmgroup.zorkclone.items.io.ItemReader;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.JsonPlayerReader;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.rooms.Direction;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;
import com.fdmgroup.zorkclone.webcontrollers.websockets.OutputEndpoint;
import com.fdmgroup.zorkclone.weboutput.Output;

public class CommandProcessor {
	private Player player;
	private InventoryManager inventory = new InventoryManager();
	private Random random = new Random();

	public CommandProcessor(Player player) {
		this.player = player;
	}

	public Player processCommand(String[] commands) {
		player = refreshPlayer(player);
		player.setCurrentRoom(getRoom(player.getCurrentRoom().getName()));
		
		Effect effect = checkForEffects(commands);
		if (effect != null) {
			player = effect.doEffect(commands, this);
			updatePlayer(player);
			return player;
		}

		String firstCommand = commands[0].toUpperCase();
		DirectionalCommands directionalCommands = new DirectionalCommands();
		if (directionalCommands.isDirectional(firstCommand)) {
			directionalCommands.doDirection(player, commands);
		} else if (firstCommand.equalsIgnoreCase("GO")) {
			if (commands.length > 1 && directionalCommands.isDirectional(commands[1].toUpperCase())) {
				commands[0] = commands[1];
				directionalCommands.doDirection(player, commands);
			} else {
				Output.outputToTarget(player, "That isn't a direction you can go.");
			}
		} else if (firstCommand.equalsIgnoreCase("LOOK") || firstCommand.equalsIgnoreCase("L")) {
			doLook(commands);
		} else if (firstCommand.equalsIgnoreCase("QUIT") || firstCommand.equalsIgnoreCase("Q")) {
			doQuit();
			if (Main.isWeb) {
				return null;
			}
		} else if (firstCommand.equalsIgnoreCase("HELP")) {
			doHelp();
		} else if (firstCommand.equalsIgnoreCase("GET") || firstCommand.equalsIgnoreCase("GRAB") || firstCommand.equalsIgnoreCase("G")
				|| firstCommand.equalsIgnoreCase("TAKE")) {
			doGet(commands);
		} else if (firstCommand.equalsIgnoreCase("I") || firstCommand.equalsIgnoreCase("INVENTORY")) {
			doDisplayInventory();
		} else if (firstCommand.equalsIgnoreCase("DR") || firstCommand.equalsIgnoreCase("DROP")) {
			doDrop(commands);
		} else if (firstCommand.equalsIgnoreCase("X") || firstCommand.equalsIgnoreCase("EX") || firstCommand.equalsIgnoreCase("EXAMINE")) {
			doExamine(commands);
		} else if (firstCommand.equalsIgnoreCase("AT") || firstCommand.equalsIgnoreCase("ATTACK") || firstCommand.equalsIgnoreCase("HIT")
				|| firstCommand.equalsIgnoreCase("ASSAULT") || firstCommand.equalsIgnoreCase("K") || firstCommand.equalsIgnoreCase("KILL")
				|| firstCommand.equalsIgnoreCase("STAB") || firstCommand.equalsIgnoreCase("PUNCH")) {
			doAttackAttempt(commands);
		} else if (firstCommand.equalsIgnoreCase("SLEEP")) {
			doSleep();
		} else if (firstCommand.equalsIgnoreCase("WAKE")) {
			doWake();
		} else if (firstCommand.equalsIgnoreCase("WIELD")) {
			inventory.doWield(player, commands);
		} else if (firstCommand.equalsIgnoreCase("EQUIP") || firstCommand.equalsIgnoreCase("WEAR")) {
			inventory.doEquip(player, commands);
		} else if (firstCommand.equalsIgnoreCase("UNWIELD")) {
			inventory.doUnwield(player, commands);
		} else if (firstCommand.equalsIgnoreCase("UNEQUIP") || firstCommand.equalsIgnoreCase("REMOVE")) {
			inventory.doUnequip(player, commands);
		} else if (firstCommand.equalsIgnoreCase("CLEAR")) {
			doClear();
		} else if (firstCommand.equalsIgnoreCase("THROW")) {
			doThrow(commands);
		} else if (firstCommand.equalsIgnoreCase("SAY") || firstCommand.equalsIgnoreCase("TELL")) {
			doSpeak(commands);
		} else if (firstCommand.equalsIgnoreCase("YELL") || (firstCommand.equalsIgnoreCase("SHOUT"))) {
			doYell(commands);
		} else if (firstCommand.equalsIgnoreCase("GIVE")) {
			doGive(commands);
		} else if (firstCommand.equalsIgnoreCase("ALIAS")) {
			doAlias();
		} else {
			Output.outputToTarget(player, "I don't know how to do that...");
		}
		updatePlayer(player);
		return player;

	}
	public String[] capsCommands(String[] commands){
		for(int i=0;i<commands.length;i++){
			commands[i]=commands[i].toUpperCase();
		}
		return commands;
		
	}

	private void doAlias() {
		Output.outputToTarget(player, "Your aliases: " + player.getAliases());

	}

	private void doGive(String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Give WHAT?");
			return;
		}
		GetLists getLists = new GetLists();
		List<Player> playerList = getLists.getListPlayers(player);
		for (Player targetPlayer : playerList) {
			if (commands.length < 3) {
				Output.outputToTarget(player, "Give " + commands[1] + " WHAT?");
			}
			if (targetPlayer.getAliases().stream().anyMatch(commands[1]::equalsIgnoreCase)) {
				Item item = getItemForRemoval(commands[2]);
				if (item == null) {
					Output.outputToTarget(player, "You don't seem to have that item to give!");
					return;
				} else {
					player.getHeldItem().remove(item);
					targetPlayer.getHeldItem().add(item);
					updatePlayer(targetPlayer);
					Output.outputToTarget(player, "You give " + targetPlayer.getDisplayName() + " "
							+ Output.needsAThe(item.getDisplayName()) + item.getDisplayName() + ".");
					Output.outputToTarget(targetPlayer,
							player.getDisplayName() + " gives you " + item.getDisplayName() + ".");
					Output.outputToRoomExceptTarget(player, targetPlayer,
							player.getDisplayName() + " gives " + targetPlayer.getDisplayName() + " "
									+ Output.needsAThe(item.getDisplayName()) + item.getDisplayName() + ".");
					return;
				}
			}
		}
		Actor actor = getActorIfPresent(commands[1]);
		if (actor != null) {
			if(actor.getIsDead()){
				Output.outputToTarget(player, "Leave the dead be! They've suffered enough.");
				return;
			}
			Item item = getItemForRemoval(commands[2]);
			if (item == null) {
				Output.outputToTarget(player, "You don't seem to have that item to give!");
				return;
			} else {
				player.getHeldItem().remove(item);
				actor.getItemsHeld().add(item);
				ActorReader reader = Main.getActorReader(Main.savedGamePath);
				reader.writeActor(actor);
				Output.outputToTarget(player, "You give " + actor.getDisplayName() + " "
						+ Output.needsAThe(item.getDisplayName()) + item.getDisplayName() + ".");
				Output.outputToRoom(player,
						player.getDisplayName() + " generously donates their " + item.getDisplayName() + " to "
								+ Output.needsAThe(actor.getDisplayName()) + actor.getDisplayName() + ".");
				return;
			}
		}
		if (commands.length > 3 && commands[2].equalsIgnoreCase("to") && getItemForRemoval(commands[1]) != null) {
			String temp = commands[1];
			commands[1] = commands[3];
			commands[2] = temp;
			doGive(commands);
			return;
		}
		Output.outputToTarget(player, "I'm sorry, I don't understand how you phrased that. I'm a bit daft.");

	}

	private void doYell(String[] commands) {
		if (commands.length > 1) {
			StringBuilder chatOutput = new StringBuilder();
			for (int i = 1; i < commands.length; i++) {
				chatOutput.append(chatOutput + commands[i] + " ");
			}
			String otherOutput = "<br/>" + player.getDisplayName() + " yells \""
					+ chatOutput.substring(0, chatOutput.length() - 1) + "\"";
			Output.outputToAll(player, otherOutput);
		}

	}

	public void doSpeak(String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Say WHAT?");
			return;
		}
		if (commands[0].equalsIgnoreCase("say")) {
			StringBuilder chatOutput = new StringBuilder();
			for (int i = 1; i < commands.length; i++) {
				chatOutput.append(commands[i] + " ");
			}
			String selfOutput = "You say \"" + chatOutput.toString().trim() + "\"";
			String otherOutput = player.getDisplayName() + " says \"" + chatOutput.toString().trim() + "\"";
			Output.outputToRoom(player, otherOutput);
			Output.outputToTarget(player, selfOutput);
		} else if (commands[0].equalsIgnoreCase("tell")) {
			Player targetPlayer = getPlayer(commands[1]);
			if (targetPlayer == null) {
				Output.outputToTarget(player, commands[1] + " does not appear to be someone you can tell things to.");
			} else {
				StringBuilder chatOutput = new StringBuilder();
				for (int i = 1; i < commands.length; i++) {
					chatOutput.append(chatOutput + commands[i] + " ");
				}
				Output.outputToTarget(targetPlayer,
						player.getDisplayName() + " tells you \"" + chatOutput.toString().trim() + "\"");
				Output.outputToTarget(player,
						"You tell " + targetPlayer.getDisplayName() + " \"" + chatOutput.toString().trim() + "\"");
			}
		}

	}

	public void doQuit() {

		if (!Main.isWeb) {
			Output.outputToTarget(player, "Thanks for playing!");
			Output.outputToTarget(player, "Quitting...");
			PlayerReader reader = Main.getPlayerReader();
			reader.writePlayer(player);
			System.exit(0);
		} else {
			Output.outputToAll(player, "<br/>" + player.getDisplayName() + " has left the game.");
			JsonPlayerReader playerReader = new JsonPlayerReader();
			playerReader.writePlayer(player);
			CombatChecker.leaveRoom(player, null, null);
			OutputEndpoint.endSession(OutputEndpoint.users.get(player.getUser()), player);
		}

	}

	public Player refreshPlayer(Player playerRefresh) {
		PlayerReader reader = Main.getPlayerReader();
		return reader.getPlayer(playerRefresh.getUser().getUsername(), playerRefresh.getName());
	}

	public void doThrow(String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Throw WHAT?");
			return;
		}
		if (commands.length < 3) {
			Item item = getItemForRemoval(commands[1]);
			if (item == null) {
				Output.outputToTarget(player, "You don't seem to have that item to throw!");
			} else {
				randomThrow(item);
			}

		} else if (commands.length < 4) {
			if (commands[2].equalsIgnoreCase("AT")) {
				Output.outputToTarget(player, "Throw at WHAT?");
			} else {
				DirectionalCommands directionalCommands = new DirectionalCommands();
				if (directionalCommands.isDirectional(commands[2])) {
					Item item = getItemForRemoval(commands[1]);
					if (player.getCurrentRoom().getDirections().contains(Direction.valueOf(commands[2]))) {

						if (item == null) {
							Output.outputToTarget(player, "You don't seem to have that item to throw!");
						} else {
							directionalThrow(Direction.valueOf(commands[2]), item);
						}
					} else {
						Output.outputToTarget(player, "You can't throw " + item.getDisplayName() + " that direction!");
					}
				} else {
					Output.outputToTarget(player, "I'm sorry, I don't understand how you're asking me to throw...");
				}
			}

		} else {
			if (commands[2].equalsIgnoreCase("AT")) {
				Actor actor = getActorIfPresent(commands[3]);
				Player playerTarget = getPlayerIfPresent(commands[3]);
				if (actor != null) {
					Item item = getItemForRemoval(commands[1]);
					if (item != null) {
						throwAtTarget(actor, item);
					} else {
						Output.outputToTarget(player, "You don't seem to have that item!");
					}
				} else if(playerTarget!=null) {
					Item item = getItemForRemoval(commands[1]);
					if (item != null) {
						throwAtTarget(player, item);
					} else {
						Output.outputToTarget(player, "You don't seem to have that item!");
					}
				}else{
					Output.outputToTarget(player, "That is not a valid target!");
				}

			} else {
				Output.outputToTarget(player, "I'm sorry I don't understand how you want me to throw...");
			}
		}

	}

	private Player getPlayerIfPresent(String string) {
		GetLists getList = new GetLists();
		for(Player otherPlayer : getList.getListPlayers(player)){
			if(otherPlayer.getAliases().stream().anyMatch(string::equalsIgnoreCase)){
				return otherPlayer;
			}
		}
		return null;
	}

	public void throwAtTarget(Fightable fightable, Item item) {
		Room room = player.getCurrentRoom();
		room.getItemsPresent().add(item.getName());
		player.getHeldItem().remove(item);
		if (item.getType() == ItemType.WEAPON) {
			Item weapon = item;
			if (fightable.getIsDead()) {
				String baseMessage = "You hurl " + Output.needsAThe(item.getDisplayName()) + item.getDisplayName()
						+ " at the corpse of " + Output.needsAThe(fightable.getDisplayName()) + fightable.getDisplayName()
						+ ". Weirdo.";
				Output.outputToTarget(player, Output.capitalize(baseMessage));
				Output.outputToRoom(player,
						player.getDisplayName() + " throws " + Output.needsAThe(item.getDisplayName())
								+ item.getDisplayName() + "at the corpse of " + Output.needsAThe(fightable.getDisplayName())
								+ fightable.getDisplayName() + ".");

			} else {
				if (fightable instanceof Actor) {
					Actor actor = (Actor) fightable;
					if (actor.getBehaviours().contains(Behaviour.NEUTRAL)) {
						actor.getBehaviours().add(Behaviour.AGGRESSIVE);
						actor.getBehaviours().remove(Behaviour.NEUTRAL);
						CombatChecker.addAggressor(actor, player);
					}
				}

				Output.outputToTarget(player,
						"You hurl " + Output.needsAThe(item.getDisplayName()) + item.getDisplayName() + " at "
								+ Output.needsAThe(fightable.getDisplayName()) + fightable.getDisplayName() + ".");
				Output.outputToRoom(player,
						player.getDisplayName() + " hurls " + Output.needsAThe(item.getDisplayName())
								+ item.getDisplayName() + " at " + Output.needsAThe(fightable.getDisplayName())
								+ fightable.getDisplayName() + ".");
				Double multiplier = Double.valueOf(random.nextInt(300));
				Double attack = Double.valueOf(weapon.getDamage());
				double damage = (multiplier / 100) * attack;
				damage = Precision.round(damage, 0);
				damage = damage - fightable.getDamageReduction();
				if (damage <= 0) {
					String output = "Your " + item.getDisplayName() + " glances off "
							+ Output.needsAThe(fightable.getDisplayName()) + fightable.getDisplayName() + ", doing no damage.";
					Output.outputToTarget(player, output);
				} else {
					Output.outputToTarget(player,
							Output.capitalize(Output.needsAThe(item.getDisplayName()) + item.getDisplayName()
									+ " strikes " + Output.needsAThe(fightable.getDisplayName()) + fightable.getDisplayName()
									+ ", doing some damage."));
				}
				if (damage >= fightable.getHealth()) {
					Combat combat = new Combat();
					combat.doKillFightable(player, fightable);

				} else if (damage > 0) {
					fightable.setHealth(fightable.getHealth() - (int) damage);

				}

			}
		} else {
			Output.outputToTarget(player,
					"You throw the " + item.getDisplayName() + " at " + Output.needsAThe(fightable.getDisplayName())
							+ fightable.getDisplayName() + ". It is an unwieldy projectile and "
							+ Output.needsAThe(fightable.getDisplayName()) + fightable.getDisplayName()
							+ " just shrugs it off.");
			Output.outputToRoom(player, player.getDisplayName() + " awkwardly throws the " + item.getDisplayName()
					+ " at " + Output.needsAThe(fightable.getDisplayName()) + fightable.getDisplayName() + ".");
		}
		updateRoom(room);
		updatePlayer(player);
	}

	public Actor getActorIfPresent(String actorName) {
		for (String actor : player.getCurrentRoom().getActorsPresent()) {
			Actor thisActor = getActor(actor);
			if (thisActor.getAliases().stream().anyMatch(actorName::equalsIgnoreCase)) {
				return thisActor;
			}
		}
		return null;
	}

	public Item getItemForRemoval(String itemName) {
		InventoryManager inventory = new InventoryManager();
		return inventory.getItemForRemoval(player, itemName);
	}

	public void randomThrow(Item item) {
		List<Direction> directions = player.getCurrentRoom().getDirections();
		int index = random.nextInt(directions.size());
		Direction direction = directions.get(index);
		directionalThrow(direction, item);
	}

	public void directionalThrow(Direction direction, Item item) {
		Room room = getRoom(player.getCurrentRoom().getMapping().get(direction));
		room.getItemsPresent().add(item.getName());
		player.getHeldItem().remove(item);
		updateRoom(room);
		updatePlayer(player);
		Output.outputToRoom(player, player.getDisplayName() + " throws " + Output.needsAThe(item.getDisplayName())
				+ item.getDisplayName() + " " + Direction.getEnumString(direction));
		Output.outputToTarget(player, "You throw " + Output.needsAThe(item.getDisplayName()) + item.getDisplayName()
				+ " " + Direction.getEnumString(direction));
		System.out.println("Finished directionalThrow outputToTarget");
		Output.outputToTargetRoom(player, room,
				Output.capitalize(Output.needsAThe(item.getDisplayName()) + item.getDisplayName())
						+ " is hurled into the room from " + player.getCurrentRoom().getDisplayName() + ".");
		System.out.println("Done with directionalThrow");
	}

	public void updatePlayer(Player player) {
		PlayerReader playerReader = Main.getPlayerReader();
		playerReader.writePlayer(player);

	}

	public void updateRoom(Room room) {
		RoomReader roomReader = Main.getRoomReader(Main.savedGamePath);
		roomReader.writeRoom(room);

	}

	public Room getRoom(String string) {
		RoomReader roomReader = Main.getRoomReader(Main.savedGamePath);
		return roomReader.readRoom(string);
	}

	public Effect checkForEffects(String[] commands) {
		GetLists getLists = new GetLists();
		List<Effectables> effectables = getLists.listAllEffectablesPresent(player);
		for (Effectables effectable : effectables) {
			if (effectable.getEffects() != null) {
				for (String effect : effectable.getEffects()) {
					EffectReader reader = Main.getEffectReader();
					Effect thisEffect = reader.readEffect(effect);
					if (thisEffect != null
							&& thisEffect.getCommandTrigger().stream().anyMatch(commands[0]::equalsIgnoreCase)) {
						thisEffect.setRoom(player.getCurrentRoom());
						thisEffect.setEffectName(effect);
						thisEffect.setPlayer(player);
						return thisEffect;

					}
				}
			}
		}
		return null;
	}

	private Item getItem(String item) {
		ItemReader itemReader = Main.getItemReader(Main.savedGamePath);
		return itemReader.readItem(item);
	}

	private Actor getActor(String actor) {
		ActorReader actorReader = Main.getActorReader(Main.savedGamePath);
		return actorReader.readActor(actor);
	}

	public void doClear() {
		if (Main.isWeb) {
			Output.outputToTarget(player,
					"<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>");
		} else {
			System.out.println("/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n");
		}

	}

	public void doWake() {
		Output.outputToTarget(player, "You're already awake silly!");
		// TODO

	}

	public void doSleep() {
		Output.outputToTarget(player, "NO I DON'T WANNA");
		// TODO

	}

	public void doAttackAttempt(String[] commands) {
		Combat combat = new Combat();
		combat.doAttackAttempt(player, commands);
	}

	public void doExamine(String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Examine WHAT?");
			return;
		}
		String target = "";
		GetLists getLists = new GetLists();
		List<Effectables> effectables = getLists.listAllEffectablesPresent(player);
		List<Player> players = getLists.getListPlayers(player);
		if (commands.length > 1) {
			target = commands[1];
			if (target.equalsIgnoreCase("me") || player.getAliases().stream().anyMatch(target::equalsIgnoreCase)) {
				Output.outputToTarget(player, player.examine().replace(player.getName() + " looks", "You are"));
				Output.outputToRoom(player, player.getDisplayName() + " examines themself.");
				return;
			}
			try {
				for (Effectables effectable : effectables) {
					if (effectable.getAliases().stream().anyMatch(target::equalsIgnoreCase)) {
						Output.outputToTarget(player, effectable.examine());
						Output.outputToRoom(player,
								player.getDisplayName() + " examines " + effectable.getDisplayName() + ".");
						return;
					}
				}
				for (Player otherPlayer : players) {
					if (otherPlayer.getAliases().stream().anyMatch(target::equalsIgnoreCase)) {
						Output.outputToTarget(player, otherPlayer.examine());
						Output.outputToRoom(player,
								player.getDisplayName() + " examines " + otherPlayer.getDisplayName() + ".");
						return;
					}
				}

				Set<String> playersPresent = player.getCurrentRoom().getPlayersPresent();
				for (String playerString : playersPresent) {
					if (target.equalsIgnoreCase(playerString.toLowerCase())) {
						Player thisPlayer = getPlayer(playerString);
						Output.outputToTarget(player, thisPlayer.examine());
						Output.outputToRoom(player,
								player.getDisplayName() + " examines " + thisPlayer.getDisplayName() + ".");
						return;
					}

				}
				Output.outputToTarget(player,
						"I'm sorry, I can't find this '" + target.toLowerCase() + "' you speak of.");
			} catch (Exception e) {
				Output.outputToTarget(player, "This is an error doing something");
				e.printStackTrace();
				Output.outputToTarget(player,
						"I'm sorry, I can't find this '" + target.toLowerCase() + "' you speak of.");
			}
		} else {
			Output.outputToTarget(player, "Bro. Please. Examine WHAT?");

		}

	}

	public Player getPlayer(String playerString) {
		PlayerReader playerReader = Main.getPlayerReader();
		return playerReader.readPlayerByName(playerString);
	}

	public void doDrop(String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Drop WHAT?");
			return;
		}
		String output = "";
		String outputOther = "";
		String target = commands[1];
		if (target.equalsIgnoreCase("ALL")) {
			List<Item> items = player.getHeldItem();
			if (items.isEmpty()) {
				Output.outputToTarget(player, "You don't have any loose items to drop!");
			}
			for (Item item : items) {
				player.getCurrentRoom().getItemsPresent().add(item.getName());

				if (item.getQuantity() < 2) {
					output = "You drop " + Output.needsAThe(item.getDisplayName()) + item.getDisplayName() + ".";
					outputOther = player.getDisplayName() + " drops " + Output.needsAThe(item.getDisplayName())
							+ item.getDisplayName() + ".";
				} else {
					output = "You drop " + Output.needsAThe(item.getDisplayName()) + item.getDisplayName() + "s.";
					outputOther = player.getDisplayName() + " drops " + Output.needsAThe(item.getDisplayName())
							+ item.getDisplayName() + "s.";
				}
				Output.outputToTarget(player, output);
				Output.outputToRoom(player, outputOther);
				updateRoom(player.getCurrentRoom());
			}
			int size = items.size();
			for (int i = 0; i < size; i++) {
				player.getHeldItem().remove(items.get(0));
			}
			return;
		}

		try {
			Item item = getItemForRemoval(target);
			if (item == null) {
				output = "You don't have " + target + " to drop!";
				Output.outputToTarget(player, output);
			} else {
				if (item.getQuantity() < 2) {
					output = "You drop " + Output.needsAThe(item.getDisplayName()) + item.getDisplayName() + ".";
					outputOther = player.getDisplayName() + " drops " + Output.needsAThe(item.getDisplayName())
							+ item.getDisplayName() + ".";
				} else {
					output = "You drop " + Output.needsAThe(item.getDisplayName()) + item.getDisplayName() + "s.";
					outputOther = player.getDisplayName() + " drops " + Output.needsAThe(item.getDisplayName())
							+ item.getDisplayName() + "s.";
				}
				Output.outputToTarget(player, output);
				Output.outputToRoom(player, outputOther);
				player.getHeldItem().remove(item);
				player.getCurrentRoom().getItemsPresent().add(item.getName());
				updateRoom(player.getCurrentRoom());
				return;
			}
			Output.outputToTarget(player, "I'm sorry, you don't seem to have any " + target.toLowerCase() + "...");
		} catch (Exception e) {
			System.out.println("Some error with dropping");
			System.out.println(e);
		}

	}

	public void doDisplayInventory() {
		Output.outputToRoom(player, player.getDisplayName() + " takes stock of their inventory.");
		Output.outputToTarget(player, player.displayItems());

	}

	public void doGet(String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Get WHAT?");
			return;
		}
		String target = commands[1];
		try {

			List<String> items = player.getCurrentRoom().getItemsPresent();

			if (items == null || items.isEmpty()) {
				Output.outputToTarget(player, "I'm sorry, I dont see any " + target.toLowerCase() + " here...");
			} else {
				for (String item : items) {
					Item thisItem = getItem(item);
					if (thisItem.getAliases().stream().anyMatch(target::equalsIgnoreCase)) {
						if (!thisItem.getIsHoldable()) {
							Output.outputToTarget(player, "I'm afraid you can't pick that up!");
							return;
						} else {
							String needsAThe = Output.needsAThe(thisItem.getDisplayName());
							if (thisItem.getQuantity() < 2) {

								Output.outputToTarget(player,
										"You pick up " + needsAThe + thisItem.getDisplayName() + ".");
								Output.outputToRoom(player, player.getDisplayName() + " picks up " + needsAThe
										+ thisItem.getDisplayName() + ".");
							} else {
								Output.outputToTarget(player,
										"You pick up " + needsAThe + thisItem.getDisplayName() + "s.");
								Output.outputToRoom(player, player.getDisplayName() + " picks up " + needsAThe
										+ thisItem.getDisplayName() + "s.");
							}
							player.getHeldItem().add(thisItem);
							player.getCurrentRoom().getItemsPresent().remove(item);
							updateRoom(player.getCurrentRoom());
							return;
						}

					} else {

					}

				}
				Output.outputToTarget(player,
						"I'm sorry, I dont see any " + target.toLowerCase() + " that you can get here...");
			}
		} catch (Exception e) {

		}

	}

	public void doHelp() {
		Output.outputToTarget(player, "Try one of these commands: ");
		Output.outputToTarget(player, "l, look: redisplays room");
		Output.outputToTarget(player,
				"x, ex, examine, look at:  										 examines specified thing");
		Output.outputToTarget(player,
				"n, north, go north (etc, same format for any direction):  		 attempt to travel in specified direction");
		Output.outputToTarget(player,
				"g, get, grab, take:   											 attempts to take specified thing");
		Output.outputToTarget(player,
				"dr, drop:        			 										 drops held item; 'drop all' to drop all");
		Output.outputToTarget(player,
				"i, inventory:  													 displays held items");
		Output.outputToTarget(player,
				"equip, wield, wear: 												 equips specified item/weapon/armor, respectively");
		Output.outputToTarget(player,
				"unequip, unwield, remove:											 unequips specified item/weapon/armor, respectively");
		Output.outputToTarget(player,
				"give (item) to (target), give (target) (item): 			 		 attempts to give item to target");
		Output.outputToTarget(player, "throw (item), throw (item) (direction), throws (item) at (target): throws item");
		Output.outputToTarget(player,
				"k, kill, stab, at, attack, punch, hit: 							 attempts to attack target");
		Output.outputToTarget(player,
				"say: 																 says something to room");
		Output.outputToTarget(player,
				"tell (target): 													 says something to target, independent of distance");
		Output.outputToTarget(player,
				"yell, shout: 														 says something to everyone logged in");
		Output.outputToTarget(player,
				"Additionally, some actors or items may have special commands associated with them! Examine your environment carefully for clues.");

	}

	public void doLook(String[] commands) {
		if (commands.length < 2) {
			player.setCurrentRoom(getRoom(player.getCurrentRoom().getName()));
			Output.outputToTarget(player, player.getCurrentRoom().displayRoom(player));
			Output.outputToRoom(player, player.getDisplayName() + " looks around.");
		} else if (commands[1].equalsIgnoreCase("at")) {
			doExamine(new String[] { commands[0], commands[2] });
		}

	}

}
