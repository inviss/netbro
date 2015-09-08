<!-- body시작 -->
<script type="text/javascript">
<!--등록자 ID를 입력하기 위함-->

var perm;
<#assign perm = tpl.getAccessRule('${user.userId}', '21')>	
	<#if (perm == 'RW')>
		perm = 'RW'
	</#if>


Ext.onReady(noticeList.init);

</script>
<div id="noticeBody" style="float:left;width:65%;"></div>
<!-- body 끝 -->