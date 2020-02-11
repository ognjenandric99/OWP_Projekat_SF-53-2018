function ucitajFilm(idFilma){
		var params = {
				action: "ucitajFilm",
				filmID: idFilma
			}
			// kontrola toka se račva na 2 grane
			$.post('FilmoviServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
				// tek kada stigne odgovor izvršiće se ova anonimna funkcija
				console.log(data);
				return data;
		});
}

var params = {
		action: "ucitajSve",
		filmID: "1"
	}
	// kontrola toka se račva na 2 grane
	$.post('FilmoviServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
		// tek kada stigne odgovor izvršiće se ova anonimna funkcija
		var odg = JSON.parse(data);
		if(odg.status){
			for(i=0;i<odg.filmovi.length;i++){
				var film1 = odg.filmovi[i];
				var f = document.getElementById('tabelaFilm');
				var tr = document.createElement('tr');
				tr.className="item";
				tr.setAttribute('data-FilmID',film1.ID);
				var dugmicitamoneki = "";
				if(localStorage['uloga']=="Admin"){
					dugmicitamoneki = "<span class='editMovie' data-movieID='"+film1.ID+"'></span><span class='deleteMovie' data-movieID='"+film1.ID+"'></span>";
				}
				else{
					dugmicitamoneki = "<span class='pogledajMovie' data-movieID='"+film1.ID+"'></span>"
				}
				tr.innerHTML = "<td class='movie_name' data-filmid='"+film1.ID+"'>"+film1.Naziv+"</td><td>"+film1.Trajanje+"</td><td>"+film1.Zanrovi+"</td><td>"+film1.Godina_Proizvodnje+"</td><td>"+film1.Distributer+"</td><td>"+film1.Zemlja_Porekla+"</td><td>"+dugmicitamoneki+"</td>";
				f.appendChild(tr);
			}
			$(".pogledajMovie").on('click',function(){
				var id = this.getAttribute('data-movieID');
				if(id>0 && id!=null && id!=undefined){
					window.location.href="prikazFilma.html?id="+id;
				}
			});
			$(".movie_name").on("click", function(){
				var id = this.getAttribute('data-filmid');
				if(id>0 && id!=null && id!=undefined){
					window.location.href="prikazFilma.html?id="+id;
				}
			});
			$(".editMovie").on('click',function(){
				window.location.href="dodajIzmeniFilm.html?id="+this.getAttribute('data-movieID');
			})
			$(".deleteMovie").on("click",function(){
				if(confirm("Da li ste sigurni da zelite da obrisete?")){
					var params = {
							action: "obrisiFilm",
							filmID: this.getAttribute('data-movieID')
						}
						// kontrola toka se račva na 2 grane
					$.post('FilmoviServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
							// tek kada stigne odgovor izvršiće se ova anonimna funkcija
							var odg = JSON.parse(data);
							if(odg.status){
								window.location.href="filmovi.html";
							}
							else{
								pushNotification('red',"Greska prilikom brisanja");
							}

					});
				}
			})
		}

});

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
				document.getElementById('f_zanrovi').append(op);
			}
		}

});

$("#filterBtnFilm").on("click",function(){
	var naziv1 = $("#f_naziv").val();
	var trajanje1 = $("#f_trajanje").val();
	var zanr1 = $("#f_zanrovi").val();
	var godina1 = $("#f_godina").val();
	var distributer1 = $("#f_distributer").val();
	var zemlja1 = $("#f_zemlja").val();

	zanr1.sort();
	zanr1 = zanr1.join(";");

		var params = {
				action: "filterFilm",
				naziv:naziv1,
				trajanje:trajanje1,
				zanr:zanr1,
				opis:"",
				glumci:"",
				reziser:"",
				godina:godina1,
				distributer:distributer1,
				zemlja:zemlja1
			}
			// kontrola toka se račva na 2 grane
			$.post('FilmoviServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
				// tek kada stigne odgovor izvršiće se ova anonimna funkcija
				console.log(data);
				var odg = JSON.parse(data);
				if(odg.status){
					if(odg.filmovi.length>0){
						$('tr').slice(2).remove();
						for(i=0;i<odg.filmovi.length;i++){
							var film1 = odg.filmovi[i];
							var f = document.getElementById('tabelaFilm');
							var tr = document.createElement('tr');
							tr.className="item";
							tr.setAttribute('data-FilmID',film1.ID);
							tr.innerHTML = "<td class='movie_name' data-filmid='"+film1.ID+"'>"+film1.Naziv+"</td><td>"+film1.Trajanje+"</td><td>"+film1.Zanrovi+"</td><td>"+film1.Godina_Proizvodnje+"</td><td>"+film1.Distributer+"</td><td>"+film1.Zemlja_Porekla+"</td><td><span class='editMovie' data-movieID='"+film1.ID+"'></span><span class='deleteMovie' data-movieID='"+film1.ID+"'></span></td>";
							f.appendChild(tr);
						}
						$(".movie_name").on("click", function(){
							var id = this.getAttribute('data-filmid');
							if(id>0 && id!=null && id!=undefined){
								window.location.href="http://localhost:8080/Bioskop/prikazFilma.html?id="+id;
							}
						});
						$(".deleteMovie").on("click",function(){
							var params = {
									action: "obrisiFilm",
									filmID: this.getAttribute('data-movieID')
								}
								// kontrola toka se račva na 2 grane
							$.post('FilmoviServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
									// tek kada stigne odgovor izvršiće se ova anonimna funkcija
									var odg = JSON.parse(data);
									if(odg.status){
										window.location.href="http://localhost:8080/Bioskop/filmovi.html";
									}
									else{
										pushNotification('red',"Greska prilikom brisanja");
									}

							});
						})
					}
					else{
						pushNotification('red',"Ne postoji film sa zadatim kriterijumom");
					}
				}
				else{
					pushNotification('red',"Ne postoji film sa zadatim kriterijumom.");
				}

		});



});
