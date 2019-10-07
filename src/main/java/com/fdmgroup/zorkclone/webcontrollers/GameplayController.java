package com.fdmgroup.zorkclone.webcontrollers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.user.User;

@Controller
public class GameplayController {
	

	@GetMapping("/play*")
	public String play(Model model, HttpSession session, HttpServletResponse response) {
		if(session.getAttribute("user")==null){
			return "index";
		}
		if (session.getAttribute("player") == null) {
			return "userHome";
		}
		Player player = (Player) session.getAttribute("player");
		PlayerReader playerReader = Main.getPlayerReader();
		player = playerReader.getPlayer(player.getUser().getUsername(),player.getName());
		playerReader.writePlayer(player);
		session.setAttribute("player", player);

		return "play";
	}

	@PostMapping(value = "/play*")
	public String playPost(@ModelAttribute User user, HttpSession session, HttpServletRequest request) {

		return "play";
	}

	@GetMapping("/update")
	public @ResponseBody String update(HttpSession session) {
		Player player = (Player) session.getAttribute("player");
		PlayerReader playerReader = Main.getPlayerReader();
		player = playerReader.getPlayer(player.getUser().getUsername(),player.getName());
		String result = " ";
		session.setAttribute("output", result);
		return result;
	}

}
