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
				if(localStorage['ajdi']!=id1 && localStorage['uloga']!=['Admin']){
					localStorage['poruka']="red|Ne mozete da vidite sadrzaj na toj stranici.";
					window.location.href="index.html";
				}
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

var params = {
    'action' : 'ucitajKarteKorisnika',
    'id' : id1
}
$.post('KorisnikServlet',params,function(data){
    var odg = JSON.parse(data);

		if(odg.status){
			for(i=0;i<odg.karte.length;i++){
				var k = odg.karte[i];
				var tr = document.createElement('tr');
				var td1= document.createElement('td');
				td1.innerText=k.nazivFilma;
				td1.setAttribute('data-idFilma',k.ID_filma);
				td1.className="ticket_filmName";
				tr.appendChild(td1);

				var td2= document.createElement('td');
				td2.innerText=k.sediste;
				tr.appendChild(td2);

				var td3= document.createElement('td');
				td3.innerText=k.Termin;
				tr.appendChild(td3);

				var td4= document.createElement('td');
				td4.innerHTML="<button class='pogledajbtn confirmbtn' data-idKarte='"+k.ID+"' data-idKorisnika='"+id1+"'>Pogledaj Kartu</button>";
				td4.className="ticket_button";
				tr.appendChild(td4);

				document.getElementById('karteTable').appendChild(tr);
			}
			$(".pogledajbtn").on('click',function(){
				localStorage['kartaKorisnik']=this.getAttribute('data-idKorisnika');
				window.location.href="karta.html?id="+this.getAttribute('data-idKarte');
			})
		}
		else{
			localStorage['poruka']="red|Desila se greska, molimo Vas da pokusate kasnije";

		}
		if(odg.karte.length==0){
			document.getElementById('karteTable').style.display="none";
			localStorage['poruka']="red|Ne postoje karte za tog korisnika.";
		}
});
