<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};


window.onload=function(){	

	findCodeList(0);
}



function insertClfInfo(){

	var clfNm = $jq('#clfCdNm').val();
	var clfCd = $jq('#textclfCd').val();
	var clfGubun = $jq('#clfGubun').val();
	console.log("!!!!!!!!!!!!!!"+clfGubun);
	if(clfCd == undefined){
		clfCd='';
	}
	codeInfo.clfNM.value=clfNm;
	codeInfo.clfCD.value=clfCd;
	codeInfo.clfGubun.value=clfGubun;
	codeInfo.createWay.value = $jq('#createWay').val();
	if(clfNm == ""){
	
		alert("분류코드명을 입력하세요.");
		return;
		
	}
	
	$jq.ajax({
		url: '<@spring.url "/admin/code/insertClfInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data : $jq('#codeInfo').serialize(),
		
		success: function(data){
			
			if(data.result == "Y"){
				alert('저장 되었습니다');
				$jq('#clfCdNm').val('');
				clearData();
				$jq('#selectClfCd').val('');	
				$jq('#searchKey').val('');
				findCodeList(0);
			}else if(data.result == "N"){
				alert(data.reason);
			}
		}
		
	});

}

//한 화면된 표기되는 페이지수
var pageView=5;
var preKeyWord='';
function findCodeList(pageNum){

	if(pageNum == "init"){
		pageNum=0;
		codeInfo.pageNo.value = 0;
		codeInfo.gubun.value = $jq('#selectClfCd').val();	
		codeInfo.keyWord.value = '';
		$jq('#searchKey').val('')
	
	}else{
	
		codeInfo.pageNo.value = pageNum;
		codeInfo.gubun.value = $jq('#selectClfCd').val();
		
		if(pageNum != 0 && preKeyWord == $jq('#searchKey').val()){	
		
			codeInfo.keyWord.value = $jq('#searchKey').val();
		
		}else if(pageNum == 0){
			codeInfo.keyWord.value = $jq('#searchKey').val();
		}else{
			console.log("!!!!!!!!!!!!!!");
			codeInfo.keyWord.value = ''
			$jq('#searchKey').val('');
		
		}
		
		preKeyWord = $jq('#searchKey').val();
		
	}
	
$jq.ajax({
		url: '<@spring.url "/admin/code/findCodeList.ssc" />',
		type: 'POST',
		dataType: 'json',
		data : $jq('#codeInfo').serialize(),
		
		success: function(data){
		
		if(data.result == "Y"){
			$jq('#tbody').empty();
			
			clearData('clear');
			var tbody ="";
			
			tbody += '<colgroup><col width="80px"/><col width="140px"/><col width="80px"/><col width=""/><col width="100px"/><col width="80px"/></colgroup>'
			tbody += '<tbody>'
			tbody += '<tr>'
			tbody += '<th>분류코드</th><th>분류명</th><th>상세코드</th><th>상세코드명</th><th>등록일</th><th>사용여부</th>'
			tbody += ' </tr>'
			
			if(data.codeInfos.length != 0){
				for(var i=0;i<data.codeInfos.length;i++){	
					var date = new Date(parseInt(data.codeInfos[i].regDt, 10));
					tbody += '<tr onclick="getCodeInfo(\''+data.codeInfos[i].id.clfCD+'\',\''+data.codeInfos[i].id.sclCd+'\')">'
					tbody += '<td><a href="#">'+data.codeInfos[i].id.clfCD+'</a></td><td><a href="#">'+data.codeInfos[i].clfNM+'</a></td><td><a href="#">'+data.codeInfos[i].id.sclCd+'</a></td><td><a href="#">'+BlankCheck(data.codeInfos[i].sclNm)+'</a></td><td><a href="#">'+date.format("yyyy-MM-dd")+'</a></td><td><a href="#">'+data.codeInfos[i].useYn+'</a></td>'
					tbody += '</tr>'
				}
			}else{
				alert("검색결과가 없습니다.");
			}
			$jq('#tbody').append(tbody);
			
			//현재페이지중 최대 페이지값 default는 pageView의 값을 가진다.
			var currentMaxPaging = pageView;
			
			//최대페이지수
			var maxPaging;
			
			//화면의 시작페이지 default는 0
			var startPage = 0;
			
			//화면의 종료 페이지 defualt는 0
			var EndPage = 0;
			
			//페이지html받는 변수
			var paging = "";
			
			//총페이지를 정수로 구한다.
			var pages = Math.ceil(data.totalCount/data.pageSize);
			
			//총 페이지bar수를 구한다.
			var totalPageBar = Math.ceil(pages/pageView);
			
			//마지막 페이지 bar 여부를 판단한다. pageView값과 비교해 같거나 작다면 마지막 페이지 bar 이다.
			var lastNum = "";
			
			var lastPageBarYn = "";

			if(pageNum%pageView != 0){
			
				lastPageBarYn = Math.ceil(pageNum/pageView);
				
			}else{
			
			 	lastPageBarYn = Math.ceil(pageNum/pageView)+1;
			 	
			}
			
			console.log("pageNum<pageView " +(pageNum<pageView));
			console.log("(pageNum/pageView) " + (pageNum/pageView));
			console.log("pageNum "+pageNum);
			console.log("pageView "+pageView);
			console.log("pages "+pages);
			
			$jq('#paging').empty();			
			
			if(pageNum < pageView && (pageNum/pageView) < 1 && pages <= pageView){//최초 페이지bar화면이라면 기본값 셋팅
				currentMaxPaging = pages;
				startPage = 0;
			}else{
			//마지막 페이지 bar이라면 총페이지를 마지막 페이지로 설정한다.
				if(lastPageBarYn == totalPageBar || pages<(currentMaxPaging*(Math.ceil(pageNum/pageView))) || pages == (pageNum+1)){
				console.log("#########################333");
					currentMaxPaging = pages;
					
					if((pages%pageView) == 0){
					  
					   startPage = pages - (pageView);
					
					}else{
					  
					   startPage = pages - (pages%pageView);
					
					} 
					
					maxPaging = currentMaxPaging * (Math.ceil(pageNum/pageView));
				
				}else{			
					
					if((pageNum%pageView)!= 0){//현재페이지와 설정pageView의 나머지가 0이 아니라면...	
						
						currentMaxPaging = currentMaxPaging*(Math.ceil(pageNum/pageView))
						maxPaging = currentMaxPaging*(Math.ceil(pageNum/pageView)-1)
					
					}else{//현재페이지와 설정pageView 나머지가 0이라면 ...
						
						currentMaxPaging = currentMaxPaging*(Math.floor(pageNum/pageView)+1)
						maxPaging = currentMaxPaging*(Math.floor(pageNum/pageView))
					
					}	
						
						startPage = currentMaxPaging - pageView;
			
				}
			
			}
			
			//총페이지수가 pageView보다 작다거나 같다면 , 그리고 현재 페이지가 pageView보다 작다면 이전을 표기 하지 않는다
			if(pageView > pageNum || pageView == pages || pageView > pages){
			
			}else {
			
				paging += '<a class="direction" href="#" onClick="findCodeList('+(startPage - pageView)+');return false;">';
				paging += '<span>‹</span> 이전</a>';
			
			}
			
			console.log("11111");
			for( var page = startPage; page < currentMaxPaging; page++ ){
				if(pageNum == page){
				
					paging += '<strong>'+(page+1)+'</strong>';
					
				}else{
				
					paging += '<a href="#" onClick="findCodeList('+(page)+')">'+(page+1)+'</a>';
					
				}
				
			}
			//검색결과가 없으면 무조건 1을 표기해준다,
			if(pageNum == 0 && pages == 0){
				paging += '<strong>'+(page+1)+'</strong>';
			}

		    //마지막 페이지이거나, 마지막 페이지bar라면 다음을 표기하지 않는다.
			if(lastPageBarYn == totalPageBar||(pageNum+1) == pages || (pages-currentMaxPaging) <= 0){
			
			}else if(pages != pageView){
			
				paging += '<a class="direction" href="#" onClick="findCodeList('+(currentMaxPaging)+');return false;">다음 <span>›</span></a>';
				
			}else if(pageView<data.totalCount/data.pageSize){
			
				paging += '<a class="direction" href="#" onClick="findCodeList('+(currentMaxPaging)+');return false;">다음 <span>›</span></a>';
			
			}
			
			$jq('#paging').append(paging);
			}else{
			alert(data.reason);
			}
		}
		
	});
		
}


function changeValue(value){
	
	$jq('#selectClfCd').val(value);
}
</script>

<form name="codeInfo" id="codeInfo" method="post" >
	<@spring.bind "codeTbl" />
	<@spring.bind "search" />
	<@spring.bind "codeId" />
	<input type="hidden" name="clfNM"  />
	<input type="hidden" name="clfCD"  />
	<input type="hidden" name="sclCd"  />
	<input type="hidden" name="sclNm"  />
	<input type="hidden" name="codeCont"  />
	<input type="hidden" name="pageNo"  />
	<input type="hidden" name="gubun"  />
	<input type="hidden" name="keyWord"  />
	<input type="hidden" name="clfGubun"  />
	<input type="hidden" name="createWay"  />
</form>

<div id="left"> <!-- left시작 -->
	<div class="left_code">
    <table summary="" id = "tbody" class="board1">
    <colgroup><col width="80px"/><col width="140px"/><col width="80px"/><col width=""/><col width="100px"/><col width="80px"/></colgroup>
    <tbody>
	<tr>
    <th>분류코드</th><th>분류명</th><th>상세코드</th><th>상세코드명</th><th>등록일</th><th>사용여부</th>
    </tr>

    </tbody>
    </table>
        
    <div class="btncover4">
    <span class="btn_pack xlarge" style="left : -163px"><a href="javascript:" onclick="findCodeList('init');return false;">목록</a></span>
    <select id="selectClfCd" onChange="changeValue(value)">
    <option value="clfNm">분류명</option>
    <option value="sclNm">상세코드명</option>
  <!--  <option value="S">시스템코드</option> -->
   <!-- <option value="U">유저코드</option> -->
    </select>
    <input name="searchKey" type="text" id="searchKey" onkeypress="if(event.keyCode==13){findCodeList(0);return false;}">
       <!--권한별 버튼-->
       <!--'6' menuId--> 
         <@button1 '10' '${user.userId}' "showHide('layer5');" '코드추가' 'codeInsertButton' />
        </div>
             
<!-- paginate -->
  <article id="paging" class="paginate">
    </article>  
<!-- paginate -->
          
    </div>
          <!-- Popup start -->
                <div id="layer5" class="box7">
             
                     <table summary="" class="board2">
                <colgroup><col width="100px"></col><col width="125px"></col></colgroup>
                <tbody>
                <tr><th>분류코드 생성방식</th><td><select id="createWay" name="createWay" style="width : 126px" onChange="changeInputStyle(value)"><option value="auto">자동</option><option  value="manual">수동</option></select></td></tr>
                <tr><th>코드유형</th><td><select id="clfGubun" style="width : 126px"><option value="U">유저코드</option><option  value="S">시스템코드</option></select></td></tr>
                <tr><th>분류코드</th><td id ="inputclfCd" ></td></tr>
                <tr><th>분류코드명</th><td><input name="clfCdNm" id = "clfCdNm" type="text"  onkeypress="if(event.keyCode==13){insertClfInfo();showHide('layer5');return false;}"  ></td></tr>
                
                </tbody>
                </table>
                    <div class="btncover13"><span class="btn_pack gry" ><a id ="insertClf" href="#" onClick="insertClfInfo();showHide('layer5');return false;">저장</a></span>
                    <span class="btn_pack gry"><a href="#layer5" onClick="showHide('layer5');return false;">닫기</a></span>
                    </div>
                </div>
                <!-- //Popup end -->
</div>
<!-- left 끝 -->