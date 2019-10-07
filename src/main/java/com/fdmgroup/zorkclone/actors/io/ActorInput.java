package com.fdmgroup.zorkclone.actors.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.Actor;
import com.fdmgroup.zorkclone.actors.Behaviour;
import com.fdmgroup.zorkclone.input.MenuInput;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.io.ItemReader;

public class ActorInput {
	private BufferedReader reader;

	public ActorInput(BufferedReader reader) {
		super();
		this.reader = reader;
	}

	public Actor getActor() {

		try {
			Actor actor = new Actor();
			actor = getId(actor);
			if(actor==null){
				return null;
			}
			
			actor.displayDetails();
			actor = getDisplayName(actor);
			if(actor==null){
				return null;
			}

			actor.displayDetails();
			actor = getExamineText(actor);
			if(actor==null){
				return null;
			}
			
			actor.displayDetails();
			actor = getIsDead(actor);
			if(actor==null){
				return null;
			}
			
			actor.displayDetails();
			actor = getDeadExamineText(actor);
			if(actor==null){
				return null;
			}

			actor.displayDetails();
			actor = getDisplayText(actor);
			if(actor==null){
				return null;
			}

			actor.displayDetails();
			actor = getDeadDisplayText(actor);
			if(actor==null){
				return null;
			}

			actor.displayDetails();
			actor = getAliases(actor);
			if(actor==null){
				return null;
			}


			actor.displayDetails();
			actor = getStartHp(actor);
			if(actor==null){
				return null;
			}
			
			actor.displayDetails();
			actor = getMaxHp(actor);
			if(actor==null){
				return null;
			}
			
			actor.displayDetails();
			actor = getDamage(actor);
			if(actor==null){
				return null;
			}
			
			actor.displayDetails();
			actor = getSpeed(actor);
			if(actor==null){
				return null;
			}
			
			actor.displayDetails();
			actor = getAccuracy(actor);
			if(actor==null){
				return null;
			}
			
			actor.displayDetails();
			actor = getDamageReduction(actor);
			if(actor==null){
				return null;
			}
			
			actor.displayDetails();
			actor = getAccuracyReduction(actor);
			if(actor==null){
				return null;
			}

			actor.displayDetails();
			actor = getItems(actor);
			if(actor==null){
				return null;
			}
			
			actor.displayDetails();
			actor = getKillText(actor);
			if(actor==null){
				return null;
			}
			
			actor.displayDetails();
			actor = getBehaviour(actor);

			return actor;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Actor getKillText(Actor actor) throws IOException {

			System.out.println("Please enter text to be displayed on kill, or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else {
				actor.setKillText(input);
				return actor;

			} 

	}

	private Actor getSpeed(Actor actor) throws IOException {
		while (true) {
			System.out.println("Please enter actor attack speed(1-10, higher is slower), or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {
				actor.setSpeed(Integer.parseInt(input));
				return actor;

			} else {
				System.out.println("Invalid non-numeric input");
			}
		}
	}

	private Actor getAccuracyReduction(Actor actor) throws IOException {
		while (true) {
			System.out.println("Please enter actor evasion, or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {
				actor.setAccuracyReduction(Integer.parseInt(input));
				return actor;

			} else {
				System.out.println("Invalid non-numeric input");
			}
		}
	}

	private Actor getDamageReduction(Actor actor) throws IOException {
		while (true) {
			System.out.println("Please enter actor damage reduction, or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {
				actor.setDamageReduction(Integer.parseInt(input));
				return actor;

			} else {
				System.out.println("Invalid non-numeric input");
			}
		}
	}

	private Actor getAccuracy(Actor actor) throws IOException {
		while (true) {
			System.out.println("Please enter actor attack accuracy, or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {
				actor.setAccuracy(Integer.parseInt(input));
				return actor;

			} else {
				System.out.println("Invalid non-numeric input");
			}
		}
	}

	private Actor getDamage(Actor actor) throws IOException {
		while (true) {
			System.out.println("Please enter actor attack damage, or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {
				actor.setDamage(Integer.parseInt(input));
				return actor;

			} else {
				System.out.println("Invalid non-numeric input");
			}
		}
	}

	private Actor getBehaviour(Actor actor) throws IOException {
		List<Behaviour> behaviours = new ArrayList<>();
		while (true) {
			actor.displayDetails();
			System.out.println("Please enter a behaviour type, or type 'q' to skip.");
			System.out.println("Available behaviours: "+java.util.Arrays.asList(Behaviour.values()));
			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				actor.setBehaviours(behaviours);
				return actor;
			} else {
				try {
					Behaviour behaviour = Behaviour.valueOf(input.toUpperCase());
					
						behaviours.add(behaviour);
						actor.setBehaviours(behaviours);

					
				} catch (Exception e) {
					System.out.println("I'm sorry, that was not a valid behaviour.");
					continue;
				}

			}
		}
	}
	
	private Actor getIsDead(Actor actor) throws IOException {
		while(true){
			System.out.println("Should the actor be alive initially(Y/N)? Or type 'q' to cancel.");
			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if(input.toUpperCase().equals("Y") || input.toUpperCase().equals("YES")) {
				actor.setIsDead(false);
				return actor;
			} else if(input.toUpperCase().equals("N") || input.toUpperCase().equals("NO")) {
				actor.setIsDead(true);
				return actor;
			} else{
				System.out.println("Please enter (y)es or (n)o.");
			}
		}

	}

	private Actor getItems(Actor actor) throws IOException {

		List<Item> itemsHeld = new ArrayList<>();
		while (true) {
			System.out.println("Please enter the name of an item held by the actor, or type 'q' to skip.");
			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				actor.setItemsHeld(itemsHeld);
				return actor;
			} else {
				try{
					ItemReader reader = Main.getItemReader(Main.baseGamePath);
					Item item = reader.readItem(input);
					itemsHeld.add(item);
				}catch(Exception e){
					System.out.println("That item does not exist yet!");
				}
			}
		}
	}

	private Actor getStartHp(Actor actor) throws IOException {
		while (true) {
			System.out.println("Please enter actor start hp, or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {
				actor.setHealth(Integer.parseInt(input));
				return actor;

			} else {
				System.out.println("Invalid non-numeric input");
			}
		}
	}

	private Actor getMaxHp(Actor actor) throws IOException {

		while (true) {
			System.out.println("Please enter actor max hp, or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {
				actor.setMaxHealth(Integer.parseInt(input));
				return actor;

			} else {
				System.out.println("Invalid non-numeric input");
			}
		}

	}

	private Actor getAliases(Actor actor) throws IOException {
		List<String> aliases = new ArrayList<>();

		while (true) {
			System.out.println("Please enter an alias for this actor, or type 'q' to skip.");
			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				actor.setAliases(aliases);
				return actor;
			} else {
				aliases.add(input);
				actor.setAliases(aliases);
			}
		}
	}

	private Actor getDeadDisplayText(Actor actor) throws IOException {
		System.out.println("Please enter the actor's room display text when dead, or type 'q' to cancel.");

		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			actor.setDeadDisplayText(input);
		}

		return actor;
	}

	private Actor getDisplayText(Actor actor) throws IOException {
		System.out.println("Please enter the actor's room display text, or type 'q' to cancel.");

		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			actor.setDisplayText(input);
		}
		return actor;
	}

	private Actor getDeadExamineText(Actor actor) throws IOException {
		System.out.println("Please enter the actor's examine text when dead, or type 'q' to cancel.");

		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			actor.setDeadExamineText(input);
		}
		return actor;
	}

	private Actor getExamineText(Actor actor) throws IOException {
		System.out.println("Please enter the actor's examine text, or type 'q' to cancel.");

		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			actor.setExamine(input);
		}
		return actor;
	}

	private Actor getDisplayName(Actor actor) throws IOException {
		System.out.println("Please enter the display name of the actor, or type 'q' to cancel.");

		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			actor.setDisplayName(input);
		}
		return actor;
	}

	private Actor getId(Actor actor) throws IOException {
		System.out.println("Please enter the id name of the actor, or type 'q' to cancel.");
		MenuInput menu = new MenuInput(reader);
		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			Path path = Paths.get(Main.baseGamePath.toString(), "/actors/" + input + ".txt");
			File file = path.toFile();
			if (file.exists()) {
				if (!menu.confirmOverride(input)) {
					return null;
				}
			}
			actor.setName(input);
		}
		return actor;

	}

	public void editHub(String choice, Actor actor) throws IOException {
		if(choice.equals("ID")){
			actor = getId(actor);
		}else if(choice.equals("NAME")){
			actor = getDisplayName(actor);
		}else if(choice.equals("ALIAS")){
			actor = getAliases(actor);
		}else if(choice.equals("INITHP")){
			actor = getStartHp(actor);
		}else if(choice.equals("MAXHP")){
			actor = getMaxHp(actor);
		}else if(choice.equals("ITEMS")){
			actor = getItems(actor);
		}else if(choice.equals("XTEXT")){
			actor = getExamineText(actor);
		}else if(choice.equals("DEADXTEXT")){
			actor = getDeadExamineText(actor);
		}else if(choice.equals("DISPLAY")){
			actor = getDisplayText(actor);
		}else if(choice.equals("DEADDISPLAY")){
			actor = getDeadDisplayText(actor);
		}else if(choice.equals("DEAD")){
			actor = getIsDead(actor);
		}else if(choice.equals("BEHAVIOUR")){
			actor = getBehaviour(actor);
		}else if(choice.equals("DAMAGE")){
			actor = getDamage(actor);
		}else if(choice.equals("ACCURACY")){
			actor = getAccuracy(actor);
		}else if(choice.equals("DAMAGEREDUX")){
			actor = getDamageReduction(actor);
		}else if(choice.equals("ACCURACYREDUX")){
			actor = getAccuracyReduction(actor);
		}else if(choice.equals("SPEED")){
			actor = getSpeed(actor);
		}else if(choice.equals("KILLTEXT")){
			actor = getKillText(actor);
		}
		ActorReader reader= Main.getActorReader(Main.baseGamePath);
		reader.writeActor(actor);
		
	}

	

}
