var params = {
    'action' : "getSessionInfo"
}
$.post('KorisnikServlet',params,function(data){
    var odg = JSON.parse(data);
    localStorage['uloga']= odg.uloga;
    localStorage['status'] = odg.status;
    localStorage['username'] = odg.username;
    localStorage['ajdi']=odg.ajdi;
    if(odg.status && odg.uloga=="obicanKorisnik" && odg.username!=null){
      //Ulogovan je kao korisnik
      $("#nav_filmovi").show();
      $("#nav_repertoar").show();
      $("#nav_profil").show();
      $("#nav_logout").show();
    }
    if(odg.status && odg.uloga=="Admin" && odg.username!=null){
      //Ulogovan kao admin
      $(".nav-opcija").each(function(){this.style.display="inline"});
      $("#nav_login").hide();
    }
    if(!odg.status){
      $("#nav_filmovi").show();
      $("#nav_repertoar").show();
      $("#nav_login").show();
      localStorage['uloga']= null;
      localStorage['status'] = false;
      localStorage['username'] = null;
      localStorage['ajdi']=null;
    }
    if(odg.status){
      document.getElementById('nav_profil').href="prikazKorisnika.html?id="+odg.ajdi;
    }
});

if(localStorage['poruka']!=undefined){
  var poruka = localStorage['poruka'].split("|");
  var boja = poruka[0];
  var msg = poruka[1];
  pushNotification(boja,msg);
  localStorage.removeItem('poruka');
}
