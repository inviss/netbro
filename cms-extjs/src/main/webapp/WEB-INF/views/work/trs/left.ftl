<!-- 좌측시작 -->
<script type="text/javascript">
 
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
 Ext.onReady(trsLeft.init);
</script>
<div id="left" style="float:left;width:15%;"></div>
<!-- 좌측 끝 -->