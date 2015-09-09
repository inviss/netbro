<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl" >
<!-- 헤더시작 -->
<header id="header">
	<#assign ctx>statistic</#assign>
	<form name="header" method = "post">
	</form>
    <h1><img src="<@spring.url "/images/logo.png" />" id="logo"title="NetBro-cms" alt="NetBro-cms" class="logo"/></h1>
    <nav class="gnb">
  	<ul class="mMenu" id="topmenu">
		<@menu tpl.findUserMenus('${Session.user.userId}') '${ctx}' />
	</ul>
	</nav>
<script type="text/javascript">

function checkLogout(){
	document.header.action="<@spring.url '/loginout.ssc'/>";
	document.header.submit();
}
	
</script>
</header>
<!-- 헤더끝 -->