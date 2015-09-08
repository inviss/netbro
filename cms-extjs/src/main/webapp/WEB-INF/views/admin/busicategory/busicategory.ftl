<!-- body시작 -->
<script type="text/javascript">

var perm;
<#assign perm = tpl.getAccessRule('${user.userId}', '24')>	
	<#if (perm == 'RW')>
		perm = 'RW'
	</#if>

 Ext.onReady(busicategoryList.init);
 var arrBusicategoryId = "";
 var tmpBusiProNm = [];
 
</script>
<div id="busicategoryBody" style="float:left;width:50%;"></div>
<!-- body 끝 -->