<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};


function getNoticeInfo(noticeId){
	
	noticeInfo.noticeId.value = noticeId;
	$jq.ajax({
		url: '<@spring.url "/admin/notice/getNoticeInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data :  $jq('#noticeInfo').serialize(),
		
		success: function(data){
		clearData();
		if(data.result == "Y"){
			var tab = '';
			var html="";
			console.log(data.noticeTbl);
		 	
		 	//각 컬럼의 키값에다가 바로 삽입
		 	$jq('#title').val(data.noticeTbl.title);
		 	$jq('#cont').val(data.noticeTbl.cont);
		 	$jq('#popYn').val(data.noticeTbl.popUpYn);
		 	
		 	//popUpYn이 Y인경우에만 표기
		 	console.log('##########################   '+data.noticeTbl.popUpYn);
		 	if(data.noticeTbl.popUpYn == 'Y'){
		 	var sdate = new Date(parseInt(data.noticeTbl.startDd, 10));
		 	var edate = new Date(parseInt(data.noticeTbl.endDd, 10));
		 	$jq('#from').val(sdate.format("yyyy-MM-dd"));
		 	$jq('#to').val(edate.format("yyyy-MM-dd"));
		 	$jq('#period').css('display','');
		 	}
		 	$jq('#regId').empty();
		 	$jq('#period').after('<tr id="regId"><th>등록자</th><td>'+data.userTbl.userNm+'</td></tr>');
			$jq('#Id').empty();
		 	$jq('#period').after('<tr id="Id" style="display:none"><th>Id</th><td><input type="hidden" id="updateId" value="'+data.noticeTbl.noticeId+'"/></td></tr>');
			
			
				$jq('#btncover2').empty();
		
				//'13' --> menuId
				tab += <@ajaxButton '13' '${user.userId}' 'clearData()' '추가' />
				tab +='&nbsp;'
				tab += <@ajaxButton '13' '${user.userId}' 'updateNoticeInfo()' '수정' />
				tab +='&nbsp;'
				tab += <@ajaxButton '13' '${user.userId}' 'deleteNoticeInfo()' '삭제' />
				$jq('#btncover2').append(tab);
			}else{
			
			alert(data.reason);
			}
		}
		
	});

}


function updateNoticeInfo(){
	
if($jq('#title').val().length == 0){
	
		alert('제목을 입력하셔야 합니다');
		return;
		
	}
	
	if($jq('#cont').val() == ""){
	
		alert('공지사항 내용을 입력해주세요');
		return;
		
	}	
	if($jq('#popYn').val() == "선택해주세요"){
	
		alert('팝업 여부를 선택해 주싶시오');
		return;
		
	}
	if($jq('#popYn').val() == "Y"){
	
		if($jq('#from').val() == '' || $jq('#from').val() == ''){
		alert('팝업 기간을 선택해주세요');
		return;
		}
	}	
	noticeInfo.noticeId.value =$jq('#updateId').val();	
	noticeInfo.title.value = $jq('#title').val();
	noticeInfo.cont.value = $jq('#cont').val();
	noticeInfo.regId.value = $jq('#regId').val();
	noticeInfo.popUpYn.value = $jq('#popYn').val();
	noticeInfo.regId.value = '${user.userId}';
	if($jq('#popYn').val() == "Y"){
	noticeInfo.startDd.value = $jq('#from').val();
	noticeInfo.endDd.value = $jq('#to').val();
	}

	$jq.ajax({
		url: '<@spring.url "/admin/notice/updateNoticeInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data :  $jq('#noticeInfo').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
			alert("수정 되었습니다");
			findNoticeList('init');
			clearData();
			}else{
			
			alert(data.reason);
			}
		}
		
	});

}

function insertNoticeInfo(){
	
	if($jq('#title').val().length == 0){
	
		alert('제목을 입력하셔야 합니다');
		return;
		
	}
	
	if($jq('#cont').val() == ""){
	
		alert('공지사항 내용을 입력해주세요');
		return;
		
	}	
	if($jq('#popYn').val() == "선택해주세요"){
	
		alert('팝업 여부를 선택해 주싶시오');
		return;
		
	}
	if($jq('#popYn').val() == "Y"){
	
		if($jq('#from').val() == '' || $jq('#from').val() == ''){
		alert('팝업 기간을 선택해주세요');
		return;
		}
	}		
	noticeInfo.title.value = $jq('#title').val();
	noticeInfo.cont.value = $jq('#cont').val();
	noticeInfo.regId.value = $jq('#regId').val();
	noticeInfo.popUpYn.value = $jq('#popYn').val();
	noticeInfo.regId.value = '${user.userId}';
	if($jq('#popYn').val() == "Y"){
	noticeInfo.startDd.value = $jq('#from').val();
	noticeInfo.endDd.value = $jq('#to').val();
	}
	$jq.ajax({
		url: '<@spring.url "/admin/notice/insertNoticeInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data : $jq('#noticeInfo').serialize(),
	
		success: function(data){
		if(data.result == "Y"){
			
			alert("저장 되었습니다");
			findNoticeList('init');
			clearData();
			}else{
			alert(data.reason)
			}
		}
		
	});

}



function deleteNoticeInfo(){
	
	noticeInfo.noticeId.value =$jq('#updateId').val();	
	

	$jq.ajax({
		url: '<@spring.url "/admin/notice/deleteNoticeInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data :  $jq('#noticeInfo').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
			alert("삭제 되었습니다");
			findNoticeList('init');
			clearData();
			}else{
			
			alert(data.reason);
			}
		}
		
	});

}

function clearData(){
	noticeInfo.title.value =''; 
	noticeInfo.cont.value = '';
	noticeInfo.regId.value = '';
	noticeInfo.popUpYn.value = '';
	noticeInfo.startDd.value = '';
	noticeInfo.endDd.value = '';
	noticeInfo.regId.value='';
	$jq('#title').val('');
	$jq('#cont').val('');
	$jq('#regId').val('');
	$jq('#popYn').val('');
	$jq('#from').val('');
	$jq('#to').val('');
	$jq('#regId').css('display','none');
	$jq('#period').css('display','none');
	
	var tab = '';
	$jq('#btncover2').empty();
		
				//'13' --> menuId
				
				tab += <@ajaxButton '13' '${user.userId}' 'insertNoticeInfo()' '저장' />
	
	$jq('#btncover2').append(tab);
				
}

</script>

<form name="noticeInfo" id="noticeInfo" method="post" >
	<@spring.bind "notice" />
	<input type="hidden" name="title"  />
	<input type="hidden" name="cont"  />
	<input type="hidden" name="regId"  />
	<input type="hidden" name="popUpYn"  />
	<input type="hidden" name="startDd"  />
	<input type="hidden" name="endDd"  />
	<input type="hidden" name="pageNo"  />
	<input type="hidden" name="keyword"  />
	<input type="hidden" name="noticeId"  />
	<input type="hidden" name="searchFiled"  />
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
        <option value="<@spring.url"/admin/notice/notice.ssc"/>">공지사항 관리</option>
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
            <dl id ="info-dark" class="info dark">
                <dt>상세정보</dt>
                <dd>
                <table summary="" class="board8">
                <colgroup><col width="100px"></col><col width="286px"></col></colgroup>
                <tbody>
                <tr><th>제목</th><td><input type="text" id="title"></input></td></tr>
                <tr><th>내용</th><td><textarea id="cont" class="ip_text2"></textarea></td></tr>
                <tr><th>팝업여부</th><td><select type="text" class="ip_yn" id="popYn" onclick="showHideSelectbox(value);"><option>선택하세요</option>
                <option value="Y">Y</option>
                <option value="N">N</option></select></td></tr>
                <tr id="period" style="display:none"><th>기간</th><td><input type="text" id="from" style="width : 80px;text-align: center;"> ~ <input type="text" id="to" style="width : 80px;text-align: center;"></td></tr>
                </tbody>
                </table>
                </dd>
            </dl>
<!-- 기본정보 끝 -->
        </div>
            <div class="btncover2" id="btncover2">
            <!--권한별 버튼 -->
            <!==//'13' menuId--> 
            <@button '13' '${user.userId}' 'insertNoticeInfo()' '저장' />
            </div>
            
            
<!-- 검색박스끝 -->
    </div>
<!-- right 끝 -->