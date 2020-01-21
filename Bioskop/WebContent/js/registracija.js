$("#registracija").on('click',function(){
	var user = $("#r_username").val();
	var pass1 = $("#r_pass1").val();
	var pass2 = $("#r_pass2").val();
	
	if(user.length>0 && pass1==pass2 && pass1.length>0){
		var params = {
				action: "registracija",
				username : user,
				password : pass1
			}
			// kontrola toka se račva na 2 grane
			$.post('KorisnikServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
				// tek kada stigne odgovor izvršiće se ova anonimna funkcija
				var odg = JSON.parse(data);
				if(odg.status){
					window.location.href="korisnici.html";
				}
				else{
					alert("Greska prilikom registrovanja.");
				}
				
		});
	}
	else{
		alert("Proverite unos");
	}
});