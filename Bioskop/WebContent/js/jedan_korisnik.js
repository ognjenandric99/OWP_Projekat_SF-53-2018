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
			console.log(odg);
			if(odg.status){
				var t = document.getElementById('korisniciTabela');
				var k = odg.korisnik;
				var r = document.createElement('tr');
				r.innerHTML="<td data-id='"+k.ID+"' class='usernamelink'>"+k.Username+"</td><td>"+k.Password+"</td><td>"+k.Datum+"</td><td>"+k.Uloga+"</td>";
				t.appendChild(r);
				$('.usernamelink').on('click',function(){
					window.location.href="prikazKorisnika.html?id="+this.getAttribute('data-id');
				})
			}
			else{
				alert("Nema rezultata.");
			}
			
	});