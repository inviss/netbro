<!-- body시작 -->
<script type="text/javascript">

var perm;
<#assign perm = tpl.getAccessRule('${user.userId}', '19')>	
	<#if (perm == 'RW')>
		perm = 'RW'
	</#if>


Ext.onReady(storageList.init);

</script>
<div id="storageBody" style="float:left;width:65%;"></div>
<!-- body 끝 -->