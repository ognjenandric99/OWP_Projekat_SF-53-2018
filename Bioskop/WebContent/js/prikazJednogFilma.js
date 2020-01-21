var url_string = window.location.href;
var url = new URL(url_string);
var id = url.searchParams.get("id");
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
				var f = document.getElementById('tabelaFilm');
				var tr = document.createElement('tr');
				tr.innerHTML = "<td>"+film1.ID+"</td><td>"+film1.Naziv+"</td><td>"+film1.Trajanje+"</td><td>"+film1.Zanrovi+"</td><td>"+film1.Opis+"</td><td>"+film1.Glumci+"</td><td>"+film1.Reziser+"</td><td>"+film1.Godina_Proizvodnje+"</td><td>"+film1.Distributer+"</td><td>"+film1.Zemlja_Porekla+"</td>";
				f.appendChild(tr);
			}
			
	});
}