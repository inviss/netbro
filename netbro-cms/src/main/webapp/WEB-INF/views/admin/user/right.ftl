<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};


function saveUserInfo(){

	var userNm =$jq("#userNm").val();
 	var userPhone = $jq("#userPhone").val();
 	var userPasswd = $jq("#userPasswd").val();
 	var userId = $jq("#userId").val();
 	var authId = $jq("#authId").val();
 
 	var chked_Checkbox = "";
 	var chked_Radiobox = "";
 	 	
 	$(":radio[name='useYn']:checked").each(function(pi,po){
        chked_Radiobox += po.value;
    }); 
    
    $(":checkbox[name='authId']:checked").each(function(pi,po){
        chked_Checkbox += po.value;
    });

 	userInfo.userNm.value = userNm;
 	userInfo.userPhone.value = userPhone;
 	userInfo.userPasswd.value = userPasswd;
 	userInfo.userId.value = userId;
 	userInfo.authId.value = chked_Checkbox;
 	userInfo.useYn.value = chked_Radiobox;
 	
 	if(userInfo.userNm.value == ""){
	 	alert("이름을 입력하세요");
	 	userInfo.userNm.focus();
	 	return false;
 	}
 	
 	if(userInfo.userId.value == ""){
	 	alert("ID를 입력하세요");
	 	userInfo.userId.focus();
	 	return false;
 	}
 	
 	if(userInfo.userPhone.value == ""){
	 	alert("전화번호를 입력하세요");
	 	userInfo.userId.focus();
	 	return false;
 	}
 	
 	if(userInfo.userPasswd.value == ""){
	 	alert("비밀번호를 입력하세요");
	 	userInfo.userId.focus();
	 	return false;
 	}
 	
 	
 	if(userInfo.userPhone.value.length > 11){
	 	alert("전화번호 자릿수가 초과되었습니다.(최대자릿수 : 11자리)");
	 	userInfo.userId.focus();
	 	return false;
 	}
 	
 	if(userInfo.userPasswd.value.length < 6){
	 	alert("비밀번호 자릿수가 부족합니다.(최소자릿수 : 6자리)");
	 	userInfo.userId.focus();
	 	return false;
 	}
 	
 	if(userInfo.userPasswd.value.length > 12){
	 	alert("비밀번호 자릿수가 초과되었습니다.(최대자릿수 : 12자리)");
	 	userInfo.userId.focus();
	 	return false;
 	}
 	
 	
    if($(":checkbox[name='authId']:checked").length > 2){
        alert("권한은 최대 2개까지 입력가능합니다.");
        return false;
    }
            
 
 	if(jQuery("tr#useYn input:checked").val() == null){
        alert("사용여부 값을 넣어주세요");
        return false;
    }
 	

 	$jq.ajax({
		url: '<@spring.url "/admin/user/saveUserInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#userInfo').serialize(),
		
		success: function(data){
			 if(data.result == "Y"){
			 for(var i = 0; i<data.userTbls.length; i++){
			 	
			 	if(data.userTbls[i].userId == userInfo.userId.value){
			 		alert("아이디가 중복됩니다.");
			 		return false;
			 	}
			 	
			 	if(data.userTbls[i].userPhone == userInfo.userPhone.value){
			 		alert("전화번호가 중복됩니다.");
			 		return false;
			 	}
			 }
		
			 alert("저장 되었습니다.");
			 findUserList(0);
			 //resetUserInfo();
		}else{
		alert(data.reason);
		}
		}
	});
}


function updateUserInfo(){

    var chked_Radiobox = "";
    var chked_Checkbox = "";
        
    $(":radio[name='useYn']:checked").each(function(pi,po){
        chked_Radiobox += po.value;
    }); 
    
    $(":checkbox[name='authId']:checked").each(function(pi,po){
        chked_Checkbox += po.value;
    });
    

	var userNm =$jq("#userNm").val();
 	var userPhone = $jq("#userPhone").val();
 	var userPasswd = $jq("#userPasswd").val();
 	var userId = $jq("#userId").val();
 	var authId = $jq("#authId").val();
 	
 	userInfo.userNm.value = userNm;
 	userInfo.userPhone.value = userPhone;
 	userInfo.userPasswd.value = userPasswd;
 	userInfo.userId.value = userId;
 	userInfo.authId.value = chked_Checkbox;
 	userInfo.useYn.value = chked_Radiobox;
  
  	 	
 	if(userInfo.userNm.value == ""){
	 	alert("이름을 입력하세요");
	 	return false;
 	}

 	if(userInfo.userPhone.value == ""){
	 	alert("전화번호를 입력하세요");
	 	return false;
 	}
 	
 	if(userInfo.userPasswd.value == ""){
	 	alert("비밀번호를 입력하세요");
	 	return false;
 	}
 	
    if($(":checkbox[name='authId']:checked").length > 2){
        alert("권한은 최대 2개까지 입력가능합니다.");
        return false;
    }
 	
 	$jq.ajax({
		url: '<@spring.url "/admin/user/updateUserInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#userInfo').serialize(),
		
		success: function(data){
		
			if(data.result == "F"){
				alert("입력하신 비밀번호가 기존 비밀번호와 맞지 않습니다.");
				return false;
			}else if(data.result == "Y"){
				alert(data.reason);
				return false;
			}else{
		
			alert("수정 되었습니다.");
			findUserList(0);
			}
		}
	});

}



function updatePasswd(){
    
    userInfo.oldPasswd.value = $jq('#oldPasswd').val();
    userInfo.newPasswd.value = $jq('#newPasswd').val();
    userInfo.userId.value = $jq('#userId').val();
  
      
    $jq.ajax({
        url: '<@spring.url "/admin/user/updatePasswd.ssc" />',
        type: 'POST',
        dataType: 'json',
        data: $jq('#userInfo').serialize(),
        
        success: function(data){

            if(data.result == "F"){
                alert("입력하신 비밀번호가 기존 비밀번호와 맞지 않습니다. 다시 입력해주십시오 ");
                return false;
            }
            
            if(data.result == "E"){
                alert("입력하신 기존비밀번호와 신규비밀번호가 일치합니다. 변경할 비밀번호는 기존 비밀번호와 동일 할 수 없습니다.");
                return false;
            }
        
            alert("변경 되었습니다.");
            findUserList(0);
            getUserInfo(userInfo.userId.value);
            
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
   <select id="userSelectBox"  style="height : 20px;">
   <option value="id">ID</option>
   <option value="name">이름</option>
   </select>
   <input name="searchUserObj" type="text" id="searchUserObj" onkeypress="if(event.keyCode==13){findUserList(0);}">
   <span class="btn_pack medium" style="margin-left:175px; margin-top:-2px;"><a href="javascript:" onclick="window.location.reload(true);">목록</a></span>
   </div>
     
<!-- paginate -->
    <article class="paginate" id="paging" style="margin-top:-30px;">

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
        <option value="<@spring.url"/admin/user/user.ssc"/>">사용자 관리</option>
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
        <#if (perm6 == 'RW' || perm5 =='R')>
            <option value="<@spring.url"/admin/notice/notice.ssc"/>">공지사항 관리</option> 
        </#if>      
        </select></dd>
                </dl>
            </div>
<!-- 기본정보 시작 -->
            <dl class="info dark" id="info dark"">
                <dt>사용자정보</dt>
                <dd>
                <table summary="" class="board2">
                <colgroup><col width="100px"></col><col width="286px"></col></colgroup>
                <tbody>
                <tr><th>이름</th><td><input name="userNm" id ="userNm" type="text" class="ip_text" onkeyup="this.value=this.value.replace(/[^a-zA-z^0-9]/g,'')"></input></td></tr>
                <tr><th>ID</th><td><input name="userId" id ="userId" type="text" class="ip_text" onkeyup="this.value=this.value.replace(/[^a-zA-z^0-9]/g,'')"></input></td></tr>
                <tr><th>전화번호</th><td><input type="text" name="userPhone" id="userPhone" class="ip_text" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')"></input></td></tr>
                <tr><th>비밀번호</th><td><input type="password" name="userPasswd" id="userPasswd" class="ip_text" onkeyup="this.value=this.value.replace(/[^a-zA-z^0-9]/g,'')"></input></td></tr>
                <tr><th>역할</th><td>
                <#list authTbl as auth>
                <p> 
                <input type="checkbox" name="authId" id="authId" class="pdl10" value="${auth.authId}">${auth.authNm}
                </p>
                </#list> 
                </td></tr>
                <tr id ="useYn"><th>사용여부</th><td>사용&nbsp;<input name="useYn" id="useYn" type="radio" value="Y">&nbsp;&nbsp;미사용&nbsp;<input name="useYn" id="useYn" type="radio" value="N"></td></td></tr>
                </tbody>
                </table>
                </dd>
            </dl>
<!-- 기본정보 끝 -->
        </div>
            <div class="btncover2">
           	<!--<a href="javascript:"  onclick="saveUserInfo();"><img src="<@spring.url"/images/save_off.gif"/>" title="저장" alt="저장" onMouseOver="this.src='<@spring.url "/images/save_on.gif"/>'" onMouseOut="this.src='<@spring.url "/images/save_off.gif"/>'"></a>-->
            <!--권한별 버튼-->
            <!--'6'  menuId-->
            <@button '9' '${user.userId}' 'saveUserInfo()' '저장' />
            </div>
<!-- 검색박스끝 -->
    </div>
<!-- right 끝 -->
</div>
<!-- 본문끝 -->
</div>