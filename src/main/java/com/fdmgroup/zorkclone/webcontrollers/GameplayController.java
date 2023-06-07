package com.fdmgroup.zorkclone.webcontrollers;


import com.fdmgroup.zorkclone.combat.CombatChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpSession;
import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.user.User;

@Controller
public class GameplayController {
	@Autowired
	private CombatChecker checker;
	

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
		checker.enterRoom(player, player.getCurrentRoom());
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
