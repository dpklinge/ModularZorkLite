package com.fdmgroup.zorkclone.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.combat.CombatChecker;
import com.fdmgroup.zorkclone.combat.Fightable;
import com.fdmgroup.zorkclone.commands.ListFetchUtil;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.Slot;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;
import com.fdmgroup.zorkclone.user.User;
import com.fdmgroup.zorkclone.weboutput.Output;

public class Player implements Fightable {
	private User user = new User();

	{
		user.setUsername("System");
	}

	private boolean attackReady = true;
	private String name = "me";
	private String displayText;
	private Room currentRoom = Main.getRoomReader(Main.baseGamePath).readRoom(Main.startRoom);
	private List<Item> heldItem = new ArrayList<>();
	private int health = 100;
	private int maxHealth = 100;
	private String description = "You are a generic adventurer, not worthy of describing. Cry me a river.";
	private int damage = 1;
	private int accuracy = 100;
	private int damageReduction = 0;
	private int accuracyReduction = 0;
	private int bulk = 1;
	private HashMap<Slot, Item> slotsEquipped = new HashMap<>();
	private boolean isDead = false;

	public String getDisplayName() {
		return name;
	}

	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add(name);
		for (String alias : name.split(" ")) {
			if (!aliases.contains(alias)) {
				aliases.add(alias);
			}
		}
		return aliases;
	}

	public String getKillText() {
		return "You have defeated " + getDisplayName() + "!";
	}

	public String getDisplayText() {
		return name + " is standing here.";
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHp) {
		this.maxHealth = maxHp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isAttackReady() {
		return attackReady;
	}

	public void setAttackReady(boolean attackReady) {
		this.attackReady = attackReady;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsDead() {
		return isDead;
	}

	public void setIsDead(boolean isDead) {
		this.isDead = isDead;
	}

	public int getBulk() {
		return bulk;
	}

	public void setBulk(int bulk) {
		this.bulk = bulk;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public HashMap<Slot, Item> getSlotsEquipped() {
		return slotsEquipped;
	}

	public void setSlotsEquipped(HashMap<Slot, Item> slotsEquipped) {
		this.slotsEquipped = slotsEquipped;
	}

	public Player() {

	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int attack) {
		this.damage = attack;
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

	public Room getCurrentRoom() {
		return Main.getRoomReader().readRoom(currentRoom.getName());
	}

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}

	public List<Item> getHeldItem() {
		return heldItem;
	}

	public void setHeldItem(List<Item> heldItem) {
		this.heldItem = heldItem;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int hp) {
		this.health = hp;
	}

	public String getDescription() {
		return description;
	}

	public String examine() {
		String appearance = new String();
		if (Main.isWeb) {
			appearance = description;
			if (health == maxHealth) {
				appearance = appearance + "<br />" + name + " is in full health.";
			} else if (health >= 3 * (maxHealth / 4)) {
				appearance = appearance + "<br />" + name + " looks lightly injured.";
			} else if (health >= 2 * (maxHealth / 4)) {
				appearance = appearance + "<br />" + name + " looks moderately injured.";
			} else {
				appearance = appearance + "<br />" + name + " looks severely injured.";

			}
			return appearance;

		}
		appearance = description + "\n";
		if (health == maxHealth) {
			appearance = appearance + "\n" + name + " is in full health.";
		} else if (health >= 3 * (maxHealth / 4)) {
			appearance = appearance + "\n" + name + " looks lightly injured.";
		} else if (health >= 2 * (maxHealth / 4)) {
			appearance = appearance + "\n" + name + " looks moderately injured.";
		} else {
			appearance = appearance + "\n" + name + " looks severely injured.";

		}
		return appearance;

	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String displayItems() {
		if (Main.isWeb) {
			return displayItemsWeb();
		}
		String inventory = "You are currently holding: \n";
		if ((heldItem.isEmpty() || Objects.isNull(heldItem))
				&& (slotsEquipped.isEmpty() || Objects.isNull(slotsEquipped))) {
			inventory = inventory + "Nothing!";
		}
		for (Item item : heldItem) {
			inventory = inventory + item.getQuantity() + " ";
			inventory = inventory + item.getDisplayName() + "\n";

		}
		for (Entry<Slot, Item> gear : slotsEquipped.entrySet()) {
			inventory = inventory + gear.getValue().getQuantity() + " (equipped to " + Slot.getEnumString(gear.getKey())
					+ ") " + gear.getValue().getDisplayName() + "\n";
		}

		return inventory;
	}

	public String die() {
		// setDead(true);
		PlayerReader playerReader = Main.getPlayerReader();
		playerReader.writePlayer(this);
		System.out.println("You have been slain!");

		RoomReader roomReader = Main.getRoomReader(Main.savedGamePath);
		String dropped = name + " dropped: ";

		for (Item item : heldItem) {
			currentRoom.getItemsPresent().add(item.getName());
			dropped = dropped + " " + item.getDisplayName();
		}
		for (Entry<Slot, Item> gear : slotsEquipped.entrySet()) {
			currentRoom.getItemsPresent().add(gear.getValue().getName());
			dropped = dropped + " " + gear.getValue().getDisplayName();
		}
		roomReader.writeRoom(currentRoom);
		System.exit(0);
		return dropped;

	}

	public static void newPlayer() {
		PlayerReader playerReader = Main.getPlayerReader();
		Player player = new Player();

		playerReader.writePlayer(player);

	}

	public String examineWeb() {
		String appearance = new String();
		appearance = description + "<br />";
		if (health == maxHealth) {
			appearance = appearance + "<br />" + name + " is in full health.";
		} else if (health >= 3 * (maxHealth / 4)) {
			appearance = appearance + "<br />" + name + " looks lightly injured.";
		} else if (health >= 2 * (maxHealth / 4)) {
			appearance = appearance + "<br />" + name + " looks moderately injured.";
		} else {
			appearance = appearance + "<br />" + name + " looks severely injured.";

		}
		return appearance;

	}

	public String displayItemsWeb() {
		String inventory = "You are currently holding: <br />";
		if ((heldItem.isEmpty() || Objects.isNull(heldItem))
				&& (slotsEquipped.isEmpty() || Objects.isNull(slotsEquipped))) {
			inventory = inventory + "Nothing!";
		}
		for (Item item : heldItem) {

			inventory = inventory + item.getQuantity() + " ";
			inventory = inventory + item.getDisplayName() + "<br />";
		}
		for (Entry<Slot, Item> gear : slotsEquipped.entrySet()) {
			inventory = inventory + gear.getValue().getQuantity() + " (equipped to " + Slot.getEnumString(gear.getKey())
					+ ") " + gear.getValue().getDisplayName() + "<br />";
		}

		return inventory;
	}

	public String dieWeb(Output output, CombatChecker checker) {
		setIsDead(true);
		health = maxHealth;

		RoomReader roomReader = Main.getRoomReader(Main.savedGamePath);

		String dropped = name + " dropped: ";
		int invSize = heldItem.size();
		for (int i = 0; i < invSize; i++) {
			dropped = dropped + " " + heldItem.get(0).getDisplayName() + ",";
			currentRoom.getItemsPresent().add(heldItem.get(0).getName());
			heldItem.remove(0);
		}
		heldItem = new ArrayList<>();
		Set<Entry<Slot, Item>> gear = slotsEquipped.entrySet();
		List<Entry<Slot, Item>> gearList = new ArrayList<>(gear);
		invSize = gearList.size();
		for (int i = 0; i < invSize; i++) {
			dropped = dropped + " " + gearList.get(0).getValue().getDisplayName() + ",";
			currentRoom.getItemsPresent().add(gearList.get(0).getValue().getName());
			gearList.remove(0);
		}
		slotsEquipped = new HashMap<>();
		dropped = dropped.substring(0, dropped.length() - 1);
		output.outputToTarget(this, dropped);
		output.outputToTarget(this, "You are magically whisked back to the starting room!");
		output.outputToTargetRoom(this, roomReader.readRoom(Main.startRoom),
				"The heavens open up, and " + getDisplayName() + " descends, returned again to this mortal coil!");
		output.outputToRoom(this, dropped);
		checker.leaveRoom(this, roomReader.readRoom(Main.startRoom), null);

		checker.enterRoom(this, roomReader.readRoom(Main.startRoom));
		currentRoom = roomReader.readRoom(Main.startRoom);

		output.outputToTarget(this, currentRoom.displayRoomWeb(this, output));
		PlayerReader playerReader = Main.getPlayerReader();
		setIsDead(false);
		playerReader.writePlayer(this);

		return dropped;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Player [user=" + user + ", attackReady=" + attackReady + ", name=" + name + ", displayText="
				+ displayText + ", currentRoom=" + currentRoom.getName() + ", heldItem=" + heldItem + ", health="
				+ health + ", maxHealth=" + maxHealth + ", description=" + description + ", damage=" + damage
				+ ", accuracy=" + accuracy + ", damageReduction=" + damageReduction + ", accuracyReduction="
				+ accuracyReduction + ", bulk=" + bulk + ", slotsEquipped=" + slotsEquipped + ", isDead=" + isDead
				+ ", killText=" + getKillText() + "]";
	}

}
