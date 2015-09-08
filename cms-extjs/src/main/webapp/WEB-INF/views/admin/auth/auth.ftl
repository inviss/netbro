<!-- body시작 -->
<script type="text/javascript">

var perm;
<#assign perm = tpl.getAccessRule('${user.userId}', '14')>	
	<#if (perm == 'RW')>
		perm = 'RW'
	</#if>


 Ext.onReady(authList.init);
 var arrAuthId = "";
 
</script>
<div id="authBody" style="float:left;width:65%;"></div>
<!-- body 끝 -->