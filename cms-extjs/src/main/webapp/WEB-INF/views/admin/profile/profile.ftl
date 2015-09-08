<!-- body시작 -->
<script type="text/javascript">

var perm;
<#assign perm = tpl.getAccessRule('${user.userId}', '22')>	
	<#if (perm == 'RW')>
		perm = 'RW'
	</#if>

 Ext.onReady(profileList.init);
 var arrAuthId = "";
 
</script>
<div id="profileBody" style="float:left;width:65%;"></div>
<!-- body 끝 -->