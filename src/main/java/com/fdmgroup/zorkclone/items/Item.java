package com.fdmgroup.zorkclone.items;

import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.zorkclone.effects.Effectables;

public class Item implements Effectables {
	private String name;
	private String displayName;
	private List<String> aliases=new ArrayList<>();
	private String examine;
	private boolean visible;
	private String groundText;
	private ItemType type;
	private List<String> effects;
	private int damageReduction;
	private int accuracyReduction;
	private int bulk;
	private Slot slot;
	private int quantity;
	private int damage;
	private int accuracy;
	private boolean isHoldable;
	private boolean isEquipable;
	
	
	public boolean getIsHoldable() {
		return isHoldable;
	}
	public void setIsHoldable(boolean isHoldable) {
		this.isHoldable = isHoldable;
	}
	public boolean getIsEquipable() {
		return isEquipable;
	}
	public void setIsEquipable(boolean isEquipable) {
		this.isEquipable = isEquipable;
	}
	public int getDamageReduction() {
		return damageReduction;
	}
	public void setDamageReduction(int damageReduction) {
		this.damageReduction = damageReduction;
	}
	public int getAccuracyReduction() {
		return accuracyReduction;
	}
	public void setAccuracyReduction(int accuracyReduction) {
		this.accuracyReduction = accuracyReduction;
	}
	public int getBulk() {
		return bulk;
	}
	public void setBulk(int bulk) {
		this.bulk = bulk;
	}
	public Slot getSlot() {
		return slot;
	}
	public void setSlot(Slot slot) {
		this.slot = slot;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	public List<String> getEffects() {
		return effects;
	}
	public void setEffects(List<String> effects) {
		this.effects = effects;
	}
	public String getGroundText() {
		return groundText;
	}
	public void setGroundText(String groundText) {
		this.groundText = groundText;
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
	public String getExamine() {
		return examine;
	}
	public String examine() {
		return examine;
	}
	public void setExamine(String examine) {
		this.examine = examine;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public ItemType getType() {
		return type;
	}
	public void setType(ItemType type) {
		this.type = type;
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
		Item other = (Item) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	public void displayDetails() {
		System.out.println("Item id: "+name+" name: "+displayName);
		System.out.println("Examine text: "+examine);
		System.out.println("Aliases: "+getAliases());
		System.out.println("Is visible? "+isVisible());
		System.out.println("Ground display text: "+groundText);
		System.out.println("Item type: "+type);
		System.out.println("Effects : "+effects);
		
	}

	
	
	

	
	
	

}
