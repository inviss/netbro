<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">

<script type="text/javascript">

function saveEquipInfo(){  

    var deviceNm =$jq("#deviceNm").val();
    var deviceClfcd =$jq("#deviceClfcd").val();
    var workStatcd =$jq("#workStatcd").val();
    var deviceId =$jq("#deviceId").val();
    var deviceIp =$jq("#deviceIp").val();
   
    var chked_Radiobox = "";

    $(":radio[name='useYn']:checked").each(function(pi,po){
        chked_Radiobox += po.value;
    }); 

    equipmentInfo.deviceNm.value = deviceNm;
    equipmentInfo.deviceId.value = deviceId;
    equipmentInfo.deviceClfcd.value = deviceClfcd;
    equipmentInfo.workStatcd.value = workStatcd;
    equipmentInfo.deviceIp.value = deviceIp;
    equipmentInfo.useYn.value = chked_Radiobox;
     
          
    $jq.ajax({
        url: '<@spring.url "/admin/equipment/saveEquipInfo.ssc" />',
        type: 'POST',
        dataType: 'json',
        data: $jq('#equipmentInfo').serialize(),
        
        success: function(data){
        
            alert("저장 되었습니다.");
            findEquipList(0);
        
        }
    });

}



function updateEquipInfo(){  

    var deviceNm =$jq("#deviceNm").val();
    var deviceClfCd =$jq("#deviceClfCd").val();
    var workStatcd =$jq("#workStatcd").val();
    var deviceId =$jq("#deviceId").val();
    var deviceIp =$jq("#deviceIp").val();
    
    var chked_Radiobox = "";

    $(":radio[name='useYn']:checked").each(function(pi,po){
        chked_Radiobox += po.value;
    }); 

    equipmentInfo.deviceNm.value = deviceNm;
    equipmentInfo.deviceId.value = deviceId;
    equipmentInfo.deviceClfCd.value = deviceClfCd;
    equipmentInfo.workStatcd.value = workStatcd;
    equipmentInfo.deviceIp.value = deviceIp;
    equipmentInfo.useYn.value = chked_Radiobox;
          
    $jq.ajax({
        url: '<@spring.url "/admin/equipment/updateEquipInfo.ssc" />',
        type: 'POST',
        dataType: 'json',
        data: $jq('#equipmentInfo').serialize(),
       
        success: function(data){
        
            alert("수정 되었습니다.");
            findEquipList();
        
        }
    });

}


</script>


<div id="container">
<div id="left"> <!-- left시작 -->

    <table summary="" id = "tbody" class="board1">
    <colgroup><col width="80px"/><col width="140px"/><col width="80px"/><col width=""/><col width="100px"/><col width="80px"/></colgroup>
    <tbody>
    <tr>

    </tr>

    </tbody>
    </table>
   
   <div class="btncover19">
   <select id="equipSelectBox"  style="height : 20px;">
   <option value="id">장비ID</option>
   <option value="name">장비명</option>
   </select>
   <input name="searchEquipObj" type="text" id="searchEquipObj" onkeypress="if(event.keyCode==13){findEquipList(0);}">
   <span class="btn_pack medium" style="margin-left:360px; margin-top:-23px;"><a href="javascript:" onclick="window.location.reload(true);">목차</a></span>
   </div>
     
<!-- paginate -->
    <article class="paginate" id="paging" style="margin-top:-40px;">

    </article>
<!-- //paginate -->

    </div>
    <div class="btncover4">
    </div>

<!-- left 끝 -->
<!-- right 시작 -->
    <div id="right">
<!-- 검색박스시작 -->
        <div class="r_info">
            <div class="box2">
                <dl>
                <dd class="pdl20"> 
        <#assign perm = tpl.getAccessRule('${user.userId}', '7')>
        <#assign perm1 = tpl.getAccessRule('${user.userId}', '8')>
        <#assign perm2 = tpl.getAccessRule('${user.userId}', '9')>
        <#assign perm3 = tpl.getAccessRule('${user.userId}', '10')>
        <#assign perm4 = tpl.getAccessRule('${user.userId}', '11')>
        <#assign perm5 = tpl.getAccessRule('${user.userId}', '12')>
        <#assign perm6 = tpl.getAccessRule('${user.userId}', '13')>
        
        <select name="category" style="height:24px;width:350px;"  onChange="linkView(value);">
        <option value="<@spring.url"/admin/equipment/equipment.ssc"/>">장비 관리</option> 
        <#if (perm == 'RW' || perm =='R')>
            <option value="<@spring.url"/admin/category/category.ssc"/>">카테고리 관리</option>
        </#if>
        <#if (perm1 == 'RW' || perm1 =='R')>
            <option value="<@spring.url"/admin/auth/auth.ssc"/>">역할&권한 관리</option>    
        </#if>
        <#if (perm2 == 'RW' || perm2 =='R')>
            <option value="<@spring.url"/admin/user/user.ssc"/>">사용자 관리</option>
        </#if>
        <#if (perm3 == 'RW' || perm3 =='R')>
            <option value="<@spring.url"/admin/code/code.ssc"/>">코드 관리</option>
        </#if>
        <#if (perm4 == 'RW' || perm4 =='R')>
            <option value="<@spring.url"/admin/monitoring/monitoring.ssc"/>">모니터링 관리</option>
        </#if>
        <#if (perm5 == 'RW' || perm5 =='R')>
            <option value="<@spring.url"/admin/equipment/equipment.ssc"/>">장비 관리</option> 
        </#if>      
         <#if (perm6 == 'RW' || perm6 =='R')>
            <option value="<@spring.url"/admin/notice/notice.ssc"/>">공지사항 관리</option> 
        </#if>  
        </select></dd>
                </dl>
            </div>
<!-- 기본정보 시작 -->
            <dl class="info dark" id="info dark"">
                <dt>장비정보</dt>
                <dd>
                <table summary="" class="board2">
                <colgroup><col width="100px"></col><col width="286px"></col></colgroup>
                <tbody>
                <tr><th>장비ID</th><td><input name="deviceId" id ="deviceId" type="text" class="ip_text"></input></td></tr>
                <tr><th>장비명</th><td><input type="text" name="deviceNm" id="deviceNm" class="ip_text"></input></td></tr>
                <tr><th>장비인스턴스</th><td><input type="text" name="workStatcd" id="workStatcd" class="ip_text"></input></td></tr>
                <tr><th>IP</th><td><input type="text" name="deviceIp" id="deviceIp" class="ip_text"></input></td></tr>
                <tr id ="useYn"><th>사용여부</th><td>사용&nbsp;<input name="useYn" id="useYn" type="radio" value="Y">&nbsp;&nbsp;미사용&nbsp;<input name="useYn" id="useYn" type="radio" value="N"></td></td></tr>
                </tbody>
                </table>
                </dd>
            </dl>
<!-- 기본정보 끝 -->
        </div>
            <div class="btncover2">
            <!--<a href="javascript:"  onclick="saveEquipInfo();"><img src="<@spring.url"/images/save_off.gif"/>" title="저장" alt="저장" onMouseOver="this.src='<@spring.url "/images/save_on.gif"/>'" onMouseOut="this.src='<@spring.url "/images/save_off.gif"/>'"></a>-->
            <!--권한별 버튼-->
            <!--'6'  menuId-->
            <@button '12' '${user.userId}' 'saveEquipInfo()' '저장' />
            </div>
<!-- 검색박스끝 -->
    </div>
<!-- right 끝 -->
</div>
<!-- 본문끝 -->
</div>