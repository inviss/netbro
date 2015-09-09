function openWin( winName ) {  
   var blnCookie    = getCookie( winName );  
   var obj = eval( "window." + winName );  
   if( !blnCookie ) {  
       obj.style.display = "block";  
   }  
}  
  
// 창닫기  
function closeWin(Id) {   
   setCookie( Id, "end" , 1);   

}  
function closeWinAt00(winName, expiredays) {   
   setCookieAt00( winName, "done" , expiredays);   
   var obj = eval( "window." + winName );  
   obj.style.display = "none";  
}  
  
// 쿠키 가져오기  
function getCookie( name ) {  
	
   var nameOfCookie = name + "=";  
   var x = 0;  
   var cookieData = document.cookie;
   var start = cookieData.indexOf(name);
   var cValue = '';
   if(start != -1){
        start += name.length;
        var end = cookieData.indexOf(';', start);
        if(end == -1)end = cookieData.length;
        cValue = cookieData.substring(start, end);
   }
   return unescape(cValue);
}  
  
  
// 24시간 기준 쿠키 설정하기  
// expiredays 후의 클릭한 시간까지 쿠키 설정  
function setCookie( name, value, expiredays ) {   
	var todayDate = new Date();
    var m = (23 - todayDate.getHours()) * 60;
    var s = (m + (59 - todayDate.getMinutes())) * 60;
    var ms = (s + (59 - todayDate.getSeconds())) * 1000;
    var expireTime = todayDate.getTime() +  ms;
    todayDate.setTime(expireTime);
    cookies = name + '=' + escape(value) + '; path=/ '; 
    cookies += ';expires=' + todayDate.toGMTString() + ';';
    document.cookie = cookies;
    console.log('####################'+document.cookie );
}  
  
// 00:00 시 기준 쿠키 설정하기  
// expiredays 의 새벽  00:00:00 까지 쿠키 설정  
function setCookieAt00( name, value, expiredays ) {   
    var todayDate = new Date();   
    todayDate = new Date(parseInt(todayDate.getTime() / 86400000) * 86400000 + 54000000);  
    if ( todayDate > new Date() )  
    {  
    expiredays = expiredays - 1;  
    }  
    todayDate.setDate( todayDate.getDate() + expiredays );   
     document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";"   
  }  
  