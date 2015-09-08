<!-- 좌측시작 -->
<script type="text/javascript">

 var cornerMap = new Map();
//회면별 권한
var perm=''
   
	<#assign perm = tpl.getAccessRule('${user.userId}', '2')>	
	<#if (perm == 'RW')>
	perm = 'RW'
	<#elseif (perm == 'R')>
	perm = 'R'
	<#else>
	perm = 'L'
	</#if> 
  Ext.onReady(Top.init);
</script>
<div id="top" width="100%"></div>
<!-- 좌측 끝 -->