<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};


window.onload=function(){   
    findEquipList(0);
}


function findEquipList(pageNum){

var pageView = 5;

equipmentInfo.equipSelectBox.value = $jq('#equipSelectBox').val();
equipmentInfo.searchEquipObj.value = $jq('#searchEquipObj').val();

    $jq.ajax({
        url: '<@spring.url "/admin/equipment/findEquipmentList.ssc" />',
        type: 'POST',
        dataType: 'json',
        data: $jq('#equipmentInfo').serialize(),
        
        success: function(data){
        
        if(data.result == "Y"){
        
            $jq('#tbody').empty();
                                         
            var table ="";
    
            table +='<colgroup><col width="100px"/><col width="100px"/><col width="150px"/><col width=""/><col width="120px"/><col width="100px"/></colgroup>'
            table +='<tbody>'
            table +='<tr>'
            table +='<th>장비ID</th><th>장비명</th><th>장비인스턴스</th><th>IP</th><th>사용여부</th>'
            table +='</tr>'
            
            for(var i = 0; i<data.equipmentTbls.length; i++){           
                var regDt = new Date(parseInt(data.equipmentTbls[i].regDt, 10));
                
                if(i%2 == 0){
                    table +='<tr>'
                 }else{
                    table +='<tr class="bgc2">';
                 }
                    if(data.equipmentTbls[i].useYn == "Y"){
                        tmpUserYn = "사용";  
                    }else{
                        tmpUserYn = "미사용";
                    }
                    
                    table +='<td><a href="javascript:" onclick="getEquipInfo(\''+data.equipmentTbls[i].deviceId+'\')">'+data.equipmentTbls[i].deviceId+'</td>'
                    table +='<td><a href="javascript:" onclick="getEquipInfo(\''+data.equipmentTbls[i].deviceId+'\')">'+data.equipmentTbls[i].deviceNm+'</td>'
                    table +='<td><a href="javascript:" onclick="getEquipInfo(\''+data.equipmentTbls[i].deviceId+'\')">'+data.equipmentTbls[i].deviceClfCd+'</td>'
                    table +='<td><a href="javascript:" onclick="getEquipInfo(\''+data.equipmentTbls[i].deviceId+'\')">'+data.equipmentTbls[i].deviceIp+'</td>'
                    table +='<td><a href="javascript:" onclick="getEquipInfo(\''+data.equipmentTbls[i].deviceId+'\')">'+tmpUserYn+'</td>'
                    table +='</tr>'
            }
        
            table +='</tbody>'
            table +='</table>'
            
            $jq('#tbody').append(table);
            
            $jq('#paging').empty();
            
            var paging = "";
            
            paging +='<article class="paginate">'
            paging +='<a class="direction " href="#" >'
            //table +='<span>?</span> 이전</a>'
            
            
            //현재페이지중 최대 페이지값 default는 pageView의 값을 가진다.
            var currentMaxPaging = pageView;
            
            //최대페이지수
            var maxPaging;
                
            //화면의 시작페이지 default는 0
            var startPage=0;
                
            //화면의 종료 페이지 defualt는 0
            var EndPage=0;
                
            //페이지html받는 변수
            var paging="";
                
            //총페이지를 정수로 구한다.
            var pages = Math.ceil(data.totalCount/data.pageSize);
                
            //총 페이지bar수를 구한다.
            var totalPageBar = Math.ceil(pages/pageView);
                
            //마지막 페이지 bar 여부를 판단한다. pageView값과 비교해 같거나 작다면 마지막 페이지 bar 이다.
            var lastNum = "";
            
            var lastPageBarYn = "";
                    
            if(pageNum%pageView != 0){
                lastPageBarYn=Math.ceil(pageNum/pageView);
            }else{
                lastPageBarYn=Math.ceil(pageNum/pageView)+1;
            }
    
            if(pageNum<pageView && (pageNum/pageView)<1 && pages<pageView){//최초 페이지bar화면이라면 기본값 셋팅
                currentMaxPaging = pages;
                startPage = 0;              
            }else{
            //마지막 페이지 bar이라면 총페이지를 마지막 페이지로 설정한다.
                if(lastPageBarYn == totalPageBar || pages <(currentMaxPaging*(Math.ceil(pageNum/pageView))) || pages == (pageNum + 1)){     
                    currentMaxPaging = pages;
                    startPage = pages - (pages%pageView);
                    maxPaging = currentMaxPaging*(Math.ceil(pageNum/pageView)); 
                }else{      
                    if((pageNum%pageView)!= 0){//현재페이지와 설정pageView의 나머지가 0이 아니라면...
                        currentMaxPaging = currentMaxPaging * (Math.ceil(pageNum/pageView));
                        maxPaging = currentMaxPaging * (Math.ceil(pageNum/pageView)-1);
                    }else{//현재페이지와 설정pageView 나머지가 0이라면 ...
                        currentMaxPaging = currentMaxPaging * (Math.floor(pageNum/pageView)+1);
                        maxPaging = currentMaxPaging * (Math.floor(pageNum/pageView));
                    }
                        startPage = currentMaxPaging-pageView;
                    }
                
                }
                
            if(pageView > pageNum || pageView == pages || pageView > pages){
            
            }else {
        
                paging +='<a class="direction" href="#" onClick="findEquipList('+(startPage-pageView)+');return false;">';
                paging +='<span>‹</span>이전</a>';
            }
            
            for(var page = startPage; page<currentMaxPaging; page++ ){
                if(pageNum == page){
                    paging +='<strong>'+(page+1)+'</strong>';
                }else{
                    paging +='<a href="#" onClick="findEquipList('+(page)+')">'+(page+1)+'</a>';
                }
            }
        
            if(lastPageBarYn == totalPageBar || (pageNum+1) == pages || (pages-currentMaxPaging) <= 0){
        
            }else if(pages != pageView){
                paging +='<a class="direction" href="#" onClick="findEquipList('+(currentMaxPaging)+');return false;">다음 <span>›</span></a>';                
            }else if(pageView<data.totalCount/data.pageSize){
                paging +='<a class="direction" href="#" onClick="findEquipList('+(currentMaxPaging)+');return false;">다음 <span>›</span></a>';                
            }
            //table +='<a class="direction" href="#">다음 <span>?</span></a>'
            
            paging +='</article>'
            
            paging +='</div>'
            
            paging +='</div>'
            
            //$jq('#paging').append(paging);
        }else{
            alert(data.reason);
        }
                         
        }
    });
}


function getEquipInfo(deviceId){   

    equipmentInfo.deviceId.value = deviceId;
            
    $jq.ajax({
        url: '<@spring.url "/admin/equipment/getEquipmentInfo.ssc" />',
        type: 'GET',
        dataType: 'json',
        data: $jq('#equipmentInfo').serialize(),
        
        success: function(data){
        
        if(data.result == "Y"){
            $jq('#right').empty();
            
            var table = "";
            
            table+='<div id="right">'
            table+='<div class="r_info">'
            table+='<div class="box2">'
            table+='<dl>'
            table+='<dd class="pdl20">'
            table +='<#assign perm = tpl.getAccessRule('${user.userId}', '7')>'
            table +='<#assign perm1 = tpl.getAccessRule('${user.userId}', '8')>'
            table +='<#assign perm2 = tpl.getAccessRule('${user.userId}', '9')>'
            table +='<#assign perm3 = tpl.getAccessRule('${user.userId}', '10')>'
            table +='<#assign perm4 = tpl.getAccessRule('${user.userId}', '11')>'
            table +='<#assign perm5 = tpl.getAccessRule('${user.userId}', '12')>' 
            table+='<select name="user" style="height:24px;width:350px;" onChange="linkView(value);">'
                        
           table +='<option value="<@spring.url "/admin/equipment/equipment.ssc"/>">장비관리</option>'
                <#if (perm == 'RW' || perm =='R')>
                     table +='<option value="<@spring.url"/admin/category/category.ssc"/>">카테고리 관리</option>'
                </#if>
                <#if (perm1 == 'RW' || perm1 =='R')>
                table +='<option value="<@spring.url "/admin/auth/auth.ssc"/>">역할&권한 관리</option>'
                </#if> 
                <#if (perm2 == 'RW' || perm2 =='R')>         
                table +='<option value="<@spring.url "/admin/user/user.ssc"/>">사용자 관리</option>'
                </#if>
                <#if (perm3 == 'RW' || perm3 =='R')>     
                table +='<option value="<@spring.url "/admin/code/code.ssc"/>">코드 관리</option>'
                </#if>
                <#if (perm4 == 'RW' || perm4 =='R')>
                table +='<option value="<@spring.url "/admin/monitoring/monitoring.ssc"/>">모니터링 관리</option>'
                </#if>
                <#if (perm5 == 'RW' || perm5 =='R')>
                table +='<option value="<@spring.url "/admin/equipment/equipment.ssc"/>">장비관리</option>'
                </#if>       
            table+='</select></dd>'
            table+='</dl>'
            table+='</div>'
            table+='<dl class="info dark" id="info dark"">'
            table+='<dt>장비정보</dt>'
            table+='<dd>'
            table+='<table summary="" class="board2">'
            table+='<colgroup><col width="100px"></col><col width="286px"></col></colgroup>'
            table+='<tbody>'
            table+='<tr><th>장비ID</th><td><input name="deviceId" id ="deviceId" type="text" class="ip_text" value="'+data.equipmentTbl.deviceId+'" readOnly></input></td></tr>'
            table+='<tr><th>장비명</th><td><input type="text" name="deviceNm" id="deviceNm" class="ip_text" value="'+data.equipmentTbl.deviceNm+'"></input></td></tr>'
            table+='<tr><th>장비인스턴스</th><td><input type="text" name="deviceClfCd" id="deviceClfCd" class="ip_text" value="'+data.equipmentTbl.deviceClfCd+'"></input></td></tr>'
            table+='<tr><th>IP</th><td><input type="text" name="deviceIp" id="deviceIp" class="ip_text" value="'+data.equipmentTbl.deviceIp+'"></input></td></tr>'
            
            if(data.equipmentTbl.useYn == "Y"){
                table +='<tr id ="useYn"><th>사용여부</th><td>사용&nbsp;<input name="useYn" id="useYn" type="radio" value="Y" checked>&nbsp;&nbsp;미사용&nbsp;<input name="useYn" id="useYn" type="radio" value="N"></td></td></tr>'
            }else{
                table +='<tr id ="useYn"><th>사용여부</th><td>사용&nbsp;<input name="useYn" id="useYn" type="radio" value="Y">&nbsp;&nbsp;미사용&nbsp;<input name="useYn" id="useYn" type="radio" value="N" checked></td></td></tr>'
            }
            
            table+='</tbody>'
            table+='</table>'
            table+='</dd>'
            table+='</dl>'
            table+='</div>'
            table+='<div class="btncover2">'
            table+=<@ajaxButton '12' '${user.userId}' 'resetEquipInfo()' '추가' />
            table +='&nbsp;'
            table+=<@ajaxButton '12' '${user.userId}' 'updateEquipInfo()' '저장' />
            table+='</div>'
            table+='</div>'
            table+='</div>'
            $jq('#right').append(table);
        }else{
             alert(data.reason);
        }
        
        }
    });
}



function resetEquipInfo(){
  
    $jq.ajax({
        url: '<@spring.url "/admin/equipment/getEquipmentInfo.ssc" />',
        type: 'GET',
        dataType: 'json',
        data: $jq('#equipmentInfo').serialize(),
        
        success: function(data){
        
        $jq('#right').empty();
        
        var table = "";
        
        table+='<div id="right">'
        table+='<div class="r_info">'
        table+='<div class="box2">'
        table+='<dl>'
        table+='<dd class="pdl20">' 
        table +='<select name="user" style="height:24px;width:350px;" onChange="linkView(value);">'
        table +='<option value="<@spring.url "/admin/equipment/equipment.ssc"/>">장비관리</option>'
        <#if (perm == 'RW' || perm =='R')>
            table +='<option value="<@spring.url"/admin/category/category.ssc"/>">카테고리 관리</option>'
        </#if>
        <#if (perm1 == 'RW' || perm1 =='R')>
            table +='<option value="<@spring.url "/admin/auth/auth.ssc"/>">역할&권한 관리</option>'
        </#if> 
        <#if (perm2 == 'RW' || perm2 =='R')>         
            table +='<option value="<@spring.url "/admin/user/user.ssc"/>">사용자 관리</option>'
        </#if>
        <#if (perm3 == 'RW' || perm3 =='R')>     
            table +='<option value="<@spring.url "/admin/code/code.ssc"/>">코드 관리</option>'
        </#if>
        <#if (perm4 == 'RW' || perm4 =='R')>
            table +='<option value="<@spring.url "/admin/monitoring/monitoring.ssc"/>">모니터링 관리</option>'
        </#if>
        <#if (perm5 == 'RW' || perm5 =='R')>
            table +='<option value="<@spring.url "/admin/equipment/equipment.ssc"/>">장비관리</option>'
        </#if>    
        table+='</select></dd>'
        table+='</dl>'
        table+='</div>'
        table+='<dl class="info dark" id="info dark"">'
        table+='<dt>장비정보</dt>'
        table+='<dd>'
        table+='<table summary="" class="board2">'
        table+='<colgroup><col width="100px"></col><col width="286px"></col></colgroup>'
        table+='<tbody>'
        table+='<tr><th>장비ID</th><td><input name="deviceId" id ="deviceId" type="text" class="ip_text" value=""></input></td></tr>'
        table+='<tr><th>장비명</th><td><input type="text" name="deviceNm" id="deviceNm" class="ip_text" value=""></input></td></tr>'
        table+='<tr><th>장비인스턴스</th><td><input type="text" name="deviceClfCd" id="deviceClfCd" class="ip_text" value=""></input></td></tr>'
        table+='<tr><th>IP</th><td><input type="text" name="deviceIp" id="deviceIp" class="ip_text" value=""></input></td></tr>'
        
        if(data.equipmentTbl.useYn == "Y"){
            table +='<tr id ="useYn"><th>사용여부</th><td>사용&nbsp;<input name="useYn" id="useYn" type="radio" value="Y">&nbsp;&nbsp;미사용&nbsp;<input name="useYn" id="useYn" type="radio" value="N"></td></td></tr>'
        }else{
            table +='<tr id ="useYn"><th>사용여부</th><td>사용&nbsp;<input name="useYn" id="useYn" type="radio" value="Y">&nbsp;&nbsp;미사용&nbsp;<input name="useYn" id="useYn" type="radio" value="N"></td></td></tr>'
        }
        
        table+='</tbody>'
        table+='</table>'
        table+='</dd>'
        table+='</dl>'
        table+='</div>'
        table+='<div class="btncover2">'
        table+=<@ajaxButton '12' '${user.userId}' 'saveEquipInfo()' '저장' />
        table+='</div>'
        table+='</div>'
        table+='</div>'
        $jq('#right').append(table);

        }
    });
}

</script>


<form name="equipmentInfo" id="equipmentInfo" method="post" >
    <@spring.bind "search" />
    <@spring.bind "equipTbl" />
    <input type="hidden" name="deviceId" />
    <input type="hidden" name="deviceIp" />
    <input type="hidden" name="deviceClfCd" />
    <input type="hidden" name="workStatcd" />
    <input type="hidden" name="useYn">
    <input type="hidden" name="deviceNm">
    <input type="hidden" name="searchEquipObj">
    <input type="hidden" name="equipSelectBox">
    <input type="hidden" name="pageNo">
</form>





