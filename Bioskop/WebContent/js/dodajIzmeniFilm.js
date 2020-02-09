setInterval(function(){
	if(localStorage['uloga']!="Admin"){
		window.location.href="index.html";
	}
},100);

$("#OnefilmDiv .row span").each(function(){
  this.style.margin="0 auto";
})

var params = {
		action: "uzmiZanrove"
	}
	// kontrola toka se račva na 2 grane
	$.post('FilmoviServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
		// tek kada stigne odgovor izvršiće se ova anonimna funkcija
		var odg = JSON.parse(data);
		if(odg.status){
			for(i=0;i<odg.zanrovi.length;i++){
				if(odg.zanrovi[i]!="NeunesenZanr"){
          var op = document.createElement('option');
  				op.value=odg.zanrovi[i];
  				op.innerText = odg.zanrovi[i];
  				document.getElementById('f_zanrovi').append(op);
        }
			}
		}

});


var url_string = window.location.href;
var url = new URL(url_string);
var id = url.searchParams.get("id");
if(id!=null){
  ucitajFilm(id);
}

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
				$("#f_title").val(f.Naziv);
				$("#f_reziser").val(f.Reziser);
				$("#f_glumci").val(f.Glumci.join(";"));
				$("#f_trajanje").val(f.Trajanje);
				$("#f_zanrovi").val(f.Zanrovi);
				$("#f_distributer").val(f.Distributer);
				$("#f_zemlja").val(f.Zemlja_Porekla);
				$("#f_god").val(f.Godina_Proizvodnje);
				$("#f_opis").val(f.Opis);
				$("#deletebtn").show();
			}

	});
}



$("#savebtn").on('click',function(){
  var url_string = window.location.href;
  var url = new URL(url_string);
  var id = url.searchParams.get("id");
	console.log(id);
  if(id==null){
    var params = {
      'action' : 'dodajFilm',
      'naziv' : $("#f_title").val(),
      'trajanje' : $("#f_trajanje").val(),
      'zanr' : $("#f_zanrovi").val().join(";"),
      'opis' : $("#f_opis").val(),
      'glumci' : $("#f_glumci").val(),
      'reziser' : $("#f_reziser").val(),
      'godina' : $("#f_god").val(),
      'distributer' : $("#f_distributer").val(),
      'godina' : $("#f_god").val(),
      'zemlja' : $("#f_zemlja").val()
    }
    $.post('FilmoviServlet',params,function(data){
      var odg = JSON.parse(data);
      if(odg.status){
        localStorage['poruka']="green|Uspesno ste dodali film!";
        window.location.href="filmovi.html";
      }
      else{
        pushNotification('red','Nije bilo moguce dodati taj film. Molimo Vas da proverite unos.');
      }
    });
  }
	else{
		var params = {
      'action' : 'izmeniFilm',
			'id' : id,
      'naziv' : $("#f_title").val(),
      'trajanje' : $("#f_trajanje").val(),
      'zanr' : $("#f_zanrovi").val().join(";"),
      'opis' : $("#f_opis").val(),
      'glumci' : $("#f_glumci").val(),
      'reziser' : $("#f_reziser").val(),
      'godina' : $("#f_god").val(),
      'distributer' : $("#f_distributer").val(),
      'godina' : $("#f_god").val(),
      'zemlja' : $("#f_zemlja").val()
    }
    $.post('FilmoviServlet',params,function(data){
      var odg = JSON.parse(data);
      if(odg.status){
        localStorage['poruka']="green|Uspesno ste izmenili film!";
        window.location.href="filmovi.html";
      }
      else{
        pushNotification('red','Nije bilo moguce dodati taj film. Molimo Vas da proverite unos.');
      }
    });
	}
});
