<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">

 
function checkLogin(){   
	document.login.action="<@spring.url '/userLoginAction.ssc'/>";
	document.login.submit();
}

function keyEnter() {   
	if(event.keyCode ==13) {   //엔터키가 눌려지면,
		checkLogin(login);    //로그인 함수 호출
	}
}

</script>




<section id="wrap">
	<section id="login" class="g_login">
		<img src="<@spring.url '/images/logo.png' />" title="CMS" />
		<form name="login" id="login" method="post">
		<@spring.bind "search" />
			   <fieldset>
			    <legend>login</legend>
			    <div class="item">
			        <input name="userId" id="userId" value="" type="text" style="color:#767676;ime-mode:inactive;" >
			    </div>
			    <div class="item">
			        <input name="userPasswd" id="userPasswd" value="" type="password" style="color:#767676" onKeyDown="keyEnter();">
			    </div>
			    <p class="keeping">
			    
			    <article class="btncover10">
			    	<span class="btn_pack xlarge"><a href="javascript:"  onclick="checkLogin()"><img src="<@spring.url "/images/login_on.png"/>"</a></span>
			    </article>
			    </fieldset>
		    </form>
	</section>
</section>


