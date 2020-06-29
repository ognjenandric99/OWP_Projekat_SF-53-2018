function pushNotification(type,message){
  var color = "";
  if(type=="green"){
    color = 'sucnotif';
  }
  if(type=="red"){
    color = 'dangnotif';
  }
  var notif = document.createElement('div');
  notif.className="notification sakrivennotif "+color;
  notif.innerText=message;

  document.getElementById('notificationdiv').append(notif);
  setTimeout(function(){notif.classList.toggle('sakrivennotif')},100);

  var t1 = setTimeout(function(){notif.classList.toggle('sakrivennotif');setTimeout(function(){notif.remove();},500);},15000);
  notif.onclick=function(){
    clearTimeout(t1);
    notif.classList.toggle('sakrivennotif');
    setTimeout(function(){notif.remove();},500);
  }
}





$(document).ready(function(){
  //
  if($("#nav_logout")){
    $("#nav_logout").on('click',function(){
      var params = {
    'action' : "logOut"
      }
      $.post('KorisnikServlet',params,function(data){
          var odg = JSON.parse(data);
          console.log(odg);
          if(odg.status){
            window.location.href="index.html";
          }
          else{
            pushNotification('red',odg.message);
          }
      });
    })
  }
  
  $("#nav_login_div_loginbtn").on('click',function(){
		var user = $("#nav_login_div_input_username").val();
		var pass1 = $("#nav_login_div_input_password").val();

		if(user.length>0 && pass1.length>0){
			var params = {
					action: "login",
					username : user,
					password : pass1
				}
				
				$.post('KorisnikServlet', params, function(data) {
					var odg = JSON.parse(data);
					if(odg.status){
						window.location.href="index.html";
					}
					else{
						pushNotification("red",odg.message);
					}

			});
		}
		else{
			pushNotification("red","Proverite unos!");
		}
	});

  $("#nav_reg_div_regbtn").on('click',function(){
		var user = $("#nav_reg_div_input_username").val();
		var pass1 = $("#nav_reg_div_input_password").val();
		var pass2 = $("#nav_reg_div_input_password2").val();

		if(user.length>0 && pass1==pass2 && pass1.length>0){
			var params = {
					action: "registracija",
					username : user,
					password : pass1
				}
				// kontrola toka se račva na 2 grane
				$.post('KorisnikServlet', params, function(data) { // u posebnoj programskoj niti se šalje (asinhroni) zahtev
					// tek kada stigne odgovor izvršiće se ova anonimna funkcija
					var odg = JSON.parse(data);
					if(odg.status){
						window.location.href="index.html";
					}
					else{
						pushNotification('red',odg.message);
					}

			});
		}
		else{
			pushNotification('red',"Proverite unos");
			
		}
	});

  //
})
