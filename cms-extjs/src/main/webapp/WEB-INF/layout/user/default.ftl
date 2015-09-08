<#include "/WEB-INF/views/includes/commonTags.ftl">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Expires" content="-1"> 
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="No-Cache"> 
    <title>Extjs sample</title>
    <!-- classic extjs 테마-->
    	<link rel="stylesheet" type="text/css" href="<@spring.url '/extjs/resources/css/ext-all.css'/>" />	 
    	  <link rel="stylesheet" type="text/css" href="<@spring.url '/css/customExtjs.css'/>" />	 
		<script type="text/javascript" src="<@spring.url '/extjs/ext-all-debug.js'/>"></script>
		 <script type="text/javascript" src="<@spring.url '/extjs/locale/ext-lang-ko.js'/>" charset="utf-8"></script>
		 
	<!-- neptune 테마  
    <link rel="stylesheet" type="text/css" href="<@spring.url '/extjs/resources/css/ext-all-neptune.css'/>" />
    <script type="text/javascript" src="<@spring.url '/extjs/ext-all-debug.js'/>"></script>
    
    <script type="text/javascript" src="<@spring.url '/extjs/packages/ext-theme-neptune/build/ext-theme-neptune-debug.js'/>"></script>
	-->
	
	<!-- 공지사항 팝업 테마 -->
		<style type="text/css">
		#instructions ul li {
			list-style-type:disc;
			list-style-position:outside;
			font-size:12px;
			margin:0px 0px 0px 20px;
		}
		
		.regridColunms .x-grid-cell-inner{
			padding : 10 7 7 10
		}
		
	
		
		/*form label background  색상 지정*/
      .customFormBackground .x-form-item{
         font: normal 12px tahoma,arial,verdana,sans-serif;
         background-color: aliceblue;
      }
		
		
		/* Icons */
		.ux-notification-icon-information {
			background-image: url("<@spring.url '/images/icon16_info.png'/>");
		}

		.ux-notification-icon-error {
			background-image: url("<@spring.url '/images/icon16_error.png'/>");
		}


		/* Using standard theme */
		.ux-notification-window .x-window-body {
			text-align: center;
		padding: 15px 5px 15px 5px;
		}


		/* Custom styling */
		.ux-notification-light .x-window-header {
			background-color: transparent;
		}

		body .ux-notification-light {
			background-image: url("<@spring.url '/images/fader.png'/>");
		}

		.ux-notification-light .x-window-body {
			text-align: center;
			padding: 15px 5px 20px 5px;
			background-color: transparent;
			border: 0px solid white;
		}
	</style>
	
	
	<!-- header(공통) -->
	<script type="text/javascript" src="<@spring.url '/jsview/Header.js'/>"></script>
	
	<script type="text/javascript" src="<@spring.url '/util/jquery-2.1.1.min.js'/>"></script>	
	<script type="text/javascript" src="<@spring.url '/jsview/model/attachFileModel.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/jsview/model/archiveModel.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/jsview/model/downloadModel.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/model/noticeModel.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/model/mediaModel.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/jsview/model/menuModel.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/jsview/store/menuStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/clipSearchModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/conerInfoModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/storyBoardModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/metaModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/metaBasicModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/playerModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/contentsInstModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/statisticsModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/contentsSearchModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/episodeModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/codeModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/monitoringModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/traModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/trsModel.js'/>"></script> 	
 	<script type="text/javascript" src="<@spring.url '/jsview/model/discardModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/store/common/ctClaStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/store/common/ctTypClfStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/store/common/ristClfStore.js'/>"></script> 	
 	<script type="text/javascript" src="<@spring.url '/jsview/store/common/discardCodeStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/store/admin/notice/findNoticeStore.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/store/clip/findNoticePopUpStore.js'/>"></script>
      	<script type="text/javascript" src="<@spring.url '/jsview/store/work/categoryTreeStore.js'/>"></script>  
 	
 	
 	<!-- 유틸리티 -->
 	<script type="text/javascript" src="<@spring.url '/util/util.js'/>"></script>
 
 	<!-- 테스트용  -->
 	<script type="text/javascript" src="<@spring.url '/jsview/model/test/storyBoardModel.js'/>"></script>
 	
 	<script type="text/javascript" src="<@spring.url '/jsview/store/clip/testClipStoryBoardStore.js'/>"></script>
 	

 	 <!-- 컨텐츠관리 = 컨텐츠 정리-->
 	 	
    <script type="text/javascript" src="<@spring.url '/jsview/store/clip/PlayerStore.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/archiveStore.js'/>"></script>
 	 <script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/getMediaStore.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/findMediaStore.js'/>"></script>
   <script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/contentsSearchStore.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/contentsBasicInfoStore.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/contentsStoryBoardStore.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/PlayerStore.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/contentsConerStore.js'/>"></script>
  	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/categoryTreeStore.js'/>"></script> 
  	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/detailCategoryTreeStore.js'/>"></script> 
  	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/discardStore.js'/>"></script> 
  	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/arrangeCompleteStore.js'/>"></script> 
  	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/errorRegistStore.js'/>"></script> 
  	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/imgStore.js'/>"></script>
  	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/dividCornerStore.js'/>"></script>
  	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/episodeStore.js'/>"></script>  	
	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/attachFileStore.js'/>"></script>
		<script type="text/javascript" src="<@spring.url '/jsview/store/contents/arrange/downloadStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/view/contents/arrange/arrangeLeft.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/view/contents/arrange/arrangeSearch.js'/>"></script>
 	
 	<!-- 컨텐츠관리 = 컨텐츠 폐기 -->
 	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/discard/disuseStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/view/contents/discard/discardLeft.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/view/contents/discard/discardSearch.js'/>"></script>
 	
 	<!-- 컨텐츠관리 = 아카이브관리 -->
 	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/archive/categoryTreeStore.js'/>"></script> 
 	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/archive/archiveStore.js'/>"></script> 
 	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/archive/archiveListStore.js'/>"></script> 
 	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/archive/downloadListStore.js'/>"></script> 
 	<script type="text/javascript" src="<@spring.url '/jsview/store/contents/archive/downloadStore.js'/>"></script> 
 	<script type="text/javascript" src="<@spring.url '/jsview/view/contents/archive/archiveLeft.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/view/contents/archive/archiveSearch.js'/>"></script>
 
 	<!--변환&전송 -->
 	<script type="text/javascript" src="<@spring.url '/jsview/store/work/findTraStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/store/work/findTrsStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/store/work/retryTrsStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/view/work/tra/traList.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/view/work/tra/traLeft.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/view/work/trs/trsList.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/view/work/trs/trsLeft.js'/>"></script>
 	
 	<!--모니터링 -->
 	<script type="text/javascript" src="<@spring.url '/jsview/store/monitor/serverResourceStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/store/monitor/thirdPartyStatusStore.js'/>"></script>
 	
 	<script type="text/javascript" src="<@spring.url '/jsview/view/monitor/monitorSearchView.js'/>"></script> 
 	
 	
 	<!-- 통계 --> 
  	<script type="text/javascript" src="<@spring.url '/jsview/store/statistic/categoryTreeStore.js'/>"></script>
  	<script type="text/javascript" src="<@spring.url '/jsview/store/statistic/statisticsGraphStore.js'/>"></script>
  	<script type="text/javascript" src="<@spring.url '/jsview/store/statistic/statisticsListStore.js'/>"></script>
  	<script type="text/javascript" src="<@spring.url '/jsview/store/statistic/statisticsDetailStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/view/statistic/statisticLeft.js'/>"></script> 
 	<script type="text/javascript" src="<@spring.url '/jsview/view/statistic/statisticSearch.js'/>"></script> 
 	<script type="text/javascript" src="<@spring.url '/jsview/view/statistic/year/statisticForYearLeft.js'/>"></script> 
 	<script type="text/javascript" src="<@spring.url '/jsview/view/statistic/year/statisticForYearSearch.js'/>"></script> 
<script type="text/javascript">
//공자사항 팝업관련 변수
var reusable = null;

//메뉴 표기관련 변수
var currnetMenu="";
var previousMenu="";
var $jq = jQuery.noConflict();
 var context = "<@spring.url ''/>"
Ext.Loader.setConfig({enabled: true});
 

var loginUserId = '${user.userId}';
</script> 	
</head>

<body>
<!--<div style="margin-left:20px;margin-top:10px"><img src="<@spring.url '/images/web_logo.png'/>"><div id="logout"></div></div>-->
<div id="headerMenu" style="margin-left:20px;margin-top:10px"></div>
<@tiles.insertAttribute name="header"/>
<div style="height:10px"> </div>
<div id="body" style="margin : 0 auto;width:100%;"> 
	<@tiles.insertAttribute name="left"/>
	<div style="float:left;width:3%"></div>
	<@tiles.insertAttribute name="body"/>
</div> 
</body>

</html>
