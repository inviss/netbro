<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};


function updateAuthInfo(){

	var chked_val = "";
	var chked_useYnVal = "";
	var authNm =$jq("#authNm").val();
	var authSubNm =$jq("#authSubNm").val();

    $(":radio[name='clip']:checked").each(function(pi,po){
        chked_val += po.value;
    }); 
    
    $(":radio[name='content']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    }); 
        
    $(":radio[name='disuse']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='stat']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    }); 
        
    $(":radio[name='auth']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='category']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='part']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='user']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='code']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='monitor']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='equip']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='notice']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='useYn']:checked").each(function(pi,po){
        chked_useYnVal += po.value;
    }); 
  	
	authInfo.controlGubun.value = chked_val;
	authInfo.useYn.value = chked_useYnVal;
 	authInfo.authNm.value = authNm;
 	authInfo.authSubNm.value = authSubNm;
 	
 	
 	
 	if(authInfo.authNm.value == ""){
	 	alert("역활명을 입력하세요");
	 	authInfo.authNm.focus();
	 	return false;
 	}
 	
 		$jq.ajax({
		url: '<@spring.url "/admin/auth/updateAuthInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#authInfo').serialize(),
		
		success: function(data){
			if(data.result == "Y"){
			     alert("수정 되었습니다.");
                 findAuthList();
	         }else{
	             alert(data.reason);
	         }
		}
	});
}


function saveAuthInfo(){

	var chked_val = "";
	var authNm =$jq("#authNm").val();
	var authSubNm =$jq("#authSubNm").val();
	
	//화면권한별 라디오버튼
	$(":radio[name='clip']:checked").each(function(pi,po){
   		chked_val += po.value;
  	});	
  	
  	$(":radio[name='content']:checked").each(function(pi,po){
   		chked_val += ","+po.value;
  	});	
  	 	
  	$(":radio[name='disuse']:checked").each(function(pi,po){
   		chked_val += ","+po.value;
  	});
  	
  	$(":radio[name='stat']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    }); 
  	  	
  	$(":radio[name='auth']:checked").each(function(pi,po){
   		chked_val += ","+po.value;
  	});
  	
  	$(":radio[name='category']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='part']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='user']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='code']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='monitor']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='equip']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    $(":radio[name='notice']:checked").each(function(pi,po){
        chked_val += ","+po.value;
    });
    
    
  	
    var chked_useYnVal = "";
        
    $(":radio[name='useYn']:checked").each(function(pi,po){
        chked_useYnVal += po.value;
    }); 
  		
  		
 	authInfo.authNm.value = authNm;
 	authInfo.authSubNm.value = authSubNm;
 	authInfo.useYn.value = chked_useYnVal;
 	authInfo.controlGubun.value = chked_val;
 	 	
 	if(authInfo.authNm.value == ""){
	 	alert("역활명을 입력하세요");
	 	authInfo.authNm.focus();
	 	return;
 	}
 	if(jQuery("tr#clip input:checked").val() == null){
 		alert("클립검색 값을 넣어주세요");
 		return;
 	}
 	if(jQuery("tr#content input:checked").val() == null){
 		alert("컨텐츠 값을 넣어주세요");
 		return;
 	}
 	if(jQuery("tr#disuse input:checked").val() == null){
 		alert("작업관리 값을 넣어주세요");
 		return;
 	}
 	if(jQuery("tr#stat input:checked").val() == null){
        alert("통계 값을 넣어주세요");
        return;
    }
 	if(jQuery("tr#auth input:checked").val() == null){
 		alert("관리 값을 넣어주세요");
 		return;
 	}
 	
 	if(jQuery("tr#category input:checked").val() == null){
        alert("카테고리 값을 넣어주세요");
        return;
    }
    
    if(jQuery("tr#part input:checked").val() == null){
        alert("역활&권한 값을 넣어주세요");
        return;
    }
    
    if(jQuery("tr#user input:checked").val() == null){
        alert("사용자 값을 넣어주세요");
        return;
    }
    
    if(jQuery("tr#code input:checked").val() == null){
        alert("코드 값을 넣어주세요");
        return;
    }
    
    if(jQuery("tr#monitor input:checked").val() == null){
        alert("모니터링 값을 넣어주세요");
        return;
    }
    
    if(jQuery("tr#equip input:checked").val() == null){
        alert("장비 값을 넣어주세요");
        return;
    }
 	
 	if(jQuery("tr#useYn input:checked").val() == null){
        alert("사용여부 값을 넣어주세요");
        return;
    }
    
    
 	
 		$jq.ajax({
		url: '<@spring.url "/admin/auth/saveAuthInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#authInfo').serialize(),
		
		success: function(data){
            if(data.result == "Y"){
                 alert("수정 되었습니다.");
                 findAuthList();
             }else{
                 alert(data.reason);
             }
        }
	});
}



function check1(chkval) {

if(chkval == 'R'){

    $jq("#categoryRead").prop("checked","checked");
    $jq("#partRead").prop("checked","checked");
    $jq("#userRead").prop("checked","checked");
    $jq("#codeRead").prop("checked","checked");
    $jq("#monitorRead").prop("checked","checked");
    $jq("#equipRead").prop("checked","checked");  
    $jq("#noticeRead").prop("checked","checked");      
    
    $jq("#categoryRead").prop("disabled",false);
    $jq("#partRead").prop("disabled",false);
    $jq("#codeRead").prop("disabled",false);
    $jq("#monitorRead").prop("disabled",false);
    $jq("#userRead").prop("disabled",false);
    $jq("#equipRead").prop("disabled",false);
    $jq("#noticeRead").prop("disabled",false);
    
    $jq("#categoryWrite").prop("disabled",false);
    $jq("#partWrite").prop("disabled",false);
    $jq("#codeWrite").prop("disabled",false);
    $jq("#monitorWrite").prop("disabled",false);
    $jq("#userWrite").prop("disabled",false);
    $jq("#equipWrite").prop("disabled",false);
    $jq("#noticeWrite").prop("disabled",false);
       
}else if(chkval == 'RW'){

    $jq("#categoryWrite").prop("checked","checked");
    $jq("#partWrite").prop("checked","checked");
    $jq("#userWrite").prop("checked","checked");
    $jq("#codeWrite").prop("checked","checked");
    $jq("#monitorWrite").prop("checked","checked");
    $jq("#equipWrite").prop("checked","checked");
    $jq("#noticeWrite").prop("checked","checked");
    
    $jq("#categoryRead").prop("disabled",false);
    $jq("#partRead").prop("disabled",false);
    $jq("#codeRead").prop("disabled",false);
    $jq("#monitorRead").prop("disabled",false);
    $jq("#userRead").prop("disabled",false);
    $jq("#equipRead").prop("disabled",false);
    $jq("#noticeRead").prop("disabled",false);
    
    $jq("#categoryWrite").prop("disabled",false);
    $jq("#partWrite").prop("disabled",false);
    $jq("#codeWrite").prop("disabled",false);
    $jq("#monitorWrite").prop("disabled",false);
    $jq("#userWrite").prop("disabled",false);
    $jq("#equipWrite").prop("disabled",false);
    $jq("#noticeWrite").prop("disabled",false);

    
}else{

    $jq("#categoryLimit").prop("checked","checked");
    $jq("#partLimit").prop("checked","checked");
    $jq("#userLimit").prop("checked","checked");
    $jq("#codeLimit").prop("checked","checked");
    $jq("#monitorLimit").prop("checked","checked");
    $jq("#equipLimit").prop("checked","checked");
    $jq("#noticeLimit").prop("checked","checked");
    
    $jq("#categoryRead").attr("disabled",true);
    $jq("#partRead").attr("disabled",true);
    $jq("#userRead").attr("disabled",true);
    $jq("#codeRead").attr("disabled",true);
    $jq("#monitorRead").attr("disabled",true);
    $jq("#equipRead").attr("disabled",true);
    $jq("#noticeRead").attr("disabled",true);
    
    $jq("#categoryWrite").attr("disabled",true);
    $jq("#partWrite").attr("disabled",true);
    $jq("#userWrite").attr("disabled",true);
    $jq("#codeWrite").attr("disabled",true);
    $jq("#monitorWrite").attr("disabled",true);
    $jq("#equipWrite").attr("disabled",true);
    $jq("#noticeWrite").attr("disabled",true);
    
}

}



</script>




<div id="container">
<div id="left"> <!-- left시작 -->
	<div class="boardcover">
    <table summary="" class="board1">
    <colgroup><col width="160px"/><col width=""/><col width="120px"/><col width="120px"/></colgroup>
    <tbody>
	</tbody>
    </table>
<!-- paginate 
    <article class="paginate">
        <a class="direction" href="#">
	    <span>‹</span> 이전</a>
        <a href="#">1</a>
        <strong>2</strong>
        <a href="#">3</a>
        <a href="#">4</a>
        <a href="#">5</a>
        <a href="#">6</a>
        <a href="#">7</a>
        <a href="#">8</a>
        <a href="#">9</a>
        <a href="#">10</a>
        <a class="direction" href="#">다음 <span>›</span></a>
    </article>-->
<!-- //paginate -->
    </div>
</div>
<!-- left 끝 -->
<!-- right 시작 -->
    <div id="right">
<!-- 검색박스시작 -->
        <div class="r_info" style="height : 685px;">
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
        
        <select name="auth" style="height:24px;width:350px;" onChange="linkView(value);">
        <option value="<@spring.url"/admin/auth/auth.ssc"/>">역할&권한 관리</option>    
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
        </select>
                </dd>
                </dd>
                </dl>
            </div>
<!-- 기본정보 시작 -->
            <dl class="info dark" id = "info dark">
                <dt>권한정보</dt>
                <dd>
                <table summary="" class="board2" id="board2">
                <colgroup><col width="100px"></col><col width="286px"></col></colgroup>
                <tbody>
                <tr><th>역할명</th><td><input name="authNm" id="authNm" type="text" class="ip_text"></input></td></tr>
                <tr><th>역할설명</th><td><input name="authSubNm" id="authSubNm" type="text" class="ip_text"></input></td></tr>
                <tr><th colspan="2">
					<table summary="" class="board3" style="margin-left:6px">
                    <colgroup><col width="92px"/><col width="95px"/><col width="92px"/><col width="92px"/><col width="92px"/></colgroup>
                    <thead>
                    <tr><th>권한</th><th>읽기</th><th>쓰기</th><th>제한</th></tr>
                    </thead>
                    <tbody>
                    <tr id="clip"><th>클립검색</th><td><input name="clip" id="clipRead" type="radio" value="R"></td><td><input name="clip" id="clipWrite" type="radio" value="RW"></td><td><input name="clip" id="clipLimit" type="radio" value="L"></td></tr>
                    <tr id="content"><th>컨텐츠</th><td><input name="content" id="contentRead" type="radio" value="R"></td><td><input name="content" id="contentWrite" type="radio" value="RW"></td><td><input name="content" id="contentLimit" type="radio" value="L"></td></tr>
                    <tr id="disuse"><th>작업관리</th><td><input name="disuse" id="disuseRead" type="radio" value="R"></td><td><input name="disuse" id="disuseWrite" type="radio" value="RW"></td><td><input name="disuse" id="disuseLimit" type="radio" value="L"></td></tr>
                    <tr id="stat"><th>통계</th><td><input name="stat" id="statRead" type="radio" value="R"></td><td><input name="stat" id="statWrite" type="radio" value="RW"></td><td><input name="stat" id="statLimit" type="radio" value="L"></td></tr>
                    <tr id="auth"><th>관리</th><td><input name="auth" id="authRead" type="radio" value="R" onclick=check1("R")></td><td><input name="auth" id="authWrite" type="radio" value="RW" onclick=check1("RW")></td><td><input name="auth" id="authLimit" type="radio" value="L" onclick=check1("L")></td></tr>
           
                    <tr id="category"><th>카테고리</th><td><input name="category" id="categoryRead" type="radio" value="R"></td><td><input name="category" id="categoryWrite" type="radio" value="RW"></td><td><input name="category" id="categoryLimit" type="radio" value="L" ></td></tr>
                    <tr id="part"><th>역활&권한</th><td><input name="part" id="partRead" type="radio" value="R"></td><td><input name="part" id="partWrite" type="radio" value="RW"></td><td><input name="part" id="partLimit" type="radio" value="L"></td></tr>
                    <tr id="user"><th>사용자</th><td><input name="user" id="userRead" type="radio" value="R"></td><td><input name="user" id="userWrite" type="radio" value="RW"></td><td><input name="user" id="userLimit" type="radio" value="L"></td></tr>
                    <tr id="code"><th>코드</th><td><input name="code" id="codeRead" type="radio" value="R"></td><td><input name="code" id="codeWrite" type="radio" value="RW"></td><td><input name="code" id="codeLimit" type="radio" value="L"></td></tr>
                    <tr id="monitor"><th>모니터링</th><td><input name="monitor" id="monitorRead" type="radio" value="R"></td><td><input name="monitor" id="monitorWrite" type="radio" value="RW"></td><td><input name="monitor" id="monitorLimit" type="radio" value="L"></td></tr>
                    <tr id="equip"><th>장비</th><td><input name="equip" id="equipRead" type="radio" value="R"></td><td><input name="equip" id="equipWrite" type="radio" value="RW"></td><td><input name="equip" id="equipLimit" type="radio" value="L"></td></tr>
                    <tr id="notice"><th>공지사항</th><td><input name="notice" id="noticeRead" type="radio" value="R"></td><td><input name="notice" id="noticeWrite" type="radio" value="RW"></td><td><input name="notice" id="noticeLimit" type="radio" value="L"></td></tr>
                    </tbody>
                    </table>
                </th>
                </tr>
                </tbody>
                <tr id ="useYn"><th>사용여부</th><td>&nbsp;사용&nbsp;<input name="useYn" id="useYn" type="radio" value="Y">&nbsp;&nbsp;미사용&nbsp;<input name="useYn" id="useYn" type="radio" value="N"></td></td></tr>
                </table>
                </dd>
            </dl>
<!-- 기본정보 끝 -->
        </div>
            <div class="btncover15" style="width : 93px;">
             <!--<a href="javascript:"  onclick="saveAuthInfo();"><img src="<@spring.url"/images/save_off.gif"/>" title="저장" alt="저장" onMouseOver="this.src='<@spring.url "/images/save_on.gif"/>'" onMouseOut="this.src='<@spring.url "/images/save_off.gif"/>'"></a>-->
             <!--<span class="btn_pack xlarge"><a href="javascript:" onclick="checkLogin(this)">저장</a></span>-->
             	 <!--권한별 버튼 -->
             	 <!--'6'  menuId-->
                 <@button '8' '${user.userId}' 'saveAuthInfo()' '저장' />
            </div>
<!-- 검색박스끝 -->
    </div>
<!-- right 끝 -->
</div>
<!-- 본문끝 -->
</div>