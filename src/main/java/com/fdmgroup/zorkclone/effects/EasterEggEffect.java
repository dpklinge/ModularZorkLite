package com.fdmgroup.zorkclone.effects;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.Actor;
import com.fdmgroup.zorkclone.actors.io.ActorReader;
import com.fdmgroup.zorkclone.combat.Combat;
import com.fdmgroup.zorkclone.commands.CommandProcessor;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.weboutput.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EasterEggEffect extends Effect {
	@Autowired
	private Output output;
	@Autowired
	private Combat combat;
	
	public EasterEggEffect() {
		super();
		getCommandTrigger().add("evoke");
	}

	@Override
	public Player doEffect(String [] commands, CommandProcessor processor) {
		if(getRoom().getActorsPresent().contains("Death")){
			ActorReader actorReader =Main.getActorReader(Main.savedGamePath);
			Actor death = actorReader.readActor("Death");
			if(death.getHealth()>1){
				death.setHealth(1);
				actorReader.writeActor(death);
				output.outputToTarget(getPlayer(),"The mystical power of your Easter Egg blasts Death to...errr... Death's door?");
			}else{
				output.outputToTarget(getPlayer(),"You've already toasted Death!");
			}
		}else{
			output.outputToTarget(getPlayer(),"Evoking your easter egg doesn't seem to do anything here....");
		}
		return getPlayer();
		
	}
	
}
