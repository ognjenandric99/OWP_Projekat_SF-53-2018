var url_string = window.location.href;
var url = new URL(url_string);
var id = url.searchParams.get("id");
var params = {
    'action' : "ucitajProjekciju",
    'idProjekcije' : id
}
$.post('ProjekcijeServlet',params,function(data){
    var odg = JSON.parse(data);
      $("#p_title").html("<a href='prikazFilma.html?id="+odg.idFilma+"''>"+odg.nazivFilma+"</a>");
      $("#p_tipProjekcije").text(odg.tipProjekcije);
      $("#p_sala").text(odg.nazivSale);
      $("#p_pocetak").text(odg.termin);
      $("#p_cena").text(odg.cenaKarte);
      $("#p_brojSlobodnih").text(odg.brojKarata);
      if(odg.brojKarata>0){
        var dugme = document.createElement('button');
        dugme.className="confirmbtn";
        dugme.innerText = "Kupi kartu";
        dugme.setAttribute('data-IDFilma',odg.idFilma);
        dugme.style="font-size: 18px;margin: 0 auto;margin-top:10px;";
        dugme.setAttribute('ID','kupitbtn');
        document.getElementById('dugmici').appendChild(dugme);
      }
      if(localStorage['uloga']=="Admin"){
        var dugme = document.createElement('button');
        dugme.className="orangebutton";
        dugme.innerText = "Izmeni";
        dugme.setAttribute('data-IDProjekcije',odg.id);
        dugme.style="font-size: 18px;margin: 0 auto;margin-top:10px;";
        document.getElementById('dugmici').appendChild(dugme);

        dugme = document.createElement('button');
        dugme.className="redbtn";
        dugme.innerText = "Obrisi";
        dugme.setAttribute('data-IDProjekcije',odg.id);
        dugme.style="font-size: 18px;margin: 0 auto;margin-top:10px;";
        document.getElementById('dugmici').appendChild(dugme);
      }
      if(Object.keys(odg).length==0){
        window.location.href="index.html";
      }
      $("#kupitbtn").on('click',function(data){
        if(localStorage['status']=="false"){
          localStorage['poruka']="red|Ulogujte se da bi ste kupili kartu!";
          window.location.href="index.html";
        }
      });
});
