package com.fdmgroup.zorkclone.webcontrollers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.Actor;
import com.fdmgroup.zorkclone.actors.Behaviour;
import com.fdmgroup.zorkclone.actors.io.ActorReader;
import com.fdmgroup.zorkclone.actors.io.InMemoryActorReader;
import com.fdmgroup.zorkclone.actors.io.JsonActorReader;
import com.fdmgroup.zorkclone.effects.Effect;
import com.fdmgroup.zorkclone.effects.effectio.EffectReader;
import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.items.ItemType;
import com.fdmgroup.zorkclone.items.Slot;
import com.fdmgroup.zorkclone.items.io.InMemoryItemReader;
import com.fdmgroup.zorkclone.items.io.ItemReader;
import com.fdmgroup.zorkclone.items.io.JsonItemReader;
import com.fdmgroup.zorkclone.rooms.Direction;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.rooms.io.InMemoryRoomReader;
import com.fdmgroup.zorkclone.rooms.io.JsonRoomReader;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;

@Controller
public class EditorController {

	@GetMapping("/editor")
	public String editorMain(HttpServletRequest request) {
		
		return "editor";
	}

	@GetMapping("/createRoom")
	public String getCreateRoom(HttpServletRequest request, Model model) {
		model.addAttribute("room", new Room());
		return "createRoom";
	}
	@GetMapping("/createRoom/{room}")
	public String editRoomRedirect(@PathVariable String room, HttpServletRequest request, Model model) {
		Room roomRead = Main.getRoomReader(Main.savedGamePath).readRoom(room);
		if(roomRead==null){
			model.addAttribute("error", "Attempted to edit invalid room");
			return "editor";
		}else{
			model.addAttribute("room", roomRead);
			model.addAttribute("items", Stringifier.stringifyItems(roomRead.getItemsPresent()));
			model.addAttribute("rooms", Stringifier.stringifyRooms(roomRead.getMapping()));
			model.addAttribute("actors", Stringifier.stringifyActors(roomRead.getActorsPresent()));
			model.addAttribute("directions", roomRead.getDirections());
			model.addAttribute("overwrite", "overwrite");
			return "createRoom";
		}

	}
	

	@PostMapping("/createRoom")
	public String createRoom(Room room, HttpServletRequest request, Model model) {
		if(request.getParameter("submitButton").equals("deleteRoom")){
			Room roomRead = Main.getRoomReader(Main.savedGamePath).readRoom(room.getName());
			if(roomRead==null){
				model.addAttribute("error", "Attempted to remove invalid room");
				return "editor";
			}else{
				Main.getRoomReader(Main.savedGamePath).deleteRoom(room.getName());
				model.addAttribute("result", "Deleted room "+room.getName());
				return "editor";
			}
		}
		RoomReader roomReader = Main.getRoomReader(Main.savedGamePath);
		if(roomReader.readRoom(room.getName())!=null&&request.getParameter("overwrite")==null){
			request.setAttribute("error", "Room already exists!");
			return "createRoom";
		}
		boolean error = false;
		String errorMessage = "";
		ItemReader itemReader = Main.getItemReader(Main.savedGamePath);
		room.setItemsPresent(new ArrayList<>());
		if (!request.getParameter("items").isEmpty()) {
			for (String itemName : request.getParameter("items").split(",")) {
				Item item = itemReader.readItem(itemName);
				if (item == null) {
					error = true;
					errorMessage += "Item '" + itemName + "' not found.<br>";
				} else {
					room.getItemsPresent().add(item.getName());
				}
			}
		}
		ActorReader actorReader = Main.getActorReader(Main.savedGamePath);
		room.setActorsPresent(new ArrayList<>());
		if (!request.getParameter("actors").isEmpty()) {
			for (String actorName : request.getParameter("actors").split(",")) {
				Actor actor = actorReader.readActor(actorName);
				if (actor == null) {
					error = true;
					errorMessage += "Actor '" + actorName + "' not found.<br>";
				} else {
					room.getActorsPresent().add(actor.getName());
				}
			}
		}
		
		ArrayList<Direction> directions = new ArrayList<>();
		for (String direction : request.getParameterValues("direction")) {
			directions.add(Direction.valueOf(direction.toUpperCase()));
		}
		
		
		Map<Direction, String> rooms = new HashMap<>();
		int counter = 0;
		if (!request.getParameter("rooms").isEmpty()&&!directions.isEmpty()) {
			for (String roomName : request.getParameter("rooms").split(",")) {
				Room roomRead = roomReader.readRoom(roomName);
				if (roomRead == null) {
					error = true;
					errorMessage += "Room '" + roomName + "' not found.<br>";
				} else {
					rooms.put(directions.get(counter), roomRead.getName());
				}
				counter++;
			}
			room.setDirections(directions);
			room.setMapping(rooms);
		}
		
	
		
		if (error) {
			request.setAttribute("error", errorMessage);
			request.setAttribute("items", request.getParameter("items"));
			request.setAttribute("rooms", request.getParameter("rooms"));
			request.setAttribute("actors", request.getParameter("actors"));
			request.setAttribute("directions", room.getDirections());
			return "createRoom";
		} else {
			RoomReader memoryReader = Main.getRoomReader(Main.savedGamePath);
			RoomReader fileReader = new JsonRoomReader(Main.savedGamePath);
			fileReader.writeRoom(room);
			memoryReader.writeRoom(room);
			request.setAttribute("result", "Created room: " + room);
			return "editor";
		}
	}

	@GetMapping("/editRoom")
	public String editRoom(HttpServletRequest request, Model model) {
		model.addAttribute("rooms",InMemoryRoomReader.getRooms().values());
		return "editRoom";
	}
	

	@GetMapping(value = "/createItem")
	public String getItemCreator(HttpServletRequest request, Model model) {
		model.addAttribute("item", new Item());
		return "createItem";
	}
	@PostMapping(value = "/createItem")
	public String createItem(Item item, HttpServletRequest request, Model model) {
		if(request.getParameter("submitButton").equals("deleteItem")){
			Item itemRead = Main.getItemReader(Main.savedGamePath).readItem(item.getName());
			if(itemRead==null){
				model.addAttribute("error", "Attempted to remove invalid item");
				return "editor";
			}else{
				Main.getItemReader(Main.savedGamePath).deleteItem(item.getName());
				model.addAttribute("result", "Deleted item "+item.getName());
				return "editor";
			}
		}
		boolean error = false;
		String errorMessage = "";
		System.out.println("Item received: " + item);
		
		item.setType(ItemType.valueOf(request.getParameter("type").toUpperCase()));
		if(item.getType()==ItemType.ARMOR||item.getType()==ItemType.WEAPON||item.getType()==ItemType.EQUIPABLE){
			item.setIsEquipable(true);
			item.setIsHoldable(true);
		}
		if(item.getType()==ItemType.HOLDABLE||item.getType()==ItemType.CONSUMABLE||item.getType()==ItemType.AMMUNITION){
			item.setIsHoldable(true);
		}
		String slot = request.getParameter("slot");
		if(slot!=null){
			item.setSlot(Slot.valueOf(slot.toUpperCase()));
		}
		
		item.setAliases(new ArrayList<>());

		for (String alias : request.getParameter("aliases").split(",")) {
			item.getAliases().add(alias);
		}
		
		item.setEffects(new ArrayList<>());
		EffectReader effectReader = Main.getEffectReader();
		if (!request.getParameter("effects").isEmpty()) {
			for (String effectName : request.getParameter("effects").split(",")) {
				Effect effect = effectReader.readEffect(effectName);
				if (effect == null) {
					error = true;
					errorMessage += "Effect '" + effectName + "' not found.<br>";
				} else {
					item.getEffects().add(effectName);
				}
			}
		}
		item.setVisible(Boolean.parseBoolean(request.getParameter("isVisible")));

		if (error) {
			request.setAttribute("error", errorMessage);
			request.setAttribute("aliases", request.getParameter("aliases"));
			request.setAttribute("effect", request.getParameter("effect"));
			return "createItem";
		} else {
			ItemReader memoryReader = Main.getItemReader(Main.savedGamePath);
			ItemReader fileReader = new JsonItemReader(Main.savedGamePath);
			fileReader.writeItem(item);
			memoryReader.writeItem(item);
			request.setAttribute("result", "Created item: " + item);
			return "editor";
		}
	}
	@GetMapping(value = "/createItem/{item}")
	public String editItemForm(@PathVariable String item, HttpServletRequest request, Model model) {
		Item itemRead = Main.getItemReader(Main.savedGamePath).readItem(item);
		if(itemRead==null){
			model.addAttribute("error", "Attempted to edit invalid item");
			return "editor";
		}else{
			model.addAttribute("effects", Stringifier.stringifyItems(itemRead.getEffects()));
			model.addAttribute("aliases", Stringifier.stringifyItems(itemRead.getAliases()));
			model.addAttribute("visibility", itemRead.isVisible());
			model.addAttribute("slot", itemRead.getSlot().toString().toLowerCase());
			model.addAttribute("type", itemRead.getType().toString().toLowerCase());
			model.addAttribute("item", itemRead);
			model.addAttribute("overwrite", "overwrite");
			return "createItem";
		}

	}
	
	@GetMapping("/editItem")
	public String editItem(HttpServletRequest request, Model model) {
		model.addAttribute("items",InMemoryItemReader.getItems().values());
		return "editItem";
	}

	@GetMapping("/createActor")
	public String createActorGet(HttpServletRequest request, Model model) {
		model.addAttribute("actor", new Actor());
		return "createActor";
	}

	@PostMapping("/createActor")
	public String createActorPost(Actor actor, HttpServletRequest request, Model model) {
		if(request.getParameter("submitButton").equals("deleteCharacter")){
			Actor actorRead = Main.getActorReader(Main.savedGamePath).readActor(actor.getName());
			if(actorRead==null){
				model.addAttribute("error", "Attempted to remove invalid actor");
				return "editor";
			}else{
				Main.getActorReader(Main.savedGamePath).deleteActor(actor.getName());
				model.addAttribute("result", "Deleted actor "+actor.getName());
				return "editor";
			}
		}
		boolean error = false;
		String errorMessage = "";
		System.out.println("Actor received: " + actor);
		System.out.println("Behaviour: " + request.getParameterValues("behaviour"));
		actor.setBehaviours(new ArrayList<>());
		for (String behaviour : request.getParameterValues("behaviour")) {
			actor.getBehaviours().add(Behaviour.valueOf(behaviour.toUpperCase()));
		}
		actor.setAliases(new ArrayList<>());

		for (String alias : request.getParameter("aliases").split(",")) {
			actor.getAliases().add(alias);
		}
		ItemReader itemReader = Main.getItemReader(Main.savedGamePath);
		actor.setItemsHeld(new ArrayList<>());
		if (!request.getParameter("items").isEmpty()) {
			for (String itemName : request.getParameter("items").split(",")) {
				Item item = itemReader.readItem(itemName);
				if (item == null) {
					error = true;
					errorMessage += "Item '" + itemName + "' not found.<br>";
				} else {
					actor.getItemsHeld().add(item);
				}
			}
		}
		actor.setEffects(new ArrayList<>());
		EffectReader effectReader = Main.getEffectReader();
		if (!request.getParameter("effects").isEmpty()) {
			for (String effectName : request.getParameter("effects").split(",")) {
				Effect effect = effectReader.readEffect(effectName);
				if (effect == null) {
					error = true;
					errorMessage += "Effect '" + effectName + "' not found.<br>";
				} else {
					actor.getEffects().add(effectName);
				}
			}
		}
		actor.setIsDead(Boolean.parseBoolean(request.getParameter("isDead")));

		if (error) {
			request.setAttribute("error", errorMessage);
			request.setAttribute("aliases", request.getParameter("aliases"));
			request.setAttribute("behaviour", request.getParameter("behaviour"));
			request.setAttribute("effect", request.getParameter("effect"));
			request.setAttribute("item", request.getParameter("item"));
			return "createActor";
		} else {
			ActorReader memoryReader = Main.getActorReader(Main.savedGamePath);
			ActorReader fileReader = new JsonActorReader(Main.savedGamePath);
			fileReader.writeActor(actor);
			memoryReader.writeActor(actor);
			request.setAttribute("result", "Created actor: " + actor);
			return "editor";
		}
	}

	@RequestMapping(value = "/editActor", method = { RequestMethod.GET })
	public String editActor(HttpServletRequest request, Model model) {
		model.addAttribute("actors",InMemoryActorReader.getActors().values());
		return "editActor";
	}
	@RequestMapping(value = "/createActor/{actor}", method = { RequestMethod.GET })
	public String editActor(@PathVariable String actor, HttpServletRequest request, Model model) {
		Actor actorRead = Main.getActorReader(Main.savedGamePath).readActor(actor);
		if(actorRead==null){
			model.addAttribute("error", "Attempted to edit invalid actor");
			return "editor";
		}else{
			model.addAttribute("actor", actorRead);
			model.addAttribute("items", Stringifier.stringifyItems(actorRead.getItemsHeld()));
			model.addAttribute("behaviours", actorRead.getBehaviours());
			model.addAttribute("isDead", actorRead.getIsDead());
			model.addAttribute("aliases", Stringifier.stringifyAliases(actorRead.getAliases()));
			model.addAttribute("overwrite", "overwrite");
			return "createActor";
		}
	}
	
	
	

	

}
