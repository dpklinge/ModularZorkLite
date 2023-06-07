package com.fdmgroup.zorkclone.effects;

import com.fdmgroup.zorkclone.commands.CommandProcessor;
import com.fdmgroup.zorkclone.player.Player;
import org.springframework.stereotype.Component;

@Component
public class KnightTalkEffect extends Effect {
	
	public KnightTalkEffect() {
		super();
		getCommandTrigger().add("say");
		getCommandTrigger().add("tell");
	}
	
	@Override
	public Player doEffect(String [] commands, CommandProcessor processor) {
		Player player = getPlayer();
		processor.doSpeak(commands, player);
		return player;
	}
	
	
}
