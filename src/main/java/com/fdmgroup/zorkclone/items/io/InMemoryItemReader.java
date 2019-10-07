package com.fdmgroup.zorkclone.items.io;

import java.util.HashMap;
import java.util.Map;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.items.Item;

public class InMemoryItemReader implements ItemReader {
	private static HashMap<String, Item> items = new HashMap<>();
	@Override
	public Item readItem(String itemName) {
		if(items.containsKey(itemName)){
			return items.get(itemName);
		}else{
			ItemReader reader = new JsonItemReader(Main.baseGamePath);
			Item item = reader.readItem(itemName);
			items.put(itemName, item);
			return item;
		}
	}

	@Override
	public synchronized Item writeItem(Item item) {
		items.put(item.getName(), item);
		return item;
	}

	public static Map<String, Item> getItems() {
		return items;
	}

	@Override
	public void deleteItem(String name) {
		items.remove(name);
		
	}

}
