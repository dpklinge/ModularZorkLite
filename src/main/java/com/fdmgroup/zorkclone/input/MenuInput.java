package com.fdmgroup.zorkclone.input;

import java.io.BufferedReader;
import java.io.IOException;

import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.ItemType;
import com.fdmgroup.zorkclone.player.Player;

public class MenuInput {
	private BufferedReader reader;

	public MenuInput(BufferedReader reader) {
		this.reader = reader;

	}

	public String askNewGame(Player player) {

		boolean isValid = true;
		while (isValid) {
			System.out.println("Welcome to Daniel's Modular Zork Lite!");
			System.out.println();
			int newGameNumber=2;
			int creatorModeNumber=3;
			try {
				if (!player.getIsDead()) {
					System.out.println("1. Continue game");
				}else{
					newGameNumber=1;
					creatorModeNumber=2;
				}
				System.out.println(newGameNumber+". New game");
				System.out.println(creatorModeNumber+". Creator mode");
				String input = reader.readLine();

				if (input.equals("1") && !player.getIsDead()) {
					return "CONTINUE";
				} else if (input.equals(Integer.toString(newGameNumber))) {
					return "NEWGAME";

				} else if (input.equals(Integer.toString(creatorModeNumber))) {
					return "CREATORMODE";
				} else {
					System.out.println("Nice try, buddy. Pick one of the menu options.");
				}

			} catch (IOException e) {
				System.err.println("Reader input failed?");
				e.printStackTrace();
				return "";
			}

		}
		return "";

	}

	public String getType() {
		boolean isValid = true;
		while (isValid) {
			try {
				System.out.println("What type of thing would you to create?");
				System.out.println("1. Room");
				System.out.println("2. Item");
				System.out.println("3. Actor");
				System.out.println("4. Actually, I wanted to edit something...");
				System.out.println("5. I'm done");
				String input = reader.readLine();

				if (input.equals("1")) {
					return "ROOM";
				} else if (input.equals("2")) {
					return "ITEM";

				} else if (input.equals("3")) {
					return "ACTOR";
				} else if (input.equals("4")) {
					return "EDIT";
				} else if (input.equals("5")) {
					System.out.println("Kthx bai");
					return "QUIT";
				} else {
					System.out.println("Pick one of the listed options by number, plis.");
				}

			} catch (IOException e) {
				System.err.println("Reader input failed?");
				e.printStackTrace();
				return "";
			}

		}
		return "";
	}

	public String editMenu() {
		while (true) {
			try {
				System.out.println("What type of thing would you like to edit?");
				System.out.println("1. Room");
				System.out.println("2. Item");
				System.out.println("3. Actor");
				System.out.println("4. I'm done");
				String input = reader.readLine();

				if (input.equals("1")) {
					return "ROOM";
				} else if (input.equals("2")) {
					return "ITEM";

				} else if (input.equals("3")) {
					return "ACTOR";
				} else if (input.equals("4")) {
					System.out.println("Alrighty then. Back to the creation menu!");
					return "QUIT";
				} else {
					System.out.println("Pick one of the listed options by number, buckaroony.");
				}

			} catch (IOException e) {
				System.err.println("Reader input failed?");
				e.printStackTrace();
				return "";
			}

		}
	}

	public boolean confirmOverride(String input) {

		while (true) {
			System.out.println(input + " already exists. Would you like to override? (Y/N)");
			try {
				String choice = reader.readLine();
				if (choice.toUpperCase().equals("Y") || choice.toUpperCase().equals("YES")) {
					System.out.println("Okay, will override on completion.");
					return true;
				} else if (choice.toUpperCase().equals("N") || choice.toUpperCase().equals("NO")) {
					System.out.println("Cancelling creation...");
					return false;
				} else {
					System.out.println("Please enter either (y)es or (n)o.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public String editActorMenu() {
		while (true) {
			try {
				System.out.println("What part of the actor would you like to edit?");
				System.out.println("1. Id");
				System.out.println("2. Name");
				System.out.println("3. Aliases");
				System.out.println("4. Start health");
				System.out.println("5. Max health");
				System.out.println("6. Damage");
				System.out.println("7. Accuracy");
				System.out.println("8. Speed");
				System.out.println("9. DamageReduction");
				System.out.println("10. AccuracyReduction");
				System.out.println("11. Items held");
				System.out.println("12. Starting dead status");
				System.out.println("13. Examine text");
				System.out.println("14. Dead examine text");
				System.out.println("15. Display text");
				System.out.println("16. Dead display text");
				System.out.println("17. Behaviour");
				System.out.println("18. Kill text");
				System.out.println("19. Cancel");

				String input = reader.readLine();

				if (input.equals("1")) {
					return "ID";
				} else if (input.equals("2")) {
					return "NAME";

				} else if (input.equals("3")) {
					return "ALIAS";
				} else if (input.equals("4")) {
					return "INITHP";
				} else if (input.equals("5")) {
					return "MAXHP";
				} else if (input.equals("6")) {
					return "DAMAGE";
				} else if (input.equals("7")) {
					return "ACCURACY";
				} else if (input.equals("8")) {
					return "SPEED";
				} else if (input.equals("9")) {
					return "DAMAGEREDUX";
				} else if (input.equals("10")) {
					return "ACCURACYREDUX";
				} else if (input.equals("11")) {
					return "ITEMS";
				} else if (input.equals("12")) {
					return "DEAD";
				} else if (input.equals("13")) {
					return "XTEXT";
				} else if (input.equals("14")) {
					return "DEADXTEXT";
				} else if (input.equals("15")) {
					return "DISPLAY";
				} else if (input.equals("16")) {
					return "DEADDISPLAY";
				} else if (input.equals("17")) {
					return "BEHAVIOUR";
				} else if (input.equals("18")) {
					return "KILLTEXT";
				} else if (input.equals("19")) {
					System.out.println("Alrighty then. Back to the edit menu!");
					return "QUIT";
				} else {
					System.out.println("Pick one of the listed options, madlad.");
				}

			} catch (IOException e) {
				System.err.println("Reader input failed?");
				e.printStackTrace();
				return "";
			}

		}
	}

	public String editRoomMenu() {
		while (true) {
			try {
				System.out.println("What part of the item would you like to edit?");
				System.out.println("1. Id");
				System.out.println("2. Name");
				System.out.println("3. Display text");
				System.out.println("4. Items");
				System.out.println("5. Actors");
				System.out.println("6. Exits");
				System.out.println("7. Cancel");

				String input = reader.readLine();

				if (input.equals("1")) {
					return "ID";
				} else if (input.equals("2")) {
					return "NAME";

				} else if (input.equals("3")) {
					return "DISPLAY";
				} else if (input.equals("4")) {
					return "ITEMS";
				} else if (input.equals("5")) {
					return "ACTORS";
				} else if (input.equals("6")) {
					return "EXITS";
				} else if (input.equals("7")) {
					System.out.println("Alrighty then. Back to the edit menu!");
					return "QUIT";
				} else {
					System.out.println("Pick one of the listed options, ya rabul.");
				}

			} catch (IOException e) {
				System.err.println("Reader input failed?");
				e.printStackTrace();
				return "";
			}

		}
	}

	public String editItemMenu(Item item) {
		while (true) {
			try {
				System.out.println("What part of the room would you like to edit?");
				System.out.println("1. Id");
				System.out.println("2. Name");
				System.out.println("3. Examine text");
				System.out.println("4. Aliases");
				System.out.println("5. Visibility");
				System.out.println("6. Ground display");
				System.out.println("7. Item type");
				System.out.println("8. Effects");
				if (item.getType() == ItemType.STATIC) {
					System.out.println("9. Action-response mapping");
				} else if (item.getType() == ItemType.HOLDABLE) {
					System.out.println("9. Quantity");
				} else if (item.getType() == ItemType.EQUIPABLE) {
					System.out.println("9. Quantity");
					System.out.println("10. Slot");
					
				} else if (item.getType() == ItemType.ARMOR) {
					System.out.println("9. Quantity");
					System.out.println("10. Slot");
					System.out.println("11. Damage reduction");
					System.out.println("12. Accuracy reduction");
					System.out.println("13. Bulk");
				} else if (item.getType() == ItemType.WEAPON) {
					System.out.println("9. Quantity");
					System.out.println("10. Slot");
					System.out.println("11. Damage");
					System.out.println("12. Accuracy ");
				} else if (item.getType() == ItemType.AMMUNITION) {
					System.out.println("9. Quantity");
				} else if (item.getType() == ItemType.CONSUMABLE) {
					System.out.println("9. Quantity");
				}
				System.out.println("14. Cancel");

				String input = reader.readLine();

				if (input.equals("1")) {
					return "ID";
				} else if (input.equals("2")) {
					return "NAME";

				} else if (input.equals("3")) {
					return "EXAMINE";
				} else if (input.equals("4")) {
					return "ALIASES";
				} else if (input.equals("5")) {
					return "VISIBILITY";
				} else if (input.equals("6")) {
					return "DISPLAY";
				} else if (input.equals("7")) {
					return "TYPE";
				} else if (input.equals("8")) {
					return "EFFECTS";
				}

				if (item.getType() == ItemType.STATIC) {
				} else if (item.getType() == ItemType.HOLDABLE) {
					if (input.equals("9")) {
						return "QUANTITY";
					}
				} else if (item.getType() == ItemType.EQUIPABLE) {
					if (input.equals("9")) {
						return "QUANTITY";
					} else if (input.equals("10")) {
						return "SLOT";
					} 

				} else if (item.getType() == ItemType.ARMOR) {
					if (input.equals("9")) {
						return "QUANTITY";
					} else if (input.equals("10")) {
						return "SLOT";
					} else if (input.equals("11")) {
						return "DAMAGEREDUX";
					} else if (input.equals("12")) {
						return "ACCURACYREDUX";
					} else if (input.equals("13")) {
						return "BULK";
					}

				} else if (item.getType() == ItemType.WEAPON) {
					if (input.equals("9")) {
						return "QUANTITY";
					} else if (input.equals("10")) {
						return "SLOT";
					} else if (input.equals("11")) {
						return "DAMAGE";
					} else if (input.equals("12")) {
						return "ACCURACY";
					}

				} else if (item.getType() == ItemType.AMMUNITION) {
					if (input.equals("9")) {
						return "QUANTITY";
					}
				} else if (item.getType() == ItemType.CONSUMABLE) {
					if (input.equals("9")) {
						return "QUANTITY";
					}
				}

				if (input.equals("14")) {
					System.out.println("Alrighty then. Back to the edit menu!");
					return "QUIT";
				} else {
					System.out.println("Pick one of the listed options, ya rabul.");
				}

			} catch (IOException e) {
				System.err.println("Reader input failed?");
				e.printStackTrace();
				return "";
			}

		}
	}
}
