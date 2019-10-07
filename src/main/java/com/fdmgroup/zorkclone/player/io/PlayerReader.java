package com.fdmgroup.zorkclone.player.io;

import com.fdmgroup.zorkclone.player.Player;

public interface PlayerReader {
	
	public Player getPlayer(String username, String charName);

	public void writePlayer(Player player) ;

	public Player readPlayerByName(String characterName);

}
