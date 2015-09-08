<!-- body시작 -->
<script type="text/javascript">

var perm;
<#assign perm = tpl.getAccessRule('${user.userId}', '20')>	
	<#if (perm == 'RW')>
		perm = 'RW'
	</#if>


 Ext.onReady(equipList.init);
 var arrAuthId = "";
 
 
 var traKinds = new Ext.data.ArrayStore({
				fields: ['value', 'key'],
				data: [
				       ['2', '트랜스코더1'],
				       ['3', '트랜스코더2'],
				       ['4', '트랜스코더3'],
				       ['5', '트랜스코더4'] 
				       ]
			});
			
			var trsKinds = new Ext.data.ArrayStore({
				fields: ['value', 'key'],
				data: [
				       ['2', '트랜스퍼1'],
				       ['3', '트랜스퍼2'],
				       ['4', '트랜스퍼3'],
				       ['5', '트랜스퍼4'],
				       ]
			});
			
			
</script>
<div id="equipBody" style="float:left;width:65%;"></div>
<!-- body 끝 -->