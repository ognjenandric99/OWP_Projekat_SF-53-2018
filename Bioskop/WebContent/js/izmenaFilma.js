var params = {
		action: "uzmiZanrove"
	}
	// kontrola toka se račva na 2 grane
	$.post('FilmoviServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
		// tek kada stigne odgovor izvršiće se ova anonimna funkcija
		var odg = JSON.parse(data);
		if(odg.status){
			for(i=0;i<odg.zanrovi.length;i++){
				var op = document.createElement('option');
				op.value=odg.zanrovi[i];
				op.innerText = odg.zanrovi[i];
				document.getElementById('i_zanroviDropdown').append(op);
			}
		}
		
});

	
	var url_string = window.location.href;
	var url = new URL(url_string);
	var id = url.searchParams.get("id");
	var id1 = id;
	ucitajFilm(id);
	function ucitajFilm(idFilma){
		var params = {
				action: "ucitajFilm",
				filmID: idFilma
			}
			// kontrola toka se račva na 2 grane
			$.post('FilmoviServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
				// tek kada stigne odgovor izvršiće se ova anonimna funkcija
				var odg = JSON.parse(data);
				console.log(odg);
				if(odg.status){
					var film1 = odg.film;
					$("#i_naziv").val(film1.Naziv);
					$("#i_trajanje").val(film1.Trajanje);
					$("#i_zanroviDropdown").val(film1.Zanrovi);
					$("#i_opis").val(film1.Opis);
					$("#i_glumci").val(film1.Glumci);
					$("#i_reziser").val(film1.Reziser);
					$("#i_godinaProizvodnje").val(film1.Godina_Proizvodnje);
					$("#i_distributer").val(film1.Distributer);
					$("#i_zemljaPorekla").val(film1.Zemlja_Porekla);
				}
				else{
					window.location.href="filmovi.html";
				}
				
		});
	}
	
$("#izmeniFilm").on('click',function(){
	var naziv1 = $("#i_naziv").val();
	var trajanje1 = $("#i_trajanje").val();
	var zanr2 = $("#i_zanroviDropdown").val();
	var opis1 = $("#i_opis").val();
	var glumci2 = $("#i_glumci").val();
	var reziser1 = $("#i_reziser").val();
	var godina1 = $("#i_godinaProizvodnje").val();
	var distributer1 = $("#i_distributer").val();
	var zemlja1 = $("#i_zemljaPorekla").val();
	
	var zanr1 = zanr2.join(";");
	var glumci1 = glumci2.split(",").join(";");
	
	if(naziv1.length<1 || trajanje1<1 || godina1<1900 || distributer1.length<1 || zemlja1.length<1){
		alert("Ne valja");
	}
	else{
		var params = {
				action: "izmeniFilm",
				id : id1,
				naziv:naziv1,
				trajanje:trajanje1,
				zanr:zanr1,
				opis:opis1,
				glumci:glumci1,
				reziser:reziser1,
				godina:godina1,
				distributer:distributer1,
				zemlja:zemlja1
			}
			// kontrola toka se račva na 2 grane
			$.post('FilmoviServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
				// tek kada stigne odgovor izvršiće se ova anonimna funkcija
				var odg = JSON.parse(data);
				if(odg.status){
					window.location.href="filmovi.html";
				}
				else{
					alert("Greska prilikom dodavanja.");
				}
				
		});
	}
	
})