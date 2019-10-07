package com.fdmgroup.zorkclone.commands;

import java.util.List;
import java.util.Map.Entry;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.ItemType;
import com.fdmgroup.zorkclone.items.Slot;
import com.fdmgroup.zorkclone.items.io.ItemReader;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;
import com.fdmgroup.zorkclone.weboutput.Output;

public class InventoryManager {
	private RoomReader roomReader = Main.getRoomReader(Main.savedGamePath);
	
	public void doGet(Player player,String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Get WHAT?");
			return;
		}
		String target = commands[1];
		player.setCurrentRoom(roomReader.readRoom(player.getCurrentRoom().getName()));
		try {

			List<String> items = player.getCurrentRoom().getItemsPresent();

			if (items == null || items.size() < 1) {
				Output.outputToTarget(player, "I'm sorry, I dont see any " + target.toLowerCase() + " here...");
			} else {
				ItemReader itemReader = Main.getItemReader(Main.savedGamePath);
				for (String item : items) {
					Item thisItem = itemReader.readItem(item);
					if (thisItem.getAliases().stream().anyMatch(target::equalsIgnoreCase)) {
						if (!thisItem.getIsHoldable()) {
							Output.outputToTarget(player, "I'm afraid you can't pick that up!");
							return;
						} else {
							String needsAThe = Output.needsAThe(thisItem.getDisplayName());
							if ((thisItem).getQuantity() < 2) {

								Output.outputToTarget(player,
										"You pick up " + needsAThe + thisItem.getDisplayName() + ".");
								Output.outputToRoom(player, player.getDisplayName() + " picks up " + needsAThe
										+ thisItem.getDisplayName() + ".");
							} else {
								Output.outputToTarget(player,
										"You pick up " + needsAThe + thisItem.getDisplayName() + "s.");
								Output.outputToRoom(player, player.getDisplayName() + " picks up " + needsAThe
										+ thisItem.getDisplayName() + "s.");
							}
							player.getHeldItem().add(thisItem);
							player.getCurrentRoom().getItemsPresent().remove(item);
							roomReader.writeRoom(player.getCurrentRoom());
							return;
						}

					} else {

					}

				}
				Output.outputToTarget(player,
						"I'm sorry, I dont see any " + target.toLowerCase() + " that you can get here...");
			}
		} catch (Exception e) {

		}

	}
	
	public void doDrop(Player player, String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Drop WHAT?");
			return;
		}
		String output = "";
		String outputOther = "";
		String target = commands[1];
		if (target.toUpperCase().equalsIgnoreCase("ALL")) {
			List<Item> items = player.getHeldItem();
			if (items.size() == 0) {
				Output.outputToTarget(player, "You don't have any loose items to drop!");
			}
			for (Item item : items) {
				player.getCurrentRoom().getItemsPresent().add(item.getName());

				if (item.getQuantity() < 2) {
					output = "You drop " + Output.needsAThe(item.getDisplayName()) + item.getDisplayName() + ".";
					outputOther = player.getDisplayName() + " drops " + Output.needsAThe(item.getDisplayName())
							+ item.getDisplayName() + ".";
				} else {
					output = "You drop " + Output.needsAThe(item.getDisplayName()) + item.getDisplayName() + "s.";
					outputOther = player.getDisplayName() + " drops " + Output.needsAThe(item.getDisplayName())
							+ item.getDisplayName() + "s.";
				}
				Output.outputToTarget(player, output);
				Output.outputToRoom(player, outputOther);
			}
			int size = items.size();
			for (int i = 0; i < size; i++) {
				player.getHeldItem().remove(items.get(0));
			}
			return;
		}

		try {
			Item item =getItemForRemoval(player,target);
			if (item == null) {
				output = "You don't have " + target + " to drop!";
				Output.outputToTarget(player, output);
			} else {
				if (item.getQuantity() < 2) {
					output = "You drop " + Output.needsAThe(item.getDisplayName()) + item.getDisplayName() + ".";
					outputOther = player.getDisplayName() + " drops " + Output.needsAThe(item.getDisplayName())
							+ item.getDisplayName() + ".";
				} else {
					output = "You drop " + Output.needsAThe(item.getDisplayName()) + item.getDisplayName() + "s.";
					outputOther = player.getDisplayName() + " drops " + Output.needsAThe(item.getDisplayName())
							+ item.getDisplayName() + "s.";
				}
				Output.outputToTarget(player, output);
				Output.outputToRoom(player, outputOther);
				player.getHeldItem().remove(item);
				player.getCurrentRoom().getItemsPresent().add(item.getName());
				return;
			}
			Output.outputToTarget(player, "I'm sorry, you don't seem to have any " + target.toLowerCase() + "...");
		} catch (Exception e) {
			System.out.println("Some error with dropping");
			System.out.println(e);
		}

	}
	
	
	public void doUnequip(Player player, String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Equip WHAT?");
			return;
		}
		String target = commands[1];
		for (Entry<Slot, Item> gear : player.getSlotsEquipped().entrySet()) {
			if (gear.getValue().getAliases().stream().anyMatch(target::equalsIgnoreCase)) {
				if (gear.getValue().getType() == ItemType.WEAPON) {
					doUnwield(player, commands);
					return;
				}
				if (gear.getValue().getType() == ItemType.ARMOR) {
					doRemove(player, commands);
					return;
				}
				if (gear.getValue().getType() == ItemType.EQUIPABLE) {
					doRemove(player,commands);
					return;
				} else {
					Output.outputToTarget(player, "You cannot equip " + target.toLowerCase());
					return;
				}
			}
		}
		System.out.println("You don't seem to have that item.");

	}
	
	public void doUnwield(Player player, String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Unwield WHAT?");
			return;
		}
		String target = commands[1];
		try {
			for (Entry<Slot, Item> gear : player.getSlotsEquipped().entrySet()) {
				if (gear.getValue().getAliases().stream().anyMatch(target::equalsIgnoreCase)) {

					try {
						if (gear.getValue().getType() == ItemType.WEAPON) {
							Item weapon = gear.getValue();
							if (player.getSlotsEquipped().containsKey(weapon.getSlot())) {
								player.getSlotsEquipped().remove(weapon.getSlot());
								player.getHeldItem().add(weapon);
								player.setDamage(player.getDamage() - weapon.getDamage());
								getNewAccuracy(player);
								String output = weapon.getDisplayName() + " unequipped from "
										+ Slot.getEnumString(weapon.getSlot());
								Output.outputToTarget(player, Output.capitalize(output));
								Output.outputToRoom(player,
										player.getDisplayName() + " unequips " + weapon.getDisplayName() + ".");
							} else {
								Output.outputToTarget(player, "That item is not currently equipped.");

							}
						} else {
							String output = target.toLowerCase() + " is not something that can be wielded!";
							Output.outputToTarget(player, Output.capitalize(output));
							return;
						}
					} catch (Exception e) {
						System.out.println("Whoops something went wrong with unwielding");
						e.printStackTrace();

					}

					return;
				}

			}
			Output.outputToTarget(player, "You don't seem to have that item!");

		} catch (Exception e) {
			System.out.println("Some error with wielding");
			System.out.println(e);
		}

	}
	
	
	
	public void doRemove(Player player, String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Remove WHAT?");
			return;
		}
		String target = commands[1];
		try {
			for (Entry<Slot, Item> gear : player.getSlotsEquipped().entrySet()) {
				if (gear.getValue().getAliases().stream().anyMatch(target::equalsIgnoreCase)) {

					try {
						if (gear.getValue().getType() == ItemType.ARMOR) {
							Item armor = gear.getValue();
							if (player.getSlotsEquipped().containsKey(armor.getSlot())) {
								player.getSlotsEquipped().remove(armor.getSlot());
								player.getHeldItem().add(armor);
								player.setDamage(player.getDamageReduction() - armor.getDamageReduction());

								if (armor.getBulk() > 0) {
									player.setBulk(player.getBulk() - armor.getBulk());
									Output.outputToTarget(player, "You feel less encumbered.");
								}

								String output = armor.getDisplayName() + " unequipped from "
										+ Slot.getEnumString(armor.getSlot());

								Output.outputToTarget(player, Output.capitalize(output));
								Output.outputToRoom(player,
										player.getDisplayName() + " unequips " + armor.getDisplayName() + ".");
							} else {
								Output.outputToTarget(player, "That item is not currently equipped.");

							}
						} else if (gear.getValue().getType() == ItemType.EQUIPABLE) {
							if (player.getSlotsEquipped().containsKey(gear.getValue().getSlot())) {
								player.getSlotsEquipped().remove(gear.getValue().getSlot());
								player.getHeldItem().add(gear.getValue());
								String output = gear.getValue().getDisplayName() + " unequipped from "
										+ Slot.getEnumString(gear.getValue().getSlot());
								Output.outputToTarget(player, Output.capitalize(output));
								Output.outputToRoom(player, player.getDisplayName() + " unequips "
										+ gear.getValue().getDisplayName() + ".");
							} else {
								Output.outputToTarget(player, "That item is not currently equipped.");

							}
						} else {
							String output = target.toLowerCase() + " is not something that can be equipped!";
							Output.outputToTarget(player, Output.capitalize(output));
							return;
						}
					} catch (Exception e) {
						System.out.println("Whoops something went wrong with unequipping");
						e.printStackTrace();

					}

					return;
				}

			}
			Output.outputToTarget(player, "You don't seem to have that item!");

		} catch (Exception e) {
			System.out.println("Some error with unequipping!");
			System.out.println(e);
		}
	}
	
	public void doWear(Player player,String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Wear WHAT?");
			return;
		}
		String target = commands[1];
		try {
			List<Item> items = player.getHeldItem();
			for (Item item : items) {
				if (item.getAliases().stream().anyMatch(target::equalsIgnoreCase)) {
					try {
						if (item.getType() == ItemType.ARMOR) {
							Item armor = item;
							if (checkSlotAvailable(player,item)) {
								player.getSlotsEquipped().put(armor.getSlot(), armor);
								player.getHeldItem().remove(armor);
								player.setDamage(player.getDamageReduction() + armor.getDamageReduction());

								if (armor.getBulk() > 0) {
									Output.outputToTarget(player, "You feel somewhat more encumbered.");
									player.setBulk(player.getBulk() + armor.getBulk());
								}
								String output = item.getDisplayName() + " equipped to "
										+ Slot.getEnumString(armor.getSlot());
								Output.outputToRoom(player,
										player.getDisplayName() + " equips " + item.getDisplayName() + ".");
								Output.outputToTarget(player, Output.capitalize(output));
							} else {
								return;
							}
						} else {
							Item equip = item;
							if (checkSlotAvailable(player,item)) {
								player.getSlotsEquipped().put(equip.getSlot(), equip);
								player.getHeldItem().remove(equip);
								String output = item.getDisplayName() + " equipped to "
										+ Slot.getEnumString(equip.getSlot());
								Output.outputToRoom(player,
										player.getDisplayName() + " equips " + item.getDisplayName() + ".");
								Output.outputToTarget(player, Output.capitalize(output));
							} else {
								return;
							}
							return;
						}
					} catch (Exception e) {
						System.out.println("Whoops something went wrong with wearing armor");
						e.printStackTrace();

					}

					return;
				}

			}
			Output.outputToTarget(player, "You don't seem to have that item!");

		} catch (Exception e) {
			System.out.println("Some error with wearing armor");
			System.out.println(e);
		}

	}
		
	public void doWield(Player player,String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Wield WHAT?");
			return;
		}
		String target = commands[1];
		try {
			List<Item> items = player.getHeldItem();
			for (Item item : items) {
				if (item.getAliases().stream().anyMatch(target::equalsIgnoreCase)) {
					try {
						if (item.getType() == ItemType.WEAPON) {
							Item weapon = item;
							if (checkSlotAvailable(player,item)) {
								player.getSlotsEquipped().put(weapon.getSlot(), weapon);
								player.getHeldItem().remove(weapon);
								player.setDamage(player.getDamage() + weapon.getDamage());
								if (weapon.getAccuracy() < player.getAccuracy()) {
									player.setAccuracy(weapon.getAccuracy());
									Output.outputToTarget(player, "You feel somewhat less accurate...");
								}

								String output = item.getDisplayName() + " equipped to "
										+ Slot.getEnumString(weapon.getSlot());
								Output.outputToTarget(player, Output.capitalize(output));
								Output.outputToRoom(player,
										player.getDisplayName() + " equips " + item.getDisplayName() + ".");
							} else {
								return;
							}
						} else {
							String output = item.getDisplayName() + " is not something that can be wielded!";
							Output.outputToTarget(player, Output.capitalize(output));
							return;
						}
					} catch (Exception e) {
						System.out.println("Whoops something went wrong with wielding");
						e.printStackTrace();

					}

					return;
				}

			}
			Output.outputToTarget(player, "You don't seem to have that item!");

		} catch (Exception e) {
			System.out.println("Some error with wielding");
			System.out.println(e);
		}

	}
	
	public void doEquip(Player player,String[] commands) {
		if (commands.length < 2) {
			Output.outputToTarget(player, "Equip WHAT?");
			return;
		}
		String target = commands[1];
		List<Item> items = player.getHeldItem();
		for (Item item : items) {
			if (item.getAliases().stream().anyMatch(target::equalsIgnoreCase)) {
				if (item.getType() == ItemType.WEAPON) {
					doWield(player,commands);
					return;
				}
				if (item.getType() == ItemType.ARMOR) {
					doWear(player,commands);
					return;
				}
				if (item.getType() == ItemType.EQUIPABLE) {
					doWear(player,commands);
					return;
				} else {
					Output.outputToTarget(player, "You cannot equip " + target.toLowerCase());
					return;
				}
			}
		}
		Output.outputToTarget(player, "You cannot equip " + target.toLowerCase());

	}
	
	public boolean checkSlotAvailable(Player player, Item item) {
		if (player.getSlotsEquipped().containsKey(item.getSlot())) {
			Output.outputToTarget(player,
					"You already have something equipped in the " + Slot.getEnumString(item.getSlot()) + " slot!");
			return false;
		} else if (player.getSlotsEquipped().containsKey(Slot.TWOHANDS)
				&& (item.getSlot().equals(Slot.RIGHTHAND) || item.getSlot().equals(Slot.LEFTHAND))) {
			Output.outputToTarget(player, "Your hands are full with the "
					+ player.getSlotsEquipped().get(Slot.TWOHANDS).getDisplayName() + "!");
			return false;
		} else if (player.getSlotsEquipped().containsKey(Slot.RIGHTHAND) && (item.getSlot().equals(Slot.TWOHANDS))) {
			Output.outputToTarget(player, "Your right hand is busy holding the "
					+ player.getSlotsEquipped().get(Slot.RIGHTHAND).getDisplayName() + "!");
			return false;
		} else if (player.getSlotsEquipped().containsKey(Slot.LEFTHAND) && (item.getSlot().equals(Slot.TWOHANDS))) {
			Output.outputToTarget(player, "Your left hand is busy holding the "
					+ player.getSlotsEquipped().get(Slot.LEFTHAND).getDisplayName() + "!");
			return false;
		} else {
			return true;
		}

	}
	
	public Item getItemForRemoval(Player player, String itemName) {
		for (Item item : player.getHeldItem()) {
			if (item.getAliases().stream().anyMatch(itemName::equalsIgnoreCase)) {
				return item;
			}
		}
		for (Entry<Slot, Item> gear : player.getSlotsEquipped().entrySet()) {
			if (gear.getValue().getAliases().stream().anyMatch(itemName::equalsIgnoreCase)) {
				doUnequip(player, new String[] { "unequip", gear.getValue().getAliases().get(0) });
				return gear.getValue();
			}
		}
		return null;
	}
	
	public void getNewAccuracy(Player player) {
		int accuracy = player.getAccuracy();
		player.setAccuracy(100);
		for (Entry<Slot, Item> gear : player.getSlotsEquipped().entrySet()) {
			if (gear.getValue().getType() == ItemType.WEAPON) {
				if (player.getAccuracy() > (gear.getValue()).getAccuracy()) {
					player.setAccuracy((gear.getValue()).getAccuracy());
				}
			}
		}
		if (player.getAccuracy() > accuracy) {
			Output.outputToTarget(player, "You feel more accurate now.");
		}

	}
	
}
