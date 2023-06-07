package com.fdmgroup.zorkclone.effects;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.combat.Combat;
import com.fdmgroup.zorkclone.combat.Fightable;
import com.fdmgroup.zorkclone.commands.CommandProcessor;
import com.fdmgroup.zorkclone.commands.ListFetchUtil;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.io.ItemReader;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;
import com.fdmgroup.zorkclone.weboutput.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Reap extends Effect{
	@Autowired
	private Output output;
	@Autowired
	private Combat combat;

	public Reap() {
		super();
		getCommandTrigger().add("reap");
	}

	@Override
	public Player doEffect(String[] commands, CommandProcessor cp) {
		Player player = getPlayer();
		
		
		if(commands.length<2){
			output.outputToTarget(player,"Reap WHAT, exactly?");
			cp.updatePlayer(player);
			return player;
		}
		if(commands[1].toLowerCase().equals("me")){
			output.outputToTarget(player,"If that's how you wanna do this, who am I to stop you?");
			output.outputToTarget(player,"You reap yourself, and are immediately sent to the underworld.");
			output.outputToRoom(player,player.getDisplayName()+" reaps themself with death's scythe, and vanishes in a puff of hellfire!");
			RoomReader roomReader= Main.getRoomReader(Main.savedGamePath);
			player.setCurrentRoom(roomReader.readRoom("UnderworldLobby"));
			cp.updatePlayer(player);
			return player;
		}
		
		ItemReader itemReader =Main.getItemReader(Main.savedGamePath);
		
		for(Fightable fightable :(new ListFetchUtil()).listFightables(player)){
			if(fightable.getAliases().stream().anyMatch(commands[1]::equalsIgnoreCase)){
				fightable.setHealth(0);
				fightable.setIsDead(true);
				output.outputToTarget(player,"Using the power of death's scythe, you instantly destroy the target!");
				if(fightable instanceof Player){
					output.outputToTarget((Player)fightable, player.getDisplayName() + " reaps you with the power of death's scythe!");
					output.outputToRoomExceptTarget(player,(Player)fightable,  player.getDisplayName() + " reaps "+fightable.getDisplayName()+" with the power of death's scythe!");
				}else{
					output.outputToRoom(player,player.getDisplayName()+" reaps "+fightable.getDisplayName()+ " with the power of death's scythe!");
				}
				combat.doKillFightable(player, fightable);
				output.outputToTarget(player,"Since you are not death, you seem to have exhausted the killing power of this scythe. It reverts to a normal extremely deadly scythe.");
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
		
			
		
		output.outputToTarget(player,"You can't reap "+commands[1].toLowerCase()+"!");
		return player;
	}
		
		
}
