package com.fdmgroup.zorkclone.combat;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.math3.util.Precision;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.Actor;
import com.fdmgroup.zorkclone.actors.Behaviour;
import com.fdmgroup.zorkclone.commands.GetLists;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.weboutput.Output;

public class Combat {
	public void doAttackAttempt(Player player,String[] commands) {
		if (!player.isAttackReady()) {
			Output.outputToTarget(player, "You are still recovering from your last attack attempt!");
			return;
		}
		if (commands.length < 2) {
			Output.outputToTarget(player, "Attack WHAT, silly?");
		} else {
			String target = commands[1];
			if(target.equalsIgnoreCase("me")){
				Output.outputToTarget(player, "Nope, no thanks, not doing that.");
				return;
			}
			try {
				GetLists getLists = new GetLists();
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
			Output.outputToTarget(player, "You cannot find " + target.toLowerCase() + " to attack!");
		}
	}
	
	public void doAttackItem(Player player, Item item) {
		Output.outputToTarget(player, "You assault " + Output.needsAThe(item.getDisplayName()) + item.getDisplayName()
				+ ". Cool?... That felt unproductive.");
		Output.outputToRoom(player, player.getDisplayName() + " attacks " + Output.needsAThe(item.getDisplayName())
				+ item.getDisplayName() + " for some reason.");

	}
	
	public void doAttackFightable(Player player,Fightable fightable) {
		String needsAThe = Output.needsAThe(fightable.getDisplayName());

		if (fightable.getIsDead()) {
			String baseMessage = needsAThe + fightable.getDisplayName()
					+ " is already dead! Let the dead lie in peace.";
			Output.outputToTarget(player, Output.capitalize(baseMessage));

		} else {
			if (fightable instanceof Actor) {
				Actor actor = (Actor) fightable;
				if (actor.getBehaviours().contains(Behaviour.NEUTRAL)) {
					actor.getBehaviours().add(Behaviour.AGGRESSIVE);
					actor.getBehaviours().remove(Behaviour.NEUTRAL);
					CombatChecker.addAggressor(actor, player);
				}
			}
			String output = "You attack " + needsAThe + fightable.getDisplayName() + ".";
			String outputOther = player.getName() + " attacks " + needsAThe + fightable.getDisplayName() + ".";
			Output.outputToTarget(player, output);
			if (fightable instanceof Player) {
				Output.outputToTarget((Player) fightable, player.getDisplayName() + " attacks you!");
				Output.outputToRoomExceptTarget(player, (Player) fightable, outputOther);
			} else {
				Output.outputToRoom(player, outputOther);
			}

			Random random = new Random();
			int accuracyCheck = random.nextInt(100);
			if (accuracyCheck > player.getAccuracy()) {
				Output.outputToTarget(player, "You miss " + needsAThe + fightable.getDisplayName() + "!");
				if (fightable instanceof Player) {
					Output.outputToTarget((Player) fightable, player.getDisplayName() + " misses you!");
					Output.outputToRoomExceptTarget(player, (Player) fightable,
							player.getDisplayName() + " misses " + fightable.getDisplayName() + ".");
				} else {
					Output.outputToRoom(player,
							player.getDisplayName() + " misses " + fightable.getDisplayName() + ".");
				}
				return;
			}
			Double multiplier = new Double(random.nextInt(200));
			Double attack = new Double(player.getDamage());
			double damage = (multiplier / 100) * attack;
			damage = Precision.round(damage, 0);
			damage = damage - fightable.getDamageReduction();
			if (damage <= 0) {
				output = "Your blow is unable to damage * !";
				outputOther = "* fails to damage &";
			} else if (multiplier > 150) {
				output = "You deal * a crushing blow!";
				outputOther = "* deals & a crushing blow!";
			} else if (multiplier > 110) {
				output = "You deliver * a good strong hit.";
				outputOther = "* deals & a good strong hit.";
			} else if (multiplier > 90) {
				output = "You strike *.";
				outputOther = "* strikes &.";
			} else if (multiplier > 50) {
				output = "You weakly hit *.";
				outputOther = "* weakly hits &.";
			} else {
				output = "You barely scratch *.";
				outputOther = "* barely scratches &.";
			}
			output = output.replace("*", needsAThe + fightable.getDisplayName());

			outputOther = outputOther.replace("*", player.getDisplayName());
			Output.outputToTarget(player, output);
			if (fightable instanceof Player) {
				outputOther = outputOther.replace("&", "you");
				Output.outputToTarget((Player) fightable, outputOther);
				Output.outputToRoomExceptTarget(player, (Player) fightable, outputOther);
			} else {
				outputOther = outputOther.replace("&", needsAThe + fightable.getDisplayName());
				Output.outputToRoom(player, outputOther);
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

			String needsAThe = Output.needsAThe(actor.getDisplayName());
			if (actor.getKillText() != null && actor.getKillText().length() > 0) {
				Output.outputToTarget(player, actor.getKillText());
				Output.outputToRoom(player,
						player.getDisplayName() + " has slain " + needsAThe + actor.getDisplayName() + "!");
			} else {
				Output.outputToTarget(player, "You have slain " + needsAThe + actor.getDisplayName() + "!");
				Output.outputToRoom(player,
						player.getDisplayName() + " has slain " + needsAThe + actor.getDisplayName() + "!");

			}
			actor.setIsDead(true);
			if (actor.getItemsHeld() != null && actor.getItemsHeld().size() > 0) {
				Room room = player.getCurrentRoom();
				for (Item item : actor.getItemsHeld()) {
					room.getItemsPresent().add(item.getName());
					String baseMessage = needsAThe + actor.getDisplayName() + " drops " + item.getDisplayName() + ".";
					Output.outputToTarget(player, Output.capitalize(baseMessage));
					Output.outputToRoom(player, Output.capitalize(baseMessage));
				}
			}
		} else if (fightable instanceof Player) {
			Player deadPlayer = (Player) fightable;
			Output.outputToTarget(player, "You have slain " + deadPlayer.getDisplayName() + "!");
			Output.outputToRoomExceptTarget(player, deadPlayer, player.getDisplayName()+" has slain "+deadPlayer.getDisplayName()+"!");
			Output.outputToTarget(deadPlayer, "You have been slain by "+player.getDisplayName()+"!");
			deadPlayer.die();

		}

	}
}
