<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">

<script type="text/javascript">

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};


window.onload=function(){	

	findNoticeList('init');
}



//한 화면된 표기되는 페이지수
var pageView=5;
var preKeyWord='';
function findNoticeList(pageNum){

	if(pageNum == "init"){
		pageNum=0;
		noticeInfo.pageNo.value = 1;
		noticeInfo.keyword.value =''; 
		noticeInfo.cont.value = '';
		noticeInfo.regId.value = '';
		$jq('#keyword').val('')
	
	}else{
	
		noticeInfo.pageNo.value = pageNum;
		if($jq('#keyword').val() != ''){
			noticeInfo.searchFiled.value = $jq('#selectField').val();
			noticeInfo.keyword.value = $jq('#keyword').val();
		}
		
	}
	
$jq.ajax({
		url: '<@spring.url "/admin/notice/findNoticeList.ssc" />',
		type: 'GET',
		dataType: 'json',
		data : $jq('#noticeInfo').serialize(),
		success: function(data){
		
		if(data.result == "Y"){
			$jq('#tbody').empty();
			
			var tbody ="";
			
			tbody += '<colgroup><col width="30px"/><col width="140px"/><col width="80px"/><col width="80px"/><col width="30px"/></colgroup>'
			tbody += '<tbody>'
			tbody += '<tr>'
			tbody += '<th>번호</th><th>제목</th><th>등록자</th><th>등록일</th><th>팝업여부</th>'
			tbody += ' </tr>'
			
			if(data.noticeTbls.length != 0){
				for(var i=0;i<data.noticeTbls.length;i++){	
					var date = new Date(parseInt(data.noticeTbls[i].regDt, 10));
					tbody += '<tr onclick="getNoticeInfo(\''+data.noticeTbls[i].noticeId+'\')">'
					tbody += '<td><a href="#">'+data.noticeTbls[i].noticeId+'</a></td><td><a href="#">'+data.noticeTbls[i].title+'</a></td><td><a href="#">'+BlankCheck(data.noticeTbls[i].userNm)+'</a></td><td><a href="#">'+date.format("yyyy-MM-dd")+'</a></td><td><a href="#">'+BlankCheck(data.noticeTbls[i].popUpYn)+'</a></td>'
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

</script>


<div id="left"> <!-- left시작 -->
	<div class="left_code">
    <table summary="" id = "tbody" class="board1">
    <colgroup><col width="30px"/><col width="140px"/><col width="80px"/><col width="80px"/><col width="30px"/></colgroup>
    <tbody>
	<tr>
    <th>번호</th><th>제목</th><th>등록자</th><th>등록일</th><th>팝업여부</th>
    </tr>

    </tbody>
    </table>
        
    <div class="btncover21">
   	<select id="selectField" >
    <option value="title">제목</option>
    <option value="regNm">등록자명</option>
    </select>
    <input name="searchKey" type="text" id="keyword" onkeypress="if(event.keyCode==13){findNoticeList('1');return false;}">
     
        </div>
             
<!-- paginate -->
  <article id="paging" class="paginate">
    </article>  
<!-- paginate -->
          
    </div>
         
</div>
<!-- left 끝 -->