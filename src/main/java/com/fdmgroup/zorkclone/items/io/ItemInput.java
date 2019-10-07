package com.fdmgroup.zorkclone.items.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.input.MenuInput;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.ItemType;
import com.fdmgroup.zorkclone.items.Slot;

public class ItemInput {
	private BufferedReader reader;

	public ItemInput(BufferedReader reader) {
		super();
		this.reader = reader;
	}

	public Item getItem() {
		try {
			Item item = getType();
			if (item == null) {
				return null;
			}
			item = getId(item);
			if (item == null) {
				return null;
			}

			item.displayDetails();
			item = getName(item);
			if (item == null) {
				return null;
			}

			item.displayDetails();
			item = getExamine(item);
			if (item == null) {
				return null;
			}

			item.displayDetails();
			item = getAliases(item);
			if (item == null) {
				return null;
			}

			item.displayDetails();
			item = getVisible(item);
			if (item == null) {
				return null;
			}

			item.displayDetails();
			item = getGroundText(item);
			if (item == null) {
				return null;
			}
			
			item.displayDetails();
			item = getEffects(item);
			if (item == null) {
				return null;
			}

			if (item.getType() == ItemType.FLAVOR) {
				return item;
			} else if (item.getType() == ItemType.STATIC) {

				return getStaticItem(item);
			} else if (item.getType() == ItemType.HOLDABLE) {
				return getHoldableItem(item);
			} else if (item.getType() == ItemType.EQUIPABLE) {

				return getEquipableItem(item);
			} else if (item.getType() == ItemType.ARMOR) {
				return getArmor(item);
			} else if (item.getType() == ItemType.WEAPON) {
				return getWeapon(item);
			} else if (item.getType() == ItemType.AMMUNITION) {
				return getAmmunition(item);
			} else if (item.getType() == ItemType.CONSUMABLE) {
				return getConsumable(item);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	public Item getType() throws IOException {
		while (true) {
			System.out.println("");
			System.out.println("Please enter the type of the item, or type 'q' to cancel.");
			System.out.println("Available types: " + java.util.Arrays.asList(ItemType.values()));
			String input = reader.readLine();
			try {
				if (input.equals("q") || input.equals("Q")) {
					return null;
				} else if (ItemType.valueOf(input.toUpperCase()) != null) {
					Item item = new Item();		
					item.setType(ItemType.valueOf(input.toUpperCase()));
					return item;
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid item type.");
			}
		}

	}

	private Item getHoldableItem(Item item) throws IOException {

		item = getQuantity(item);
		return item;

	}

	private Item getArmor(Item item) throws IOException {
		item = getQuantity(item);
		if (item == null) {
			return null;
		}
		item = getSlot(item);
		if (item == null) {
			return null;
		}
		item = getDamageReduction(item);
		if (item == null) {
			return null;
		}
		item = getAccuracyReduction(item);
		if (item == null) {
			return null;
		}
		item = getBulk(item);
		return item;

	}

	private Item getWeapon(Item item) throws IOException {

		item = getQuantity(item);
		if (item == null) {
			return null;
		}
		item = getSlot(item);
		if (item == null) {
			return null;
		}
		item = getDamage(item);
		if (item == null) {
			return null;
		}
		item = getAccuracy(item);
		return item;

	}

	private Item getAccuracy(Item item) throws IOException {
		item.displayDetails();
		while (true) {
			System.out.println("");
			System.out.println("Please enter weapon accuracy, or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {
				item.setAccuracy(Integer.parseInt(input));
				return item;

			} else {
				System.out.println("Invalid non-numeric input");
			}
		}
	}

	private Item getDamage(Item item) throws IOException {
		item.displayDetails();
		while (true) {
			System.out.println("");
			System.out.println("Please enter weapon damage, or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {
				item.setDamage(Integer.parseInt(input));
				return item;

			} else {
				System.out.println("Invalid non-numeric input");
			}
		}
	}

	private Item getEquipableItem(Item item) throws IOException {

		item = getQuantity(item);
		if (item == null) {
			return null;
		}
		item = getSlot(item);
		
		return item;
	}

	private Item getStaticItem(Item item) {
		item.displayDetails();
		System.out.println("");
		System.out.println("Please enter an action item, or type 'q' to cancel.");
		// TODO
		// String input = reader.readLine();
		System.out.println("Static items currently unsupported");
		return item;

	}

	private Item getAmmunition(Item item) {
		System.out.println("");
		System.out.println("Please enter ammunition information, or type 'q' to cancel.");
		// TODO
		// String input = reader.readLine();
		System.out.println("Ammunition items currently unsupported");
		return item;

	}

	private Item getConsumable(Item item) {
		System.out.println("");
		System.out.println("Please enter consumable effect, or type 'q' to cancel.");
		// TODO
		// String input = reader.readLine();
		System.out.println("Static items currently unsupported");
		return item;

	}

	private Item getId(Item item) throws IOException {

		MenuInput menu = new MenuInput(reader);
		System.out.println("");
		System.out.println("Please enter the id name of the item, or type 'q' to cancel.");

		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			Path path = Paths.get(Main.baseGamePath.toString(), "/items/" + input + ".txt");
			File file = path.toFile();
			if (file.exists()) {
				if (!menu.confirmOverride(input)) {
					return null;
				}
			}
			item.setName(input);
		}
		return item;
	}

	private Item getName(Item item) throws IOException {
		System.out.println("");
		System.out.println("Please enter the display name of the item, or type 'q' to cancel.");

		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			item.setDisplayName(input);
		}
		return item;
	}

	private Item getExamine(Item item) throws IOException {
		System.out.println("");
		System.out.println("Please enter item examine text, or type 'q' to cancel.");

		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			item.setExamine(input);
		}
		return item;
	}

	private Item getAliases(Item item) throws IOException {
		List<String> aliases = new ArrayList<>();
		while (true) {
			System.out.println("");
			System.out.println("Please enter an alias for this item, or type 'q' to skip.");
			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				item.setAliases(aliases);
				return item;
			} else {
				aliases.add(input);
			}
		}
	}

	private Item getVisible(Item item) throws IOException {
		while (true) {
			System.out.println("");
			System.out.println(
					"Please enter if the item is immediately visible in the room (true/false), or type 'q' to cancel.");
			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (input.toLowerCase().equals("true") || input.toLowerCase().equals("t")) {
				item.setVisible(true);
				return item;
			} else if (input.toLowerCase().equals("false") || input.toLowerCase().equals("f")) {
				item.setVisible(false);
				return item;
			} else {
				System.out.println("Not a valid input");
			}
		}
	}

	private Item getGroundText(Item item) throws IOException {
		System.out.println("");
		System.out.println("Please enter item ground display text, or type 'q' to cancel.");

		String input = reader.readLine();
		if (input.equals("q") || input.equals("Q")) {
			return null;
		} else {
			item.setGroundText(input);
			return item;

		}
	}

	private Item getQuantity(Item item) throws IOException {
		item.displayDetails();
		while (true) {
			System.out.println("");
			System.out.println("Please enter item quantity, or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {

				item.setQuantity(Integer.parseInt(input));
				return item;

			} else {
				System.out.println("Invalid non-numeric input");
			}
		}
	}

	private Item getDamageReduction(Item item) throws IOException {
		item.displayDetails();
		while (true) {
			System.out.println("");
			System.out.println("Please enter armor damage reduction, or type 'q' to cancel.");
			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {
				item.setDamageReduction(Integer.parseInt(input));
				return item;
			} else {
				System.out.println("Invalid non-numeric input");
			}
		}
	}

	private Item getEffects(Item item) throws IOException {
		item.displayDetails();
		
		List<String> effects = new ArrayList<>();
		while (true) {
			System.out.println("Please enter an effect name, or type 'q' to skip.");
			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				item.setEffects(effects);
				return item;
			} else {
				effects.add(input);
			}
		}
	}

	private Item getSlot(Item item) throws IOException {
		item.displayDetails();
		while (true) {
			System.out.println("");
			System.out.println("Please enter slot, or type 'q' to cancel.");
			System.out.println("Available types: " + java.util.Arrays.asList(Slot.values()));
			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else {
				try {
					Slot slot = Slot.valueOf(input.toUpperCase());

					item.setSlot(slot);
					return item;

				} catch (Exception e) {
					System.out.println("I'm sorry, that was not a valid slot.");
					continue;
				}

			}
		}
	}

	private Item getBulk(Item item) throws IOException {
		item.displayDetails();
		while (true) {
			System.out.println("");
			System.out.println("Please enter armor bulk, or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {
				item.setBulk(Integer.parseInt(input));
				return item;

			} else {
				System.out.println("Invalid non-numeric input");
			}
		}
	}

	private Item getAccuracyReduction(Item item) throws IOException {
		item.displayDetails();
		while (true) {
			System.out.println("");
			System.out.println("Please enter armor accuracyReduction, or type 'q' to cancel.");

			String input = reader.readLine();
			if (input.equals("q") || input.equals("Q")) {
				return null;
			} else if (NumberUtils.isDigits(input)) {
				item.setAccuracyReduction(Integer.parseInt(input));
				return item;

			} else {
				System.out.println("Invalid non-numeric input");
			}
		}
	}

	public void editHub(String choice, Item item) throws IOException {
		if (choice.equals("ID")) {
			item = getId(item);
		} else if (choice.equals("NAME")) {
			item = getName(item);
		} else if (choice.equals("EXAMINE")) {
			item = getExamine(item);
		} else if (choice.equals("ALIASES")) {
			item = getAliases(item);
		} else if (choice.equals("VISIBILITY")) {
			item = getVisible(item);
		} else if (choice.equals("DISPLAY")) {
			item = getGroundText(item);
		} else if (choice.equals("TYPE")) {
			item = editType(item);
		} else if (choice.equals("QUANTITY")) {
			item = getQuantity(item);
		} else if (choice.equals("SLOT")) {
			item = getSlot(item);
		} else if (choice.equals("EFFECTS")) {
			item = getEffects(item);
		} else if (choice.equals("DAMAGEREDUX")) {
			item = getDamageReduction(item);
		} else if (choice.equals("ACCURACYREDUX")) {
			item = getAccuracyReduction(item);
		} else if (choice.equals("BULK")) {
			item = getBulk(item);
		} else if (choice.equals("ACCURACY")) {
			item = getAccuracy(item);
		} else if (choice.equals("DAMAGE")) {
			item = getDamage(item);
		} else if (choice.equals("QUIT")) {
			return;
		}
		ItemReader reader = Main.getItemReader(Main.baseGamePath);
		reader.writeItem(item);

	}


	private Item editType(Item item) throws IOException {
		Item tempItem = getType();
		item.setType(tempItem.getType());
		return item;
	}

}
