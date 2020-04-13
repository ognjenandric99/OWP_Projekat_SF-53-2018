var url_string = window.location.href;
var url = new URL(url_string);
var id = url.searchParams.get("id");
localStorage['odabranaSedista']="";
var params = {
    'action' : 'kupiKartuInfo',
    'idProjekcije' : id
}
$.post('KorisnikServlet',params,function(data){
    var odg = JSON.parse(data);
    if(odg.status){
      var k = odg.info;
      $("#film_title").text(k.nazivFilma);
      $("#tipProjekcije").text(k.tipProjekcije);
      $("#termin").text(k.termin);
      $("#sala").text(k.nazivSale);
      $("#cenaKarte").text(k.cenaKarte);
      if(k.slobodnaSedista.length<=0){
        localStorage['poruka']="red|Nema slobodnih sedista za tu projekciju";
        window.location.href="index.html";
      }
      
      var brojacZaBreakRow = 0;
      console.log(k.slobodnaSedista);
      for(i=0;i<parseInt(k.maxSedista);i++){
    	var tipSedista = "zauzetoSediste";
    	if(k.slobodnaSedista.includes(i+1+"")){
    		tipSedista = "slobodnoSediste";
    	}
    	else{
    		
    	}
        document.getElementById('sedista').value=k.slobodnaSedista[0];
        var o = document.createElement('div');
        o.className = "sedisteDiv "+tipSedista+" col-sm-1";
        o.setAttribute('value',i+1);
        o.innerText= i+1;
        document.getElementById('sedista').appendChild(o);
        
        brojacZaBreakRow++;
        if(brojacZaBreakRow==10){
        	brojacZaBreakRow= 0;
        	var br = document.createElement('div');
        	br.className="w-100";
        	document.getElementById('sedista').appendChild(br);
        }
        
      }
      $("#ukupnaCena").text("0");
      $(".slobodnoSediste").on('click',function(){
    	  
    	  var broj = this.getAttribute('value');
    	  var s = localStorage['odabranaSedista'];
    	  if(s==undefined || s==null){
    		  localStorage['odabranaSedista']=[broj];
    		  this.classList.toggle("pickovanoSediste");
    	  }
    	  else{
    		  
    		  s = s.split(";");
    		  if(s.includes(broj)){
    			  novaLista = [];
        		  for(z=0;z<s.length;z++){
        			  if(s[z]!=broj){
        				  novaLista.push(s[z]);
        			  }
        		  }
        		  s=novaLista;
        		  this.classList.toggle("pickovanoSediste");
    		  }
    		  else{
    			  console.log("Broj : "+broj);
    			  console.log("S : "+s);
    			  console.log("S.length : "+s.length);
    			  var visemanje = false;
    			  for(sk=0;sk<s.length;sk++){
    				  console.log("poredi se : "+s[sk]+" i : "+broj);
    				  if(parseInt(s[sk])+1==parseInt(broj) || parseInt(s[sk])-1==parseInt(broj)){
    					  visemanje=true;
    				  }
    			  }
    			  if(visemanje){
    				  s.push(broj);
    				  this.classList.toggle("pickovanoSediste");
    			  }
    			  
    			  else{
    				  if(s[0]==""){
    					  s[0]=broj;
    					  this.classList.toggle("pickovanoSediste");
    				  }
    				  else{
    					  pushNotification("red","Sedista moraju biti jedno do drugog.");
    				  }
    			  }
    		  }
    		  localStorage['odabranaSedista']=s.join(";");
    	  }
    	  console.log(localStorage['odabranaSedista']);
    	  if(localStorage['odabranaSedista']!=""){
    		  $("#ukupnaCena").text(localStorage['odabranaSedista'].split(";").length*parseInt(k.cenaKarte));
    	  }
    	  else{
    		  $("#ukupnaCena").text("0");
    	  }
      })
    }
    else{
      localStorage['poruka']="red|"+odg.message;
      window.location.href="index.html";
    }
});

$("#kupibtn").on('click',function(){
  var sedista = localStorage['odabranaSedista'];
  if(sedista==undefined || sedista==null || sedista==""){
	  pushNotification('red',"Morate da odaberete barem jedno sediste");
  }
  else{
	sedista = sedista.split(";");
    var status = true;
    for(i=0;i<sedista.length;i++){
      if(sedista[i+1]){
        if(parseInt(sedista[i])+1!=parseInt(sedista[i+1]) && parseInt(sedista[i])!=parseInt(sedista[i+1])-1){
          status=false;
        }
      }
    }
    if(status){
      var ajdi = id;
      var odabrana_sedista = localStorage['odabranaSedista'];
      var params = {
        'action' : 'kupiKartu',
        'id' : ajdi,
        'odabrana_sedista' : odabrana_sedista
      }
      $.post('KorisnikServlet',params,function(data){
        var odg = JSON.parse(data);
        if(odg.status){
          localStorage['poruka'] = "green|"+odg.message;
          window.location.href="index.html";
        }
        else{
          pushNotification('red',odg.message);
        }
      });
    }
    else{
      pushNotification('red','Sedista moraju biti jedno do drugog');
    }
  }
})
