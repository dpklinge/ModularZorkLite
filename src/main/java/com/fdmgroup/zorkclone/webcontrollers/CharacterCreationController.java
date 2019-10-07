package com.fdmgroup.zorkclone.webcontrollers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.JsonPlayerReader;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.rooms.Room;
import com.fdmgroup.zorkclone.rooms.io.RoomReader;
import com.fdmgroup.zorkclone.user.JsonUserReader;
import com.fdmgroup.zorkclone.user.User;
import com.fdmgroup.zorkclone.user.UserReader;

@Controller
public class CharacterCreationController {
	@GetMapping("/createCharacter")
	public String characterCreation(Model model, HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user")==null){
			return "redirect:index";
		}
		request.setAttribute("loginError", "");

		return "createCharacter";
	}

	@PostMapping("/createCharacter")
	public String characterCreationSubmit(HttpServletRequest request ,HttpSession session,ModelMap model) {
		Player player = new Player();
		player.setName(request.getParameter("CharacterName"));
		player.setDescription(request.getParameter("Description"));
		User user = (User) session.getAttribute("user");
		player.setUser(user);
		user.setCharacterName(player.getName());
		RoomReader roomReader=Main.getRoomReader(Main.savedGamePath);
		Room room = roomReader.readRoom(Main.startRoom);
		player.setCurrentRoom(room);
		session.setAttribute("room", room);
		System.out.println("User creating character: "+user.getUsername());
		UserReader userReader = new JsonUserReader();
		userReader.update(user);
		session.setAttribute("user", user);
		Path path =Paths.get(Main.userPath.toString(),"/"+player.getUser().getUsername()+"/characters/"+player.getName()+".txt");
		File file = path.toFile();
		if (!file.exists()) {
			System.out.println("===================================================");
			System.out.println("Creating player file: "+path.toString());
			file.getParentFile().mkdirs();
			PlayerReader reader = new JsonPlayerReader();
			session.setAttribute("player", player);
			try {
				reader.writePlayer(player);
				return "redirect:play";
			} catch (Exception e) {
				return "createCharacter";
			} finally {
			}
		} else {
			System.out.println("CHARACTER NAME ALREADY EXISTS");
			request.setAttribute("characterError", player.getName()+" has already been taken!");
			model.addAttribute("characterError", player.getName()+" has already been taken!");
			return "createCharacter";
		}
	}
}