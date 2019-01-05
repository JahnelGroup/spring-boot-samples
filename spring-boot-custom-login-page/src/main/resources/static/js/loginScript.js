//This function switches the mode of the password input from the user giving them
//the ability to toggle on and off the visability of their password. It accomplishes
//this by changing the type of the input based on its current type.
function showPassword() {
	// password input in login.html
	var x = document.getElementById("passwordInput");
	// in password mode
	if (x.type === "password") {
		x.type = "text";
		document.getElementById('showPassIcon').src='/images/showPassword.png'
	}
	// in text mode
	else{
		x.type = "password";
		document.getElementById('showPassIcon').src='/images/noPassword.png'
	}
}
