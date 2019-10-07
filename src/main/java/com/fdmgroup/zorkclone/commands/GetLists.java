package com.fdmgroup.zorkclone.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.Actor;
import com.fdmgroup.zorkclone.actors.io.ActorReader;
import com.fdmgroup.zorkclone.combat.Fightable;
import com.fdmgroup.zorkclone.effects.Effectables;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.Slot;
import com.fdmgroup.zorkclone.items.io.ItemReader;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.PlayerReader;

public class GetLists {
	private PlayerReader playerReader = Main.getPlayerReader();
	private ItemReader itemReader = Main.getItemReader(Main.savedGamePath);
	private ActorReader actorReader = Main.getActorReader(Main.savedGamePath);
	
	public List<Player> getListPlayers(Player player) {
		List<Player> playerList = new ArrayList<>();
		
		for (String playerName : player.getCurrentRoom().getPlayersPresent()) {
			if (!playerName.equalsIgnoreCase(player.getName())) {
				playerList.add(playerReader.readPlayerByName(playerName));
			}
		}
		return playerList;
	}

	public List<Effectables> listAllEffectablesPresent(Player player) {
		List<Effectables> allEffectables = new ArrayList<>();
		for (Item item : player.getHeldItem()) {
			allEffectables.add(item);
		}
		for (Entry<Slot, Item> gear : player.getSlotsEquipped().entrySet()) {
			allEffectables.add(gear.getValue());
		}
		for (String item : player.getCurrentRoom().getItemsPresent()) {
			Item thisItem = itemReader.readItem(item);
			allEffectables.add(thisItem);
		}	
		for (String actor : player.getCurrentRoom().getActorsPresent()) {
			Actor thisActor = actorReader.readActor(actor);
			for (Item item : thisActor.getItemsHeld()) {
				allEffectables.add(item);
			}
			allEffectables.add(thisActor);
		}
		return allEffectables;
	}

	public List<Fightable> listFightables(Player player) {
			List<Fightable> fightables = new ArrayList<>();
			for (String actor : player.getCurrentRoom().getActorsPresent()) {
				Actor thisActor = actorReader.readActor(actor);
				fightables.add(thisActor);
			}
			for (String playerTarget : player.getCurrentRoom().getPlayersPresent()) {
				if (!playerTarget.equalsIgnoreCase(player.getName())) {
					Player thisPlayer = playerReader.readPlayerByName(playerTarget);
					fightables.add(thisPlayer);
				}

			}
			return fightables;
		
	}
}
