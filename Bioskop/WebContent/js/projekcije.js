var params = {
  'action': "ucitajProjekcijeZaDanas"
}
$.post('ProjekcijeServlet',params,function(data){
  var odg = JSON.parse(data);
  if(odg.status){
    for(i=0;i<odg.listaProjekcija.length;i++){
      var p = odg.listaProjekcija[i];
      var tr = document.createElement('tr');
      tr.setAttribute('data-idProjekcije',p.id_projekcije);
      var td1 = document.createElement('td');
      td1.innerText = p.naziv_filma;
      td1.setAttribute('data-idFilma',p.id_filma);
      td1.className="proj_film_link";
      tr.appendChild(td1);

      var td2 = document.createElement('td');
      td2.innerText = p.terminProjekcije;
      tr.appendChild(td2);

      var td3 = document.createElement('td');
      td3.innerText = p.naziv_sale;
      tr.appendChild(td3);

      var td4 = document.createElement('td');
      td4.innerText = p.tip_projekcije;
      tr.appendChild(td4);

      var td5 = document.createElement('td');
      td5.innerText = p.cena;
      tr.appendChild(td5);

      document.getElementById('tabelaProjekcija').appendChild(tr);
    }
  }
  else{
    $("#tabelaProjekcija").hide();
    pushNotification('red','Za danasnji dan, nema projekcija.');
  }
})
