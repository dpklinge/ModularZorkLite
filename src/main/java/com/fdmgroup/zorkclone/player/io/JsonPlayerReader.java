package com.fdmgroup.zorkclone.player.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.Slot;
import com.fdmgroup.zorkclone.player.Player;

public class JsonPlayerReader implements PlayerReader {
	private Path storagePath;

	private ObjectMapper mapper = new ObjectMapper();

	{
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public JsonPlayerReader() {
		super();
	}

	public void getPlayerPath(String username, String charName) {
		System.out.println("Json reading this player:");
		System.out.println(Main.userPath.toString() + "/" + username + "/characters/" + charName + ".txt");
		this.storagePath = Paths.get(Main.userPath.toString(), "/" + username + "/characters/" + charName + ".txt");
	}

	public Player getPlayer(String username, String charName) {
		getPlayerPath(username, charName);
		Player player = new Player();

		try {
			player = mapper.readValue(storagePath.toFile(), Player.class);

			List<Item> itemList = new ArrayList<>();
			for (Item item : player.getHeldItem()) {

				itemList.add(mapper.readValue(new File((Main.savedGamePath + "\\items\\" + item.getName() + ".txt")),
						Item.class));

			}
			player.setHeldItem(itemList);
			HashMap<Slot, Item> map = new HashMap<>();
			for (Entry<Slot, Item> entry : player.getSlotsEquipped().entrySet()) {

				map.put(entry.getKey(),
						(mapper.readValue(
								new File((Main.savedGamePath + "\\items\\" + entry.getValue().getName() + ".txt")),
								Item.class)));

			}
			player.setSlotsEquipped(map);
			return player;

		} catch (JsonParseException e) {
			System.err.println("Json parsing player failed");
		} catch (JsonMappingException e) {
			System.err.println("Json parsing player failed");
		} catch (IOException e) {
			System.err.println("Player file not located");
		}
		return null;

	}

	public void writePlayer(Player player) {
		getPlayerPath(player.getUser().getUsername(), player.getName());
		File file = storagePath.toFile();
		new File(file.getParent()).mkdirs();
		try {
			mapper.writeValue(file, player);
		}

		catch (IOException e) {
			System.out.println("Player file write failed");
		}

	}

	public Player readPlayerByName(String characterName) {
		try {
			Collection<File> files = FileUtils.listFiles(Main.userPath.toFile(), null, true);

			for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {

				File thisFile = iterator.next();
				if (thisFile.getName().equals(characterName + ".txt")) {
					storagePath = Paths.get(thisFile.getAbsolutePath());
				}
			}

			Player player = mapper.readValue(storagePath.toFile(), Player.class);
			player = getPlayer(player.getUser().getUsername(), player.getName());
			return player;
		}

		catch (IOException e) {
			System.out.println("Failed player read by name");
		}
		return null;

	}

}
