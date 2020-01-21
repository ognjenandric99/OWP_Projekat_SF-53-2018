var params = {
		action: "ucitajSve"
	}
	// kontrola toka se račva na 2 grane
$.post('KorisnikServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
		// tek kada stigne odgovor izvršiće se ova anonimna funkcija
		var odg = JSON.parse(data);
		if(odg.status){
			var t = document.getElementById('korisniciTabela');
			
			for(i=0;i<odg.lista.length;i++){
				var k = odg.lista[i];
				var r = document.createElement('tr');
				r.innerHTML="<td data-id='"+k.ID+"' class='usernamelink'>"+k.Username+"</td><td>"+k.Password+"</td><td>"+k.Datum+"</td><td>"+k.Uloga+"</td>";
				t.appendChild(r);
			}
			$('td').on('click',function(){
				window.location.href="prikazKorisnika.html?id="+this.getAttribute('data-id');
			})
		}
		
});