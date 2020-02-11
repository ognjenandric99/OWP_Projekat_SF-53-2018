var url_string = window.location.href;
var url = new URL(url_string);
var id1 = url.searchParams.get("id");

var params = {
    'action' : 'ucitajKarteKorisnika',
    'id' : localStorage['kartaKorisnik']
}
$.post('KorisnikServlet',params,function(data){
    var odg = JSON.parse(data);
		if(odg.status){
      var stanje = false;
      for(i=0;i<odg.karte.length;i++){
        var k = odg.karte[i];

        if(id1==k.ID+""){
          stanje = true;
          $("#film_title").text(k.nazivFilma);
          $("#tipProjekcije").text(k.tipProjekcije);
          $("#termin").text(k.Termin);
          $("#sala").text(k.sala);
          $("#cenaKarte").text(k.cena);
          $("#Sediste").text(k.sediste);

          if(localStorage['uloga']!="Admin"){
            $("#deletebtn").remove();
          }
        }
      }
      if(!stanje){
        localStorage['poruka']="red|Desila se greska2, molimo Vas da pokusate kasnije";
  			window.location.href="index.html";
      }
		}
		else{
			localStorage['poruka']="red|Desila se greska, molimo Vas da pokusate kasnije";
			window.location.href="index.html";
		}
});

$("#deletebtn").on('click',function(){
  if(confirm("Da li sigurno zelite da obrisete ovu kartu?")){
    var params = {
      'action' : 'deleteTicket',
      'idKarte' : id1
    }
    $.post("AdminServlet",params,function(data){
      var odg = JSON.parse(data);
      if(odg.status){
        localStorage['poruka']="green|"+odg.message;
      }
      else{
        localStorage['poruka']="red|"+odg.message;
      }
      window.location.href="admin_panel.html";
    })
  }
});
