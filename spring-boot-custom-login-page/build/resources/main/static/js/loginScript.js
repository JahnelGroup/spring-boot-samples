function showPassword() {
	var x = document.getElementById("passwordInput");
	if (x.type === "password") {
		x.type = "text";
		document.getElementById('showPassIcon').src='/images/showPassword.png'
	}else{
		x.type = "password";
		document.getElementById('showPassIcon').src='/images/noPassword.png'
	}
}
