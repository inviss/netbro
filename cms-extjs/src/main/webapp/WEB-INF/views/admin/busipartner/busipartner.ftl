<!-- body시작 -->
<script type="text/javascript">

var perm;
<#assign perm = tpl.getAccessRule('${user.userId}', '23')>	
	<#if (perm == 'RW')>
		perm = 'RW'
	</#if>

 Ext.onReady(busipartnerList.init);
 var arrProBusiId = "";
 
</script>
<div id="busipartnerBody" style="float:left;width:65%;"></div>
<!-- body 끝 -->