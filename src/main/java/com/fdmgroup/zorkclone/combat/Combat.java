package com.fdmgroup.zorkclone.combat;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.math3.util.Precision;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.Actor;
import com.fdmgroup.zorkclone.actors.Behaviour;
import com.fdmgroup.zorkclone.commands.ListFetchUtil;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.weboutput.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Combat {
	@Autowired
	private CombatChecker combatChecker;
	@Autowired
	private Output output;
	public void doAttackAttempt(Player player,String[] commands) {
		if (!player.isAttackReady()) {
			output.outputToTarget(player, "You are still recovering from your last attack attempt!");
			return;
		}
		if (commands.length < 2) {
			output.outputToTarget(player, "Attack WHAT, silly?");
		} else {
			String target = commands[1];
			if(target.equalsIgnoreCase("me")){
				output.outputToTarget(player, "Nope, no thanks, not doing that.");
				return;
			}
			try {
				ListFetchUtil getLists = new ListFetchUtil();
				List<Fightable> fightables = getLists.listFightables(player);
				for (Fightable fightable : fightables) {
					if (fightable.getAliases().stream().anyMatch(target::equalsIgnoreCase)) {

						doAttackFightable(player,fightable);
						return;
					}
				}

				List<String> items = player.getCurrentRoom().getItemsPresent();

				for (String item : items) {
					Item thisItem = Main.getItemReader(Main.savedGamePath).readItem(item);
					if (thisItem.getAliases().stream().anyMatch(target::equalsIgnoreCase)) {
						doAttackItem(player,thisItem);
						return;
					}

				}

			} catch (Exception e) {

			}
			output.outputToTarget(player, "You cannot find " + target.toLowerCase() + " to attack!");
		}
	}
	
	public void doAttackItem(Player player, Item item) {
		output.outputToTarget(player, "You assault " + output.needsAThe(item.getDisplayName()) + item.getDisplayName()
				+ ". Cool?... That felt unproductive.");
		output.outputToRoom(player, player.getDisplayName() + " attacks " + output.needsAThe(item.getDisplayName())
				+ item.getDisplayName() + " for some reason.");

	}
	
	public void doAttackFightable(Player player,Fightable fightable) {
		String needsAThe = output.needsAThe(fightable.getDisplayName());

		if (fightable.getIsDead()) {
			String baseMessage = needsAThe + fightable.getDisplayName()
					+ " is already dead! Let the dead lie in peace.";
			output.outputToTarget(player, output.capitalize(baseMessage));

		} else {
			if (fightable instanceof Actor) {
				Actor actor = (Actor) fightable;
				if (actor.getBehaviours().contains(Behaviour.NEUTRAL)) {
					actor.getBehaviours().add(Behaviour.AGGRESSIVE);
					actor.getBehaviours().remove(Behaviour.NEUTRAL);
					combatChecker.addAggressor(actor, player);
				}
			}
			String outputMessage = "You attack " + needsAThe + fightable.getDisplayName() + ".";
			String outputOtherMessage = player.getName() + " attacks " + needsAThe + fightable.getDisplayName() + ".";
			output.outputToTarget(player, outputMessage);
			if (fightable instanceof Player) {
				output.outputToTarget((Player) fightable, player.getDisplayName() + " attacks you!");
				output.outputToRoomExceptTarget(player, (Player) fightable, outputOtherMessage);
			} else {
				output.outputToRoom(player, outputOtherMessage);
			}

			Random random = new Random();
			int accuracyCheck = random.nextInt(100);
			if (accuracyCheck > player.getAccuracy()) {
				output.outputToTarget(player, "You miss " + needsAThe + fightable.getDisplayName() + "!");
				if (fightable instanceof Player) {
					output.outputToTarget((Player) fightable, player.getDisplayName() + " misses you!");
					output.outputToRoomExceptTarget(player, (Player) fightable,
							player.getDisplayName() + " misses " + fightable.getDisplayName() + ".");
				} else {
					output.outputToRoom(player,
							player.getDisplayName() + " misses " + fightable.getDisplayName() + ".");
				}
				return;
			}
			double multiplier = random.nextInt(200);
			double attack = player.getDamage();
			double damage = (multiplier / 100) * attack;
			damage = Precision.round(damage, 0);
			damage = damage - fightable.getDamageReduction();
			if (damage <= 0) {
				outputMessage = "Your blow is unable to damage * !";
				outputOtherMessage = "* fails to damage &";
			} else if (multiplier > 150) {
				outputMessage = "You deal * a crushing blow!";
				outputOtherMessage = "* deals & a crushing blow!";
			} else if (multiplier > 110) {
				outputMessage = "You deliver * a good strong hit.";
				outputOtherMessage = "* deals & a good strong hit.";
			} else if (multiplier > 90) {
				outputMessage = "You strike *.";
				outputOtherMessage = "* strikes &.";
			} else if (multiplier > 50) {
				outputMessage = "You weakly hit *.";
				outputOtherMessage = "* weakly hits &.";
			} else {
				outputMessage = "You barely scratch *.";
				outputOtherMessage = "* barely scratches &.";
			}
			outputMessage = outputMessage.replace("*", needsAThe + fightable.getDisplayName());

			outputOtherMessage = outputOtherMessage.replace("*", player.getDisplayName());
			output.outputToTarget(player, outputMessage);
			if (fightable instanceof Player) {
				outputOtherMessage = outputOtherMessage.replace("&", "you");
				output.outputToTarget((Player) fightable, outputOtherMessage);
				output.outputToRoomExceptTarget(player, (Player) fightable, outputOtherMessage);
			} else {
				outputOtherMessage = outputOtherMessage.replace("&", needsAThe + fightable.getDisplayName());
				output.outputToRoom(player, outputOtherMessage);
			}
			if (damage >= fightable.getHealth()) {
				doKillFightable(player,fightable);

			} else if (damage > 0) {
				fightable.setHealth(fightable.getHealth() - (int) damage);

			}
			madeAttack(player);

		}
	}
	
	public void madeAttack(Player player) {
		player.setAttackReady(false);
		Timer timer = new Timer("timer");
		TimerTask attackCooldown = new TimerTask() {
			public void run() {
				player.setAttackReady(true);
				cancel();
				timer.cancel();
			}
		};
		timer.schedule(attackCooldown, player.getBulk() * 500);

	}
	
	public void doKillFightable(Player player, Fightable fightable) {
		if (fightable instanceof Actor) {
			Actor actor = (Actor) fightable;

			String needsAThe = output.needsAThe(actor.getDisplayName());
			if (actor.getKillText() != null && actor.getKillText().length() > 0) {
				output.outputToTarget(player, actor.getKillText());
				output.outputToRoom(player,
						player.getDisplayName() + " has slain " + needsAThe + actor.getDisplayName() + "!");
			} else {
				output.outputToTarget(player, "You have slain " + needsAThe + actor.getDisplayName() + "!");
				output.outputToRoom(player,
						player.getDisplayName() + " has slain " + needsAThe + actor.getDisplayName() + "!");

			}
			actor.setIsDead(true);
			if (actor.getItemsHeld() != null && actor.getItemsHeld().size() > 0) {
				Room room = player.getCurrentRoom();
				for (Item item : actor.getItemsHeld()) {
					room.getItemsPresent().add(item.getName());
					String baseMessage = needsAThe + actor.getDisplayName() + " drops " + item.getDisplayName() + ".";
					output.outputToTarget(player, output.capitalize(baseMessage));
					output.outputToRoom(player, output.capitalize(baseMessage));
				}
			}
		} else if (fightable instanceof Player) {
			Player deadPlayer = (Player) fightable;
			output.outputToTarget(player, "You have slain " + deadPlayer.getDisplayName() + "!");
			output.outputToRoomExceptTarget(player, deadPlayer, player.getDisplayName()+" has slain "+deadPlayer.getDisplayName()+"!");
			output.outputToTarget(deadPlayer, "You have been slain by "+player.getDisplayName()+"!");
			deadPlayer.die();

		}

	}
}
