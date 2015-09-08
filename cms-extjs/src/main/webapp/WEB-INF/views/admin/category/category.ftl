<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">

<script type=text/javascript>	 

var perm;
<#assign perm = tpl.getAccessRule('${user.userId}', '15')>	
	<#if (perm == 'RW')>
		perm = 'RW'
	</#if>


Ext.onReady(categoryList.init);
</script>

<!-- 우측 body시작 -->

<div id="categoryBody" style="float:left;width:50%;"></div>
<!-- 우측body끝 -->

