/* 
  function getRealOffsetTop(el) {
     return el ? el.offsetTop + getRealOffsetTop(el.offsetParent) : 0;
   }

  function getRealOffsetLeft(el) {
     return el ? el.offsetLeft + getRealOffsetLeft(el.offsetParent) : 0;
   }

  var obj;
   function roadMenu(el, pos) {

    var top = getRealOffsetTop(el) + el.offsetHeight;
     var left = getRealOffsetLeft(el);
     var sub = document.getElementById("sub");
     sub.style.left = parseInt(el.offsetWidth / 2)+left+"px";
     sub.style.top = top-50+"px";
     sub.style.display = "block";
     if(obj) obj.style.display = "none";
     obj = document.getElementById("lay_"+pos);
     obj.style.display = "block";
     obj.style.top=top-38+"px";
     obj.style.left=100+"px";
   }

//- 롤오버서브메뉴 
 */
function MM_preloadImages() {
	var d = document; if(d.images){ if(!d.MM_p) d.MM_p = new Array();
	var i,j = d.MM_p.length,a = MM_preloadImages.arguments; for(i = 0; i < a.length; i++)
		if (a[i].indexOf("#") != 0){ d.MM_p[j] = new Image; d.MM_p[j++].src = a[i];}}
}

function MM_swapImgRestore() { 
	var i,x,a=document.MM_sr; for(i = 0; a && i < a.length&&(x = a[i]) && x.oSrc;i++) x.src = x.oSrc;
}

function MM_findObj(n, d) { 
	var p,i,x;  if(!d) d = document; if((p = n.indexOf("?")) > 0 && parent.frames.length) {
		d = parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	if(!(x = d[n]) && d.all) x = d.all[n]; for (i = 0; !x && i < d.forms.length;i++) x = d.forms[i][n];
	for( i= 0; !x && d.layers && i < d.layers.length;i++) x = MM_findObj(n,d.layers[i].document);
	if(!x && d.getElementById) x = d.getElementById(n); return x;
}

function MM_swapImage() {
	var i,j = 0,x,a = MM_swapImage.arguments; document.MM_sr = new Array; for(i = 0; i < (a.length-2); i+=3)
		if ((x = MM_findObj(a[i])) != null){document.MM_sr[j++] = x; if(!x.oSrc) x.oSrc = x.src; x.src = a[i+2];}
}

function flashCreateObject0( id, name, width, height ,srcValue) {
	document.write('<object classid="clsid:'+id+'" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" width="'+width+'" height="'+height+'">');
	document.write('<param name="'+name+'" value="'+srcValue+'">');
	document.write('<param name="quality" value="high">');
	document.write('<param name="wmode" value="transparent">');
	document.write('<embed src="'+srcValue+'" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="'+width+'" height="'+height+'" wmode="transparent"></embed>');
	document.write('</object>');
}

function winopen_pop_read() {		
	window.open("/movie/pop.html","","width=732,height=538");
}	

function openWinCenter(url, wname, wopt) {
	var newopt = "", wHeight = 0, wWidth = 0;
	if (wopt != undefined) {
		var woptlist = wopt.replace(/ /g, "").split(",");
		for (var i in woptlist) {
			if (woptlist[i].match(/^height=/i)) {
				wHeight = parseInt(woptlist[i].substr(7),10);
				if (!isNaN(wHeight)) newopt += "top=" + Math.floor((screen.availHeight - wHeight) / 2) + ",";
			}
			if (woptlist[i].match(/^width=/i)) {
				wWidth = parseInt(woptlist[i].substr(6),10);
				if (!isNaN(wWidth)) newopt += "left=" + Math.floor((screen.availWidth - wWidth) / 2) + ",";
			}
		}
	}
	return window.open(url, wname, newopt + wopt);
} 

function closewin()
{ 
	openWinCenter.close();
}

//* topmenu *//

function JS_mainTab(menuId){
	var nav = document.getElementById("main-tab"+menuId);
	for(i = 1; i <= 4; i++) {
		document.getElementById("main-tab"+i).style.display = "none";
	}
	nav.style.display = "block";
}

function JS_tabClick(imgId) {
	var nav = document.getElementById("img"+imgId);
	//alert(nav.src);
	if(nav.src.substring(nav.src.length-7) != "_on.gif"){
		nav.src = nav.src.replace("_off.gif", "_on.gif");
		return;
	}

	if(nav.src.substring(nav.src.length-8) != "_off.gif"){
		nav.src = nav.src.replace("_on.gif", "_off.gif");
		return;
	}
}

function JS_tabOver(imgId) {
	var nav = document.getElementById("img"+imgId);
	//alert(nav.src);
	if(nav.src.substring(nav.src.length-7) != "_on.gif")
		nav.src = nav.src.replace("_off.gif", "_on.gif");
}

function JS_tabOut(imgId) {
	var nav = document.getElementById("img"+imgId);
	nav.src = nav.src.replace("_on.gif", "_off.gif");
}

function JS_contentSaveOver(code) {
	var nav = document.getElementById(code);
	nav.className = "part contentOverColor";
}
function JS_contentSaveOut(code) {
	var nav = document.getElementById(code);
	nav.className = "part";
}


/* IMG 메뉴 스크립트 */
function initNavigation(seq) {
	nav = document.getElementById("topmenu");
	nav.menu = new Array();
	nav.current = null;
	nav.menuseq = 0;
	navLen = nav.childNodes.length;

	allA = nav.getElementsByTagName("LI");
	for(k = 0; k < allA.length; k++) {
		allA.item(k).onmouseover = allA.item(k).onfocus = function () {
			nav.isOver = true;
		}
		allA.item(k).onmouseout = allA.item(k).onblur = function () {
			nav.isOver = false;
			setTimeout(function () {
				if (nav.isOver == false) {
					if(nav.menu[seq])
						nav.menu[seq].onmouseover();
					else if(nav.current){
						if(nav.current.submenu)
							nav.current.submenu.style.visibility = "hidden";
						nav.current = null;
					}
				}
			}, 3000);
		}
	}

	// 1depth
	for (i = 0; i < navLen; i++) {
		navItem = nav.childNodes.item(i);
		if (navItem.tagName != "LI")
			continue;

		navAnchor = navItem.getElementsByTagName("a").item(0);
		navAnchor.submenu = navItem.getElementsByTagName("ul").item(0);

		navAnchor.onmouseover = navAnchor.onfocus = function () {
			
			if (nav.current) {
				
				if (nav.current.submenu)
				
					nav.current.submenu.style.visibility = "hidden";
				
				nav.current = null;
			
			}
			
			if (nav.current != this) {
				
				if (this.submenu) {
					
					this.submenu.style.visibility = "visible";
				
				}
				
				nav.current = this;
			
			}
			
			nav.isOver = true;
		
		}
		
		nav.menuseq++;
		nav.menu[nav.menuseq] = navAnchor;
	
	}
	
	if (nav.menu[seq])
		nav.menu[seq].onmouseover();

}

//Adijust Layout
window.onload = function() {
	
	window.setInterval(function() {
		
		bodyEI = document.getElementById("body");
		subEI = document.getElementById("sub");
		
		if(!bodyEI || !subEI) {
			
			return;
			
			if(bodyEI.offsetHeight < subEI.offsetHeight + 25) {
				
				bodyEI.style.height = subEI.offsetHeight + "px";
			
			}
		
		}
		
	}, 200);
	
}

function initBoardList() {
	boardPager = document.getElementById("board-pager");
	
	if(boardPager)
		boardPager.getElementByTagName("li").item(2).style.borderStyle = "none";

}

function initBoardViewPhoto() {
	
	imageContainer = document.getElementById("attach-photo");
	
	if(imageContainer) {
		
		imgEI = imageContainer.getElementsByTagName("img");
		
		for(i = 0; i < imgEI.length; i++) {
			
			if(imagesContainer.offsetWidth < imgEI.item(i).offsetWidth) {
				
				imgEI.item(i).style.width = imageContainer.offsetWidth;
			
			}
		
		}
	
	}

}

//popup layer
var cc=0
function show(id) {

	document.getElementById(id).style.display = "block";



}

function hide(id) {

	document.getElementById(id).style.display = "none";

}

function v1h23(){
	show('dbox1')
	hide('dbox2') 
	hide('dbox3') 
}

function v2h13(){
	show('dbox2')
	hide('dbox1') 
	hide('dbox3') 
}
function v3h13(){
	show('dbox2')
	hide('dbox1') 
	hide('dbox3') 
}

function v3h12(){
	show('dbox3')
	hide('dbox1') 
	hide('dbox2') 
}

function v1h23(){
	show('dbox1')
	hide('dbox2') 
	hide('dbox3') 
}

function v1h2Tra(){
	show('trabox1')
	hide('trabox2')  
}

function v2h1Tra(){
	show('trabox2')
	hide('trabox1') 
}





function v1h2(){
	show('forPeriod')
	hide('forYear') 
}

function v2h1(){
	show('forYear')
	hide('forPeriod') 
}

/* 화면 변경 v1h23 기능 동적화 
 * select : 선택한 탭, 
 * end : 탭의 총갯수 */

function showandhide(select){
var tabcount = $jq(".tabcover2 li").length;
	
	var Ids = new Array();
	var i = 0
	
	$jq('.westpanel section').each(function(index) {
		
			Ids[i]=$jq(this).attr("id");
		
		i++;  
		
	  }); 
	
	for (i = 0; i < tabcount; i++){
		
		if(Ids[i] == 'section'+select){
			
			show(Ids[i]);
			
		}else{
			
			hide(Ids[i]);
		
		}
		
		}
		
	}

/* 화면탭 활성화
 * Id : 선택한 탭
 * end : 탭의 총갯수*/
function viewOnOff(Id,end){
	
	var tabcount = $jq(".tabcover2 li").length;
	
	var Ids = new Array();
	
	var i = 0
	
	console.log("Ids"+Id);
	$jq('.tabcover2 li').each(function(index) {
		
			Ids[i]=$jq(this).attr("id");
		
		i++;
	 
	});

	for(i = 0; i < tabcount; i++){
		
		console.log("Ids  "+Ids[i])
		
		if(Ids[i] == 'onoff'+Id){
			
			document.getElementById(Ids[i]).className='on';
			
		}else{
			
			document.getElementById(Ids[i]).className='off';
		
		}
		
		}
	
}




//form
function change(obj){
	if(obj.value == "id") {
		obj.value = "";
		obj.style.color = "#000";
		
	}
	
	else if(obj.value == "") {
		obj.value = "id";
		obj.style.color = "#767676";
		
	}
	
}

function change2(passobj){
	
	if(passobj.value == "password") {
		
		passobj.value ="";
		passobj.style.color="#000";
		
	}else if(passobj.value == "") {
		
		passobj.value = "password";
		passobj.style.color = "#767676";
		
	}
	
}


//-> input layer pop close

isIE = document.all;
isNN = !document.all && document.getElementById;
isN4 = document.layers;
isHot = false;

function ddInit(e){
	topDog = isIE ? "BODY" : "HTML";
	whichDog = isIE ? document.all.theLayer : document.getElementById("theLayer"); 
	hotDog = isIE ? event.srcElement : e.target; 
	
	while (hotDog.id != "titleBar"&&hotDog.tagName != topDog){
	
		hotDog = isIE ? hotDog.parentElement : hotDog.parentNode;
	
	} 
	
	if (hotDog.id == "titleBar"){
		
		offsetx = isIE ? event.clientX : e.clientX;
		offsety = isIE ? event.clientY : e.clientY;
		nowX = parseInt(whichDog.style.left);
		nowY = parseInt(whichDog.style.top);
		ddEnabled = true;
		document.onmousemove = dd;
	
	}
	
}

function dd(e){
	
	if (!ddEnabled) return;
	
	whichDog.style.left=isIE ? nowX+event.clientX-offsetx : nowX+e.clientX-offsetx;
	whichDog.style.top=isIE ? nowY+event.clientY-offsety : nowY+e.clientY-offsety;
	
	return false; 
	
}

function ddN4(whatDog){
	
	if (!isN4) return;
	
	N4 = eval(whatDog);
	N4.captureEvents(Event.MOUSEDOWN|Event.MOUSEUP);
	
	N4.onmousedown=function(e){
		
		N4.captureEvents(Event.MOUSEMOVE);
		N4x = e.x;
		N4y = e.y;
		
	}
	
	N4.onmousemove=function(e){
		
		if (isHot){
			
			N4.moveBy(e.x-N4x,e.y-N4y);
			
			return false;
			
		}
		
	}
	
	N4.onmouseup=function(){
		
		N4.releaseEvents(Event.MOUSEMOVE);
		
	}
	
}


function hideMe(){
	
	if (isIE || isNN) whichDog.style.visibility = "hidden";
	else if (isN4) document.theLayer.visibility = "hide";
	
}

function showMe(){
	
	if (isIE||isNN) whichDog.style.visibility = "visible";
	
	else if (isN4) document.theLayer.visibility = "show";
	
}

document.onmousedown = ddInit;
document.onmouseup = Function("ddEnabled=false");

function notice_setCookie( name, value, expiredays )
{
	var todayDate = new Date();
	todayDate.setDate( todayDate.getDate() + expiredays );
	document.cookie = name + '=' + escape( value ) + '; path=/; expires=' + todayDate.toGMTString() + ';'
	return;
}

<!--
function na_open_window(name, url, left, top, width, height, toolbar, menubar, statusbar, scrollbar, resizable)
{
	toolbar_str = toolbar ? 'yes' : 'no';
	menubar_str = menubar ? 'yes' : 'no';
	statusbar_str = statusbar ? 'yes' : 'no';
	scrollbar_str = scrollbar ? 'yes' : 'no';
	resizable_str = resizable ? 'yes' : 'no';
	window.open(url, name, 'left='+left+',top='+top+',width='+width+',height='+height+',toolbar='+toolbar_str+',menubar='+menubar_str+',status='+statusbar_str+',scrollbars='+scrollbar_str+',resizable='+resizable_str);
}

//<--



// --> tree

jQuery(function($){
/*tree 구현 스크립트 */
var tree_menu = $('#tree_menu');
var icon_open = '/images/tree_open.gif';
var icon_close = '/images/tree_close.gif';

tree_menu.find('li:has("ul")').prepend('<a href="#" class="control"><img src="' + icon_close + '" /></a> ');
tree_menu.find('li:last-child').addClass('end');

$('.control').click(function(){
	var temp_el = $(this).parent().find('>ul');
	console.log("temp_el.css('display') " +temp_el.css('display'));
	if (temp_el.css('display') == 'none'){
		
		temp_el.slideDown(100);
		$(this).find('img').attr('src', icon_close);
		
		return false;
		
	} else {
		
		temp_el.slideUp(100);
		$(this).find('img').attr('src', icon_open);
		
		return false;
		
	}
	
});

function tree_init(status){
	
	if (status == 'close'){
		
		tree_menu.find('ul').hide();
		$('a.control').find('img').attr('src', icon_open);
		
	} else if (status == 'open'){
		
		tree_menu.find('ul').show();
		$('a.control').find('img').attr('src', icon_close);
		
	}
	
}

tree_init('close');

});


Date.prototype.format = function(f) {
	
    if (!this.valueOf()) return " ";

    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    
    var d = this;

    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
    	
        switch ($1) {
        
            case "yyyy" : return d.getFullYear();
            case "yy" : return (d.getFullYear() % 1000).zf(2);
            case "MM" : return (d.getMonth() + 1).zf(2);
            case "dd" : return d.getDate().zf(2);
            case "E" : return weekName[d.getDay()];
            case "HH" : return d.getHours().zf(2);
            case "hh" : return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm" : return d.getMinutes().zf(2);
            case "ss" : return d.getSeconds().zf(2);
            case "a/p" : return d.getHours() < 12 ? "오전" : "오후";
            default : return $1;
            
        }
        
    });
    
};