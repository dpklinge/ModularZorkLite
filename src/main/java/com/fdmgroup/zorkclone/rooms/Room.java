package com.fdmgroup.zorkclone.rooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.io.ActorReader;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.io.ItemReader;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;
import com.fdmgroup.zorkclone.weboutput.Output;

public class Room {
	private String name;
	private String displayName;
	private Set<String> playersPresent = new HashSet<>();
	private List<Direction> directions = new ArrayList<>();
	private Map<Direction, String> mapping = new HashMap<>();
	private String description;
	private List<String> actorsPresent = new ArrayList<>();
	private List<String> itemsPresent = new ArrayList<>();

	public Room() {
		super();
	}
	
	

	public Set<String> getPlayersPresent() {
		return playersPresent;
	}



	public void setPlayersPresent(Set<String> playersPresent) {
		this.playersPresent = playersPresent;
	}



	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Direction> getDirections() {
		return directions;
	}

	public void setDirections(List<Direction> directions) {
		this.directions = directions;
	}

	public Map<Direction, String> getMapping() {
		return mapping;
	}

	public void setMapping(Map<Direction, String> mapping) {
		this.mapping = mapping;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getActorsPresent() {
		return actorsPresent;
	}

	public void setActorsPresent(List<String> actorsPresent) {
		this.actorsPresent = actorsPresent;
	}

	public List<String> getItemsPresent() {
		return itemsPresent;
	}

	public void setItemsPresent(List<String> itemsPresent) {
		this.itemsPresent = itemsPresent;
	}

	public Room(String name, String displayName, List<Direction> directions, Map<Direction, String> mapping,
			String description, List<String> actorsPresent, List<String> itemsPresent) {
		super();
		this.name = name;
		this.displayName = displayName;
		this.directions = directions;
		this.mapping = mapping;
		this.description = description;
		this.actorsPresent = actorsPresent;
		this.itemsPresent = itemsPresent;
	}

	public Room(String displayName, String description) {
		super();
		this.displayName = displayName;
		this.description = description;

	}

	public String displayRoom(Player player) {
		if(Main.isWeb){
			return displayRoomWeb(player);
		}
		String roomText = displayName + "\n" + description + "\n";

		if (actorsPresent.size() > 0) {
			ActorReader reader = Main.getActorReader(Main.savedGamePath);
			for (String actor : actorsPresent) {
				
				roomText = roomText + reader.readActor(actor).getDisplay() + "\n";
			}
		}
		if (itemsPresent.size() > 0) {
			ItemReader reader = Main.getItemReader(Main.savedGamePath);
			for (String item : itemsPresent) {
				Item thisItem = reader.readItem(item);
				if (thisItem.isVisible()) {
					
					roomText = roomText + thisItem.getGroundText() + "\n";
				}
			}
		}
		roomText = roomText + "\nExits:\n";
		if (!mapping.isEmpty()) {
			RoomReader roomReader = Main.getRoomReader(Main.savedGamePath);
			for (Direction direction : directions) {

				roomText = roomText + Output.capitalize(Direction.getEnumString(direction)) + " - ";
			
				roomText = roomText + 	roomReader.readRoom(mapping.get(direction)).displayName + "\n";
			}
		}
		return roomText;
	}
	public String displayRoomWeb(Player player) {
		String roomText = displayName + "<br />&emsp;" + description + "<br />";

		if (actorsPresent.size() > 0) {
			ActorReader reader = Main.getActorReader(Main.savedGamePath);
			for (String actor : actorsPresent) {
				
				roomText = roomText + reader.readActor(actor).getDisplay() + "<br />";
			}
		}
		if (playersPresent.size() > 0) {
			PlayerReader playerReader =Main.getPlayerReader();
			for (String playerName : playersPresent) {
				if(!playerName.toLowerCase().equals(player.getName().toLowerCase())){
					Player otherPlayer = playerReader.readPlayerByName(playerName);
					roomText = roomText + otherPlayer.getDisplayText() + "<br />";
				}
				
				
			}
		}
		if (itemsPresent.size() > 0) {
			ItemReader reader = Main.getItemReader(Main.savedGamePath);
			for (String item : itemsPresent) {
				Item thisItem = reader.readItem(item);
				if (thisItem.isVisible()) {
					
					roomText = roomText + thisItem.getGroundText() + "<br />";
				}
			}
		}
		roomText = roomText + "<br />Exits:<br />";
		if (!mapping.isEmpty()) {
			RoomReader roomReader = Main.getRoomReader(Main.savedGamePath);
			for (Direction direction : directions) {

				roomText = roomText + Output.capitalize(Direction.getEnumString(direction)) + " - ";
			
				roomText = roomText + 	roomReader.readRoom(mapping.get(direction)).displayName + "<br />";
			}
		}
		return roomText;
	}

	@Override
	public String toString() {
		return "Room [name=" + name + ", displayName=" + displayName + ", directions=" + directions + ", mapping="
				+ mapping + ", description=" + description + ", actorsPresent=" + actorsPresent + ", itemsPresent="
				+ itemsPresent + "]";
	}

	public void displayDetails() {
		System.out.println("Room id: "+getName()+" name: "+getDisplayName());
		System.out.println("Display text: "+getDescription());
		System.out.println("Items present: "+itemsPresent);
		System.out.println("Actors present: "+getActorsPresent());
		System.out.println("Exit Direction to room mappings:");
		for(Entry<Direction,String> entry:getMapping().entrySet()){
			System.out.println("Direction:"+entry.getKey()+" Room:"+entry.getValue());
		}

		
	}

}
