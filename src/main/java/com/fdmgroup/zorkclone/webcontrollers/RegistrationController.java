package com.fdmgroup.zorkclone.webcontrollers;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.user.User;
import com.fdmgroup.zorkclone.user.UserReader;
import com.fdmgroup.zorkclone.user.Validator;

@Controller
public class RegistrationController {

	@GetMapping("/register")
	public String userRegistration(Model model, HttpSession session) {
		session.setAttribute("loginError", "");
		User user = new User();

		model.addAttribute(user);
		return "register";
	}

	@PostMapping("/register")
	public String userRegistrationSubmit(@ModelAttribute User user, HttpServletRequest request ,HttpSession session) {
		Validator validator = new Validator();
		
		String[] errorString = validator.validateUserRegistration(user, request.getParameter("PasswordConfirm"));
		if (Objects.isNull(errorString)||errorString.length<0) {
			
			UserReader reader = Main.getUserReader();
			try {
				reader.create(user);
				session.setAttribute("user", user);
				return "redirect:login";
			} catch (Exception e) {
				return "register";
			} finally {
			}
		} else {
			request.setAttribute("registrationError", "This is the error string:"+errorString);
			request.setAttribute("usernameError", errorString[0]);
			request.setAttribute("passwordError", errorString[1]);
			request.setAttribute("firstNameError", errorString[2]);
			request.setAttribute("lastNameError", errorString[3]);
			request.setAttribute("emailError", errorString[4]);
			
			return"register";
		}
	}
	
}
