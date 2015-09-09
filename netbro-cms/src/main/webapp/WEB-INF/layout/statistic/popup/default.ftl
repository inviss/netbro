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
	<link rel="stylesheet" type="text/css" href="<@spring.url '/css/jquery-ui.css'/>" />
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
	<script type="text/javascript" src="<@spring.url '/js/jquery.printElement.js'/>"></script>
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
        error:function(request,status,error){
            if(status.status==0){
            alert('네트워크를 체크해주세요.');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(status.status==404){
            alert('페이지를 찾을수 없습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(status.status==500){
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
	</script>

</head>
<body>
<div id="wrap">
	
	<!-- Container -->
	<div id="detailContainer">
	<@tiles.insertAttribute name="body"/>
	<hr />
	
        
	</div>
</div>	
  
<!-- 본문끝 -->
</body>
</html>