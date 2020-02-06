$("#loginbtn").on('click',function(){
	var user = $("#l_username").val();
	var pass1 = $("#l_pass").val();

	if(user.length>0 && pass1.length>0){
		var params = {
				action: "login",
				username : user,
				password : pass1
			}
			// kontrola toka se račva na 2 grane
			$.post('KorisnikServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
				// tek kada stigne odgovor izvršiće se ova anonimna funkcija
				var odg = JSON.parse(data);
				if(odg.status){
					window.location.href="index.html";
				}
				else{
					pushNotification("red",odg.message);
				}

		});
	}
	else{
		alert("Proverite unos!");
	}
});
