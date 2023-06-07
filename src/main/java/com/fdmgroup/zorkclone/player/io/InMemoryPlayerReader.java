package com.fdmgroup.zorkclone.player.io;

import java.util.HashMap;

import com.fdmgroup.zorkclone.player.Player;

public class InMemoryPlayerReader implements PlayerReader {
	private static HashMap<String, Player> players = new HashMap<>();
	
	@Override
	public Player getPlayer(String username, String name) {
		String charName = name.toLowerCase();
		if(players.containsKey(charName)){
			System.out.println("Found player: "+players.get(charName));
			return players.get(charName);
		}else{
			PlayerReader reader= new JsonPlayerReader();
			Player player= reader.getPlayer(username, charName);
			players.put(charName, player);
			return player;
		}

	}

	@Override
	public synchronized void writePlayer(Player player) {
		players.replace(player.getName().toLowerCase(), player);
	}

	@Override
	public Player readPlayerByName(String character) {
		String characterName = character.toLowerCase();
		if(players.containsKey(characterName)){
			System.out.println("Found player: "+players.get(characterName));
			return players.get(characterName);
		}else{
			return null;
		}
	}

}
