<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};

function episodeSearch(categoryId,episodeNm){
	
	epsodeSearch.categoryId.value = categoryId;
	epsodeSearch.episodeNm.value = episodeNm;
	
	$jq.ajax({
		url: '<@spring.url "/admin/category/getEpisodeSearch.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#epsodeSearch').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
		var list = "";
		var li = "";
		var insert = "";
		var li2 = ""
		var update = "";
		
		if(data.episodeTbls.length != 0){
		
			$jq('#episodeSearchResult').empty();
			
			list += '<tr><th>회차명</th><td><input type="text" class="ip_text" onkeypress="if(event.keyCode==13){episodeSearch('+data.episodeTbls[0].id.categoryId+',value)};"></input></td></tr>'
			list += '<tr>'
			list += '<td colspan="2" class="pdtb5">'
			list += '<table summary="" class="board3">'
			list += '<colgroup><col width="33px"/><col width="150px"/><col width="152px"/><col width="155px"/><col width="155px"/></colgroup>'
			list += '<thead>'
			list += '<tr><th></th><th>회차명</th><th>등록일</th><th>등록자</th><th>수정자</th></tr>'
			list += '</thead>'
			list += '<tbody>'
				
				for(var i = 0; i < data.episodeTbls.length; i++){
					
					var date = new Date(parseInt(data.episodeTbls[i].regDt, 10));
					var format = "yy"
												
					list += '<tr><td><input name="check" type="checkbox" onClick="changeEpisodeNm(\''+data.episodeTbls[i].episodeNm+'\');"  value="'+data.episodeTbls[i].id.episodeId+'"></td><td id="'+data.episodeTbls[i].id.episodeId+'">'+data.episodeTbls[i].episodeNm+'</td><td>'+date.format("yyyy-MM-dd")+'</td><td>'+BlankCheck(data.episodeTbls[i].regrId)+'</td><td>'+BlankCheck(data.episodeTbls[i].modrId)+'</td></tr>'
				 
				 }
				    
			list += '</tbody>'
			list += '</table>'
			list += '</td>'
			list += '</tr>'
	
			$jq('#episodeSearchResult').append(list);
			$jq('#episodeul').empty();
				
			li += '<li id="episodeid">회차명</li>'
			li += '<li><input name="episodeNm" id= "episodeNm" type="text" onkeypress="if(event.keyCode==13){insertEpisode('+data.episodeTbls[0].id.categoryId+');showHide(\'layer2\');return false;}" ></li>'
				
			$jq('#episodeul').append(li);
			$jq('#insertEpisode1').empty();
		
			//버튼에 cateogoryId 값을 넣어준다.
			insert += '<a href="#" id = "insertEpisode" onClick="insertEpisode('+data.episodeTbls[0].id.categoryId+');showHide(\'layer2\');return false;">저장</a>' ;
				
			$jq('#insertEpisode1').append(insert);
			$jq('#episodeul2').empty();
	
			li2 += '<li id="episodeul2">수정회차명</li>'
			li2 += '<li><input name="episodeNm2" id= "episodeNm2" type="text" onkeypress="if(event.keyCode==13){updateEpisode('+data.episodeTbls[0].id.categoryId+');showHide(\'layer4\');return false;}" ></li>'
			
			$jq('#episodeul2').append(li2);
			$jq('#updateEpisode1').empty();
					
			//버튼에 cateogoryId 값을 넣어준다.
			update += '<a href="#" id = "updateEpisode" onClick="updateEpisode('+data.episodeTbls[0].id.categoryId+');showHide(\'layer4\');return false;">저장</a>' ;
			$jq('#updateEpisode1').append(update);		
			
		}else{
			alert(data.reason);
			$jq('#episodeSearchResult').empty();
									
			list += '<tr><th>회차명</th><td><input type="text" class="ip_text" onkeypress="if(event.keyCode==13){episodeSearch('+categoryId+',value)};"></input></td></tr>'
			list += '<tr>'
			list += '<td colspan="2" class="pdtb5">'
			list += '<table summary="" class="board3">'
			list += '<colgroup><col width="34px"/><col width="150px"/><col width="152px"/><col width="155px"/><col width="155px"/></colgroup>'
			list += '<thead>'
			list += '<tr><th></th><th>회차명</th><th>등록일</th><th>등록자</th><th>수정자</th></tr>'
			list += '</thead>'
			list += '<tbody>'	
			list += '</tbody>'
			list += '</table>'
			list += '</td>'
			list += '</tr>'
		
			$jq('#episodeSearchResult').append(list);	
			$jq('#episodeul').empty();
			
			li+= '<li id="episodeid" >회차명</li>'
			li+= '<li><input name="episodeNm" id= "episodeNm" type="text" onkeypress="if(event.keyCode==13){insertEpisode('+categoryId+');showHide(\'layer2\');return false;}" ></li>'
			
			$jq('#episodeul').append(li);	
			$jq('#insertEpisode').empty();
		
			//버튼에 cateogoryId 값을 넣어준다.
			insert += '<a href="#" onClick="insertEpisode('+ categoryId+');showHide(\'layer2\');return false;">저장</a>' ;
			
			$jq('#insertEpisode').append(insert);
			$jq('#episodeul2').empty();
			
			li2 += '<li id="episodeul2">수정회차명</li>'
			li2 += '<li><input name="episodeNm2" id= "episodeNm2" type="text" onkeypress="if(event.keyCode==13){updateEpisode('+categoryId+');showHide(\'layer4\');return false;}" ></li>'
			
			$jq('#episodeul2').append(li2);
			$jq('#updateEpisode').empty();
		
			//버튼에 cateogoryId 값을 넣어준다.
			update += '<a href="#" onClick="updateEpisode('+categoryId+');showHide(\'layer4\');return false;">저장</a>' ;
			$jq('#updateEpisode').append(update);
		
			}
			
		}else{
		alert(data.reason);
		}
		}
	});
	
}

function insertEpisode(categoryId){

if($jq('#episodeNm').val()== undefined || $jq('#episodeNm').val() == ""){

	alert("회차명을 입력하세요.");
	return;
	
}

if(categoryId == undefined || categoryId == ""){

	alert("카테고리가 선택되지 않았습니다.");
	return;
	
}

epsodeSearch.categoryId.value = categoryId;
epsodeSearch.episodeNm.value = $jq('#episodeNm').val();

$jq.ajax({
		url: '<@spring.url "/admin/category/insertEpisode.ssc" />',
		type: 'POST',
		dataType: 'json',
		//data: { "categoryId" : categoryId, "episodeNm" : $jq('#episodeNm').val() },
		data: $jq('#epsodeSearch').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
			episodeSearch(categoryId,'');
	 		alert("저장되었습니다.");
			$jq('#episodeNm').val('');
		
		}else{
		alert(data.reason);
		}
		}
	});
		
}


function updateEpisode(categoryId){

	var chked_val = "";
	
	if($jq(":checkbox[name='check']:checked").length == 0){
	
		alert("수정할 회차를 선택해 주세요.");
		return false;
	}else if($jq(":checkbox[name='check']:checked").length != 1){
	
		alert("수정할 회차를 하나만 선택해 주세요.");
		return false;
	}else {
		
	$(":checkbox[name='check']:checked").each(function(pi,po){
	
		chked_val += ","+po.value;
		
	});
	
	}
	
	
	if(chked_val != "")chked_val = chked_val.substring(1);
	if($jq('#episodeNm2').val() != ""){	
	
	epsodeSearch.categoryId.value = categoryId;
	epsodeSearch.episodeId.value = chked_val;
    epsodeSearch.episodeNm.value = $jq('#episodeNm2').val();
			
		$jq.ajax({
			url: '<@spring.url "/admin/category/updateEpisode.ssc" />',
			type: 'POST',
			dataType: 'json',
			//data: { "categoryId" : categoryId, "episodeId" : chked_val ,"episodeNm" : $jq('#episodeNm2').val() },
			data: $jq('#epsodeSearch').serialize(),
			
			success: function(data){
				if(data.result == "Y"){
				episodeSearch(categoryId,'');
				alert("수정되었습니다.");
				$jq('#episodeNm2').val('');
				}else{
				alert(data.reason);
				}
			}
			
		});
		
	}else{
	
		alert("회차명이 공백입니다 값을 넣어주세요");
		
	}

}

function deleteEpisode(){

	var categoryId = epsodeSearch.categoryId.value;
	
	if($jq(":checkbox[name='check']:checked").length == 0){
	
		alert("삭제할 회차를 선택해 주세요.");
		
	}else if($jq(":checkbox[name='check']:checked").length != 1){
	
		alert("삭제할 회차를 하나만 선택해 주세요.");
		
	}else {
	
		var chked_val = "";
		$(":checkbox[name='check']:checked").each(function(pi,po){
		
			chked_val += ","+po.value;

		});
		
	}
	
	if(chked_val != "")chked_val = chked_val.substring(1);

	epsodeSearch.categoryId.value = categoryId;
	epsodeSearch.episodeId.value = chked_val;
	
	$jq.ajax({
		url: '<@spring.url "/admin/category/deleteEpisode.ssc" />',
		type: 'POST',
		dataType: 'json',
		//data: { "categoryId" : categoryId, "episodeId" : chked_val  },
		data: $jq('#epsodeSearch').serialize(),
		
		success: function(data){
		
			if(data.result == "Y"){
				alert("정상적으로 삭제되었습니다.");
				episodeSearch(categoryId,'');
				$jq('#episodeNm').val('')
				$jq('#episodeNm2').val('')
			}else{
			 	alert(data.reason);
			}
		}
		
	});

}


</script>

<form name="epsodeSearch" id="epsodeSearch" method="post" >
	<@spring.bind "categoryTbl" />
	<@spring.bind "episodeTbl" />
	<@spring.bind "episodeId" />
	<@spring.bind "search" />
	<input type="hidden" name="episodeNm" />
	<input type="hidden" name="categoryId" />
	<input type="hidden" name="categoryNm" />
	<input type="hidden" name="episodeId" />
</form>

<!-- right 시작 -->
    <div class="right_cate">
     <div class="r_info" style="width : 642px;">
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
                </dl>
            </div>
<!-- 기본정보 시작 -->
            <dl class="info">
                <dt>상세정보</dt>
                <dd>
                <table summary="" class="board2">
                <colgroup><col width="180px"></col><col width="445px"></col></colgroup>
                <tbody id ="episodeSearchResult">
                <tr><th>회차명</th><td><input type="text" class="ip_text" onkeypress="episodeSearch(1,value);enterKey(13);"></input></td></tr>
                <tr>
                <td colspan="2" class="pdtb5">
                    <table summary="" class="board3">
                    <colgroup><col width="34px"/><col width="150px"/><col width="152px"/><col width="155px"/><col width="155px"/></colgroup>
                    <thead>
                    <tr><th></th><th>회차명</th><th>등록일</th><th>등록자</th><th>수정자</th></tr>
                    </thead>
                    
                    <tbody>   
                                                    
                    </tbody>
                    </table>
                </td>
                </tr>
                </tbody>
                </table>
                </dd>
            </dl>
<!-- 기본정보 끝 -->
        </div>
        <div class="btncover5">
            <!--<a href="#layer6"  onClick="showHide('layer6');return false;"><img src="<@spring.url "/images/add_off.gif"/>" id ="2" title="추가" alt="추가" onMouseOver="this.src='<@spring.url "/images/add_on.gif"/>'" onMouseOut="this.src='<@spring.url "/images/add_off.gif"/>'"></a>-->
            <!--<a href="#layer4"  onClick="showHide('layer4');return false;"><img src="<@spring.url "/images/modify_off.gif"/>" id = "3" title="수정" alt="수정" onMouseOver="this.src='<@spring.url "/images/modify_on.gif"/>'" onMouseOut="this.src='<@spring.url "/images/modify_off.gif"/>'"></a>-->
            <!--권한별 버튼-->
            <!--'6'  menuId-->
            <@button1 '7' '${user.userId}' "showHide('layer6')" '추가' 'episodeInsertButton' />
            <@button1 '7' '${user.userId}' "showHide('layer4')" '수정' 'episodeUpdateButton'/>
            <@button1 '7' '${user.userId}' 'deleteEpisode()' '삭제' 'episodeDeleteButton'/>
          
            <!--<a onClick="deleteEpisode();return false;"><img src="<@spring.url "/images/del_off.gif"/>" title="삭제" alt="삭제" onMouseOver="this.src='<@spring.url "/images/del_on.gif"/>'" onMouseOut="this.src='<@spring.url "/images/del_off.gif"/>'"></a>-->
                <!-- Popup 저장용-->
                <div id="layer6" class="box9">
                	<ul id="episodeul1" style="width : inherit;">
                        <li id="episodeid">카테고리명</li>
                        <li><input name="episodeNm" id= "categoryNm3" readonly="readonly" class="cateNm2" type="text" onkeypress="if(event.keyCode==13){insertEpisode();showHide('layer6');return false;}" ></li>
                    </ul>
                    <ul id="episodeul" style="margin-left:22px;width : inherit;">
                        <li id="episodeid">회차명</li>
                        <li><input name="episodeNm" id= "episodeNm" type="text" onkeypress="if(event.keyCode==13){insertEpisode();showHide('layer6');return false;}" ></li>
                    </ul>
                    <div class="btncover10"><span class="btn_pack gry" id ="insertEpisode1"><a href="#" id ="insertEpisode" onClick="insertEpisode();showHide('layer6');return false;">저장</a></span>
                    <span class="btn_pack gry"><a href="#layer6" onClick="showHide('layer6');return false;">닫기</a></span>
                    </div>
                </div>
                <!-- //Popup -->
                  <!-- Popup 수정용 -->
                <div id="layer4" class="box10">
                	<ul id="episodeul1" style="width : inherit;"> 
                        <li id="episodeid">카테고리명</li>
                        <li><input name="episodeNm" id= "categoryNm3" readonly="readonly" class="cateNm2" type="text" onkeypress="if(event.keyCode==13){insertEpisode();showHide('layer2');return false;}" ></li>
                    </ul>
                    <ul  id="episodeul2" style="width : inherit;">
                        <li id="episodeid2">수정회차명</li>
                        <li><input name="episodeNm2" id = "episodeNm2" type="text"  onkeypress="if(event.keyCode==13){updateEpisode();showHide('layer4');return false;}"></li>
                    </ul>
                    <div class="btncover12"><span class="btn_pack gry" id ="updateEpisode1"><a id ="updateEpisode" href="#" onClick="updateEpisode();showHide('layer4');return false;">저장</a></span>
                    <span class="btn_pack gry"><a href="#layer4" onClick="showHide('layer4');return false;">닫기</a></span>
                    </div>
                </div>
                <!-- //Popup -->
            </div>
<!-- 검색박스끝 -->
    </div>
<!-- right 끝 -->