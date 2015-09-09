/**
 * 관리화면- 카테고리 select box link
 * @param url
 */
function linkView(url){

	location.href =url

}

/**
 * 카테고리 트리 구현부
 * 각노드의 id값을 받아서 각각의 노드에 hidden,show 함수를 적용한다.
 * display의 속성값에 따라서 img의 값을 동적으로 변경한다.
 */
function tree_call(Id,url){

	var tree_menu = $jq('#'+Id);
	var icon_open = url + '/images/tree_open.gif';
	var icon_close = url + '/images/tree_close.gif';

	tree_menu.find('li:has("ul")').prepend('<a  class="control"><img src="' + icon_close + '" /></a> ');
	tree_menu.find('li:last-child').addClass('end');
	//console.log('############tree_menu   '+ tree_menu.find('li:last-child'));
	//입력된 노드id를 눌렀을때 이미지를 display의 속성값에 따라 변경한다.
	$jq('#'+Id+' .control').click(function(){

		var temp_el = $jq(this).parent().find('>ul');

		if (temp_el.css('display') == 'none'){

			temp_el.slideDown(100);
			$jq(this).find('img').attr('src', icon_close);

		} else if(temp_el.css('display') == undefined){
			//최초 입력시에는 무조건 close이미지를 넣는다.
			temp_el.slideDown(100);
			$jq(this).find('img').attr('src', icon_close);

		}else {

			temp_el.slideUp(100);
			$jq(this).find('img').attr('src', icon_open);

		}

	});

};



/**
 * 탭 닫기함수 구현부
 */
function tabClose(key,totalCount){

	//section,tab1의 onff 요소 삭제
	$jq('#onoff'+key).remove();

	$jq('#section'+key).remove();
	//탭에 현재 있는 탭의 갯수를 구한다.
	var tabcount = $jq(".tabcover2 li").length;
	var Ids = new Array();
	var i = 0

	$jq('.westpanel section').each(function(index) {

		Ids[i]=$jq(this).attr("id");

		i++;  

	}); 

	showandhide(tabcount);
	viewOnOff(tabcount,totalCount);

};

/* 엔터키 입력시 검색 함수*/

function enterKey(e){

	if(e.keyCode == 13){

		return true;
	}else{

		e.keyCode == 0;
		return false;

	}

}


/**
 * 카테고리 화면 초기화 함수 =id 초기화
 * @param categoryId
 */
function changeCategoryId(categoryId){

	$jq('#categoryId').val(categoryId)

}

/**
 * 카테고리 화면 초기화 함수 =업데이트 INPUT 값 초기화
 * @param categoryId
 */
function changeCategoryNm(categoryNm){

	$jq('#categoryNm2').val(categoryNm)

	if(categoryNm==undefined||categoryNm==""){

		$jq('.cateNm').val("전체")

	}else{

		$jq('.cateNm').val(categoryNm)	

	}

	$jq('.cateNm2').val(categoryNm)

}


/**
 * 카테고리 화면 초기화 함수 =업데이트 INPUT 값 초기화
 * @param categoryId
 */
function changeEpisodeNm(episodeNm){

	if($jq('#episodeNm2').val()==undefined ||$jq('#episodeNm2').val()==""||$jq('#episodeNm2').val()==episodeNm){

		$jq('#episodeNm2').val(episodeNm)

	}else{

		$jq('#episodeNm2').val("")	

	}

}


/**
 * 선택한 노드강조표현
 */
var OBJECT_BOLD = null;
function changeBold(id){

	var style = $jq('#'+id).attr("style");
//	console.log('###########before style '+style)
	if((OBJECT_BOLD == id && style == "")||(OBJECT_BOLD == id && style == undefined)){

		$jq('#'+id).attr("style","font-weight : bold")

	}else if(OBJECT_BOLD == id){

		$jq('#'+id).attr("style","")
		$jq('#categoryNm2').val("")
		$jq('.cateNm').val("전체")

	}else if(OBJECT_BOLD != id){

		$jq('#'+id).attr("style"," font-weight : bold")
		$jq('#'+OBJECT_BOLD).attr("style","")
		$jq('#'+OBJECT_BOLD).val("")
		OBJECT_BOLD=id;

	}
//	console.log('###########after style '+$jq('#'+id).attr("style"))
}

/**
 *  클릭시 해당 레이어를 보여주는 함수 보여주는 함수이외의 창은 닫는다.
 */
var showLayer =null;
function showHide(id){

	var newId =id.replace(" ","");

	var style = $jq('#'+newId).css("display");

	if((showLayer == newId && style == "")|| (showLayer == newId && style == undefined)){

		$jq('#'+newId).attr("style","posion : relative; z-index: 99999;display : block")

	}else if(showLayer == newId || (showLayer == newId+'temp' && style == "none") ){

		var layer1 = document.getElementById(newId); 
		layer1.style.display = 'none'; 
		showLayer=null;
		hideLayer=null;		

	}else if(showLayer != newId){

		$jq('#'+newId).attr("style"," posion : relative; z-index: 99999;display : block")
		$jq('#'+showLayer).attr("style","")
		showLayer = newId;

	}

}
function hide(id){

	var newId =id.replace(" ","");

	var style = $jq('#'+newId).attr("style");

	var layer1 = document.getElementById(id); 
	layer1.style.display = 'none'; 
	showLayer=id+'temp';
	hideLayer=null;	

}

function hideDetailSearch(){

	var layer1 = document.getElementById('detailSearch'); 
	layer1.style.display = 'none'; 
	showLayer=null;
	hideLayer=null;	
}

/**
 *  로그인창(로그인창 외 클릭시 로그인창은 close).
 */
function layer_open(el){
	var temp = $('#' + el);
	var bg = temp.prev().hasClass('bg');    //dimmed 레이어를 감지하기 위한 boolean 변수

	if(bg){

		$('.layer').fadeIn();   //'bg' 클래스가 존재하면 레이어가 나타나고 배경은 dimmed 된다.

	}else{

		temp.fadeIn();

	}

	// 화면의 중앙에 레이어를 띄운다.
	if (temp.outerHeight() < $(document).height() ) temp.css('margin-top', '-'+temp.outerHeight()/2+'px');

	else temp.css('top', '0px');

	if (temp.outerWidth() < $(document).width() ) temp.css('margin-left', '-'+temp.outerWidth()/2+'px');

	else temp.css('left', '0px');

	temp.find('a.cbtn').click(function(e){

		if(bg){

			$('.layer').fadeOut(); //'bg' 클래스가 존재하면 레이어를 사라지게 한다.

		}else{

			temp.fadeOut();

		}

		e.preventDefault();

	});

	$('.layer .bg').click(function(e){  //배경을 클릭하면 레이어를 사라지게 하는 이벤트 핸들러

		$('.layer').fadeOut();
		e.preventDefault();

	});

}              

/**
 * 데이터의 NULL 여부를 체크하는 함수
 * 만약 NULL이라면 해당값을 공백으로 치환한다.
 * @param data
 * @returns
 */
function BlankCheck(data){

	if(data == undefined){

		data=""
	}

	return data;

}

/**
 * 데이터의 NULL 여부를 체크하는 함수
 * 만약 NULL이라면 해당값을 0으로 치환한다.
 * @param data
 * @returns
 */
function BlankToZero(data){

	if(data == undefined){

		data=0

	}

	return data;

}

/**
 * 레이어 이외의 화면을 클릭시 레이어를 사라지게 하는 함수
 * theLayer와 theInputLayer의 경우 다른 화면과 반대로 작용해야하는 점이 있음으로
 * 따로 if절로 구분하여 구현된다.
 */


var hideLayer = null;
var beforeId =''
	var buttonYn = 'N'
		var nonUseButton = 'N'
			/*
			 * 방식 변경 오류 남아있음 추후 해결샇
			 */
			$(document).ready(function(){

				$(document).mousedown(function(e){

					var target = (e)?e.target:window.event.srcElement; 

					$('#theLayer').each(function(){

						var i=0;
						if( $('#theLayer').css('display') == 'block' )
						{

							if( $(this).css('display') == 'block' )
							{
								var l_position = $(this).offset();
								l_position.right = parseInt(l_position.left) + ($(this).width());
								l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());


								if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
										&& ( l_position.top <= e.pageY && e.pageY <= l_position.bottom ) )
								{
									console.log( 'popup in click' );
								}
								else
								{
									console.log( 'popup out click' );
									if(buttonClickYn(target.id) == false){
										showHide('theLayer');
									}
								}
							}
						}
					});


					$('#detailSearch').each(function(){

						if( $(this).css('display') == 'block' )
						{
							var l_position = $(this).offset();
							l_position.right = parseInt(l_position.left) + ($(this).width());
							l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());



							if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
									&& ( l_position.top <= e.pageY && e.pageY <= l_position.bottom  ))
							{
								console.log( 'popup in click' );
								beforeId = target.id;
							}
							else
							{
								if( (beforeId =='from' ||  beforeId =='to')&& target.id==''){
									console.log( 'from  ' + beforeId);
								}else{
									console.log( 'from  ' + beforeId);
									console.log( 'popup out click' );
									if(buttonClickYn(target.id) == false){
										showHide('detailSearch');
									}
								}
							}
						}
					});

					$('#theInputLayer').each(function(){

						if( $(this).css('display') == 'block' )
						{
							var l_position = $(this).offset();
							l_position.right = parseInt(l_position.left) + ($(this).width());
							l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());


							if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
									&& ( l_position.top <= e.pageY && e.pageY <= l_position.bottom ) )
							{
								console.log( 'popup in click' );
							}
							else
							{
								console.log( 'popup out click' );
								if(buttonClickYn(target.id) == false){
									showHide('theInputLayer');
								}
							}
						}
					});


					$('#layer1').each(function(){

						if( $(this).css('display') == 'block' )
						{
							var l_position = $(this).offset();
							l_position.right = parseInt(l_position.left) + ($(this).width());
							l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());


							if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
									&& ( l_position.top <= e.pageY && e.pageY <= l_position.bottom ) )
							{
								console.log( 'popup in click' );
							}
							else
							{
								console.log('###########target.id   '+target.id);
								if(buttonClickYn(target.id) == false){
									showHide('layer1');
								}
							}
						}
					});

					$('#layer6').each(function(){

						if( $(this).css('display') == 'block' )
						{
							var l_position = $(this).offset();
							l_position.right = parseInt(l_position.left) + ($(this).width());
							l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());


							if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
									&& ( l_position.top <= e.pageY && e.pageY <= l_position.bottom ) )
							{
								console.log( 'popup in click' );
							}
							else
							{
								console.log('###########target.id   '+target.id);
								if(buttonClickYn(target.id) == false){
									showHide('layer6');
								}
							}
						}
					});

					$('#layer3').each(function(){

						if( $(this).css('display') == 'block' )
						{
							var l_position = $(this).offset();
							l_position.right = parseInt(l_position.left) + ($(this).width());
							l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());


							if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
									&& ( l_position.top <= e.pageY && e.pageY <= l_position.bottom ) )
							{
								console.log( 'popup in click' );
							}
							else
							{
								console.log( 'popup out click' );
								if(buttonClickYn(target.id) == false){
									showHide('layer3');
								}
							}
						}
					});

					$('#layer4').each(function(){

						if( $(this).css('display') == 'block' )
						{
							var l_position = $(this).offset();
							l_position.right = parseInt(l_position.left) + ($(this).width());
							l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());


							if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
									&& ( l_position.top <= e.pageY && e.pageY <= l_position.bottom ) )
							{
								console.log( 'popup in click' );
							}
							else
							{
								console.log( 'popup out click' );
								if(buttonClickYn(target.id) == false){
									showHide('layer4');
								}
							}
						}
					});
					$('#layer5').each(function(){

						if( $(this).css('display') == 'block' )
						{
							var l_position = $(this).offset();
							l_position.right = parseInt(l_position.left) + ($(this).width());
							l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());


							if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
									&& ( l_position.top <= e.pageY && e.pageY <= l_position.bottom ) )
							{
								console.log( 'popup in click' );
							}
							else
							{
								console.log( 'popup out click' );
								if(buttonClickYn(target.id) == false){
									showHide('layer5');
								}
							}
						}
					});


					$('#layer7').each(function(){

						if( $(this).css('display') == 'block' )
						{
							var l_position = $(this).offset();
							l_position.right = parseInt(l_position.left) + ($(this).width());
							l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());


							if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
									&& ( l_position.top <= e.pageY && e.pageY <= l_position.bottom ) )
							{
								console.log( 'popup in click' );
							}
							else
							{
								console.log( 'popup out click' );
								if(buttonClickYn(target.id) == false){
									qcr('pop-layer1');
								}
							}
						}
					});

					$('#layer8').each(function(){

						if( $(this).css('display') == 'block' )
						{
							var l_position = $(this).offset();
							l_position.right = parseInt(l_position.left) + ($(this).width());
							l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());


							if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
									&& ( l_position.top <= e.pageY && e.pageY <= l_position.bottom ) )
							{
								console.log( 'popup in click' );
							}
							else
							{
								console.log( 'popup out click  '+target.id );
								if(buttonClickYn(target.id) == false){
									console.log( '$$$$$$$$$$$$$$$$$$$$$$' );
									qcr('layer8');
								}
							}
						}
					});


					$('#layer9').each(function(){

						if( $(this).css('display') == 'block' )
						{
							var l_position = $(this).offset();
							l_position.right = parseInt(l_position.left) + ($(this).width());
							l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());


							if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
									&& ( l_position.top <= e.pageY && e.pageY <= l_position.bottom ) )
							{
								console.log( 'popup in click' );
							}
							else
							{
								console.log( 'popup out click  '+target.id );
								if(buttonClickYn(target.id) == false){
									console.log( '$$$$$$$$$$$$$$$$$$$$$$' );
									qcr('layer9');
								}
							}
						}
					});


					$('#noticeLayOut').each(function(){
						console.log('####################################'+$('.box12').css('display'));
						if( $('.box12').css('display') == 'block' )
						{
							var l_position = $('.box12').offset();
							l_position.right = parseInt(l_position.left) + ($('.box12').width());
							l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());


							if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
									&& ( l_position.top <= e.pageY && e.pageY <= l_position.bottom ) )
							{
								console.log( 'popup in click' );
							}
							else
							{
								console.log( 'popup out click  '+target.id );
								if(buttonClickYn(target.id) == false){
									console.log( '$$$$$$$$$$$$$$$$$$$$$$' );
									//$('#noticeLayOut').css('display','none');
									//$('.box12').css('display','none');
								}
							}
						}
					});

				}); 
			})


			/*var hideLayer = null;
	document.onclick = function(e){


		var target = (e)?e.target:window.event.srcElement; 
		if(hideLayer == undefined){
			//최초 레이어를 열었을때 그 레이어의 id값을 넣는다.
			hideLayer = showLayer;

		} else if(hideLayer != showLayer){//기존에 저장되어있던 레이어와 새로 오픈한 레이어의 값이 동일하지 않을때 기존레이어를 hidden처리한다.
			//if((showLayer=='null' && hideLayer=='detailSearch') && target.id =='from'||showLayer=='1'){
			if((showLayer==null && hideLayer=='detailSearch' && (target.id =='from' ||target.id =='to'  ))){

				showLayer = hideLayer; 

			}else{

				var layer1 = document.getElementById(hideLayer); 

				layer1.style.display = 'none';
				if(hideLayer != 'detailSearch'){
					hideLayer = showLayer;
				}
			}

		} else {//그이외의 경우..

			if(target.id == undefined || target.id == ""){//태그의ID가 존재하지 않는 부분을 클릭한 경우

				if(target.id != hideLayer){

					//기존에 저장되어있던 레이아웃의 id가 theLayter이거나 therInputLayer인경우
					if((hideLayer == "theLayer" && target.id == "") || (hideLayer == "theInputLayer" && target.id == "") || (hideLayer == "detailSearch" && target.id == "") || (hideLayer == "layer5" && target.id == "")){

						if(hideLayer == "theLayer"){

							if($jq('#theLayer').css('display' == 'block')){

								var position = $jq('#theLayer').offset();

								position.right = parseInt(position.left)+($jq('#theLayer').width());
								position.bottom = parseInt(position.top) + parseInt($('#theLayer').height());

								if((position.left <= e.pageX && e.pageX <= position.right)
										&&(position.top <= e.pageY && e.pageY <= position.bottom)){

								}else{

									var layer1 = document.getElementById(hideLayer); 
									layer1.style.display = 'none'; 
									showLayer=null;
									hideLayer=null;		

								}

							}

						}else if(hideLayer == "theInputLayer"){

							var position = $jq('#theInputLayer').offset();

							position.right = parseInt(position.left)+($jq('#theInputLayer').width());
							position.bottom = parseInt(position.top) + parseInt($('#theInputLayer').height());

							if((position.left <= e.pageX && e.pageX <= position.right)
									&& (position.top <= e.pageY && e.pageY <= position.bottom)){

							}else{

								var layer1 = document.getElementById(hideLayer); 
								layer1.style.display = 'none'; 
								showLayer=null;
								hideLayer=null;	

							}

						}else if(hideLayer == "detailSearch"){

					        if( $(this).css('display') == 'block' )
					        {
					            var l_position = $('#detailSearch').offset();
					            l_position.right = parseInt(l_position.left) + ($(this).width());
					            l_position.bottom = parseInt(l_position.top) + parseInt($(this).height());

					            console.log('###########target.id   '+target.id);

					            if( ( l_position.left <= e.pageX && e.pageX <= l_position.right )
					                && ( l_position.top <= e.pageY && e.pageY <= l_position.bottom  ))
					            {
					                console.log( 'popup in click' );
					                beforeId = target.id;
					            }
					            else
					            {
					            	if( (beforeId =='from' ||  beforeId =='to')&& target.id==''){

					            	}else{
					            		console.log( 'from  ' + beforeId);
					            	console.log( 'popup out click' );
					              //  $(this).hide("fast");
					                showHide('detailSearch');
					                }
					            }
					        }}else if(hideLayer == "layer5"){

							var position = $jq('#layer5').offset();

							position.right = parseInt(position.left)+($jq('#layer5').width());
							position.bottom = parseInt(position.top) + parseInt($('#layer5').height());

							if((position.left <= e.pageX && e.pageX <= position.right)
									&& (position.top <= e.pageY && e.pageY <= position.bottom)){

							}

						}

					}else{

						var layer1 = document.getElementById(hideLayer); 

						layer1.style.display = 'none'; 
						showLayer = null;
						hideLayer = null;

					}

				}

			}else if(target.id != hideLayer){

				if(hideLayer == showLayer){

					if(hideLayer == "theLayer"||hideLayer == "theInputLayer"){

						var layer1 = document.getElementById(hideLayer); 
						layer1.style.display = 'none'; 
						showLayer = null;
						hideLayer = null;	

					}else if(hideLayer == "layer1"){

						var position = $jq('#layer1').offset();

						position.right = parseInt(position.left)+($jq('#layer1').width());
						position.bottom = parseInt(position.top) + parseInt($('#layer1').height());

						if((position.left <= e.pageX && e.pageX <= position.right)
								&& (position.top <= e.pageY && e.pageY <= position.bottom)){

						}else{

							var layer1 = document.getElementById(hideLayer); 
							layer1.style.display = 'none'; 
							showLayer = null;
							hideLayer = null;	

						}

					}else if(hideLayer == "layer2"){

						var position = $jq('#layer2').offset();

						position.right = parseInt(position.left)+($jq('#layer2').width());
						position.bottom = parseInt(position.top) + parseInt($('#layer2').height());

						if((position.left <= e.pageX && e.pageX <= position.right)
								&& (position.top <= e.pageY && e.pageY <= position.bottom)){

						}else{

							var layer1 = document.getElementById(hideLayer); 
							if(target.id != "detailCondition"){
								layer1.style.display = 'none'; 
							}else{
								layer1.style.display = 'block'; 	
							}
							showLayer = null;
							hideLayer = null;	

						}

					}else if(hideLayer == "layer3"){

						var position = $jq('#layer3').offset();

						position.right = parseInt(position.left)+($jq('#layer3').width());
						position.bottom = parseInt(position.top) + parseInt($('#layer3').height());

						if((position.left <= e.pageX && e.pageX <= position.right)
								&&(position.top <= e.pageY && e.pageY <= position.bottom)){

						}else{

							var layer1 = document.getElementById(hideLayer); 
							layer1.style.display = 'none'; 
							showLayer=null;
							hideLayer=null;	

						}

					}else if(hideLayer == "layer4"){

						var position =$jq('#layer4').offset();

						position.right = parseInt(position.left)+($jq('#layer4').width());
						position.bottom = parseInt(position.top) + parseInt($('#layer4').height());

						if((position.left <= e.pageX && e.pageX <= position.right)
								&& (position.top <= e.pageY && e.pageY <= position.bottom)){

						}else{

							var layer1 = document.getElementById(hideLayer); 
							layer1.style.display = 'none'; 
							showLayer = null;
							hideLayer = null;	

						}

					}else if(hideLayer == "layer5"){

						var position = $jq('#layer5').offset();

						position.right = parseInt(position.left)+($jq('#layer5').width());
						position.bottom = parseInt(position.top) + parseInt($('#layer5').height());

						if((position.left <= e.pageX && e.pageX <= position.right)
								&& (position.top <= e.pageY && e.pageY <= position.bottom)){

						}else{

							var layer1 = document.getElementById(hideLayer); 
							layer1.style.display = 'none'; 
							//showLayer = null;
							//hideLayer = null;	

						}

					}else if(hideLayer == "detailSearch"){

						var position = $jq('#detailSearch').offset();

						position.right = parseInt(position.left)+($jq('#detailSearch').width());
						position.bottom = parseInt(position.top) + parseInt($('#detailSearch').height());

						if((position.left <= e.pageX && e.pageX <= position.right)
								&&(position.top <= e.pageY && e.pageY <= position.bottom)){

						}else{

							var layer1 = document.getElementById(hideLayer); 
							if(target.id != "detailCondition"){
								layer1.style.display = 'none'; 
							}else{
								layer1.style.display = 'block'; 	
							}

							showLayer = null;
							//hideLayer = null;	

						}

					}else{
						showLayer = null;
						hideLayer = null;
					}

				}else{

					var layer1 = document.getElementById(hideLayer); 
					layer1.style.display = 'none'; 
					showLayer = null;
					hideLayer = null;	

				}

			} 

		}

	} 
			 */
			function mouseClick(e,where){

	var browser = navigator.appName

	if(browser=="Microsoft Internet Explorer")
	{   //브라우저가 IE일때 돌아간다. 크롬에서 써도 잘 된다.
		console.log("IE    " + event.x + "/" + event.y)

		if(event.x <= 853 && event.x >= 416){
			var layer1 = document.getElementById(where); 
			layer1.style.display = 'none'; 
			showLayer = null;
			hideLayer = null;

		}else if(event.y <= 117 && event.y >= 422){

			var layer1 = document.getElementById(where); 
			layer1.style.display = 'none'; 
			showLayer = null;
			hideLayer = null;

		}

	} else {   //그외(파이어폭스)일 때 돌아간다.

		if(where == "theLayer"){

			if(e.clientX <= 853 && e.clientX >= 416){

				var layer1 = document.getElementById(where); 
				layer1.style.display = 'none'; 
				showLayer = null;
				hideLayer = null;	

			}else if(e.clientY <= 117 && e.clientY >= 422){

				var layer1 = document.getElementById(where); 
				layer1.style.display = 'none'; 
				showLayer = null;
				hideLayer = null;

			}

		}else if(where=="theInputLayer"){

			console.log("FIRE FOX X" + e.clientX ) 
			console.log("FIRE FOX Y"  + e.clientY) 

			if(e.clientX <= 199 || e.clientX >= 488){

				console.log("##########e.clientX"+e.clientX);
				var layer1 = document.getElementById(where); 
				layer1.style.display = 'none'; 
				showLayer = null;
				hideLayer = null;

			}else if(e.clientY <= 66 || e.clientY >= 204){
				console.log("##########e.clientY"+e.clientY);
				var layer1 = document.getElementById(where); 
				layer1.style.display = 'none'; 
				showLayer = null;
				hideLayer = null;	
			}

		}

	}

}

/**
 * 컨텐츠, 클립검색, 폐기 화면의 우측의 화면의 변환을 조작한다.
 * @param listView(clipList(이미지view),clipTableList(리스트view))
 */ 
function changeListView(listView){

	if(listView == 'clipList'){

		var showLayer = document.getElementById(listView); 
		var HiddenLayer = document.getElementById('clipTableList'); 
		showLayer.style.display = 'block'; 	
		HiddenLayer.style.display = 'none'; 	

	} else {

		var showLayer = document.getElementById(listView); 
		var HiddenLayer = document.getElementById('clipList'); 
		showLayer.style.display = 'block';
		HiddenLayer.style.display = 'none'; 	

	}

}

String.prototype.trim = function() {

	return this.replace(/(^\s*)|(\s*$)/gi, "");

}

//달력 계산관련
//기준월 첫날
function getMonthOfFirstDate(dt){

	var newDt = new Date(dt);
	newDt.setDate(1);

	return converDateString(newDt);

}

function getToDay(dt){

	var newDt = new Date(dt);

	return converDateString(newDt);

}

function getbeforeToday(dt){

	var newDt = new Date(dt);
	var dayOfMonth  = newDt.getDate();

	newDt.setDate(dayOfMonth -30);
	return converDateString(newDt);

}
//기준월 말일
function getMonthOfLastDate(dt){

	var newDt = new Date(dt);

	newDt.setMonth( newDt.getMonth() + 1);
	newDt.setDate(0);

	return converDateString(newDt);

}

//기준년 
function getYear(dt){

	var newDt = new Date(dt);

	newDt.setMonth( newDt.getMonth() + 1);
	newDt.setDate(0);

	return converYearString(newDt);

}


function converDateString(dt){

	return dt.getFullYear() + "-" + addZero(eval(dt.getMonth()+1)) + "-" + addZero(dt.getDate());

}

function converYearString(dt){

	return dt.getFullYear();

}

function addZero(i){

	var rtn = i + 100;

	return rtn.toString().substring(1,3);

}

//json을 xml로 치환한다.
function jsonToXmlForPeriod(data){

	console.log(data.statisticsTbls.length)
	var xml = "";

	xml +="<chart palette='2' caption='기간별 통계 그래프' xAxisName='카테고리명'  showValues='0'  decimals='0'   numberSuffix='건' useRoundEdges='1' legendBorderAlpha='0'>";
	xml +="<categories>";

	for(var i = 0; i < data.statisticsTbls.length; i++){

		xml += "<category label='"+data.statisticsTbls[i].categoryNm+"' />"	

	}

	for(var k = 0; k <( 13 - data.statisticsTbls.length); k++){

		xml += "<category label='' />"	

	}
	xml += "</categories>";
	xml += "<dataset seriesName='등록건' color='00FFFF' showValues='0'>";

	for(var i = 0; i < data.statisticsTbls.length; i++){

		xml += "<set value='"+BlankToZero(data.statisticsTbls[i].regist)+"' />"	

	}	 

	for(var k = 0; k <( 13 - data.statisticsTbls.length); k++){

		xml += "<category label='0' />"	

	}

	xml += "</dataset>";
	xml += "<dataset seriesName='정리전건' color='98FB98' showValues='0'>";

	for(var i = 0; i < data.statisticsTbls.length; i++){

		xml +=" <set value='"+BlankToZero(data.statisticsTbls[i].beforeArrange)+"' />"	

	}	
	for(var k = 0; k <( 13 - data.statisticsTbls.length); k++){

		xml += "<category label='0' />"	

	}
	xml += "</dataset>";
	xml += "<dataset seriesName='정리완료건' color='F6BD0F' showValues='0'>";

	for(var i= 0; i < data.statisticsTbls.length; i++){

		xml += "<set value='"+BlankToZero(data.statisticsTbls[i].completeArrange)+"' />"	

	}	
	for(var k = 0; k <( 13 - data.statisticsTbls.length); k++){

		xml += "<category label='0' />"	

	}
	xml += "</dataset>";
	xml += "<dataset seriesName='폐기건' color='8BBA00' showValues='0'>";

	for(var i = 0; i<data.statisticsTbls.length ; i++){

		xml += "<set value='"+BlankToZero(data.statisticsTbls[i].discard)+"' />"	

	}	
	for(var k = 0; k <( 13 - data.statisticsTbls.length); k++){

		xml += "<category label='0' />"	

	}
	xml += "</dataset>";
	xml += "<dataset seriesName='오류건' color='FF0000' showValues='0'>";

	for(var i = 0; i < data.statisticsTbls.length; i++){

		xml += "<set value='"+BlankToZero(data.statisticsTbls[i].error)+"' />"	

	}	
	for(var k = 0; k <( 13 - data.statisticsTbls.length); k++){

		xml += "<category label='0' />"	

	}
	xml += "</dataset>";
	xml += "</chart>";

	return xml

}


function jsonToXmlForYear(data){

	var temp_regist = 0;
	var temp_complete = 0;
	var temp_before = 0;
	var temp_discard = 0;
	var temp_a = 0;
	var makeNode='N'
		var xml = "";

	xml += "<chart palette='2' caption='연별 통계 그래프'   showValues='0'  decimals='0'   numberSuffix='건' useRoundEdges='1' legendBorderAlpha='0'>";
	xml += "<categories>";

	xml +="<category label='1월' />"	
		xml +="<category label='2월' />"	
			xml +="<category label='3월' />"	
				xml +="<category label='4월' />"	
					xml +="<category label='5월' />"	
						xml +="<category label='6월' />"	
							xml +="<category label='7월' />"	
								xml +="<category label='8월' />"	
									xml +="<category label='9월' />"	
										xml +="<category label='10월' />"	
											xml +="<category label='11월' />"	
												xml +="<category label='12월' />"	


													xml +="</categories>";
	xml +="<dataset seriesName='등록건' color='00FFFF' showValues='0'>";

	for(var i = 0; i < 12; i++){		

		for(var k =0 ; k <data.statisticsTbls.length ; k++ ){

			var MM = data.statisticsTbls[k].regDd.substring(4,6)
			if(addZero(i+1) == MM ){
				xml +="<set value='"+BlankToZero(data.statisticsTbls[k].regist)+"' />"	
				makeNode='Y'
			}
		}
		if(makeNode == 'N'){
			xml +="<set value='0'/>"
		}
	}	 

	xml += "</dataset>";

	makeNode ='N'
		xml += "<dataset seriesName='정리전건' color='98FB98' showValues='0'>";

	for(var i = 0; i < 12; i++){	

		for(var k = 0; k < data.statisticsTbls.length; k++ ){

			var MM = data.statisticsTbls[k].regDd.substring(4,6)

			if(addZero(i+1) == MM ){

				xml += "<set value='"+BlankToZero(data.statisticsTbls[k].beforeArrange)+"' />"	
				makeNode='Y'
			}
		}
		if(makeNode == 'N'){
			xml +="<set value='0'/>"
		}
	}

	xml += "</dataset>";

	makeNode ='N'
		xml += "<dataset seriesName='정리완료건' color='F6BD0F' showValues='0'>";

	for(var i = 0 ; i < 12; i++){		 
		for(var k = 0; k < data.statisticsTbls.length; k++ ){

			var MM = data.statisticsTbls[k].regDd.substring(4,6)

			if(addZero(i+1) == MM ){

				xml += "<set value='"+BlankToZero(data.statisticsTbls[k].completeArrange)+"' />"	
				makeNode='Y'
			}
		}
		if(makeNode == 'N'){
			xml +="<set value='0'/>"
		}
	}	

	xml += "</dataset>";

	makeNode ='N'
		xml += "<dataset seriesName='폐기건' color='8BBA00' showValues='0'>";

	for(var i = 0; i < 12; i++){		 

		for(var k = 0; k < data.statisticsTbls.length; k++ ){

			var MM = data.statisticsTbls[k].regDd.substring(4,6)

			if(addZero(i+1) == MM ){

				xml +="<set value='"+BlankToZero(data.statisticsTbls[k].discard)+"' />"	
				makeNode='Y'
			}
		}
		if(makeNode == 'N'){
			xml +="<set value='0'/>"
		}
	}	

	xml += "</dataset>";

	makeNode ='N'
		xml += "<dataset seriesName='오류건' color='FF0000' showValues='0'>";

	for(var i = 0; i<12; i++){		 

		for(var k = 0;  k < data.statisticsTbls.length; k++ ){

			var MM = data.statisticsTbls[k].regDd.substring(4,6)

			if(addZero(i+1) == MM ){

				xml += "<set value='"+BlankToZero(data.statisticsTbls[k].error)+"' />"	
				makeNode='Y'
			}
		}
		if(makeNode == 'N'){
			xml +="<set value='0'/>"
		}
	}	
	xml += "</dataset>";
	xml += "</chart>";
	return xml

}

//monitoringGraph
function jsonToXmlHighStorage(data){

	var highPercVolume = (data.highStorageTbl.useVolume/data.highStorageTbl.totalVolume)*100;
	var highTotalVolume = data.highStorageTbl.totalVolume;
	var highUseVolume = data.highStorageTbl.useVolume;
	var highIdleVolume = data.highStorageTbl.idleVolume;
	var MathRoundHighPercVolume = (Math.round(highPercVolume*10)/10);

	var useCpu = data.cpuAndMem.useCpu;
	var idleCpu = data.cpuAndMem.idleCpu;

	//alert(useCpu);

	var useMem = data.cpuAndMem.useMem;
	var mathRoundMemPerc = (Math.round(useMem / 1024 / 1024*100))/100;

	var xml = "";
	xml += "<chart palette='4'><set value='"+highUseVolume+"' label='Use'/><set value='"+highIdleVolume+"' label='Free'/></chart>";

	return xml;

}

function jsonToXmlLowStorage(data){

	var lowPercVolume = (data.lowStorageTbl.useVolume/data.lowStorageTbl.totalVolume)*100;
	var lowTotalVolume = data.lowStorageTbl.totalVolume;
	var lowUseVolume = data.lowStorageTbl.useVolume;
	var lowIdleVolume = data.lowStorageTbl.idleVolume;
	var MathRoundlowPercVolume = (Math.round(lowPercVolume*10)/10);




	//alert(useCpu);

	var xml = "";
	xml += "<chart palette='4'><set value='"+lowUseVolume+"' label='Use'/><set value='"+lowIdleVolume+"' label='Free'/></chart>";

	return xml;

}

function jsonToXmlCpu(data){

	var useCpu = data.cpuAndMem.useCpu;
	var idleCpu = data.cpuAndMem.idleCpu;

	var xml = "";
	xml += "<chart useRoundEdges='1' formatNumberScale='100' decimals='100' showValues='100'  xAxisName='CPU'  palette='2'><set value='"+useCpu+"'/></chart>";

	return xml;

}

function jsonToXmlMem(data){

	var useMem = data.cpuAndMem.useMem;
	var idleMem = data.cpuAndMem.idleMem;
	var totalMem = data.cpuAndMem.totalMem;

	var percMem = (useMem/totalMem)*100

	var mathRoundMemPerc = (Math.round(useMem / 1024 / 1024*100))/100;

	var xml = "";
	xml += "<chart useRoundEdges='1' formatNumberScale='0' decimals='0' showValues='100'  xAxisName='MEM'  palette='2'><set value='"+percMem+"'/></chart>";

	return xml;

}




function changeInputStyle(data){


	var xml = "";

	$jq('#inputclfCd').empty();

	if(data == 'auto'){

	}else{

		xml += '<input name="inputclfCd" id = "textclfCd" type="text"       >';

	}

	$jq('#inputclfCd').append(xml);

}

function changeSecondSelectbox(val){
	var secondLayer = document.getElementById('secondSelectBox'); 

	if(val == "all" || val == ""){
		secondLayer.style.display = 'none';  
	}else{
		secondLayer.style.display = '';  
	}

}


/*
 * 컨텐츠, 클립검색의 상세조회 화면을 동적으로 추가한다.*/
function changeDeatilSearchView(val){
	var searchCondition = document.getElementById('ul-'+val); 
	console.log('###########################del val   '+val);
	searchCondition.style.display = 'block';  
	if(val == 'dataStatCd'){
		findCodeInfos('DSCD');
	}else if(val == 'ctTyp'){
		findCodeInfos('CTYP');
	}
	del(val)
}


function DeleteDeatilSearchView(val){
	var searchCondition = document.getElementById(val); 
	console.log('###########################add val   '+val);

	searchCondition.style.display = 'none';  
	$jq('#detailCondition').val('');
	add(val); 
}





function del(val){
	console.log(val);


	$jq("#detailCondition > option[value="+val+"]").remove();

}
function add(val){

	var selectList='';

	var condition = val.substring(3,val.length);
	console.log('###########################condition   '+condition);
	if(condition == 'dataStatCd'){
		$jq('#detailDataStatCd').val(null)
		$jq('#detailCondition option:eq(0)').after("<option value='dataStatCd'>정리상태</option>")
	}else if(condition == 'ctTyp'){
		$jq('#detailCtTyp').val(null)
		$jq('#detailCondition option:eq(0)').after("<option value='ctTyp'>영상유형</option>")
	}

	console.log('###########################detailCondition   '+$jq('#detailCondition').size());


}

/*
 * video seeking 
 */
function videoSeeking(val){

	console.log("duration  "+val)
	var video = document.getElementById('videoplay');
	video.currentTime = val;
	video.play();
}

function showRigthMenu(){
	var rightMenu = document.getElementById('rightMenu');
	rightMenu.style.display = 'block'; 
}
function hideRigthMenu(){
	var rightMenu = document.getElementById('rightMenu');
	rightMenu.style.display = 'none'; 
}



var display_url = 1

function showmenu(){
	var meun =  document.getElementById('menu');

	e = window.Event;
	pos = abspos(e);
	console.log("before meun.style.display  "+meun.style.display);
	if(meun.style.display == ''){
		meun.style.display = 'none'
	}
	meun.style.left = pos.x+"px";
	meun.style.top = (pos.y+10)+"px";
	meun.style.display = meun.style.display=='none'?'block':'none';
	console.log("meun.style.left   "+meun.style.left);
	console.log("meun.style.top   "+meun.style.top);
	console.log("after meun.style.display  "+meun.style.display);


}

function hidemenu(){

	menu.style.display = "none"

}

function over(){

	if (event.srcElement.className == "menuitems") {

		event.srcElement.style.backgroundColor = "highlight"

			event.srcElement.style.color = "yellow"

				if (display_url == 1)

					window.status = event.srcElement.url

	}

}

function out(){

	if (event.srcElement.className == "menuitems") {

		event.srcElement.style.backgroundColor = ""

			event.srcElement.style.color = "black"

				window.status = ''

	}

}

function linkTo(){

	if (event.srcElement.className == "menuitems")

		window.location = event.srcElement.url

} 


function abspos(e){
	this.x = e.clientX + (document.documentElement.scrollLeft?document.documentElement.scrollLeft:document.body.scrollLeft);
	this.y = e.clientY + (document.documentElement.scrollTop?document.documentElement.scrollTop:document.body.scrollTop);
	return this;
}



/*
 * 클릭된 체크박스의 css를 변경한다.
 * */ 
function changeCheckBoxCss(){
	var obj = window.event.srcElement; 

	if(obj.checked == true){
		$jq('#'+obj.value).attr("style","border:2px solid #00FFC2")
	}else{

		if(rpImgKfrm == obj.value){
			$jq('#'+obj.value).attr("style","border:2px solid #FF0040")
		}else{
			$jq('#'+obj.value).attr("style","")
		}
	}

}



$(document).ready(function(){
	$("#clfGubun").change(function(){
		console.log('#################################################');
		$jq('#layer5').attr("style","posion : relative; z-index: 99999;display : block")

	})

	$("#createWay").change(function(){
		console.log('#################################################');
		$jq('#layer5').attr("style","posion : relative; z-index: 99999;display : block")

	})
})




/*
 * 버튼을 클릭했는지 여부를 리턴한다.
 * */ 

function buttonClickYn(id){

	if(id == 'categorySearchButton' || id == 'detailSearchButton' || id == 'intreeButton' ){
		return true;
	}else if(  id == 'categoryInsertButton' || id == 'categoryUpdateButton'  ){
		return true;
	}else if (  id == 'episodeInsertButton' || id == 'episodeUpdateButton'  ){
		return true;
	}else if (  id == 'codeInsertButton' || id.substr(0,3) == 'tra' || id == 'updateEpisode' ){
		return true;
	}else if (  id == 'insertClf' || id == 'button1' || id =='insertEpisode' ){
		return true;
	}else if ( id == 'button2' || id == 'registAttach' || id == 'insertCategory' || id == 'updateCategory'){

	}else{
		return false;
	}
}

document.ondblclick = function(evt) { 
	if (window.getSelection) 
		window.getSelection().removeAllRanges(); 
	else if (document.selection) 
		document.selection.empty(); 
}




/*
 * 팝업창의 선택여부에 따라 기간컬럼을 추가 삭제
 * */ 

function showHideSelectbox(value){

	console.log("#####################value"+value);
	if(value == "Y"){
		$jq('#period').css("display","")
	}else{
		$jq('#period').css("display","none")
	}
	
}



/*
 * 공지 팝업 레이어의 제목 클릭시 하부 내용 보여주기
 * */ 

function showNoticeCont(value){

	
	console.log("#####################display  "+$jq('#cont_'+value).css("display"));
		if($jq('#cont_'+value).css("display") == "none"){
		$jq('#cont_'+value).css("display","")
		}else{
		$jq('#cont_'+value).css("display","none")	
		}
	
}

/**
 * 공지사항을 가운데로 띄우게 하는 함수
 */
function notice_close(Id){
	console.log("#####################Id    "+Id);
	$('#'+Id).css('display','none');
	
}              

