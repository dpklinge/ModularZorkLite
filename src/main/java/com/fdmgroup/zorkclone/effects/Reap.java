package com.fdmgroup.zorkclone.effects;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.combat.Combat;
import com.fdmgroup.zorkclone.combat.Fightable;
import com.fdmgroup.zorkclone.commands.CommandProcessor;
import com.fdmgroup.zorkclone.commands.GetLists;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.io.ItemReader;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;
import com.fdmgroup.zorkclone.weboutput.Output;

public class Reap extends Effect{

	public Reap() {
		super();
		getCommandTrigger().add("reap");
	}

	@Override
	public Player doEffect(String[] commands, CommandProcessor cp) {
		Player player = getPlayer();
		
		
		if(commands.length<2){
			Output.outputToTarget(player,"Reap WHAT, exactly?");
			cp.updatePlayer(player);
			return player;
		}
		if(commands[1].toLowerCase().equals("me")){
			Output.outputToTarget(player,"If that's how you wanna do this, who am I to stop you?");
			Output.outputToTarget(player,"You reap yourself, and are immediately sent to the underworld.");
			Output.outputToRoom(player,player.getDisplayName()+" reaps themself with death's scythe, and vanishes in a puff of hellfire!");
			RoomReader roomReader= Main.getRoomReader(Main.savedGamePath);
			player.setCurrentRoom(roomReader.readRoom("UnderworldLobby"));
			cp.updatePlayer(player);
			return player;
		}
		
		ItemReader itemReader =Main.getItemReader(Main.savedGamePath);
		
		for(Fightable fightable :(new GetLists()).listFightables(player)){
			if(fightable.getAliases().stream().anyMatch(commands[1]::equalsIgnoreCase)){
				fightable.setHealth(0);
				fightable.setIsDead(true);
				Output.outputToTarget(player,"Using the power of death's scythe, you instantly destroy the target!");
				if(fightable instanceof Player){
					Output.outputToTarget((Player)fightable, player.getDisplayName() + " reaps you with the power of death's scythe!");
					Output.outputToRoomExceptTarget(player,(Player)fightable,  player.getDisplayName() + " reaps "+fightable.getDisplayName()+" with the power of death's scythe!");
				}else{
					Output.outputToRoom(player,player.getDisplayName()+" reaps "+fightable.getDisplayName()+ " with the power of death's scythe!");
				}
				
				Combat combat=new Combat();
				combat.doKillFightable(player, fightable);
				Output.outputToTarget(player,"Since you are not death, you seem to have exhausted the killing power of this scythe. It reverts to a normal extremely deadly scythe.");
				Item scythe = itemReader.readItem("DeathScythe");
				scythe.setExamine("The power has drained from this scythe, but it is still quite sharp and lethal.");
				scythe.setEffects(null);	
				player.getHeldItem().remove(scythe);
				player.getHeldItem().add(scythe);
				itemReader.writeItem(scythe);
				cp.updatePlayer(player);
				return player;
			}
		}
		
			
		
		Output.outputToTarget(player,"You can't reap "+commands[1].toLowerCase()+"!");
		return player;
	}
		
		
}
