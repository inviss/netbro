<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};

function getCodeInfo(clf_cd,scl_cd){
	$jq.ajax({
		url: '<@spring.url "/admin/code/getCodeInfo.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: { "clfCd" : clf_cd,"sclCd" :scl_cd  },
		
		success: function(data){
		if(data.result == "Y"){
		$jq('#info-dark').empty();
		
		var dl="";
		var a="";
		
		dl += '<dt>기본정보</dt>'
		dl += '<dd>'
		dl += '<table summary="" class="board2">'
		dl += '<colgroup><col width="100px"></col><col width="286px"></col></colgroup>'
		dl += '<tbody>'
		dl += '<tr><th>분류코드명</th><td id="clfnm" >'+data.codeinfo.clfNM+'</td></tr>'
		dl += '<tr><th>분류코드</th><td >'+data.codeinfo.id.clfCD+'<input type="hidden" id="clfcd" value="'+data.codeinfo.id.clfCD+'"></input></td></tr>'
		dl += '<tr><th>상세코드명</th><td><input type="text" id="sclNm" class="ip_text" value ="'+BlankCheck(data.codeinfo.sclNm)+'"></input></td></tr>'
		dl += '<tr><th>상세코드</th><td >'+data.codeinfo.id.sclCd+'<input type="hidden" id="sclCd" value="'+data.codeinfo.id.sclCd+'"></input></td></tr>'
		dl += '<tr><th>코드설명</th><td class="pdtb7"><textarea name="" id ="desc" class="ip_text2" >'+BlankCheck(data.codeinfo.codeCont)+'</textarea></td></tr>'
		dl += '<tr><th>사용여부</th><td class="pdtb7"><select name="" id ="useYn" >'
		
		if(data.codeinfo.useYn == 'Y'){
		
			dl += '<option value="Y">Y</option><option value="N">N</option>'
		
		}else{
		
			dl += '<option value="N">N</option><option value="Y">Y</option>'
		
		}
		
		dl += '</select></td></tr>'
		dl += '</tbody>'
		dl += '</table>'
		dl += '</dd>'
		
		$jq('#info-dark').append(dl);
		
		$jq('#btncover2').empty();
		//'6' --> menuId
		a += <@ajaxButton '10' '${user.userId}' 'clearData()' '추가' />
		a += '&nbsp;'
		//'6' --> menuId
		a += <@ajaxButton '10' '${user.userId}' 'updateCodeInfo()' '저장' />
		//a += '<a href="#" onClick="clearData();return false;"><img src="<@spring.url "/images/reg_off.gif"/>" title="등록" alt="등록" </a>'
		//a += '<a href="#" onClick="updateCodeInfo();return false;"><img src="<@spring.url "/images/save_off.gif"/>" title="저장" alt="저장" </a>'
		$jq('#btncover2').append(a);
		codeInfo2.clfCD.value=data.codeinfo.id.clfCD;
		codeInfo2.clfNM.value=data.codeinfo.clfNM;
		}else{
		
		alert(data.reason);
		}
		}
		
	});
	
}


function updateCodeInfo(){
	
	codeInfo2.clfCD.value=$jq('#clfcd').val();
	codeInfo2.sclCd.value=$jq('#sclCd').val();
	codeInfo2.sclNm.value=$jq('#sclNm').val();
	codeInfo2.codeCont.value=$jq('#desc').val();
	codeInfo2.useYn.value=$jq('#useYn').val();

	$jq.ajax({
		url: '<@spring.url "/admin/code/updateCodeInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data :  $jq('#codeInfo2').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
			alert("수정 되었습니다");
			findCodeList(0);
			}else{
			
			alert(data.reason);
			}
		}
		
	});

}

function insertCodeInfo(){
	
	if($jq('#clfcds').val().length == 7){
	
		alert('분류코드명을 선택하세요');
		return;
		
	}
	
	if($jq('#sclNm').val() == ""){
	
		alert('상세코드명을 입력하세요');
		return;
		
	}	
	
	codeInfo.clfCD.value = $jq('#clfcds').val();
	codeInfo.sclNm.value = $jq('#sclNm').val();
	codeInfo.codeCont.value = $jq('#desc').val();
	
	$jq.ajax({
		url: '<@spring.url "/admin/code/insertCodeInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		//data: { "clfCd" : $jq('#clfcds').val(),"sclNm" :$jq('#sclNm').val(),  "desc" : $jq('#desc').val()  },
		data : $jq('#codeInfo').serialize(),
	
		success: function(data){
		if(data.result == "Y"){
			$jq('#selectClfCd').val('');	
			$jq('#searchKey').val('');
			
			console.log("selectClfCd  "+$jq('#selectClfCd').val()+"  searchKey   "+$jq('#searchKey').val());
			
			alert("저장 되었습니다");
			findCodeList(0);
			clearData();
			}else{
			alert(data.reason)
			}
		}
		
	});

}

function clearData(value){

$jq.ajax({
		url: '<@spring.url "/admin/code/findClfInfoList.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: {   },
		
		success: function(data){
		
		$jq('#info-dark').empty();
		
		var dl="";
		var a="";
		
		dl += '<dt>기본정보</dt>'
		dl += '<dd>'
		dl += '<table summary="" class="board2">'
		dl += '<colgroup><col width="100px"></col><col width="286px"></col></colgroup>'
		dl += '<tbody>'
		dl += '<tr><th>분류코드명</th><td id="clfnm">'
		dl += '<select class="clfcds" name="clfcds" id = "clfcds" >'
		if(value != 'clear'){
		
		dl += '<option value="'+codeInfo2.clfCD.value+'">'+codeInfo2.clfNM.value+'</option>'
		
		}else{
		
		dl += '<option value=""></option>'
		
		}
		
		for(var k = 0; k < data.clfInfos.length; k++){
		
			if(data.clfInfos[k].clfCd != codeInfo2.clfCD.value){
			
				dl += '<option value="'+data.clfInfos[k].clfCd+'">'+data.clfInfos[k].clfNM+'</option>'
				
			}
			
		}
		
		dl += '</select>'
		dl += '</td></tr>'
		dl += '<tr><th>상세코드명</th><td ><input type="text" id="sclNm" class="ip_text"  value =""></input></td></tr>'
		dl += '<tr><th>코드설명</th><td class="pdtb7"><textarea name="" id="desc" class="ip_text2" value =""></textarea></td></tr>'
		dl += '</tbody>'
		dl += '</table>'
		dl += '</dd>'
		
		$jq('#info-dark').append(dl);
		
		$jq('#btncover2').empty();
		
		//'6' --> menuId
		a += <@ajaxButton '10' '${user.userId}' 'insertCodeInfo()' '저장' />
	
		$jq('#btncover2').append(a);
		
		}
		
	});
	
}

</script>

<form name="codeInfo2" id="codeInfo2" method="post" >
	<@spring.bind "codeTbl" />
	<@spring.bind "codeId" />
	<input type="hidden" name="clfNM"  />
	<input type="hidden" name="clfCD"  />
	<input type="hidden" name="sclCd"  />
	<input type="hidden" name="sclNm"  />
	<input type="hidden" name="codeCont"  />
	<input type="hidden" name="useYn"  />
</form>
<!-- right 시작 -->
    <div id="right">
<!-- 검색박스시작 -->
        <div class="code_r_info">
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
        <option value="<@spring.url"/admin/code/code.ssc"/>">코드 관리</option>
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
            <dl id ="info-dark" class="info dark">
                <dt>기본정보</dt>
                <dd>
                <table summary="" class="board2">
                <colgroup><col width="100px"></col><col width="286px"></col></colgroup>
                <tbody>
                <tr><th>분류코드명</th><td></td></tr>
                <tr><th>분류코드</th><td></td></tr>
                <tr><th>상세코드명</th><td></td></tr>
                <tr><th>상세코드</th><td></td></tr>
                <tr><th>코드설명</th><td class="pdtb7"></td></tr>
                </tbody>
                </table>
                </dd>
            </dl>
<!-- 기본정보 끝 -->
        </div>
            <div class="btncover2" id="btncover2">
            <!--<a href="#" onClick="clearData();return false;"><img src="<@spring.url "/images/reg_off.gif"/>" title="등록" alt="등록" onMouseOver="this.src='<@spring.url "/images/reg_on.gif"/>'" onMouseOut="this.src='<@spring.url "/images/reg_off.gif"/>'"></a>-->
            <!--권한별 버튼 -->
            <!==//'6' menuId--> 
             <!--<@button '10' '${user.userId}' 'clearData()' '추가' />-->
            </div>
            
            
<!-- 검색박스끝 -->
    </div>
<!-- right 끝 -->