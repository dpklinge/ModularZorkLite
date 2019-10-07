package com.fdmgroup.zorkclone.items.io;

import com.fdmgroup.zorkclone.items.Item;

public interface ItemReader {
	public Item readItem(String itemName);
	public Item writeItem(Item item);
	public void deleteItem(String name);
}
