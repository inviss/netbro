<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};



window.onload=function(){	
	findAuthList();
}



function findAuthList(){ 	

 	$jq.ajax({
		url: '<@spring.url "/admin/auth/findAuthList.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#authInfo').serialize(),
		
		success: function(data){
		
		if(data.authTbl.length != 0){
		$jq('#left').empty();

         var table =""; 
         table +='<div id="left">'
         table +='<div class="boardcover">'
         table +='<table summary="" class="board1">'
         table +='<colgroup><col width="160px"/><col width=""/><col width="120px"/><col width="120px"/></colgroup>'
         table +='<tbody>' 
         table +='<tr>'          
         table +='<th>역할명</th><th>역할설명</th><th>사용여부</th><th>등록일</th>'
         table +='</tr>'
         
         for(var i=0; i<data.authTbl.length; i++){
         
         var regDt = new Date(parseInt(data.authTbl[i].regDt, 10));
         
            if(i%2 == 0){
                table +='<tr>'
             }else{
                table +='<tr class="bgc2">';
             }
                table +='<td><a href="javascript:" onclick="getAuthInfo('+data.authTbl[i].authId+')">'+data.authTbl[i].authNm+'</td><td><a href="javascript:" onclick="getAuthInfo('+data.authTbl[i].authId+')">'+BlankCheck(data.authTbl[i].authSubNm)+'</td><td><a href="javascript:" onclick="getAuthInfo('+data.authTbl[i].authId+')">'+BlankCheck(data.authTbl[i].useYn)+'</td><td><a href="javascript:" onclick="getAuthInfo('+data.authTbl[i].authId+')">'+regDt.format("yyyy-MM-dd")+'</td>'
                table +='</tr>'
         }
            
         table +='</tbody>'
         table +='</table>'
         table +='<article class="paginate">'
         table +='</div>'
         table +='</div>'
        
         $jq('#left').append(table);
         
		}else{
		  alert('데이터가 없습니다.');
		}
	   }		
	});
}



function getAuthInfo(authId){ 	
	
	authInfo.authId.value = authId;
	
	
	$jq.ajax({
		url: '<@spring.url "/admin/auth/getAuthInfo.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#authInfo').serialize(),
		
		success: function(data){

		if(data.result == "Y"){
		if(data.roleAuth != null){
                var result = stringSplit(data.roleAuth,",");
            }
                
            $jq('#right').empty();
            
            var table = "";
            var useY = "";
            var useN = "";
            
            table +='<div id="right">'
            table +='<div class="r_info" style="height : 685px;">'
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
            table +='<option value="<@spring.url "/admin/auth/auth.ssc"/>">역할&권한 관리</option>'
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
            table +='</dd>'
            table +='</dl>'
            table +='</div>'
            table +='<dl class="info dark" id = "info dark">'
            table +='<dt>권한정보</dt>'
            table +='<dd>'
            table +='<table summary="" class="board2" id="board2">'
            table +='<colgroup><col width="100px"></col><col width="286px"></col></colgroup>'
            table +='<tbody>'
            table +='<tr><th>역할명</th><td><input name="authNm" id="authNm" type="text" class="ip_text" value="'+data.authTbl.authNm+'"></input></td></tr>'
            table +='<tr><th>역할설명</th><td><input name="authSubNm" id="authSubNm" type="text" class="ip_text" value="'+BlankCheck(data.authTbl.authSubNm)+'"></input></td></tr>'
            table +='<tr><th colspan="2">'
            table +='<table summary="" class="board3" style="margin-left:6px">'
            table +='<colgroup><col width="92px"/><col width="95px"/><col width="92px"/><col width="92px"/><col width="92px"/></colgroup>'
            table +='<thead>'
            table +='<tr><th>권한</th><th>읽기</th><th>쓰기</th><th>제한</th></tr>'
            table +='</thead>'
            table +='<tbody>'
            
            //클립검색(R:read, W:write, L:limit)
            var SR = ""; var SW = ""; var SL = "";
            //컨텐츠관리
            var CR = ""; var CW = ""; var CL = "";
            //작업관리
            var DR = ""; var DW = ""; var DL = "";
            //통계
            var STR = ""; var STW = ""; var STL = "";
            //관리
            var AR = ""; var AW = ""; var AL = "";  
            //카테고리
            var CAR = ""; var CAW = ""; var CAL = "";
            //역활
            var PAR = ""; var PAW = ""; var PAL = "";
            //사용자
            var USR = ""; var USW = ""; var USL = "";
            //코드
            var COR = ""; var COW = ""; var COL = "";
            //모니터링
            var MOR = ""; var MOW = ""; var MOL = "";
            //장비
            var EQR = ""; var EQW = ""; var EQL = "";
            //공지사항
            var NOR = ""; var NOW = ""; var NOL = "";
            
            if(result[0] == "R") SR = checked="checked"; if(result[0] == "RW") SW = checked="checked"; if(result[0] == "L") SL = checked="checked";             
            if(result[1] == "R") CR = checked="checked"; if(result[1] == "RW") CW = checked="checked"; if(result[1] == "L") CL = checked="checked";                                 
            if(result[2] == "R") DR = checked="checked"; if(result[2] == "RW") DW = checked="checked"; if(result[2] == "L") DL = checked="checked";
            if(result[3] == "R") STR = checked="checked"; if(result[3] == "RW") STW = checked="checked"; if(result[3] == "L") STL = checked="checked";              
            if(result[4] == "R") AR = checked="checked"; if(result[4] == "RW") AW = checked="checked"; if(result[4] == "L") AL = checked="checked";
            if(result[5] == "R") CAR = checked="checked"; if(result[5] == "RW") CAW = checked="checked"; if(result[5] == "L") CAL = checked="checked";
            if(result[6] == "R") PAR = checked="checked"; if(result[6] == "RW") PAW = checked="checked"; if(result[6] == "L") PAL = checked="checked";
            if(result[7] == "R") USR = checked="checked"; if(result[7] == "RW") USW = checked="checked"; if(result[7] == "L") USL = checked="checked";
            if(result[8] == "R") COR = checked="checked"; if(result[8] == "RW") COW = checked="checked"; if(result[8] == "L") COL = checked="checked";
            if(result[9] == "R") MOR = checked="checked"; if(result[9] == "RW") MOW = checked="checked"; if(result[9] == "L") MOL = checked="checked";
            if(result[10] == "R") EQR = checked="checked"; if(result[10] == "RW") EQW = checked="checked"; if(result[10] == "L") EQL = checked="checked";
            if(result[11] == "R") NOR = checked="checked"; if(result[11] == "RW") NOW = checked="checked"; if(result[11] == "L") NOL = checked="checked";
            
            var limited;
            
            //관리 버튼 클릭하면 그 밑에 있는 메뉴들 일괄 disabled
            if(AL == "checked"){
                limited = "disabled=disabled";
            }
            
            if(data.authTbl.useYn == "Y"){
              useY = checked="checked";
            }else{
              useN = checked="checked";
            }               

            table +='<tr><th>클립검색</th><td><input name="clip" id="clipRead" type="radio" value="R" '+SR+' ></td><td><input name="clip" id="clipWrite" type="radio" value="RW" '+SW+'></td><td><input name="clip" id="clipLimit" type="radio" value="L" '+SL+'></td></tr>'
            table +='<tr><th>컨텐츠</th><td><input name="content" id="contentRead" type="radio" value="R" '+CR+'></td><td><input name="content" id="contentWrite" type="radio" value="RW" '+CW+'></td><td><input name="content" id="contentLimit" type="radio" value="L" '+CL+'></td></tr>'
            table +='<tr><th>작업관리</th><td><input name="disuse" id="disuseRead" type="radio" value="R" '+DR+'></td><td><input name="disuse" id="disuseWrite" type="radio" value="RW" '+DW+'></td><td><input name="disuse" id="disuseLimit" type="radio" value="L" '+DL+'></td></tr>'
            table +='<tr><th>통계</th><td><input name="stat" id="statRead" type="radio" value="R" '+STR+'></td><td><input name="stat" id="statWrite" type="radio" value="RW" '+STW+'></td><td><input name="stat" id="statLimit" type="radio" value="L" '+STL+'></td></tr>'
            table +='<tr><th>관리</th><td><input name="auth" id="authRead" type="radio" value="R" '+AR+'  onclick=check1("R")></td><td><input name="auth" id="authWrite" type="radio" value="RW" '+AW+' onclick=check1("RW")></td><td><input name="auth" id="authLimit" type="radio" value="L" '+AL+' onclick=check1("L")></td></tr>'
            table +='<tr><th>카테고리</th><td><input name="category" id="categoryRead" type="radio" value="R" '+CAR+' '+limited+'></td><td><input name="category" id="categoryWrite" type="radio" value="RW" '+CAW+' '+limited+'></td><td><input name="category" id="categoryLimit" type="radio" value="L" '+CAL+' ></td></tr>'
            table +='<tr><th>역활&권한</th><td><input name="part" id="partRead" type="radio" value="R" '+PAR+' '+limited+'></td><td><input name="part" id="partWrite" type="radio" value="RW" '+PAW+' '+limited+'></td><td><input name="part" id="partLimit" type="radio" value="L" '+PAL+' ></td></tr>'
            table +='<tr><th>사용자</th><td><input name="user" id="userRead" type="radio" value="R" '+USR+' '+limited+'></td><td><input name="user" id="userWrite" type="radio" value="RW" '+USW+' '+limited+'></td><td><input name="user" id="userLimit" type="radio" value="L" '+USL+' ></td></tr>'
            table +='<tr><th>코드</th><td><input name="code" id="codeRead" type="radio" value="R" '+COR+' '+limited+'></td><td><input name="code" id="codeWrite" type="radio" value="RW" '+COW+' '+limited+'></td><td><input name="code" id="codeLimit" type="radio" value="L" '+COL+' ></td></tr>'
            table +='<tr><th>모니터링</th><td><input name="monitor" id="monitorRead" type="radio" value="R" '+MOR+' '+limited+'></td><td><input name="monitor" id="monitorWrite" type="radio" value="RW" '+MOW+' '+limited+'></td><td><input name="monitor" id="monitorLimit" type="radio" value="L" '+MOL+' ></td></tr>'
            table +='<tr><th>장비</th><td><input name="equip" id="equipRead" type="radio" value="R" '+EQR+' '+limited+'></td><td><input name="equip" id="equipWrite" type="radio" value="RW" '+EQW+' '+limited+'></td><td><input name="equip" id="equipLimit" type="radio" value="L" '+EQL+' ></td></tr>'
            table +='<tr><th>공지사항</th><td><input name="notice" id="noticeRead" type="radio" value="R" '+NOR+' '+limited+'></td><td><input name="notice" id="noticeWrite" type="radio" value="RW" '+NOW+' '+limited+'></td><td><input name="notice" id="noticeLimit" type="radio" value="L" '+NOL+' ></td></tr>'
            table +='</tbody>'
            table +='</table>'
            table +='</th>'
            table +='</tr>'
            table +='</tbody>'
            table +='<tr id ="useYn"><th>사용여부</th><td>&nbsp;사용&nbsp;<input name="useYn" id="useYn" type="radio" value="Y" '+useY+'>&nbsp;&nbsp;미사용&nbsp;<input name="useYn" id="useYn" type="radio" value="N" '+useN+'></td></td></tr>'
            table +='</table>'
            table +='</dd>'
            table +='</dl>'
            table +='</div>'
            table +='<div class="btncover2">'
            //table +='<a href="javascript:"  onclick="updateAuthInfo()"><img src="<@spring.url "/images/save_off.gif"/>" title="저장" alt="저장"></a>'
            //table +='<a href=""><img src="/images/save_off.gif" title="저장" alt="저장" onMouseOver=this.src="/images/save_on.gif" onMouseOut=this.src="/images/save_off.gif"></a>'
            //권한별 버튼
            <!--'6'  menuId-->
            table +=<@ajaxButton '6' '${user.userId}' 'resetAuthInfo()' '추가' />
            table +='&nbsp;'
            table +=<@ajaxButton '6' '${user.userId}' 'updateAuthInfo()' '저장' />
            table +='</div>'
            table +='</div>'
            table +='</div>'
    
            $jq('#right').append(table);
            
		}else{
		  alert(data.reason);
		}
		}
	});
}







function resetAuthInfo(){   
        
	$jq('#authNm').val("");
	$jq('#authSubNm').val("");
    $jq.ajax({
        url: '<@spring.url "/admin/auth/getAuthInfo.ssc" />',
        type: 'GET',
        dataType: 'json',
        data: $jq('#authInfo').serialize(),
        
        success: function(data){
        
            if(data.roleAuth != null){
                var result = stringSplit(data.roleAuth,",");
            }
                
            $jq('#right').empty();
            
            var table = "";
            var useY = "";
            var useN = "";
            
            table +='<div id="right">'
            table +='<div class="r_info" style="height : 685px;">'
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
            table +='<option value="<@spring.url "/admin/auth/auth.ssc"/>">역할&권한 관리</option>'
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
            table +='</dd>'
            table +='</dl>'
            table +='</div>'
            table +='<dl class="info dark" id = "info dark">'
            table +='<dt>권한정보</dt>'
            table +='<dd>'
            table +='<table summary="" class="board2" id="board2">'
            table +='<colgroup><col width="100px"></col><col width="286px"></col></colgroup>'
            table +='<tbody>'
            table +='<tr><th>역할명</th><td><input name="authNm" id="authNm" type="text" class="ip_text" ></input></td></tr>'
            table +='<tr><th>역할설명</th><td><input name="authSubNm" id="authSubNm" type="text" class="ip_text" ></input></td></tr>'
            table +='<tr><th colspan="2">'
            table +='<table summary="" class="board3" style="margin-left:6px">'
            table +='<colgroup><col width="92px"/><col width="95px"/><col width="92px"/><col width="92px"/><col width="92px"/></colgroup>'
            table +='<thead>'
            table +='<tr><th>권한</th><th>읽기</th><th>쓰기</th><th>제한</th></tr>'
            table +='</thead>'
            table +='<tbody>'
            
            
            if(data.authTbl.useYn == "Y"){
              useY = checked="checked";
            }else{
              useN = checked="checked";
            }               

            table +='<tr><th>클립검색</th><td><input name="clip" id="clipRead" type="radio" value="R"></td><td><input name="clip" id="clipWrite" type="radio" value="RW" ></td><td><input name="clip" id="clipLimit" type="radio" value="L" ></td></tr>'
            table +='<tr><th>컨텐츠</th><td><input name="content" id="contentRead" type="radio" value="R"></td><td><input name="content" id="contentWrite" type="radio" value="RW" ></td><td><input name="content" id="contentLimit" type="radio" value="L" ></td></tr>'
            table +='<tr><th>작업관리</th><td><input name="disuse" id="disuseRead" type="radio" value="R" ></td><td><input name="disuse" id="disuseWrite" type="radio" value="RW" ></td><td><input name="disuse" id="disuseLimit" type="radio" value="L" ></td></tr>'
            table +='<tr><th>통계</th><td><input name="stat" id="statRead" type="radio" value="R" ></td><td><input name="stat" id="statWrite" type="radio" value="RW" ></td><td><input name="stat" id="statLimit" type="radio" value="L" ></td></tr>'
            table +='<tr><th>관리</th><td><input name="auth" id="authRead" type="radio" value="R" ></td><td><input name="auth" id="authWrite" type="radio" value="RW" ></td><td><input name="auth" id="authLimit" type="radio" value="L" ></td></tr>'
            table +='<tr><th>카테고리</th><td><input name="category" id="categoryRead" type="radio" value="R" ></td><td><input name="category" id="categoryWrite" type="radio" value="RW" ></td><td><input name="category" id="categoryLimit" type="radio" value="L" ></td></tr>'
            table +='<tr><th>역활&권한</th><td><input name="part" id="partRead" type="radio" value="R" ></td><td><input name="part" id="partWrite" type="radio" value="RW" ></td><td><input name="part" id="partLimit" type="radio" value="L" ></td></tr>'
            table +='<tr><th>사용자</th><td><input name="user" id="userRead" type="radio" value="R" ></td><td><input name="user" id="userWrite" type="radio" value="RW"></td><td><input name="user" id="userLimit" type="radio" value="L" ></td></tr>'
            table +='<tr><th>코드</th><td><input name="code" id="codeRead" type="radio" value="R" ></td><td><input name="code" id="codeWrite" type="radio" value="RW" ></td><td><input name="code" id="codeLimit" type="radio" value="L" ></td></tr>'
            table +='<tr><th>모니터링</th><td><input name="monitor" id="monitorRead" type="radio" value="R" ></td><td><input name="monitor" id="monitorWrite" type="radio" value="RW" ></td><td><input name="monitor" id="monitorLimit" type="radio" value="L" ></td></tr>'
            table +='<tr><th>장비</th><td><input name="equip" id="equipRead" type="radio" value="R" ></td><td><input name="equip" id="equipWrite" type="radio" value="RW" ></td><td><input name="equip" id="equipLimit" type="radio" value="L" ></td></tr>'
            table +='<tr><th>공지사항</th><td><input name="notice" id="noticeRead" type="radio" value="R"></td><td><input name="notice" id="noticeWrite" type="radio" value="RW"></td><td><input name="notice" id="noticeLimit" type="radio" value="L"></td></tr>'
            table +='</tbody>'
            table +='</table>'
            table +='</td>'
            table +='</tr>'
            table +='</tbody>'
            table +='<tr id ="useYn"><th>사용여부</th><td>&nbsp;사용&nbsp;<input name="useYn" id="useYn" type="radio" value="Y" >&nbsp;&nbsp;미사용&nbsp;<input name="useYn" id="useYn" type="radio" value="N" ></td></td></tr>'
            table +='</table>'
            table +='</dd>'
            table +='</dl>'
            table +='</div>'
            table +='<div class="btncover2">'
            //table +='<a href="javascript:"  onclick="updateAuthInfo()"><img src="<@spring.url "/images/save_off.gif"/>" title="저장" alt="저장"></a>'
            //table +='<a href=""><img src="/images/save_off.gif" title="저장" alt="저장" onMouseOver=this.src="/images/save_on.gif" onMouseOut=this.src="/images/save_off.gif"></a>'
            //권한별 버튼
            <!--'6'  menuId-->
            table +=<@ajaxButton '8' '${user.userId}' 'resetAuthInfo()' '추가' />
            table +='&nbsp;'
            table +=<@ajaxButton '8' '${user.userId}' 'saveAuthInfo()' '저장' />
            table +='</div>'
            table +='</div>'
            table +='</div>'
    
        
            $jq('#right').append(table);
            
        }
    });
}






function stringSplit(strData, strIndex){ 

	var stringList = new Array(); 
	
	while(strData.indexOf(strIndex) != -1){
		stringList[stringList.length] = strData.substring(0, strData.indexOf(strIndex)); 
		strData = strData.substring(strData.indexOf(strIndex)+(strIndex.length), strData.length); 
	} 
	
	stringList[stringList.length] = strData; 
	return stringList; 
}

</script>


<form name="authInfo" id="authInfo" method="post" >
	<@spring.bind "search" />
	<input type="hidden" name="authNm" />
	<input type="hidden" name="authSubNm" />
	<input type="hidden" name="authId" />
	<input type="hidden" name="useYn" />
	<input type="hidden" name="controlGubun" />
</form>


