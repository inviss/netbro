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
			text-align: left;
			padding: 15px 5px 20px 5px;
			background-color: transparent;
			border: 0px solid white;
		}
	</style>
	
	
	<!-- header(공통) -->
	<script type="text/javascript" src="<@spring.url '/jsview/Header.js'/>"></script>
	
	<script type="text/javascript" src="<@spring.url '/util/jquery-2.1.1.min.js'/>"></script>	
 	<script type="text/javascript" src="<@spring.url '/jsview/model/mediaModel.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/model/noticeModel.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/jsview/model/menuModel.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/jsview/model/downloadModel.js'/>"></script>
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
 	<script type="text/javascript" src="<@spring.url '/jsview/store/clip/downloadStore.js'/>"></script>
 	<!-- 유틸리티 -->
 	<script type="text/javascript" src="<@spring.url '/util/util.js'/>"></script>
 
 	<!-- 테스트용  -->
 	<script type="text/javascript" src="<@spring.url '/jsview/model/test/storyBoardModel.js'/>"></script>
 	
 	<script type="text/javascript" src="<@spring.url '/jsview/store/clip/testClipStoryBoardStore.js'/>"></script>
 	
	<!-- 클립검색 -->
	<script type="text/javascript" src="<@spring.url '/jsview/store/clip/getMediaStore.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/jsview/store/clip/findMediaStore.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/store/clip/clipSearchStore.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/store/clip/clipBasicInfoStore.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/store/clip/clipStoryBoardStore.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/store/clip/PlayerStore.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/jsview/store/clip/clipConerStore.js'/>"></script>
  	<script type="text/javascript" src="<@spring.url '/jsview/store/clip/categoryTreeStore.js'/>"></script> 
	<script type="text/javascript" src="<@spring.url '/jsview/view/clip/clipSearch.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/view/clip/search.js'/>"></script>
 	
 	
 	
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

<div id="headerMenu" style="margin-left:20px;margin-top:10px"></div>
<@tiles.insertAttribute name="header"/>
<div style="height:10px"> </div>
<div id="top" style="margin : 0 auto;width:100%;">
<@tiles.insertAttribute name="top"/>
 </div>
<div id="body" style="width:100%;"> 
	<@tiles.insertAttribute name="body"/>
</div>
</body>

</html>
