<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">
var parentfrm = opener.parent.document.statisticSearchForDetail

window.onload = function(){	

	findStatisticDetailList();
	
}


String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};


//한 화면된 표기되는 페이지수
var pageView = 5;
function findStatisticDetailList(pageNum){

statisticSearch.startDD.value = parentfrm.startDD.value;
statisticSearch.endDD.value = parentfrm.endDD.value;
statisticSearch.categoryId.value = parentfrm.categoryId.value;
statisticSearch.gubun.value = parentfrm.gubun.value;
statisticSearch.yearList.value = parentfrm.yearList.value;
statisticSearch.findgubun.value = parentfrm.findgubun.value;

	if(pageNum == undefined){

		pageNum = 0;

	}

statisticSearch.pageNo.value = pageNum;

//연간검색이라면 당해년도의 1월 1일부터 12월 31일까지 조회를 한다.
	if(statisticSearch.findgubun.value == 'year'){

		statisticSearch.startDD.value = statisticSearch.yearList.value + '-01-01';
		statisticSearch.endDD.value = statisticSearch.yearList.value + '-12-31';
		
	}
	
	$jq.ajax({
		url: '<@spring.url "/statistic/popup/findStatisticListForDetail.ssc" />',
		type: 'GET',
		data : $jq('#statisticSearch').serialize(),
		dataType: 'json',		
	
	success: function(data){
	if(data.result == "Y"){
	//상단에 카테고리와 컬럼정보를 넣어준다.
	var tab ="";
	$jq('#searchbox1').empty();
	
	if(statisticSearch.gubun.value == '001'){//등록건
	
		tab += '<dt>'+data.categoryTbl.categoryNm+'의 등록건 상세조회</dt>'
	
	}else if(statisticSearch.gubun.value == '002'){//정리전건
	
		tab += '<dt>'+data.categoryTbl.categoryNm+'의 정리전건 상세조회</dt>'
	
	}else if(statisticSearch.gubun.value == '003'){//정리완료건
	
		tab += '<dt>'+data.categoryTbl.categoryNm+'의 정리완료건 상세조회</dt>'
	
	}else if(statisticSearch.gubun.value == '004'){//폐기건
	
		tab += '<dt>'+data.categoryTbl.categoryNm+'의 폐기건 상세조회</dt>'
	
	}else if(statisticSearch.gubun.value == '005'){//오류건
	
		tab += '<dt>'+data.categoryTbl.categoryNm+'의 오류건 상세조회</dt>'
	
	}
	
	$jq('#searchbox1').append(tab);
	
	var vdResol = ""
		
	//테이블에 리스트를 뿌려준다.	
	var table = "";
	
		$jq('#statisticListForDetail').empty();
		
		table += '<table summary="" class="board4">'
		table += '<colgroup><col width="100px"/><col width="200px"/><col width="250px"/><col width="100px"/><col width="100px"/></colgroup>'
		table += '<tbody>'
		table += '<tr>'
		table += '<th>에피소드명</th><th>컨텐츠명</th><th>영상길이</th><th>화질</th><th>방송일</th><th>등록일</th>'
		table += '</tr>'
		
		for(i = 0; i < data.statistics.length; i++){
		
		var date = new Date(parseInt(data.statistics[i].regDt, 10));
		
		if(data.statistics[i].vdHresol != null && data.statistics[i].vdVresol != null){
		
			vdResol = data.statistics[i].vdVresol+"X"+data.statistics[i].vdHresol
			
		}
		
		table += '<tr>'
		table += '<td style="text-align: left">'+data.statistics[i].episodeNm+'</td><td>'+data.statistics[i].ctNm+'</td><td>'+BlankCheck(data.statistics[i].ctLeng)+'</td><td>'+vdResol+'</td><td>'+data.statistics[i].brdDd+'</td><td>'+date.format("yyyy-MM-dd")+'</td>'
		table += '</tr>'
		
		}
		
		table += '</tbody>'
		table += '</table>'
		
		$jq('#statisticListForDetail').append(table);
		
		
		//페이징 구현
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
			
			$jq('#paging').empty();			
			
			if(pageNum<pageView && (pageNum/pageView)<1 && pages<=pageView){//최초 페이지bar화면이라면 기본값 셋팅
			
				currentMaxPaging = pages;
				startPage = 0;
				
			}else{
			
			//마지막 페이지 bar이라면 총페이지를 마지막 페이지로 설정한다.
				if(lastPageBarYn == totalPageBar || pages<(currentMaxPaging*(Math.ceil(pageNum/pageView))) || pages == (pageNum+1)){
					
					currentMaxPaging = pages;
					
					if((pages%pageView) == 0){
					
						startPage = pages - (pageView);
					
					}else{
					
						startPage = pages - (pages%pageView);
					
					}
					
					maxPaging = currentMaxPaging * (Math.ceil(pageNum/pageView));
				
				}else{			
					
					if((pageNum%pageView) != 0){//현재페이지와 설정pageView의 나머지가 0이 아니라면...
						
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
			
				paging += '<a class="direction" href="#" onClick="findStatisticDetailList('+(startPage - pageView)+');return false;">';
				paging += '<span>></span> 이전</a>';
				
			}
			
			for( var page = startPage; page < currentMaxPaging; page++ ){
			
				if(pageNum == page){
				
					paging += '<strong>'+(page+1)+'</strong>';
					
				}else{
				
					paging += '<a href="#" onClick="findStatisticDetailList('+(page)+')">'+(page+1)+'</a>';
				
				}
			
			}

		    //마지막 페이지이거나, 마지막 페이지bar라면 다음을 표기하지 않는다.
			if(lastPageBarYn == totalPageBar||(pageNum+1) == pages || (pages-currentMaxPaging) <= 0){
			
			}else if(pages != pageView){
				
				paging += '<a class="direction" href="#" onClick="findStatisticDetailList('+(currentMaxPaging)+');return false;">다음 <span>'+">"+'</span></a>';
			
			}else if(pageView<data.totalCount/data.pageSize){
				
				paging += '<a class="direction" href="#" onClick="findStatisticDetailList('+(currentMaxPaging)+');return false;">다음 <span>'+">"+'</span></a>';
			
			}
			
			$jq('#paging').append(paging);
			}else{
			alert(data.reason);
			}
		}
		
	})
	
}

</script>

<form name="statisticSearch" id="statisticSearch" method="post" >
	<@spring.bind "search" />
	<@spring.bind "statistic" />
	<input type="hidden" name="pageNo" />
	<input type="hidden" name="startDD" />
	<input type="hidden" name="endDD" />
	<input type="hidden" name="categoryId" />
	<input type="hidden" name="gubun" />
	<input type="hidden" name="findgubun" />
	<input type="hidden" name="yearList" />

</form>

<form name="categoryTreeSearch" id="categoryTreeSearch" method="post" >
	<input type="hidden" name="depth"  />
	<input type="hidden" name="categoryId"  />
	<input type="hidden" name="preParent"  />
</form>




        <!-- 상세 통계조회 시작 -->
		<div id="forDetail" class="forDetail"> 
            <nav class="tabcover"> <!-- 탭시작 -->
                <ul class="tab1">
                <li class="on"><a href="#forDetail"><span>상세정보</span></a></li>                 
                </ul>
            </nav> <!-- 탭끝 -->
            <div class="detail">
        	<div class="searchbox1" id="searchbox1">
            	
                <dt>카테고리</dt>
                </div>
                
               
        </div>
              <div class="box2" id ="statisticListForDetail">
                  <table summary="" class="board4">
                <colgroup><col width="100px"/><col width="200px"/><col width="250px"/><col width="100px"/><col width="100px"/></colgroup>
                <tbody>
                <tr>
                <th>등록일</th><th>에피소드명</th><th>컨텐츠명</th><th>방송길이</th><th>방송일</th><th>화질</th>
                </tr>
                </tbody>
                </table>
            </div>
                  <!-- paginate -->
  <article id="paging" class="paginate">
    </article>  
<!-- paginate -->
                
        </div>  
    
       <!-- 상세 통계조회 끝 -->
       
  
