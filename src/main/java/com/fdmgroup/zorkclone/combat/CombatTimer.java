package com.fdmgroup.zorkclone.combat;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.math3.util.Precision;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.Actor;
import com.fdmgroup.zorkclone.actors.io.ActorReader;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.weboutput.Output;

public class CombatTimer {
	private Timer timer = new Timer();
	private ActorReader reader = Main.getActorReader(Main.savedGamePath);
	PlayerReader playerReader;
	private Actor actor;
	private Player player;

	public CombatTimer(Actor actor, Player player) {
		super();
		this.actor = actor;
		this.player = player;
		playerReader = Main.getPlayerReader();
	}

	private TimerTask actorAttack = new TimerTask() {
		public void run() {
			player = playerReader.getPlayer(player.getUser().getUsername(), player.getName());
			actor = reader.readActor(actor.getName());
			if (actor.getIsDead() || !player.getCurrentRoom().getActorsPresent().contains(actor.getName())) {
				cancel();
				timer.cancel();
			} else {
				doAttack(actor);
			}

		}

		private void doAttack(Actor actor) {
			String needsAThe;
			if (Character.isUpperCase(actor.getDisplayName().charAt(0))) {

				needsAThe = "";
			} else {

				needsAThe = "the ";

			}

			Random random = new Random();
			Output.outputToTarget(player, "You are attacked by " + needsAThe + actor.getDisplayName() + "!");

			int accuracyCheck = random.nextInt(100);
			if (accuracyCheck > actor.getAccuracy()) {
				Output.outputToTarget(player, "...but it's a miss!");
				String roomOutput = Output
						.capitalize(needsAThe + actor.getDisplayName() + " misses " + player.getName());
				Output.outputToRoom(player, roomOutput);
				return;
			}
			Double multiplier = new Double(random.nextInt(200));
			Double attack = new Double(actor.getDamage());
			double damage = (multiplier / 100) * attack;
			damage = Precision.round(damage, 0);
			damage = damage - player.getDamageReduction();
			if (damage <= 0) {
				Output.outputToTarget(player, "Your armor completely absorbs the blow.");
			} else if (multiplier > 150) {
				Output.outputToTarget(player,
						"You are dealt a crushing blow by " + needsAThe + actor.getDisplayName() + "!");
				String roomOutput = Output.capitalize(
						needsAThe + actor.getDisplayName() + " dealt a crushing blow to " + player.getName());
				Output.outputToRoom(player, roomOutput);
			} else if (multiplier > 110) {
				Output.outputToTarget(player,
						"You recieve a good strong hit from " + needsAThe + actor.getDisplayName() + ".");
				String roomOutput = Output.capitalize(player.getName() + " recieves a good strong hit from " + needsAThe
						+ actor.getDisplayName() + ".");
				Output.outputToRoom(player, roomOutput);
			} else if (multiplier > 90) {
				Output.outputToTarget(player, "You are hit by " + needsAThe + actor.getDisplayName() + ".");
				String roomOutput = Output
						.capitalize(player.getName() + " is hit by " + needsAThe + actor.getDisplayName() + ".");
				Output.outputToRoom(player, roomOutput);
			} else if (multiplier > 50) {
				Output.outputToTarget(player, "You are weakly hit by " + needsAThe + actor.getDisplayName() + ".");
				String roomOutput = Output
						.capitalize(player.getName() + " is weakly hit by " + needsAThe + actor.getDisplayName() + ".");
				Output.outputToRoom(player, roomOutput);
			} else {
				Output.outputToTarget(player,
						"You are barely scratched by " + needsAThe + actor.getDisplayName() + ".");
				String roomOutput = Output
						.capitalize(needsAThe + actor.getDisplayName() + " barely scratches " + player.getName());
				Output.outputToRoom(player, roomOutput);
			}
			if (damage >= player.getHealth()) {
				Output.outputToTarget(player, "You have been slain by " + needsAThe + actor.getDisplayName() + "!");
				Output.outputToRoom(player,
						player.getDisplayName() + "has been slain by " + needsAThe + actor.getDisplayName() + "!");
				if (Main.isWeb) {
					player.dieWeb();
				} else {
					player.die();
				}
			} else if (damage > 0) {
				player.setHealth(player.getHealth() - (int) damage);
			}
			playerReader.writePlayer(player);

		}
	};

	public void stopAttackThread(Actor actor) {
		timer.cancel();
		actorAttack.cancel();

	}

	public void startAttackThread(Actor actor) {
		this.actor = actor;
		if (actor.getSpeed() <= 0) {
			timer.schedule(actorAttack, 100);
		} else {
			timer.scheduleAtFixedRate(actorAttack, actor.getSpeed() * 1000, actor.getSpeed() * 1000);
		}
	}

}
