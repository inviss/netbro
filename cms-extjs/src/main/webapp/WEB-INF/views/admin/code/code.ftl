<!-- body시작 -->
<script type="text/javascript">

var perm;
<#assign perm = tpl.getAccessRule('${user.userId}', '18')>	
	<#if (perm == 'RW')>
		perm = 'RW'
	</#if>

 Ext.onReady(codeList.init);
 var arrAuthId = "";
 
</script>
<div id="codeBody" style="float:left;width:65%;"></div>
<!-- body 끝 -->