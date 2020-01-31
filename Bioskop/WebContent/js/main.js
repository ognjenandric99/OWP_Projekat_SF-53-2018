function pushNotification(type,message){
  var color = "";
  if(type=="green"){
    color = 'sucnotif';
  }
  if(type=="red"){
    color = 'dangnotif';
    var audio = new Audio('../../audio/tick_bad.mp3');
    audio.play();
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