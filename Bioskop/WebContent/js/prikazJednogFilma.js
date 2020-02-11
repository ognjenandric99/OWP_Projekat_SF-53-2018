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
				var f = odg.film;
				$("#f_title").text(f.Naziv);
				$("#f_reziser").text(f.Reziser);
				$("#f_glumci").text(f.Glumci.join(","));
				$("#f_trajanje").text(f.Trajanje);
				$("#f_zanrovi").text(f.Zanrovi.join(","));
				$("#f_distributer").text(f.Distributer);
				$("#f_zemlja").text(f.Zemlja_Porekla);
				$("#f_god").text(f.Godina_Proizvodnje);
				$("#f_opis").val(f.Opis);
				if(localStorage['status']!="false" && odg.imaKarata){
					var dugme = document.createElement('button');
					dugme.className="confirmbtn";
					dugme.innerText = "Kupi kartu";
					dugme.setAttribute('data-IDFilma',odg.film.ID);
					dugme.setAttribute('ID',"kupiKartubtn");
					dugme.style="font-size: 18px;margin: 0 auto;margin-top:10px;";
					document.getElementById('dugmici').appendChild(dugme);

					$("#kupiKartubtn").on('click',function(){
						localStorage['projekcija_film_id'] = $("#kupiKartubtn").attr('data-IDFilma');
						window.location.href="index.html";
					})
				}
				if(localStorage['uloga']=="Admin"){
					var dugme = document.createElement('button');
					dugme.className="orangebutton";
					dugme.innerText = "Izmeni";
					dugme.setAttribute('data-IDFilma',odg.film.ID);
					dugme.style="font-size: 18px;margin: 0 auto;margin-top:10px;";
					dugme.onclick=function(){window.location.href="dodajIzmeniFilm.html?id="+odg.film.ID}
					document.getElementById('dugmici').appendChild(dugme);

					dugme = document.createElement('button');
					dugme.className="redbtn";
					dugme.innerText = "Obrisi";
					dugme.setAttribute('data-IDFilma',odg.film.ID);
					dugme.style="font-size: 18px;margin: 0 auto;margin-top:10px;";
					dugme.onclick=function(){window.location.href="dodajIzmeniFilm.html?id="+odg.film.ID}
					document.getElementById('dugmici').appendChild(dugme);
				}
			}

	});
}
