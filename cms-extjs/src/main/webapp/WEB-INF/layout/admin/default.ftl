<#include "/WEB-INF/views/includes/commonTags.ftl">

    <html>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<META http-equiv="Expires" content="-1"> 
<META http-equiv="Pragma" content="no-cache"> 
<META http-equiv="Cache-Control" content="No-Cache"> 
    <head>
        <title>Extjs sample</title>
        <!-- classic extjs 테마		-->
        <link rel="stylesheet" type="text/css" href="<@spring.url '/extjs/resources/css/ext-all.css'/>" />
        <script type="text/javascript" src="<@spring.url '/extjs/ext-all-debug.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/extjs/locale/ext-lang-ko.js'/>" charset="utf-8"></script>
        
		<style type="text/css">
			/*Colunms*/
		 
	/*form label background  색상 지정*/
      .customFormBackground .x-form-item{
         font: normal 12px tahoma,arial,verdana,sans-serif;
         background-color: aliceblue;
      }
			
			
		</style>
		
		
    	 <link rel="stylesheet" type="text/css" href="<@spring.url '/css/customExtjs.css'/>" />	 
		
		        
        <!-- header(공통) -->
        <script type="text/javascript" src="<@spring.url '/jsview/model/menuModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/menuStore.js'/>"></script>
		<script type="text/javascript" src="<@spring.url '/util/jquery-2.1.1.min.js'/>"></script>	
        <script type="text/javascript" src="<@spring.url '/jsview/Header.js'/>"></script>

        <!-- 유틸리티 -->
        <script type="text/javascript" src="<@spring.url '/util/util.js'/>"></script>


        <!--model-->
        <script type="text/javascript" src="<@spring.url '/jsview/model/userModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/memory/roleAuthModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/memory/proBusiModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/authModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/equipModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/profileModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/proBusiModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/menuModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/busipartnerModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/busicategoryModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/busiCategoryProflNmModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/noticeModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/codeModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/storageModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/categoryModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/episodeModel.js'/>"></script>
  		<script type="text/javascript" src="<@spring.url '/jsview/model/menuTreeModel.js'/>"></script>



        <!--auth store-->
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/auth/getAuthStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/auth/getRoleAuthStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/auth/findAuthStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/auth/findRoleAuthStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/auth/saveAuthStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/auth/roleAuthTreeStore.js'/>"></script>
        
        <!--user store-->
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/user/findUserAuthStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/user/getUserStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/user/findUserStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/user/saveUserStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/user/updateUserPasswdStore.js'/>"></script>
        <!--equip store-->
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/equip/findEquipStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/equip/getEquipStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/equip/saveEquipStore.js'/>"></script>
        <!--profile store-->
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/profile/findProfileStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/profile/getProfileStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/profile/saveProfileStore.js'/>"></script>
        <!--busipartner store-->
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/busipartner/findBusipartnerStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/busipartner/getBusipartnerStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/busipartner/saveBusipartnerStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/busipartner/getProBusiStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/busipartner/findProbusiStore.js'/>"></script>
        <!--busicategory store-->
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/busicategory/findBusicategoryStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/busicategory/getBusicategoryStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/busicategory/saveBusicategoryStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/busicategory/findBusicategoryProflNmStore.js'/>"></script>
        <!--notice store-->
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/notice/findNoticeStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/notice/getNoticeStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/notice/saveNoticeStore.js'/>"></script>
        <!--code store-->
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/code/getCodeStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/code/findCodeStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/code/saveCodeStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/common/findClfStore.js'/>"></script>
        <!--storage store-->
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/storage/getStorageStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/storage/findStorageStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/storage/saveStorageStore.js'/>"></script>
        <!--category store-->
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/category/categoryTreeStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/category/categoryStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/admin/category/episodeStore.js'/>"></script>

        <!--user view-->
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/user/userRight.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/user/userList.js'/>"></script>
        <!--auth view-->
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/auth/authList.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/auth/authRight.js'/>"></script>
        <!--equip view-->
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/equip/equipList.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/equip/equipRight.js'/>"></script>
        <!--category view-->
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/category/categoryList.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/category/categoryRight.js'/>"></script>
        <!--profile view-->
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/profile/profileList.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/profile/profileRight.js'/>"></script>
        <!--busipartner view-->
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/busipartner/busipartnerList.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/busipartner/busipartnerRight.js'/>"></script>
        <!--busicategory view-->
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/busicategory/busicategoryList.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/busicategory/busicategoryRight.js'/>"></script>
        <!--notice view-->
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/notice/noticeList.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/notice/noticeRight.js'/>"></script>
        <!--code view-->
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/code/codeList.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/code/codeRight.js'/>"></script>
        <!--storage view-->
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/storage/storageList.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/view/admin/storage/storageRight.js'/>"></script>


        <!--권한/카테고리 -->
        <script type="text/javascript" src="<@spring.url '/jsview/store/clip/clipSearchStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/clip/categoryTreeStore.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/clip/categoryTreeStore.js'/>"></script>


    </head>
    <script type="text/javascript">
        var context = "<@spring.url ''/>"
     var $jq = jQuery.noConflict();
	 var context = "<@spring.url ''/>"
		Ext.Loader.setConfig({enabled: true});
        var loginUserId = '${user.userId}';
    </script>

    <body>
	<div id="headerMenu" style="margin-left:20px;margin-top:10px"></div>
        <@tiles.insertAttribute name="header" />
        <div style="height:10px"></div>
        <div id="body" style="margin : 0 auto;width:100%;">
            <@tiles.insertAttribute name="body" />

            <@tiles.insertAttribute name="right" />
        </div>

    </body>

    </html>
