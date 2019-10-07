package com.fdmgroup.zorkclone.webcontrollers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import com.fdmgroup.zorkclone.user.User;
import com.fdmgroup.zorkclone.user.UserReader;
import com.fdmgroup.zorkclone.user.Validator;

@Controller
public class LoginController {

	@GetMapping(value = "/login")
	public String userLogin(Model model, HttpSession session) {

			User user = new User();
			model.addAttribute(user);

		return "login";
	}
	
	@GetMapping(value = "/logout")
	public String userLogout(Model model, HttpSession session) {
		session.invalidate();
		return "index";
	}

	@PostMapping("/login")
	public String userLoginSuccess(@ModelAttribute User user,Validator validator, HttpSession session, Model model) {
		String validationError = validator.validateUserLogin(user.getUsername(), user.getPassword());
		if (validationError.equals("") || validationError.equals(null)) {
			UserReader reader = Main.getUserReader();
			user = reader.read(user.getUsername());
			if(user.getCharacterName()!=null){
				PlayerReader playerReader = Main.getPlayerReader();
				Player player= playerReader.getPlayer(user.getUsername(), user.getCharacterName());
				System.out.println("This is the player that was read:");
				System.out.println(player);
				session.setAttribute("player", player);
			}
			System.out.println("Username login: " + user.getUsername());
			session.setAttribute("user", user);
			session.setAttribute("firstPlay", "true");
			return "redirect:userHome";
		} else {
			model.addAttribute("loginError", validationError);
			return "login";
		}
	}

	@GetMapping(value = "/userHome")
	public String userHome(Model model, HttpSession session) {
		if(session.getAttribute("user")==null){
			return "redirect:/";
		}
		return "userHome";
	}
}
