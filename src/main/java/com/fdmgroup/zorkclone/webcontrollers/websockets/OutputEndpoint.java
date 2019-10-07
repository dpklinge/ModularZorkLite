package com.fdmgroup.zorkclone.webcontrollers.websockets;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.combat.CombatChecker;
import com.fdmgroup.zorkclone.commands.CommandParser;
import com.fdmgroup.zorkclone.commands.CommandProcessor;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;
import com.fdmgroup.zorkclone.user.JsonUserReader;
import com.fdmgroup.zorkclone.user.User;
import com.fdmgroup.zorkclone.user.UserReader;

@ServerEndpoint(value = "/webSocketEndpoint", configurator=ServletAwareConfig.class)
public class OutputEndpoint {
	private User user;
	private Session session;
	private static Set<OutputEndpoint> setToSynchronize = new CopyOnWriteArraySet<>();
	public static Set<OutputEndpoint> outputEndpoints =Collections.synchronizedSet(setToSynchronize);
	private static HashMap<User, OutputEndpoint> usersToSynchronize = new HashMap<>();
	public static Map<User, OutputEndpoint> users = Collections.synchronizedMap(usersToSynchronize);

	public User getUser() {
		return user;
	}

	@OnOpen
	public void onOpen(Session webSocketSession, EndpointConfig config) {
		
		System.out.println("Opening websocket");
		
		this.session = webSocketSession;
		outputEndpoints.add(this);
		HttpSession session = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		User user = (User) session.getAttribute("user");
		System.out.println("Adding user endpoint: "+user.getUsername());
		UserReader reader = new JsonUserReader();
		this.user =reader.read(user.getUsername());;
		users.put(user, this);
		System.out.println("Open Connection ...");
		broadcastSingle(this,"<br/>Connection opened.<br/><br/>");
		
		Player player =Main.getPlayerReader().getPlayer(user.getUsername(), user.getCharacterName());

		RoomReader roomReader = Main.getRoomReader(Main.savedGamePath);
		Room room = roomReader.readRoom(player.getCurrentRoom().getName());
		System.out.println(room.getPlayersPresent());
		player.setCurrentRoom(room);
		broadcastSingle(this,player.getCurrentRoom().displayRoomWeb(player));
		broadcast("<br/>"+user.getCharacterName()+ " has connected!");
		CombatChecker.enterRoom(player, room);
		
	}

	

	@OnClose
	public void onClose() {
		outputEndpoints.remove(this);
		users.remove(user);
		System.out.println("<br/>"+user.getCharacterName()+" disconnected...");
		broadcast("<br/>"+user.getCharacterName()+" disconnected...");
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println("Message from the client: " + message);
		PlayerReader playerReader = Main.getPlayerReader();
		Player player = playerReader.getPlayer(user.getUsername(),user.getCharacterName());
		if (message.length() > 0) {
			broadcastSingle(this,"<br />" + message);
			String[] commands = commandParser(message);
			CommandProcessor processor = new CommandProcessor(player);
			player = processor.processCommand(commands);
			if (player == null) {
				return;
			}
		} else {
			broadcastSingle(this,"You didn't enter anything!");
		}
		playerReader.writePlayer(player);
		return;
	}

	@OnError
	public void onError(Throwable e) {
		e.printStackTrace();
		broadcastSingle(this,"<br/>Help me debug! Here's the error.<br/>");
		broadcastSingle(this,e.toString());
	}
	
	public String[] commandParser(String command) {
		String[] commands = {};

		commands = command.split(" ");

		CommandParser parser = new CommandParser();
		String firstCommand = parser.validateFirstCommand(commands);
		if (Objects.isNull(firstCommand) || firstCommand.length() < 1) {
		} else if (command.equals("Bad command")) {
			return null;
		} else {
			return commands;
		}

		return null;

	}
	
	public static void broadcastSingle(OutputEndpoint oe, String message) {
		System.out.println("Message should be:");
		System.out.println(message);
		System.out.println("oe data:");
		System.out.println(oe.user);
		System.out.println("Single text message : '"+message+"' to: "+oe.getUser().getCharacterName());
		
					try {
						oe.session.getBasicRemote().sendText(message);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Ending single broadcast.");
		
	}

	public static void broadcast(String message) {
		System.out.println("everyone text message: "+message);
		for(OutputEndpoint oe : outputEndpoints){
				try {
					oe.session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	public static void endSession(OutputEndpoint oe, Player player){
		oe.onClose();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((session == null) ? 0 : session.hashCode());
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
		OutputEndpoint other = (OutputEndpoint) obj;
		if (session == null) {
			if (other.session != null)
				return false;
		} else if (!session.equals(other.session))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	

}