<!-- body시작 -->
<script type="text/javascript">

var perm;
<#assign perm = tpl.getAccessRule('${user.userId}', '17')>	
	<#if (perm == 'RW')>
		perm = 'RW'
	</#if>

 Ext.onReady(userList.init);
 var arrAuthId = "";
 
</script>
<div id="userBody" style="float:left;width:65%;"></div>
<!-- body 끝 -->