package com.fdmgroup.zorkclone.actors;


import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.combat.Fightable;
import com.fdmgroup.zorkclone.effects.Effectables;
import com.fdmgroup.zorkclone.items.Item;

public class Actor implements Effectables, Fightable{
	private String name;
	private String displayName;
	private List<String> aliases;
	private String displayText;
	private String examine;
	private List<Behaviour> behaviours = new ArrayList<>();;
	private int health;
	private int maxHealth;
	private List<Item> itemsHeld=new ArrayList<>();
	private boolean isDead;
	private String deadDisplayText;
	private String deadExamineText;
	private int damage;
	private int damageReduction;
	private int accuracy;
	private int accuracyReduction;
	private int speed;
	private String killText;
	private List<String> effects = new ArrayList<>();
	
	
	
	
	
	public List<String> getEffects() {
		return effects;
	}

	public void setEffects(List<String> effects) {
		this.effects = effects;
	}

	public String getKillText() {
		return killText;
	}

	public void setKillText(String killText) {
		this.killText = killText;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public List<Behaviour> getBehaviours() {
		return behaviours;
	}

	public void setBehaviours(List<Behaviour> behaviours) {
		this.behaviours = behaviours;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDamageReduction() {
		return damageReduction;
	}

	public void setDamageReduction(int damageReduction) {
		this.damageReduction = damageReduction;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public int getAccuracyReduction() {
		return accuracyReduction;
	}

	public void setAccuracyReduction(int accuracyReduction) {
		this.accuracyReduction = accuracyReduction;
	}

	public String getExamine() {
		return examine;
	}

	public String examine() {
		if(Main.isWeb){
			return examineWeb();
		}
		if(isDead) {
			return deadExamineText;
		}else {
		String appearance = examine;
		String needsAThe;
		if (Character.isUpperCase(displayName.charAt(0))) {
			needsAThe="";
		}else {
			needsAThe="The ";
		}
		if(health==maxHealth) {
			appearance = appearance+"\n"+needsAThe+displayName+" is in full health.";
		}else if(health>=3*(maxHealth/4)){
			appearance = appearance+"\n"+needsAThe+displayName+" looks lightly injured.";
		}else if(health>=2*(maxHealth/4)){
			appearance = appearance+"\n"+needsAThe+displayName+" looks moderately injured.";
		}else{
			appearance = appearance+"\n"+needsAThe+displayName+" looks severely injured.";
			
		}
		
		return appearance;
		}
	}
	
	public String getDisplay() {
		if(isDead) {
			return deadDisplayText;
		}else {
			return displayText;
		}
	}

	public String getDeadExamineText() {
		return deadExamineText;
	}

	public void setDeadExamineText(String deadExamineText) {
		this.deadExamineText = deadExamineText;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}



	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public List<Item> getItemsHeld() {
		return itemsHeld;
	}

	public void setItemsHeld(List<Item> itemsHeld) {
		this.itemsHeld = itemsHeld;
	}

	public boolean getIsDead() {
		return isDead;
	}

	public void setIsDead(boolean isDead) {
		this.isDead = isDead;
	}

	public String getDeadDisplayText() {
		return deadDisplayText;
	}

	public void setDeadDisplayText(String deadDisplayText) {
		this.deadDisplayText = deadDisplayText;
	}

	public void setExamine(String examine) {
		this.examine = examine;
	}

	

	public Actor(String name, String displayName, List<String> aliases, String displayText, String examine,
			List<Behaviour> behaviours, int health, int maxHealth, List<Item> itemsHeld, boolean isDead,
			String deadDisplayText, String deadExamineText, int damage, int damageReduction, int accuracy,
			int accuracyReduction, int speed) {
		super();
		this.name = name;
		this.displayName = displayName;
		this.aliases = aliases;
		this.displayText = displayText;
		this.examine = examine;
		this.behaviours = behaviours;
		this.health = health;
		this.maxHealth = maxHealth;
		this.itemsHeld = itemsHeld;
		this.isDead = isDead;
		this.deadDisplayText = deadDisplayText;
		this.deadExamineText = deadExamineText;
		this.damage = damage;
		this.damageReduction = damageReduction;
		this.accuracy = accuracy;
		this.accuracyReduction = accuracyReduction;
		this.speed=speed;
	}

	public Actor() {
		super();
	}

	@Override
	public String toString() {
		return "Actor [name=" + name + ", displayName=" + displayName + ", aliases=" + aliases + ", displayText="
				+ displayText + ", examine=" + examine + ", behaviours=" + behaviours + ", health=" + health
				+ ", maxHealth=" + maxHealth + ", itemsHeld=" + itemsHeld + ", isDead=" + isDead + ", deadDisplayText="
				+ deadDisplayText + "]";
	}

	public void displayDetails() {
		System.out.println("Actor id: "+getName()+" name: "+getDisplayName());
		System.out.println("Aliases: "+aliases);
		System.out.println("Start Health: "+health+" and max health: "+maxHealth);
		System.out.println("Damage: "+damage+" and accuracy: "+accuracy+" and attack speed:"+speed);
		System.out.println("Damage reduction: "+damageReduction+" and evasion: "+accuracyReduction);
		
		String items="";
		for(Item item:getItemsHeld()){
			items+=item.getDisplayName()+", ";
		}
		System.out.println("Items held: "+items);
		System.out.println(items);
		System.out.println("Starts dead?: "+isDead);
		System.out.println("Examine: "+examine);
		System.out.println("Examine when dead: "+deadExamineText);
		System.out.println("Display text: "+displayText);
		System.out.println("Dead display text: "+deadDisplayText);
		System.out.println("Behaviour: "+behaviours);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actor other = (Actor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String examineWeb() {
		if(isDead) {
			return deadExamineText;
		}else {
		String appearance = examine;
		String needsAThe;
		if (Character.isUpperCase(displayName.charAt(0))) {
			needsAThe="";
		}else {
			needsAThe="The ";
		}
		if(health==maxHealth) {
			appearance = appearance+"<br />"+needsAThe+displayName+" is in full health.";
		}else if(health>=3*(maxHealth/4)){
			appearance = appearance+"<br />"+needsAThe+displayName+" looks lightly injured.";
		}else if(health>=2*(maxHealth/4)){
			appearance = appearance+"<br />"+needsAThe+displayName+" looks moderately injured.";
		}else{
			appearance = appearance+"<br />"+needsAThe+displayName+" looks severely injured.";
			
		}
		
		return appearance;
		}
	}

	

	
	
}
