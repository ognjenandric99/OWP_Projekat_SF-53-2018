var params = {
  "action":"adminPanelInfo"
}
$.post('AdminServlet',params,function(data){
  var odg = JSON.parse(data);
  if(odg.status){
    pushNotification('green',odg.message);
    for(i=0;i<odg.listaFilmova.length;i++){
      var f = odg.listaFilmova[i];
      var o = document.createElement('option');
      o.innerText = f.Naziv;
      o.setAttribute('data-IDFilma',f.ID);
      document.getElementById('filmSelect').appendChild(o);
    }
    for(i=0;i<odg.listaSala.length;i++){
      var f = odg.listaSala[i];
      var o = document.createElement('option');
      o.innerText = f.Naziv;
      o.setAttribute('data-IDSale',f.ID);
      document.getElementById('salaSelect').appendChild(o);
      document.getElementById('salaSelect').getElementsByTagName('option')[0].click();
    }
  }
  else{
    window.location.href="index.html";
  }
});

$("#salaSelect").on('change',function(data){
  var IDSale = $("#salaSelect option:selected").attr('data-IDSale');
  alert(IDSale);
});
