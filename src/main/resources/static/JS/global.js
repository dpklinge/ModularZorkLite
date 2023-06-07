var validateRegister = function RegisterValidate() {
//  var MyJavaClass = Java.type('com.fdmgroup.documentuploader.DispathController.UserRegistrationSubmit');
//  var Register = MyJavaClass.userRegistrationSubmit;
	
  var first_name = document.querySelector(".first-name").value;
  var last_name = document.querySelector(".last-name").value;
  var email = document.querySelector(".email").value;
  var user_name = document.querySelector(".username").value;
  var create_password = document.querySelector(".create-password").value
  var confirm_password = document.querySelector(".confirm-password").value;
  var security_question = document.querySelector(".security-question").value;

  if (first_name == "") {
    var element1 = document.getElementById("missingFirstName");
    element1.classList.remove("validate-form");
    var element11 = document.getElementById("incorrectFirstName");
    element11.classList.add("incorrect");
    return false;
  }else{
	  var element1 = document.getElementById("missingFirstName");
	  element1.classList.add("validate-form");
	  var element11 = document.getElementById("incorrectFirstName");
	  element11.classList.remove("incorrect");
  }

  if (last_name == "") {
    var element2 = document.getElementById("missingLastName");
    element2.classList.remove("validate-form");
    var element12 = document.getElementById("incorrectLastName");
    element12.classList.add("incorrect");
    return false;
  }else{
	  var element2 = document.getElementById("missingLastName");
	    element2.classList.add("validate-form");
	    var element12 = document.getElementById("incorrectLastName");
	    element12.classList.remove("incorrect");
  }
  
  if (email == "") {
    var element3 = document.getElementById("missingEmail");
    element3.classList.remove("validate-form");
    var element13 = document.getElementById("incorrectEmail");
    element13.classList.add("incorrect");
    return false;
  }else{
	  var element3 = document.getElementById("missingEmail");
	    element3.classList.add("validate-form");
	    var element13 = document.getElementById("incorrectEmail");
	    element13.classList.remove("incorrect");
  }

  if (email.indexOf("@", 0) < 0) {
    var element4 = document.getElementById("nonValidEmail");
    element4.classList.remove("validate-form");
    var element14 = document.getElementById("incorrectEmail");
    element14.classList.add("incorrect");
    email.focus();
    return false;
  }else{
	  var element4 = document.getElementById("nonValidEmail");
	    element4.classList.add("validate-form");
	    var element14 = document.getElementById("incorrectEmail");
	    element14.classList.remove("incorrect");
	    email.focus();
  }

  if (email.indexOf(".", 0) < 0) {
    var element5 = document.getElementById("nonValidEmail");
    element5.classList.remove("validate-form");
    var element15 = document.getElementById("incorrectEmail");
    element15.classList.add("incorrect");
    email.focus();
    return false;
  }else{
	  var element5 = document.getElementById("nonValidEmail");
	    element5.classList.add("validate-form");
	    var element15 = document.getElementById("incorrectEmail");
	    element15.classList.remove("incorrect");
	    email.focus();
  }

  if (user_name == "") {
    var element6 = document.getElementById("missingUsername");
    element6.classList.remove("validate-form");
    var element16 = document.getElementById("incorrectUsername");
    element16.classList.add("incorrect");
    return false;
  }else{
	  var element6 = document.getElementById("missingUsername");
	    element6.classList.add("validate-form");
	    var element16 = document.getElementById("incorrectUsername");
	    element16.classList.remove("incorrect");
  }

  if (create_password == "") {
    var element7 = document.getElementById("missingPassword");
    element7.classList.remove("validate-form");
    var element17 = document.getElementById("incorrectPassword");
    element17.classList.add("incorrect");
    return false;
  }else{
	  var element7 = document.getElementById("missingPassword");
	    element7.classList.add("validate-form");
	    var element17 = document.getElementById("incorrectPassword");
	    element17.classList.remove("incorrect");
  }

  if (confirm_password == "") {
    var element8 = document.getElementById("missingConfirmPassword");
    element8.classList.remove("validate-form");
    var element18 = document.getElementById("incorrectConfirmPassword");
    element18.classList.add("incorrect");
    return false;
  }else{
	  var element8 = document.getElementById("missingConfirmPassword");
	    element8.classList.add("validate-form");
	    var element18 = document.getElementById("incorrectConfirmPassword");
	    element18.classList.remove("incorrect");
  }

  if (create_password !== confirm_password) {
    var element9 = document.getElementById("nonValidConfirmPassword");
    element9.classList.remove("validate-form")
    var element19 = document.getElementById("incorrectConfirmPassword");
    element19.classList.add("incorrect");
    return false;
  }else{
	  var element9 = document.getElementById("nonValidConfirmPassword");
	    element9.classList.add("validate-form")
	    var element19 = document.getElementById("incorrectConfirmPassword");
	    element19.classList.remove("incorrect");
  }
  
  if (security_question == "") {
    var element10 = document.getElementById("missingSecurityQuestion");
    element10.classList.remove("validate-form");
    var element20 = document.getElementById("incorrectSecurityQuestion");
    element20.classList.add("incorrect");
    return false;
  }else{
	  var element10 = document.getElementById("missingSecurityQuestion");
	    element10.classList.add("validate-form");
	    var element20 = document.getElementById("incorrectSecurityQuestion");
	    element20.classList.remove("incorrect");
  }
  

    return true;
  
}

function LoginValidate(){
  var user_name = document.querySelector(".username").value;
  var password = document.querySelector(".password").value;
  
   if (user_name == "") {
    var element1 = document.getElementById("missingUsername");
    element1.classList.remove("validate-form");
    var element11 = document.getElementById("incorrectUsername");
    element11.classList.add("incorrect");
    return false;
   }
   
   if (password == "") {
    var element2 = document.getElementById("missingPassword");
    element2.classList.remove("validate-form");
    var element17 = document.getElementById("incorrectPassword");
    element17.classList.add("incorrect");
    return false;
  }
  else{
    true;
  }
  
}