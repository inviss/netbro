<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl" >
<!-- 헤더시작 -->
<script type=text/javascript>
var menuList="";
var size="";
var logInUserId='guest';
var clipSearchListURL = '<@spring.url "/clip/findClipSearchList.ssc" />';
var categoryRootListURL = '<@spring.url "/clip/findCategoryList.ssc" />';
var img = '<@spring.url "/images/logo.png" />';
Ext.onReady(Header.init );
</script>
<header id="header">
	<form name="header" method = "post">
	</form>
	<#assign ctx>clipsrch</#assign>
		<#if '${default}' != "true">
  		
  		<#list tpl.findUserMenus('${user.userId}') as menu>
  		
  		<script type=text/javascript>
			
			 var ${menu.menuEnNm}_button = Ext.create('Ext.Button',{
			
			 text : '${menu.menuNm}',
			 flex : 1,
			 margin:'0 5 0 0',
             href: '<@spring.url "${menu.url}"/>'
             ,hrefTarget:'_self'
            ,enableToggle: true
			 });
		
			menuList +=  '${menu.menuEnNm}_button' +","
		
			
		</script>
		</#list>

  	</#if>

	<!-- Popup 로그인-->
               <div class="layer">
                <div class="bg"></div>
                <div id="layer2" class="pop-layer">
                <div class="pop-conts">
                <img src="<@spring.url '/images/logo.png' />" title="CMS" style="margin-left:10px;margin-top:9px;"/>
                <form name="login" id="login" method="post">
                    <ul id="loginUI">
                   		<ul>         
                        	<li><input name="userId" id="userId" value="" type="text" style="margin-left:8px;"></li>
                        </ul>
                        <ul>
                        	<li><input name="userPasswd" id="userPasswd" value="" type="password" style="margin-left:8px;" onkeypress="if(event.keyCode==13){getUseYn();layer_open('layer1');return false;};"></li>
                        </ul>
                    </ul>
                    </form>
                    <div class="btncover14"><span class="btn_pack gry" style="margin-left:45px";><a onClick="getUseYn();layer_open('layer1');return false;">확인</a></span></div>
                </div>
                </div>
                </div>
                <!-- //Popup -->
                
<form name="userInfo" id="userInfo" method="post" >
    <@spring.bind "search" />
    <input type="hidden" name="userId" />
    <input type="hidden" name="userPasswd" />
</form>
	
<script type="text/javascript">


function checkLogout(){
	document.header.action="<@spring.url '/loginout.ssc'/>";
	document.header.submit();
}
	
function checkLogin(){
	document.login.action="<@spring.url '/userLoginAction.ssc'/>";
	document.login.submit();
	
}

//userId와 userPasswd를 이용해 alert창 띄우는 function
function getUseYn() {

    var userId = $jq('#userId').val();
    var userPasswd = $jq('#userPasswd').val();

    userInfo.userId.value = userId;
    userInfo.userPasswd.value = userPasswd;

    $jq.ajax({
        url: '<@spring.url "/getUseYn.ssc" />',
        type: 'POST',
        dataType: 'json',
        data: $jq('#userInfo').serialize(),
        error: function (r, e) {
            alert('return error ' + r.status);
        },
        success: function (data) {

            if(data.user == "Y") {
                checkLogin();
            } else {
                alert("ID와 비밀번호가 일치하지 않습니다.");
            }

        }
    });

}


</script>
</header>
<!-- 헤더끝 -->