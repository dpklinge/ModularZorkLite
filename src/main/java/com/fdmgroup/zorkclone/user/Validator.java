package com.fdmgroup.zorkclone.user;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.player.Player;
import org.apache.commons.validator.routines.EmailValidator;

public class Validator {


	private UserReader reader;
	

	public Validator() {
		super();
		this.reader = Main.getUserReader();
	}

	public String validateUserLogin(String username, String password) {
		
			User user = reader.read(username);
			if(user==null){
				System.out.println("Null user");
				return "User does not match user data records.";
			}else if (user.getPassword().equals(password)) {
				System.out.println("Matching password");
				return "";
			} else {
				System.out.println("Non matching password");
				return "Password does not match our records!";
			}
	}
	
	public boolean validatePasswordConfirmation(String password,String confirmPassword){
		if(password.equals(confirmPassword)){
			return true;
		}
		return false;
	}
	

	@SuppressWarnings("unused")
	public String[] validateUserRegistration(User user, String passwordConfirm) {
		System.out.println(user.getPassword()+" vs "+passwordConfirm);
		boolean isValid = true;
		String usernameValid = "";
		String passwordValid = "";
		String firstNameValid = "";
		String lastNameValid = "";
		String emailValid = "";

		try {
			if (user != null) {
				if (user.getUsername() != null) {
					
					
					if(reader.doesExist(user)){
						usernameValid = "Username is already taken!";
						isValid=false;
					}else if (!user.getUsername().isEmpty() && user.getUsername().length() <= 20) {
						usernameValid = "";
					}else{
						usernameValid = "Username too long!";
						isValid=false;
					}
				}
				if (user.getPassword() != null) {
					if(!user.getPassword().equals(passwordConfirm)){
						passwordValid = "Passwords did not match.";
						isValid=false;
					}else if (!user.getPassword().isEmpty() && user.getPassword().length() >= 8) {
						passwordValid = "";
					}else {
						passwordValid = "Password must be at least 8 characters.";
						isValid=false;
					}
				}
				if (user.getFirstName() != null) {
					if (!user.getFirstName().isEmpty() && user.getFirstName().length() <= 20) {
						firstNameValid = "";
					}else{
						firstNameValid = "First name too long!";
						isValid=false;
					}
				}
				if (user.getLastName() != null) {
					if (!user.getLastName().isEmpty() && user.getLastName().length() <= 20) {
						lastNameValid = "";
					}else{
						lastNameValid = "Last name too long!";
						isValid=false;
					}
				}
				if (user.getEmail() != null) {
					if (!user.getEmail().isEmpty() && emailValidator(user.getEmail())) {
						emailValid = "";
					}else{
						emailValid = "Email invalid.";
						isValid=false;
					}
				}

				if (isValid) {
					return null;
				} else {
					String[] errors = { usernameValid, passwordValid , firstNameValid , lastNameValid , emailValid};
					return errors;
				}
			} 
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
		return null;

	}

	public boolean emailValidator(String email) {
		return EmailValidator.getInstance().isValid(email);
	}

	public String[] validateCharacter(Player character) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
