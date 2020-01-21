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
				r.className="item";
				r.innerHTML="<td data-id='"+k.ID+"' class='usernamelink'>"+k.Username+"</td><td>"+k.Password+"</td><td>"+k.Datum+"</td><td>"+k.Uloga+"</td>";
				t.appendChild(r);
			}
			$('.usernamelink').on('click',function(){
				window.location.href="prikazKorisnika.html?id="+this.getAttribute('data-id');
			});
			
			
		}
		
});


$("#korisnici_filter").on('click',function(){
	var f_us = $("#UsernameFilter").val();
	var f_pa = $("#PasswordFilter").val();
	var f_datum = $("#Datum").val();
	var f_tip = $("#Tip").val();
	console.log(f_us+" "+f_pa+" "+f_datum+" "+f_tip);
	
	var params = {
			action:"filter",
			username : f_us,
			password : f_pa,
			datum : f_datum,
			tip : f_tip
		}
	
	$.post('KorisnikServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
		// tek kada stigne odgovor izvršiće se ova anonimna funkcija
		var odg = JSON.parse(data);
		if(odg.status){
			var t = document.getElementById('korisniciTabela');
			$('tr').slice(2).remove();
			for(i=0;i<odg.lista.length;i++){
				var k = odg.lista[i];
				var r = document.createElement('tr');
				r.className="item";
				r.innerHTML="<td data-id='"+k.ID+"' class='usernamelink'>"+k.Username+"</td><td>"+k.Password+"</td><td>"+k.Datum+"</td><td>"+k.Uloga+"</td>";
				t.appendChild(r);
			}
			$('.usernamelink').on('click',function(){
				window.location.href="prikazKorisnika.html?id="+this.getAttribute('data-id');
			})
			
		}
		else{
			alert("Nema rezultata sa tim kriterijumom.");
		}
		
});
});


