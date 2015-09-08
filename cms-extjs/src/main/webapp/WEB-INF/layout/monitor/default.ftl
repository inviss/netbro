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
    	 
		<script type="text/javascript" src="<@spring.url '/extjs/ext-all-debug.js'/>"></script> 
		<script type="text/javascript" src="<@spring.url '/extjs/locale/ext-lang-ko.js'/>" charset="utf-8"></script>
		
 

	<!-- neptune 테마  
    <link rel="stylesheet" type="text/css" href="<@spring.url '/extjs/resources/css/ext-all-neptune.css'/>" />
    <script type="text/javascript" src="<@spring.url '/extjs/ext-all-debug.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/extjs/packages/ext-theme-neptune/build/ext-theme-neptune-debug.js'/>"></script>
	-->
	
	<!-- header(공통) -->
	<script type="text/javascript" src="<@spring.url '/jsview/Header.js'/>"></script>	
	<script type="text/javascript" src="<@spring.url '/util/jquery-2.1.1.min.js'/>"></script>	
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
 	<script type="text/javascript" src="<@spring.url '/jsview/model/equipModel.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/model/storageModel.js'/>"></script>
 	
 	
 
 	<!-- 유틸리티 -->
 	<script type="text/javascript" src="<@spring.url '/util/util.js'/>"></script>
 	
 	
 	<!-- 모니터링 모델 -->
 	<script type="text/javascript" src="<@spring.url '/jsview/model/monitoringModel.js'/>"></script>
 	
 	<!-- store -->
 	<script type="text/javascript" src="<@spring.url '/jsview/store/monitor/serverResourceStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/store/monitor/thirdPartyStatusStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/store/monitor/equipMentStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/store/monitor/hIghStorageStore.js'/>"></script>
 	<script type="text/javascript" src="<@spring.url '/jsview/store/monitor/rowStorageStore.js'/>"></script>
 	
 	<!-- view -->
 	<script type="text/javascript" src="<@spring.url '/jsview/view/monitor/monitorSearchView.js'/>"></script>
 	
<script type="text/javascript">
 var $jq = jQuery.noConflict();
 var context = "<@spring.url ''/>"
Ext.Loader.setConfig({enabled: true});
 
 var context = "<@spring.url ''/>"
var loginUserId = '${user.userId}';
</script> 	
</head>

<body>
<div id="headerMenu" style="margin-left:20px;margin-top:10px"></div>
<@tiles.insertAttribute name="header"/>
<div style="height:10px"> </div>
<div id="body" style="margin : 0 auto;width:100%;"> 
	<@tiles.insertAttribute name="body"/>
</div>
</body>
 
</html>
