<#include "/WEB-INF/views/includes/commonTags.ftl">
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8" />
    <title><@tiles.getAsString name="title"/></title>
	<!--[if lt IE 9]>
  <!--<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>-->
  <![endif]-->

  <!--[if lt IE 9]>
  <!--<script src="http://ie7-js.googlecode.com/svn/version/2.1(beta4)/IE9.js"></script>-->
  <![endif]-->
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8" />
    
  	<link rel="stylesheet" type="text/css" href="<@spring.url '/css/master.css'/>" />
	<link rel="stylesheet" type="text/css" href="<@spring.url '/css/layout.css'/>" />	
	<link rel="stylesheet" type="text/css" href="<@spring.url '/css/control.css'/>" />	
	<link rel="stylesheet" type="text/css" href="<@spring.url '/css/jquery-ui.css'/>" media="all" />
	<link rel="stylesheet" type="text/css" href="<@spring.url '/extjs/resources/css/ext-all.css'/>" />	
	<script type="text/javascript" src="<@spring.url '/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/js/script.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/js/control.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/js/html5.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/js/ie9.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/js/jquery.form.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/js/json2.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/js/jquery-1.3.2.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/js/ajaxfileupload.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/js/jquery-ui.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/js/cookie.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/extjs/ext-all-debug.js'/>"></script>
	<!-- 클리화면 -->
	<script type="text/javascript" src="<@spring.url '/extjsView/Header.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/extjsView/view/clip/clipLeft.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/extjsView/view/clip/clipSearch.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/extjsView/model/categoryTreeModel.js'/>"></script>  
	<script type="text/javascript" src="<@spring.url '/extjsView/model/clipSearchModel.js'/>"></script>  
	<script type="text/javascript" src="<@spring.url '/extjsView/controller/clipController/ClipController.js'/>"></script>  
	<script type="text/javascript" src="<@spring.url '/extjsView/store/clip/clipSearchStore.js'/>"></script>  
	<script type="text/javascript" src="<@spring.url '/extjsView/store/clip/categoryTreeStore.js'/>"></script>  
	<!-- 컨텐츠조회화면 -->
	<!--<script type="text/javascript" src="<@spring.url '/extjsView/view/contents/contentsLeft.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/extjsView/model/contentsSearchModel.js'/>"></script>  
	<script type="text/javascript" src="<@spring.url '/extjsView/controller/contentsController/contentsController.js'/>"></script>  
	<script type="text/javascript" src="<@spring.url '/extjsView/store/contents/contentsSearchStore.js'/>"></script>  
  	<script type="text/javascript" src="<@spring.url '/extjsView/store/contents/categoryTreeStore.js'/>"></script> --> 
  	<!-- 변환&전송 화면 -->
	<script type="text/javascript" src="<@spring.url '/extjsView/controller/work/workController.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/extjsView/model/traSearchModel.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/extjsView/model/trsSearchModel.js'/>"></script>    
	<script type="text/javascript" src="<@spring.url '/extjsView/store/work/traSearchStore.js'/>"></script>  
	<script type="text/javascript" src="<@spring.url '/extjsView/store/work/trsSearchStore.js'/>"></script>  
  	<script type="text/javascript" src="<@spring.url '/extjsView/store/work/categoryTreeStore.js'/>"></script>  
 	<script type="text/javascript" src="<@spring.url '/extjsView/view/work/workLeft.js'/>"></script>  
  
	
	<script type=text/javascript>	 
		var $jq = jQuery.noConflict();
		var baseUrl = "<@spring.url '/'/>";
		$jq(function() {
  		 var dates = $jq( "#from, #to" ).datepicker({
   		    prevText: '이전 달',
  			nextText: '다음 달',
  			monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
  			monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
  			dayNames: ['일','월','화','수','목','금','토'],
  			dayNamesShort: ['일','월','화','수','목','금','토'],
  			dayNamesMin: ['일','월','화','수','목','금','토'],
  			dateFormat: 'yy-mm-dd',
  			showMonthAfterYear: true,
  			yearSuffix: '년'
  			});
		});
		
		$jq(document).ready(function(){
    $jq.ajaxSetup({
    beforeSend : function(a,b){
  
    },
        error:function(request,status,error){
            if(request.status==0){
            alert('네트워크를 체크해주세요.');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(request.status==404){
            alert('페이지를 찾을수 없습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(request.status==500){
            alert('서버에러 발생하였습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(error=='parsererror'){
            alert('json 파싱에 에러가 발행하였습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(error=='timeout'){
            alert('응답 대기시간이 초과하였습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else {
            alert('알수없는 에러입니다 ');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }
        }
    });
    

});


function findNoticeList(){

 	$jq.ajax({
		url: '<@spring.url "/clip/findNoticeList.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#noticeInfo').serialize(),
		
		success: function(data){
		if(data.noticeInfos.length != 0){
	
			
			for(var i=0; i<data.noticeInfos.length; i++){
			var tab = '';
			var regDd = new Date(parseInt(data.noticeInfos[i].regDt, 10));
			 	 tab += '<div class="box12" id="notice_'+data.noticeInfos[i].noticeId+'">'
				  tab += '<div style="width:100%;background-color: red; height: 30px; margin-top: -10px; text-align: center;line-height:29px;" >금일의 공지사항</div>'
				 tab += '<div style="overflow-y:scroll;height:90px"><pre style="white-space: pre-wrap;margin-left:12px;margin-top:12px">'+data.noticeInfos[i].cont+'</pre></div>'
				 tab += '<div  id="close" style="width:100px; margin-left:236px;"><a href="#" onclick="notice_close(\'notice_'+data.noticeInfos[i].noticeId+'\');closeWin(\'notice_'+data.noticeInfos[i].noticeId+'\');return false;">close</a></div>'
            	
				  tab += '</div>'
				$jq(tab).appendTo('body');
				$jq('.box12').draggable();
			var cookie = getCookie('notice_'+data.noticeInfos[i].noticeId+'=');
				console.log('#############################cookie  '+cookie);
			    if(cookie == '' ){
					$jq('#notice_'+data.noticeInfos[i].noticeId).css("display","block");
				}else{
					$jq('#notice_'+data.noticeInfos[i].noticeId).css("display","none");
				}
			}
	
			
			}else{
			$jq('#noticeLayOut').css("display","none");
			}
		}
		
	});
}

	</script>
	
</head>
<body>
<div id="wrap">
	<@tiles.insertAttribute name="header"/>
	<hr />
	<!-- Container -->
	<div id="container">
	<@tiles.insertAttribute name="body"/>
	<hr />
	<@tiles.insertAttribute name="right"/>	
	<hr />
	</div>

</div>	
<!-- 본문끝 -->
	<div ><@tiles.insertAttribute name="footer"/></div>
	
	
</body>
</html>