  
<!-- 좌측시작 -->
<script type="text/javascript">
var cornerMap = new Map();
var totalStoryBoard = new Map();
//회면별 권한
var perm=''
	<#assign perm = tpl.getAccessRule('${user.userId}', '4')>	
	<#if (perm == 'RW')>
	perm = 'RW'
	<#elseif (perm == 'R')>
	perm = 'R'
	<#else>
	perm = 'L'
	</#if>
	
 Ext.onReady(contentsLeft.init);
</script>
<div id="left" style="float:left;width:240;"></div>
<!-- 좌측 끝 -->