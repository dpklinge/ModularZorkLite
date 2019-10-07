package com.fdmgroup.zorkclone.items.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.zorkclone.items.Item;

public class JsonItemReader implements ItemReader {
	private ObjectMapper mapper = new ObjectMapper();

	{
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	private Path storagePath;

	public JsonItemReader(Path storagePath) {
		super();
		this.storagePath = storagePath;
	}

	@Override
	public Item readItem(String itemId) {
		Item item = null;
		try {
			item = mapper.readValue(new File((storagePath + "\\items\\" + itemId + ".txt")), Item.class);
		} catch (JsonParseException e) {
			System.err.println("Json read failed");
		} catch (JsonMappingException e) {
			System.err.println("Json read failed");
		} catch (IOException e) {
			System.err.println("Failed to read from file path: " + (storagePath+ "\\items\\" + itemId + ".txt"));
		}
		return item;
	}

	@Override
	public Item writeItem(Item item) {
		try {
			mapper.writeValue(new File(storagePath+ "\\items\\" + item.getName() + ".txt"), item);
		} catch (JsonGenerationException e) {
			System.err.println("JSON failed");
		} catch (JsonMappingException e) {
			System.err.println("JSON failed");
		} catch (IOException e) {
			System.err.println("Failed to write to " + storagePath+ "\\items\\" + item.getName() + " not found!");
		}
		return item;
	}

	@Override
	public void deleteItem(String name) {
		// TODO Auto-generated method stub
		
	}

}
