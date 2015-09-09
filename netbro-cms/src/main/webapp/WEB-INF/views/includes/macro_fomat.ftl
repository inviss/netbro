<#-- 날짜관련 공통 포맷 -->
<#macro dateFormt date><#if (date?length == 8)>${date[0..3]}/${date[4..5]}/${date[6..7]}<#else>Date length Error</#if></#macro>

<#macro dateTimeFormt date><#if (date?length == 14)>${date[0..3]}/${date[4..5]}/${date[6..7]} ${date[8..9]}:${date[10..11]}:${date[12..13]}<#else>Date length Error</#if></#macro>

<#-- CommonData를 보여주는 ComboBox -->
<#macro commCombo commonList selValue>
	<#list commonList as common>
	<option value='${common.devVal}' <#if common.devVal?number == selValue?number>selected</#if>>${common.cmmCdNm}</option>
	</#list>
</#macro>

<#-- 카테고리를 보여주는 ComboBox -->
<#macro category srvTypList selValue>
	<#list srvTypList as srvTyp>
	<option value='${srvTyp.typId}' <#if srvTyp.typId?number == selValue?number>selected</#if>>${srvTyp.typNm}</option>
	</#list>
</#macro>

<#-- 분류 보여주는 ComboBox -->
<#macro itemCla chkClas selValue>
	<#list chkClas as chkCla>
	<option value='${chkCla.claId}' <#if chkCla.claId?number == selValue?number>selected</#if>>${chkCla.claNm}</option>
	</#list>
</#macro>

<#-- 사용자권한명 보여주는 ComboBox -->
<#macro usrPerm usrPerms selValue>
	<#list usrPerms as usrprm>
	  <#if (usrprm.permId?number==9) != true>
		<option value='${usrprm.permId}' <#if usrprm.permId?number == selValue?number>selected</#if>>${usrprm.permNm}</option>
	  </#if>
	</#list>
</#macro>

<#-- ArrayString를 보여주는 ComboBox -->
<#macro arrayCombo arrayIdx arrayVal selValue>
	<#list arrayIdx as idx>
	<option value='${idx}' <#if idx?string == selValue?string>selected</#if>>${arrayVal[idx_index]}</option>
	</#list>
</#macro>

<#-- CommonData를 보여주는 RadioBox -->
<#macro commRadio optName commonList selValue>
	<#list commonList as common>
	<input type="radio" id="${optName}" name="${optName}" value="${common.devVal}" <#if common.devVal?number == selValue?number>checked</#if> />${common.cmmCdNm}
	</#list>
</#macro>

<#-- ArrayString를 보여주는 CheckBox -->
<#macro arrayRadio optName arrayIdx arrayVal selValue>
	<#list arrayIdx as idx>
	<input type="radio" id="${optName}" name="${optName}" value="${idx}" <#if idx?string == selValue?string>checked</#if> />${arrayVal[idx_index]}
	</#list>
</#macro>

<#-- CommonData를 보여주는 CheckBox -->
<#macro commCheck optName commonList selValue>
	<#list commonList as common>
		<input type="checkbox" id="${optName}_${common_index}" name="${optName}_${common_index}" value="${common.devVal}" <#if common.devVal?number == selValue?number>checked</#if> />${common.cmmCdNm}
		<#if common_has_next></br></#if>
	</#list>
</#macro>

<#-- ArrayString를 보여주는 CheckBox -->
<#macro arrayCheck optName arrayIdx arrayVal selValue>
	<#list arrayIdx as idx>
		<input type="checkbox" id="${optName}_${idx_index}" name="${optName}_${idx_index}" value="${idx}" <#if idx?string == selValue?string>checked</#if> />${arrayVal[idx_index]}
		<#if idx_has_next></br></#if>
	</#list>
</#macro>

<#-- CommonData를 보여주는 String ul-li -->
<#macro commUl commonList>
	<ul>
	<#list commonList as common>
		<li>${common.cmmCdNm}</li>
	</#list>
	</ul>
</#macro>

<#-- Pagination -->
<#macro paging pageList currPage prefix>
	<#if (currPage > pageList.pageIndexCount)><a href="javascript:void(0)" onClick="goPage('1')"><img src="${prefix}/img/bul_firstpage.gif" alt="처음으로"></a></#if>
	<#if (pageList.previousIndex > 1)><a href="javascript:void(0)" onClick="goPage('${pageList.previousIndex}');"><img src="${prefix}/img/bul_pre.gif" alt="이전으로"></a></#if>
	<#list pageList.indexs as index>
		<#if (currPage == index)><span>${index}</span><#elseif index?int==0>...<#else><a href="javascript:void(0)" onClick="goPage('${index}')">${index}</a></#if>
	</#list>
	<#if (pageList.nextIndex < pageList.lastIndex?number)><a href="javascript:void(0)" onClick="goPage('${pageList.nextIndex}')"><img src="${prefix}/img/bul_next.gif" alt="다음으로"></a></#if>
	<#if (currPage < pageList.lastIndex)><a href="javascript:void(0)" onClick="goPage('${pageList.lastIndex}')"><img src="${prefix}/img/bul_lastpage.gif" alt="마지막으로"/></a></#if>
</#macro>






<#-- Top User Menu -->
<#macro menu menuList prefix >	
	
	<#list menuList as menu>


	<#if menu.menuEnNm == "${ctx}">
        <#if menu.controlGubun == "RW" || menu.controlGubun == "R" >
            <img src="<@spring.url "/images/${menu.menuEnNm}_focus.png"/>" title="${menu.menuNm}" alt="${menu.menuNm}"/ id="${menu.menuEnNm}">
        </#if>
	<#else>
		<a href="<@spring.url "${menu.url}"/>"><img src="<@spring.url "/images/${menu.menuEnNm}_on.png"/>" title="${menu.menuNm}" alt="${menu.menuNm}" id="${menu.menuEnNm}" onMouseOver="this.src='<@spring.url "/images/${menu.menuEnNm}_off.png"/>'" onMouseOut="this.src='<@spring.url "/images/${menu.menuEnNm}_on.png"/>'"></a>
	</#if>
	
	</#list>
        <a href="javascript:void(0)"  onclick="checkLogout();"><img src="<@spring.url "/images/login_out.png"/>" id ="loginOut" title="로그아웃" alt="로그아웃" ></a>
</#macro>

<#-- Top User Menu -->
<#macro submenu menuList prefix >  
    <select name="user" style="height:24px;width:130px; margin-left:927px;margin-top:20px;" onChange="linkView(value);">
   
    
    <#list menuList as menu>
    
   
    <#if menu.controlGubun == "RW" || menu.controlGubun == "R" >
         <option value="<@spring.url"/admin/auth/auth.ssc"/>">역할&권한 관리</option>          
         <option value="<@spring.url"/admin/user/user.ssc"/>">사용자 관리</option>
    </#if>    

     </#list>
 
  
        </select>
     

</#macro>






<#-- User Button Permit -->
<#-- 메뉴ID, 사번, 버튼 Properties ID값을 넣어주면 권한에 따라 버튼을 표시한다. -->
<#macro submenu1 menuId userId  url css>
	<#if menuId?exists && (menuId?length > 0)>
		<#assign perm = tpl.getAccessRule(userId, menuId)>
			<#if (perm == 'RW')>
			<#if (css?length > 2)>
				<span class="btn_pack xlarge"><a href="javascript:" onclick="${url}">${css}</a></span>				
			<#else>			
				<span class="btn_pack xlarge"><a href="javascript:" onclick="${url}">&nbsp;&nbsp;&nbsp;${css}&nbsp;&nbsp;&nbsp;</a></span>			
			</#if>
			
				
			<#elseif (perm == 'R')>
			     <a href="javascript:" onclick="${url}"></a>
			<#else>
		
			</#if>
	</#if>
</#macro>


<#macro button menuId userId  url css>
    <#if menuId?exists && (menuId?length > 0)>
        <#assign perm = tpl.getAccessRule(userId, menuId)>
            <#if (perm == 'RW')>
            <#if (css?length > 2)>
                <span class="btn_pack xlarge"><a href="javascript:" onclick="${url}">${css}</a></span>              
            <#else>         
                <span class="btn_pack xlarge"><a href="javascript:" onclick="${url}">&nbsp;&nbsp;&nbsp;${css}&nbsp;&nbsp;&nbsp;</a></span>          
            </#if>
            
                
            <#elseif (perm == 'R')>
                 <a href="javascript:" onclick="${url}"></a>
            <#else>
        
            </#if>
    </#if>
</#macro>


<#macro button1 menuId userId  url css id >
    <#if menuId?exists && (menuId?length > 0)>
        <#assign perm = tpl.getAccessRule(userId, menuId)>
            <#if (perm == 'RW')>
            <#if (css?length > 2)>
                <span class="btn_pack xlarge"><a href="javascript:" id="${id}" onclick="${url}">${css}</a></span>              
            <#else>         
                <span class="btn_pack xlarge"><a href="javascript:" id="${id}" onclick="${url}">&nbsp;&nbsp;&nbsp;${css}&nbsp;&nbsp;&nbsp;</a></span>          
            </#if>
            
                
            <#elseif (perm == 'R')>
                 <a href="javascript:" onclick="${url}"></a>
            <#else>
        
            </#if>
    </#if>
</#macro>

<#macro ajaxButton menuId userId url css>
	<#if menuId?exists && (menuId?length > 0)>
		<#assign perm = tpl.getAccessRule(userId, menuId)>	
			<#if (perm == 'RW')>
			<#if (css?length > 2)>
				'<span class="btn_pack xlarge"><a href="javascript:" onclick="${url}">${css}</a></span>'
			<#else>
				'<span class="btn_pack xlarge"><a href="javascript:" onclick="${url}">&nbsp;&nbsp;&nbsp;${css}&nbsp;&nbsp;&nbsp;</a></span>'		
			</#if>
			
			<#elseif (perm == 'R')>
			    '<a href="javascript:" onclick="${url}"></a>'
			<#else>

			</#if>
	</#if>
</#macro>



<#macro ajaxMonitoringButton menuId userId url css>
    <#if menuId?exists && (menuId?length > 0)>
        <#assign perm = tpl.getAccessRule(userId, menuId)>  
            <#if (perm == 'RW')>
            <#if (css?length > 2)>
                '<span class="btn_pack medium" style="margin-left:68px;margin-top:-52px;"><a href="javascript:" onclick="${url}">${css}</a></span>'
            <#else>
                '<span class="btn_pack medium" style="margin-left:68px;margin-top:-52px;"><a href="javascript:" onclick="${url}">${css}</a></span>'        
            </#if>
            
            <#elseif (perm == 'R')>
                '<a href="javascript:" onclick="${url}"></a>'
            <#else>

            </#if>
    </#if>
</#macro>


<#macro traButton menuId userId url css>
    <#if menuId?exists && (menuId?length > 0)>
        <#assign perm = tpl.getAccessRule(userId, menuId)>  
            <#if (perm == 'RW')>
                <span class="btn_pack medium"><a href="javascript:" onclick="${url}">${css}</a></span>
            <#elseif (perm == 'R')>
                <a href="javascript:" onclick="${url}"></a>
            <#else>

            </#if>
    </#if>
</#macro>

<#macro traButtonId menuId userId url css id>
    <#if menuId?exists && (menuId?length > 0)>
        <#assign perm = tpl.getAccessRule(userId, menuId)>  
            <#if (perm == 'RW')>
                <span class="btn_pack medium"><a id="${id}" href="javascript:" onclick="${url}">${css}</a></span>
            <#elseif (perm == 'R')>
                <a id="${id}" href="javascript:" onclick="${url}"></a>
            <#else>

            </#if>
    </#if>
</#macro>


<#-- User Button Permit -->
<#-- 메뉴ID, 사번, 버튼 Properties ID값을 넣어주면 권한에 따라 버튼을 표시한다. -->
<#macro statisticButton menuId userId  url css>
	<#if menuId?exists && (menuId?length > 0)>
		<#assign perm = tpl.getAccessRule(userId, menuId)>
			<#if (perm == 'RW')>
			<#if (css?length > 2)>
				<span class="btn_pack gry"><a href="javascript:" onclick="${url}">${css}</a></span>				
			<#else>			
				<span class="btn_pack gry"><a href="javascript:" onclick="${url}">&nbsp;&nbsp;&nbsp;${css}&nbsp;&nbsp;&nbsp;</a></span>			
			</#if>
			
				
			<#elseif (perm == 'R')>
			     <a href="javascript:" onclick="${url}"></a>
			<#else>
		
			</#if>
	</#if>
</#macro>
