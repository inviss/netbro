<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};


window.onload=function(){   
    findUserList(0);
}


function findUserList(pageNum){

var pageView = 5;
var tmpUserYn = "";

if(pageNum == "all"){
        pageNum = 0;
        userInfo.pageNo.value = 0;   
        userInfo.searchUserObj.value = '';
        userInfo.userSelectBox.value = '';
    }else{
        userInfo.pageNo.value =  pageNum;
        userInfo.searchUserObj.value = $jq('#searchUserObj').val();
        userInfo.userSelectBox.value = $jq('#userSelectBox').val();
    }

if(userInfo.searchUserObj.value == "undefined"){
    userInfo.searchUserObj.value = "";
}


    $jq.ajax({
        url: '<@spring.url "/admin/user/findUserList.ssc" />',
        type: 'GET',
        dataType: 'json',
        data: $jq('#userInfo').serialize(),
        
        success: function(data){
         if(data.result == "Y"){                
        $jq('#tbody').empty();
                                     
        var table ="";
        var tmpUserId = "";
        var tmpAuthNm = "";
        var tmpSeq = "";

        table +='<colgroup><col width="100px"/><col width="100px"/><col width="150px"/><col width=""/><col width="120px"/><col width="100px"/></colgroup>'
        table +='<tbody>'
        table +='<tr>'
        table +='<th>ID</th><th>이름</th><th>역할</th><th>등록일</th><th>사용여부</th>'
        table +='</tr>'
        
        for(var i = 0; i<data.userInfos.length; i++){   
        
            if(tmpUserId == data.userInfos[i].userId){
                tmpAuthNm = data.userInfos[i].authNm+", "+data.userInfos[i-1].authNm;  
            }else{
                tmpAuthNm = data.userInfos[i].authNm;
            } 

            var regDt = new Date(parseInt(data.userInfos[i].regDt, 10));
            
            if(i%2 == 0){
                table +='<tr id="'+i+'" >'
             }else{
                table +='<tr id="'+i+'" >';
             }
                //table +='<td><input name="check" type="checkbox" value="'+data.userInfos[i].userId+'"></td><td><a href="javascript:" onclick="getUserInfo(\''+data.userInfos[i].userId+'\')">'+data.userInfos[i].userId+'</td><td><a href="javascript:" onclick="getUserInfo(\''+data.userInfos[i].userId+'\')">'+data.userInfos[i].userNm+'</td><td><a href="javascript:" onclick="getUserInfo(\''+data.userInfos[i].userId+'\')">'+data.userInfos[i].authNm+'</td><td><a href="javascript:" onclick="getUserInfo(\''+data.userInfos[i].userId+'\')">'+BlankCheck(data.userInfos[i].useYn)+'</td><td><a href="javascript:" onclick="getUserInfo(\''+data.userInfos[i].userId+'\')">'+regDt.format("yyyy-MM-dd")+'</td>'
                if(data.userInfos[i].useYn == "Y"){
                    tmpUserYn = "사용";  
                }else{
                    tmpUserYn = "미사용";
                }
                
                table +='<td><a href="javascript:" onclick="getUserInfo(\''+data.userInfos[i].userId+'\')">'+data.userInfos[i].userId+'</td>'
                table +='<td><a href="javascript:" onclick="getUserInfo(\''+data.userInfos[i].userId+'\')">'+data.userInfos[i].userNm+'</td>'
                table +='<td><a href="javascript:" onclick="getUserInfo(\''+data.userInfos[i].userId+'\')">'+tmpAuthNm+'</td>'
                table +='<td><a href="javascript:" onclick="getUserInfo(\''+data.userInfos[i].userId+'\')">'+regDt.format("yyyy-MM-dd")+'</td>'
                table +='<td><a href="javascript:" onclick="getUserInfo(\''+data.userInfos[i].userId+'\')">'+tmpUserYn+'</td>'
                table +='</tr>'
               
                tmpUserId = data.userInfos[i].userId;
                
           
        }
       
        table +='</tbody>'

        table +='</table>'
        
        $jq('#tbody').append(table);
        
        for(var j = 0; j<data.userInfos.length; j++){
        
            if(tmpUserId == data.userInfos[j].userId){
                tmpAuthNm = data.userInfos[j].authNm+", "+data.userInfos[j-1].authNm;
                tmpSeq = '#'+(j-1);
                $jq(tmpSeq).empty();
                 
                
            }
              tmpUserId = data.userInfos[j].userId;
        }
        
    
        $jq('#paging').empty();
        
        var paging = "";
            
        paging +='<article class="paginate" style="margin-top:-20px;">'
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
    
            paging +='<a class="direction" href="#" onClick="findUserList('+(startPage-pageView)+');return false;">';
            paging +='<span>?</span>이전</a>';
        }
        
        for(var page = startPage; page<currentMaxPaging; page++ ){
            if(pageNum == page){
                paging +='<strong>'+(page+1)+'</strong>';
            }else{
                paging +='<a href="#" onClick="findUserList('+(page)+')">'+(page+1)+'</a>';
            }
        }
    
        if(lastPageBarYn == totalPageBar || (pageNum+1) == pages || (pages-currentMaxPaging) <= 0){
    
        }else if(pages != pageView){
            paging +='<a class="direction" href="#" onClick="findUserList('+(currentMaxPaging)+');return false;">다음 <span>?</span></a>';                
        }else if(pageView<data.totalCount/data.pageSize){
            paging +='<a class="direction" href="#" onClick="findUserList('+(currentMaxPaging)+');return false;">다음 <span>?</span></a>';                
        }
        //table +='<a class="direction" href="#">다음 <span>?</span></a>'
        
        paging +='</article>'
        
        paging +='</div>'
        
        paging +='</div>'
        
        $jq('#paging').append(paging);
             
        }else{
            alert(data.reason);
        }
        }
    });
}


function getUserInfo(userId){   

    userInfo.userId.value = userId;
    
        
    $jq.ajax({
        url: '<@spring.url "/admin/user/getUserInfo.ssc" />',
        type: 'GET',
        dataType: 'json',
        data: $jq('#userInfo').serialize(),
        
        success: function(data){
        if(data.result){
         $jq('#right').empty();
         
         var table ="";
         var selected = "";
         
         var useY = "";
         var useN = "";

         for(var j = 0; j< 1; j++){
            table +='<div id="right">'
            table +='<div class="r_info">'
            table +='<div class="box2">'
            table +='<dl>'
            table +='<dd class="pdl20">'
            table +='<#assign perm = tpl.getAccessRule('${user.userId}', '7')>'
            table +='<#assign perm1 = tpl.getAccessRule('${user.userId}', '8')>'
            table +='<#assign perm2 = tpl.getAccessRule('${user.userId}', '9')>'
            table +='<#assign perm3 = tpl.getAccessRule('${user.userId}', '10')>'
            table +='<#assign perm4 = tpl.getAccessRule('${user.userId}', '11')>'
            table +='<#assign perm5 = tpl.getAccessRule('${user.userId}', '12')>' 
            table +='<select name="user" style="height:24px;width:350px;" onChange="linkView(value);">'
            table +='<option value="<@spring.url "/admin/user/user.ssc"/>">사용자 관리</option>'
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
            table +='</select>'
            table +='</dd>'
            table +='</dl>'
            table +='</div>'
            table +='<dl class="info dark">'
            table +='<dt>사용자정보</dt>'
            table +='<dd>'
            table +='<table summary="" class="board2">'
            table +='<colgroup><col width="100px"></col><col width="286px"></col></colgroup>'
            table +='<tbody>'
            table +='<tr><th>이름</th><td><input name="userNm" id ="userNm" type="text" class="ip_text" value="'+data.userTbl[j].userNm+'"></input></td></tr>'
            table +='<tr><th>ID</th><td><input name="userId" id ="userId" type="text" class="ip_text" value="'+data.userTbl[j].userId+'" readOnly ></input></td></tr>'
            table +='<tr><th>전화번호</th><td><input type="text" name="userPhone" id="userPhone" class="ip_text" value="'+data.userTbl[j].userPhone+'" onkeyup="this.value=this.value.replace(/[^a-zA-z^0-9]/g,"")"></input></td></tr>'
            table +='<tr><th>비밀번호</th><td><input type="password" name="userPasswd" id="userPasswd" class="ip_text" value=""></input></td></tr>'
            table +='<tr><th>역할</th><td>'
            //table +='<select class="pdl10"  id="authId">'
            //table +='<option value="'+data.userTbl[j].authId+'">'+data.userTbl[j].authNm+'</option>'

            //사용자 관리에서 역활 check box
            for(var i = 0; i<data.authTbls.length; i++){
                var  k= 0;
                    for(var l = 0; l<data.userTbl.length; l++){
                        //authTbl에 등록된 모든 authId랑 userId로 조회된 authId 데이터 값 비교.
                        if(data.authTbls[i].authId == data.userTbl[l].authId ){
                            k++;
                           
                        }
                    }
                 table +='<p>'
                 if(k != 0){                 
                    table +='<input type="checkbox" name="authId" id="authId" class="pdl10" value="'+data.authTbls[i].authId+'"checked>'+data.authTbls[i].authNm+'&nbsp;</input>'
                 }else{
                    table +='<input type="checkbox" name="authId" id="authId" class="pdl10" value="'+data.authTbls[i].authId+'">'+data.authTbls[i].authNm+'</input>'
                 }
                 table +='</p>'
            }
            
            
            if(data.userTbl[j].useYn == "Y"){
              table +='<tr id ="useYn"><th>사용여부</th><td>사용&nbsp;<input name="useYn" id="useYn" type="radio" value="Y" checked>&nbsp;&nbsp;미사용&nbsp;<input name="useYn" id="useYn" type="radio" value="N"></td></td></tr>'
            }else{
              table +='<tr id ="useYn"><th>사용여부</th><td>사용&nbsp;<input name="useYn" id="useYn" type="radio" value="Y" '+useY+'>&nbsp;&nbsp;미사용&nbsp;<input name="useYn" id="useYn" type="radio" value="N" checked></td></td></tr>'
            }
            

            table +='</tbody>'
            table +='</table>'
            table +='</dd>'
            table +='</dl>'
            table +='</div>'
            table +='<div class="btncover2">'
            
            table +=<@ajaxButton '9' '${user.userId}' 'changePasswd(userId)' '비밀번호변경' />
            table +='&nbsp;'
            //table +='<a href="javascript:"  onclick="updateUserInfo()"><img src="<@spring.url "/images/save_off.gif"/>" title="저장" alt="저장"></a>'
            //권한별 버튼
            //'6' --> menuId
            table +=<@ajaxButton '9' '${user.userId}' 'resetUserInfo()' '추가' />
            table +='&nbsp;'
            //'6' --> menuId 
            table +=<@ajaxButton '9' '${user.userId}' 'updateUserInfo()' '저장' />
            
            table +='</div>'
            table +='</div>'
            table +='</div>'

            $jq('#right').append(table);
            
         }
         }else{
             alert(data.reason);
         }
        }
    });
}



function resetUserInfo(){

$jq.ajax({
    url: '<@spring.url "/admin/user/getUserInfo.ssc" />',
    type: 'GET',
    dataType: 'json',
    data: $jq('#userInfo').serialize(),
   
    
    success: function(data){
	if(data.result == "Y"){
       var table = "";
       var selected = "";
       
       $jq('#right').empty();
       
       for(var j = 0; j< 1; j++){
       
           table +='<div id="right">'
           table +='<div class="r_info">'
           table +='<div class="box2">'
           table +='<dl>'
           table +='<dd class="pdl20">' 
           table +='<#assign perm = tpl.getAccessRule('${user.userId}', '7')>'
            table +='<#assign perm1 = tpl.getAccessRule('${user.userId}', '8')>'
            table +='<#assign perm2 = tpl.getAccessRule('${user.userId}', '9')>'
            table +='<#assign perm3 = tpl.getAccessRule('${user.userId}', '10')>'
            table +='<#assign perm4 = tpl.getAccessRule('${user.userId}', '11')>'
            table +='<#assign perm5 = tpl.getAccessRule('${user.userId}', '12')>'
            table +='<select name="user" style="height:24px;width:350px;" onChange="linkView(value);">'
            table +='<option value="<@spring.url "/admin/user/user.ssc"/>">사용자 관리</option>'
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
           table +='</select></dd>'
           table +='</dl>'
           table +='</div>'
           table +='<dl class="info dark">'
           table +='<dt>사용자정보</dt>'
           table +='<dd>'
           table +='<table summary="" class="board2">'
           table +='<colgroup><col width="100px"></col><col width="286px"></col></colgroup>'
           table +='<tbody>'
           table +='<tr><th>이름</th><td><input name="userNm" id ="userNm" type="text" class="ip_text" value=""></input></td></tr>'
           table +='<tr><th>ID</th><td><input name="userId" id ="userId" type="text" class="ip_text" value=""  ></input></td></tr>'
           table +='<tr><th>전화번호</th><td><input type="text" name="userPhone" id="userPhone" class="ip_text" value="" onkeyup="this.value=this.value.replace(/[^a-zA-z^0-9]/g,"")"></input></td></tr>'
           table +='<tr><th>비밀번호</th><td><input type="password" name="userPasswd" id="userPasswd" class="ip_text" value="" onkeyup="this.value=this.value.replace(/[^a-zA-z^0-9]/g,"")"></input></td></tr>'
           table +='<tr><th>역할</th><td>'
          //사용자 관리에서 역활 check box
            for(var i = 0; i<data.authTbls.length; i++){
                var  k= 0;
                    for(var l = 0; l<data.userTbl.length; l++){
                        //authTbl에 등록된 모든 authId랑 userId로 조회된 authId 데이터 값 비교.
                        if(data.authTbls[i].authId == data.userTbl[l].authId ){
                            k++;
                        }
                    }
                table +='<p>'
                table +='<input type="checkbox" name="authId" id="authId" class="pdl10" value="'+data.authTbls[i].authId+'">'+data.authTbls[i].authNm+'</input>'
                table +='</p>' 
            }
            
            
          
            table +='<tr id ="useYn"><th>사용여부</th><td>사용&nbsp;<input name="useYn" id="useYn" type="radio" value="Y">&nbsp;&nbsp;미사용&nbsp;<input name="useYn" id="useYn" type="radio" value="N"></td></td></tr>'
            table +='</tbody>'
            table +='</table>'
            table +='</dd>'
            table +='</dl>'
            table +='</div>'
            table +='<div class="btncover2">'
            table +='&nbsp;'
            //table +='<a href="javascript:"  onclick="updateUserInfo()"><img src="<@spring.url "/images/save_off.gif"/>" title="저장" alt="저장"></a>'
            //권한별 버튼
           
            //'6' --> menuId 
            table +=<@ajaxButton '9' '${user.userId}' 'saveUserInfo()' '저장' />
            table +='</div>'
            table +='</div>'
            table +='</div>'

            $jq('#right').append(table);
          }
          }else{
          alert(data.reason);
          }
        }
    });
}

function changePasswd(userId){

userInfo.userId.value = userId;

$jq.ajax({
    url: '<@spring.url "/admin/user/getUserInfo.ssc" />',
    type: 'GET',
    dataType: 'json',
    data: $jq('#userInfo').serialize(),
   
    success: function(data){
    
    if(data.result == "Y"){
       var table = "";
       
       $jq('#right').empty();
       
       for(var j = 0; j< 1; j++){
       
           table +='<div id="right">'
           table +='<div class="r_info">'
           table +='<div class="box2">'
           table +='<dl>'
           table +='<dd class="pdl20">' 
           table +='<select name="user" style="height:24px;width:350px;" onChange="linkView(value);">'          
           table +='<option value="<@spring.url "/admin/user/user.ssc"/>">사용자 관리</option>  '     
           table +='<option value="<@spring.url "/admin/category/category.ssc"/>">카테고리 관리</option>'
           table +='<option value="<@spring.url "/admin/auth/auth.ssc"/>">역할&권한 관리</option>'
           table +='<option value="<@spring.url "/admin/user/user.ssc"/>">사용자 관리</option>  ' 
           table +='<option value="<@spring.url "/admin/code/code.ssc"/>">코드 관리</option>'
           table +='<option value="<@spring.url "/admin/monitoring/monitoring.ssc"/>">모니터링 관리</option>'
           table +='<option value="<@spring.url "/admin/equipment/equipment.ssc"/>">장비 관리</option>'
           table +='</select></dd>'
           table +='</dl>'
           table +='</div>'
           table +='<dl class="info dark">'
           table +='<dt>사용자정보</dt>'
           table +='<dd>'
           table +='<table summary="" class="board2">'
           table +='<colgroup><col width="100px"></col><col width="286px"></col></colgroup>'
           table +='<tbody>'
           table +='<tr><th>ID</th><td><input name="userId" id ="userId" type="text" class="ip_text" value="'+userId.value+'"  readOnly></input></input></td></tr>'
           table +='<tr><th>기존비밀번호</th><td><input name="oldPasswd" id ="oldPasswd" type="password" class="ip_text" value=""></input></td></tr>'
           table +='<tr><th>신규비밀번호</th><td><input name="newPasswd" id ="newPasswd" type="password" class="ip_text" value=""  ></input></td></tr>'                    
           table +='<input name="userId" id ="userId" type="hidden" class="ip_text" value="'+userId.value+'"  ></input>'
           table +='</tbody>'
           table +='</table>'
           table +='</dd>'
           table +='</dl>'
           table +='</div>'
           table +='<div class="btncover2">'
         
           table +=<@ajaxButton '9' '${user.userId}' 'updatePasswd()' '저장' />
           table +='</div>'
           table +='</div>'
           table +='</div>'
           $jq('#right').append(table);
          }
        }else{
        alert(data.reason);
        }
        }
    });
    

}

</script>


<form name="userInfo" id="userInfo" method="post" >
    <@spring.bind "search" />
    <@spring.bind "userTbl" />
    <input type="hidden" name="userNm" />
    <input type="hidden" name="userPhone" />
    <input type="hidden" name="userPasswd" />
    <input type="hidden" name="userId" />
    <input type="hidden" name="authId" />
    <input type="hidden" name="pageNo">
    <input type="hidden" name="useYn">
    <input type="hidden" name="searchUserObj">
    <input type="hidden" name="userSelectBox">
    <input type="hidden" name="oldPasswd">
    <input type="hidden" name="newPasswd">
</form>





