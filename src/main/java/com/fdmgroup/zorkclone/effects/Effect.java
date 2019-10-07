package com.fdmgroup.zorkclone.effects;

import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.zorkclone.commands.CommandProcessor;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.rooms.Room;

public abstract class Effect {
	private String effectName;
	private List<String> commandTrigger = new ArrayList<>();
	private Room room;
	private Player player;
	
	public Player doEffect(String[] commands, CommandProcessor commandProcessor){
		System.out.println("Default output");
		return player;
	}
	
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getEffectName() {
		return effectName;
	}

	public void setEffectName(String effectName) {
		this.effectName = effectName;
	}

	public List<String> getCommandTrigger() {
		return commandTrigger;
	}

	public void setCommandTrigger(List<String> commandTrigger) {
		this.commandTrigger = commandTrigger;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}


}
