var url_string = window.location.href;
var url = new URL(url_string);
var id1 = url.searchParams.get("id");
	var params = {
			action: "ucitajKorisnika",
			id: id1
		}
		// kontrola toka se račva na 2 grane
		$.post('KorisnikServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
			// tek kada stigne odgovor izvršiće se ova anonimna funkcija
			var odg = JSON.parse(data);
			if(odg.status){
				$("#user_username").text(odg.korisnik.Username);
				if(odg.korisnik.Username==localStorage['username']){
					$("#user_pass").val(odg.korisnik.Password);
				}
				else{
					$("#user_pass").prop('disabled',true);
					$("#user_pass").prop("readonly",true);
				}
				$("#user_registracija").text(odg.korisnik.Datum);
				$("#user_ulogaselect").val(odg.korisnik.Uloga);
				if(localStorage['uloga']!="Admin"){
					$("#user_ulogaselect").prop('disabled',true);
				}
				$("#user_status").text(odg.korisnik.Status);
				if(localStorage['uloga']=="Admin"){
					var dugme = document.createElement('button');
					dugme.className="redbtn margin0";
					dugme.innerText="Obrisi Korisnika";
					dugme.setAttribute("ID","deleteUser");
					dugme.setAttribute("data-IDUsera",odg.korisnik.ID)
					document.getElementById('dugmici').appendChild(dugme);

					//Ovde dodati funkciju za delete
					$("#deleteUser").on('click',function(){
						var params = {
						    'action' : "deleteUser",
						    'idKorisnika' : this.getAttribute('data-IDUsera')
						}
						$.post('KorisnikServlet',params,function(data){
						    var odg = JSON.parse(data);
								if(odg.status){
									pushNotification('green',odg.message);
								}
								else{
									pushNotification('red',odg.message);
								}
						});
					})

				}
			}
			else{
				window.location.href="index.html";
			}

	});

	$("#saveUser").on('click',function(){
		var promenjenaSifra = true;
		var novasifra = $("#user_pass").val();
		if(novasifra=="" || novasifra==null || novasifra==undefined){
			promenjenaSifra = false;
		}
		var promenjenaUloga = false;
		var novaUloga = $("#user_ulogaselect").val();
		if(localStorage['uloga']=="Admin"){
			promenjenaUloga = true;
		}
		var params  = {
			"action" : "changeUser",
			"promenjenaSifra" : promenjenaSifra,
			"novasifra" : novasifra,
			"promenjenaUloga" : promenjenaUloga,
			"novaUloga" : novaUloga,
			"idKorisnika" : id1
		}
		$.post('KorisnikServlet',params,function(data){
			var odg = JSON.parse(data);
			if(odg.promenjenaSifra){
				pushNotification("green","Uspesno ste promenili sifru");
			}
			if(odg.promenjenaUloga){
				pushNotification('green',"Uspesno ste promenili ulogu");
			}
			if(!odg.promenjenaSifra && !odg.promenjenaUloga){
				pushNotification('red',"Nije bilo moguce promeniti atribute korisnika.");
			}
		})
	})
