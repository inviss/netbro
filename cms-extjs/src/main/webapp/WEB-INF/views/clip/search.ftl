<!--<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">

<script type=text/javascript>	 
 Ext.onReady(ClipSearch.init);
</script>
 

<div id="body시작" style="float:left;width :80% "></div>
 
-->

<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">

<script type=text/javascript>	 
 var context = '<@spring.url "" />'; // 콘텍스트 설정
 Ext.onReady(ClipSearch.init);
</script>

<!-- 우측 body시작 -->

<div id="body" width="100%"></div>
<!-- 우측body끝 -->
