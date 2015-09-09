<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">
//카테고리 검색용 
var Ids = new Array();
var tempIds = new Array();
var tempId = "";

//엑셀 데이터용
var temp_table ="";
//통계데이터 계산용 임시 변수
var temp_regist_total = 0;
var temp_beforeArrange_total = 0;
var temp_completeArrange_total =0 ;
var temp_discard_total = 0;
var temp_error_total = 0;
var temp_groupId = 0;

//전체합을 위한 변수
var regist_total = 0;
var beforeArrange_total = 0;
var completeArrange_total = 0;
var discard_total = 0;
var error_total = 0;

window.onload=function(){
	
	nodeCateogry();
	findYearList();
	findStatisticListForPeriod('first');
	
	tree_call("tree_start",'<@spring.url ""/>');
	
}

function findStatisticListForPeriod(value){

statisticSearch.startDD.value = $jq('#from').val();
statisticSearch.endDD.value = $jq('#to').val();
statisticSearch.pageNo.value = '0';

if(value == "first"){

statisticSearch.startDD.value = getMonthOfFirstDate(new Date());
statisticSearch.endDD.value = getMonthOfLastDate(new Date());
$jq('#from').val(statisticSearch.startDD.value);
$jq('#to').val(statisticSearch.endDD.value);
$jq('#category').val("전체");
} else if($jq('#to').val() == "" || $jq('#from').val() == ""){

 alert("시작일, 종료일값이 없습니다.");
 return false;
 
}
	
	 
	 var sdt = new Date($jq('#from').val().split("-"));
	 var edt = new Date($jq('#to').val().split("-"));
	
	if( edt.getTime() < sdt.getTime() ){
	
	alert("시작날짜가 종료날짜보다 큽니다");
	return;
	
	}
	
beforeSearch.startDD.value = statisticSearch.startDD.value;
beforeSearch.endDD.value = statisticSearch.endDD.value;
	

	$jq.ajax({
		url: '<@spring.url "/statistic/findStatisticListForPeriod.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#statisticSearch').serialize(),
		
		success: function(data){
			tab="";
		   if(data.result == "Y"){
		$jq('#statisticListForPeriod').empty();
			
    		tab += '<table summary="" class="board4">'
    		tab += '<colgroup><col width="100px"/><col width="100px"/><col width="100px"/><col width="100px"/><col width="100px"/><col width=100px"/><col width="100px"/><col width="100px"/><col width="100px"/></colgroup>'
    		tab += '<tbody>'
    		tab += '<tr>'
    		tab += '<th>상위카테고리</th><th>중간카테고리</th><th>하위카테고리</th><th>등록건</th><th>정리전건</th><th>정리완료건</th><th>폐기건</th><th>오류</th>'
    		tab += '</tr>'
    		
    		   for(var i = 0; i < data.statisticsTbls.length; i++){	
    		   
           		if(data.statisticsTbls[i].groupId != temp_groupId && temp_groupId != 0){
           	
           			tab += '<tr>'
           			tab += '<th></th><th>카테고리별 합</th><th></th><th>'+temp_regist_total+'</th><th>'+temp_beforeArrange_total+'</th><th>'+temp_completeArrange_total+'</th><th>'+temp_discard_total+'</th><th>'+temp_error_total+'</th>'
           			tab += '</tr>'
           			
           			//각 컬럼의 임시데이터를 합한다
           			regist_total += parseInt(temp_regist_total)
					beforeArrange_total += parseInt(temp_beforeArrange_total)
					completeArrange_total += parseInt(temp_completeArrange_total)
					error_total += parseInt(temp_error_total)
					discard_total  += parseInt(temp_discard_total)
           			//임시데이터 초기화
           			temp_regist_total = 0
           			temp_beforeArrange_total = 0
           			temp_completeArrange_total = 0
           			temp_discard_total = 0
           			temp_error_total = 0
           			
           			}
           			
           		tab += '<tr>'
           		
           		if(data.statisticsTbls[i].depth == 0){
           		
           			tab += '<td>'+data.statisticsTbls[i].categoryNm+'</td><td></td><td></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"001"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].regist)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"002"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].beforeArrange)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"003"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].completeArrange)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"004"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].discard)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"005"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].error)+'</a></td>'
           			
           			//각 컬럼의 값을 각각 더한다.
           			temp_regist_total += parseInt(BlankToZero(data.statisticsTbls[i].regist))
           			temp_beforeArrange_total += parseInt(BlankToZero(data.statisticsTbls[i].beforeArrange))
           			temp_completeArrange_total += parseInt(BlankToZero(data.statisticsTbls[i].completeArrange))
           			temp_discard_total += parseInt(BlankToZero(data.statisticsTbls[i].discard))
           			temp_error_total += parseInt(BlankToZero(data.statisticsTbls[i].error))
           			
           		}else if(data.statisticsTbls[i].depth == 1){           			
           			
           			tab += '<td></td><td>'+data.statisticsTbls[i].categoryNm+'</td><td></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"001"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].regist)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"002"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].beforeArrange)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"003"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].completeArrange)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"004"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].discard)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"005"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].error)+'</a></td>'
           			
           			//각 컬럼의 값을 각각 더한다.
           			temp_regist_total += parseInt(BlankToZero(data.statisticsTbls[i].regist))
           			temp_beforeArrange_total += parseInt(BlankToZero(data.statisticsTbls[i].beforeArrange))
           			temp_completeArrange_total += parseInt(BlankToZero(data.statisticsTbls[i].completeArrange))
           			temp_discard_total += parseInt(BlankToZero(data.statisticsTbls[i].discard))
           			temp_error_total += parseInt(BlankToZero(data.statisticsTbls[i].error))
           			
           			
           		}else if(data.statisticsTbls[i].depth == 2){
           		
           			tab += '<td></td><td></td><td>'+data.statisticsTbls[i].categoryNm+'</td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"001"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].regist)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"002"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].beforeArrange)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"003"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].completeArrange)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"004"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].discard)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"005"+'\',\''+"period"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].error)+'</a></td>'
           		
           			//각 컬럼의 값을 각각 더한다.
           			temp_regist_total += parseInt(BlankToZero(data.statisticsTbls[i].regist))
           			temp_beforeArrange_total += parseInt(BlankToZero(data.statisticsTbls[i].beforeArrange))
           			temp_completeArrange_total += parseInt(BlankToZero(data.statisticsTbls[i].completeArrange))
           			temp_discard_total += parseInt(BlankToZero(data.statisticsTbls[i].discard))
           			temp_error_total += parseInt(BlankToZero(data.statisticsTbls[i].error))
           			           			
           		}
           		           		
           		tab += '</tr>'
           	
           	if(i == data.statisticsTbls.length-1){
           	
           			tab += '<tr>'
           			tab += '<th></th><th>카테고리별 합</th><th></th><th>'+temp_regist_total+'</th><th>'+temp_beforeArrange_total+'</th><th>'+temp_completeArrange_total+'</th><th>'+temp_discard_total+'</th><th>'+temp_error_total+'</th>'
           			tab += '</tr>'
           			
           			//각 컬럼의 임시데이터를 합한다
           			regist_total += parseInt(temp_regist_total)
					beforeArrange_total += parseInt(temp_beforeArrange_total)
					completeArrange_total += parseInt(temp_completeArrange_total)
					error_total += parseInt(temp_error_total)
					discard_total  += parseInt(temp_discard_total)
					
					var totalSum = parseInt(regist_total) + parseInt(error_total)
           			//임시데이터 초기화
           			temp_regist_total = 0
           			temp_beforeArrange_total= 0
           			temp_completeArrange_total = 0
           			temp_discard_total = 0
           			temp_error_total = 0
           			
           			
           			tab += '<tr>'
           			tab += '<th></th><th>전체 합</th><th></th><th>'+regist_total+'</th><th>'+beforeArrange_total+'</th><th>'+completeArrange_total+'</th><th>'+discard_total+'</th><th>'+error_total+'</th>'
           			tab += '</tr>'
           			
           			}
           			
           		temp_groupId =data.statisticsTbls[i].groupId;
           		
        	}
        	
    		tab += '</tbody>'
    		tab += '</table>'
    		
    		//엑셀표기용 임시 데이터
    		temp_table = "";
    		temp_groupId = 0;
    		temp_table += '<table>'
    		temp_table += '<colgroup><col/><col/><col/><col"/><col"/><col"/><col"/><col"/><col"/></colgroup>'
    		temp_table += '<tbody>'
    		temp_table += '<tr>'
    		temp_table += '<th>상위카테고리</th><th>중간카테고리</th><th>하위카테고리</th><th>등록건</th><th>정리전건</th><th>정리완료건</th><th>폐기건</th><th>오류</th>'
    		temp_table += '</tr>'
    		
    		   for(var i = 0; i < data.statisticsTbls.length; i++){	
    		   
           		if(data.statisticsTbls[i].groupId != temp_groupId && temp_groupId != 0){
           			temp_table += '<tr>'
           			temp_table += '<th></th><th>카테고리별 합</th><th></th><th>'+temp_regist_total+'</th><th>'+temp_beforeArrange_total+'</th><th>'+temp_completeArrange_total+'</th><th>'+temp_discard_total+'</th><th>'+temp_error_total+'</th>'
           			temp_table += '</tr>'
           			
           			}
           			
           		temp_table += '<tr>'
           		
           		if(data.statisticsTbls[i].depth == 0){
           		
           			temp_table += '<td>'+data.statisticsTbls[i].categoryNm+'</td><td></td><td></td><td>'+BlankCheck(data.statisticsTbls[i].regist)+'</td><td>'+BlankCheck(data.statisticsTbls[i].beforeArrange)+'</td><td>'+BlankCheck(data.statisticsTbls[i].completeArrange)+'</td><td>'+BlankCheck(data.statisticsTbls[i].discard)+'</td><td>'+BlankCheck(data.statisticsTbls[i].error)+'</td>'
           			           			
           		}else if(data.statisticsTbls[i].depth == 1){           			
           			
           			temp_table += '<td></td><td>'+data.statisticsTbls[i].categoryNm+'</td><td></td><td>'+BlankCheck(data.statisticsTbls[i].regist)+'</td><td>'+BlankCheck(data.statisticsTbls[i].beforeArrange)+'</td><td>'+BlankCheck(data.statisticsTbls[i].completeArrange)+'</td><td>'+BlankCheck(data.statisticsTbls[i].discard)+'</td><td>'+BlankCheck(data.statisticsTbls[i].error)+'</td>'
           			           			
           		}else if(data.statisticsTbls[i].depth == 2){
           		
           			temp_table += '<td></td><td></td><td>'+data.statisticsTbls[i].categoryNm+'</td><td>'+BlankCheck(data.statisticsTbls[i].regist)+'</td><td>'+BlankCheck(data.statisticsTbls[i].beforeArrange)+'</td><td>'+BlankCheck(data.statisticsTbls[i].completeArrange)+'</td><td>'+BlankCheck(data.statisticsTbls[i].discard)+'</td><td>'+BlankCheck(data.statisticsTbls[i].error)+'</td>'
           		    
           		}
           		           		
           		temp_table += '</tr>'
           	
           	if(i == data.statisticsTbls.length-1){
           	
           			temp_table += '<tr>'
           			temp_table += '<th></th><th>카테고리별 합</th><th></th><th>'+temp_regist_total+'</th><th>'+temp_beforeArrange_total+'</th><th>'+temp_completeArrange_total+'</th><th>'+temp_discard_total+'</th><th>'+temp_error_total+'</th>'
           			temp_table += '</tr>'
           			           	
					var totalSum = parseInt(regist_total) + parseInt(error_total)
           		
           			temp_table += '<tr>'
           			temp_table += '<th></th><th>전체 합</th><th></th><th>'+regist_total+'</th><th>'+beforeArrange_total+'</th><th>'+completeArrange_total+'</th><th>'+discard_total+'</th><th>'+error_total+'</th>'
           			temp_table += '</tr>'
           		
           		}
           			
           	temp_groupId =data.statisticsTbls[i].groupId;
           		
        	}
        	
    		temp_table += '</tbody>'
    		temp_table += '</table>'
    		
			$jq('#statisticListForPeriod').append(tab);
			
			if(value == 'first'){
			
			  findStatisticForPeriodForGraph('first',1);
			
			}else{
			
			  findStatisticForPeriodForGraph('',1);
			  
			}
			//모든변수 초기화 다음 검색을 위해서
			
			//통계데이터 계산용 임시 변수
			 temp_regist_total = 0;
			 temp_beforeArrange_total = 0;
			 temp_completeArrange_total = 0;
			 temp_discard_total = 0;
			 temp_error_total = 0;
			 temp_groupId = 0;
			
			//전체합을 위한 변수
			 regist_total = 0;
			 beforeArrange_total = 0;
			 completeArrange_total = 0;
			 discard_total = 0;
			 error_total = 0;
			}else{
			alert(data.reason);
			}
		}
		
	});
	
}

//한 화면된 표기되는 페이지수
var pageView = 3;
function findStatisticForPeriodForGraph(value,pageNum){

statisticSearch.startDD.value = $jq('#from').val();
statisticSearch.endDD.value = $jq('#to').val();
statisticSearch.pageNo.value = pageNum;

if(value == "first" || statisticSearch.startDD.value == ''){

statisticSearch.startDD.value = getMonthOfFirstDate(new Date());
statisticSearch.endDD.value=getMonthOfLastDate(new Date());

} 

	 var sdt = new Date($jq('#from').val().split("-"));
	 var edt = new Date($jq('#to').val().split("-"));
	
	if( edt.getTime() < sdt.getTime() ){
	
	statisticSearch.startDD.value = beforeSearch.startDD.value;
	statisticSearch.endDD.value = beforeSearch.endDD.value;
	
	$jq('#to').val(beforeSearch.endDD.value)
	$jq('#from').val(beforeSearch.startDD.value)
	
	}
	
	$jq.ajax({
		url: '<@spring.url "/statistic/findStatisticListForPeriod.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#statisticSearch').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
		var xml  = jsonToXmlForPeriod(data);
		drawChart(xml,'period');
		
		
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
			
			//이전페이지가 참이라면 다른 css 값을 준다.
		    var beforePageYn ='N'
		    
		    
			var lastPageBarYn = "";
			
			if(pageNum%pageView != 0){
			
				lastPageBarYn = Math.ceil(pageNum/pageView);
			
			}else{
			
			  	lastPageBarYn = Math.ceil(pageNum/pageView)+1;
			
			}
			
			
			  //마지막 페이지이거나, 마지막 페이지bar라면 다음을 표기하지 않는다.
			var lastbar = data.totalCount - (pageNum*data.pageSize)
			
			
			$jq('#paging').empty();			
				console.log("pageNum<pageView  "+pageNum<pageView);
				console.log("(pageNum/pageView)  " + (pageNum/pageView));
				console.log("pages<pageView  " + pages<pageView);
				console.log("pageNum  "+pageNum);
				console.log("pageView  " + pageView);
				console.log("pages  "+pages);
				console.log("data.totalCount  " + data.totalCount);
				console.log("data.pageSize  "+data.pageSize);
				console.log("data.pageSize  "+data.pageSize);
			if(pageNum<pageView && (pageNum/pageView)<1 && pages<pageView){//최초 페이지bar화면이라면 기본값 셋팅
				console.log("###########")
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
			if(pageNum != 1){
				
				
				var changeBeforeClasse="leftDirection"
				console.log("######################lastbar    "+lastbar);
				if(lastbar > 0){
				changeBeforeClasse = "leftDirection2"
				}
			
				paging += '<a class="'+changeBeforeClasse+'" href="#" onClick="findStatisticForPeriodForGraph(\''+""+'\','+(pageNum-1)+');return false;">';
				
				paging += '<img src="<@spring.url '/images/leftmenubar.gif'/>" title="검색" alt="검색">';
				beforePageYn = "Y"
			}
			
				console.log("pageNum  "+pageNum);				
				console.log("currentMaxPaging  " + currentMaxPaging);
				console.log("startPage  "+startPage);
				console.log("maxPaging  " + maxPaging);
				
				
		
			

		  
			if(pageNum == currentMaxPaging || lastbar < 0){
			
			} else {
			var changeClass = "rightDirection";
			
			if(beforePageYn == "Y"){
			changeClass="rightDirection2";
			}
			
			paging += '<a class="'+changeClass+'" href="#" onClick="findStatisticForPeriodForGraph(\''+""+'\','+(pageNum+1)+');return false;"><img src="<@spring.url '/images/rightmenubar.gif'/>" title="검색" alt="검색" >'+'</a>';
			
			}
						
			$jq('#paging').append(paging);
			}else{
			alert(data.reason);
			}
		}
		
	});
	
}

function findStatisticListForYear(value){

statisticSearch.yearList.value=$jq('#selectYear').val();

if(value == "first"){

statisticSearch.yearList.value = getYear(new Date());

}else if($jq('#selectYear').val() == ""){

 alert("검색 연도를 선택해주세요");
 return false;
 
}

	$jq.ajax({
		url: '<@spring.url "/statistic/findStatisticListForYear.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#statisticSearch').serialize(),
		
		success: function(data){
		
			tab = "";
		   if(data.result == "Y"){
		$jq('#statisticListForYear').empty();
			
    		tab += '<table summary="" class="board4">'
    		tab += '<colgroup><col width="100px"/><col width="100px"/><col width="100px"/><col width="100px"/><col width="100px"/><col width=100px"/><col width="100px"/><col width="100px"/><col width="100px"/></colgroup>'
    		tab += '<tbody>'
    		tab += '<tr>'
    		tab += '<th>상위카테고리</th><th>중간카테고리</th><th>하위카테고리</th><th>등록건</th><th>정리전건</th><th>정리완료건</th><th>폐기건</th><th>오류</th>'
    		tab += '</tr>'
    		
    		   for(var i = 0; i < data.statisticsTbls.length; i++){	
    		 
           	if(data.statisticsTbls[i].groupId != temp_groupId && temp_groupId != 0){
           	
           			tab += '<tr>'
           			tab += '<th></th><th>카테고리별 합</th><th></th><th>'+temp_regist_total+'</th><th>'+temp_beforeArrange_total+'</th><th>'+temp_completeArrange_total+'</th><th>'+temp_discard_total+'</th><th>'+temp_error_total+'</th>'
           			tab += '</tr>'
           			
           			//각 컬럼의 임시데이터를 합한다
           			regist_total += parseInt(temp_regist_total)
					beforeArrange_total += parseInt(temp_beforeArrange_total)
					completeArrange_total += parseInt(temp_completeArrange_total)
					error_total += parseInt(temp_error_total)
					discard_total  += parseInt(temp_discard_total)
           			//임시데이터 초기화
           			temp_regist_total = 0
           			temp_beforeArrange_total = 0
           			temp_completeArrange_total = 0
           			temp_discard_total = 0
           			temp_error_total = 0
           			
           			}
           			
           		tab += '<tr>'
           		
           		if(data.statisticsTbls[i].depth == 0){
           		
           			tab += '<td>'+data.statisticsTbls[i].categoryNm+'</td><td></td><td></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"001"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].regist)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"002"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].beforeArrange)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"003"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].completeArrange)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"004"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].discard)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"005"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].error)+'</a></td>'
           			
           			//각 컬럼의 값을 각각 더한다.
           			temp_regist_total += parseInt(BlankToZero(data.statisticsTbls[i].regist))
           			temp_beforeArrange_total += parseInt(BlankToZero(data.statisticsTbls[i].beforeArrange))
           			temp_completeArrange_total += parseInt(BlankToZero(data.statisticsTbls[i].completeArrange))
           			temp_discard_total += parseInt(BlankToZero(data.statisticsTbls[i].discard))
           			temp_error_total += parseInt(BlankToZero(data.statisticsTbls[i].error))
           			
           		}else if(data.statisticsTbls[i].depth == 1){           			
           		
           			tab += '<td></td><td>'+data.statisticsTbls[i].categoryNm+'</td><td></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"001"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].regist)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"002"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].beforeArrange)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"003"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].completeArrange)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"004"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].discard)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"005"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].error)+'</a></td>'
           			
           			//각 컬럼의 값을 각각 더한다.
           			temp_regist_total += parseInt(BlankToZero(data.statisticsTbls[i].regist))
           			temp_beforeArrange_total += parseInt(BlankToZero(data.statisticsTbls[i].beforeArrange))
           			temp_completeArrange_total += parseInt(BlankToZero(data.statisticsTbls[i].completeArrange))
           			temp_discard_total += parseInt(BlankToZero(data.statisticsTbls[i].discard))
           			temp_error_total += parseInt(BlankToZero(data.statisticsTbls[i].error))
           			
           			
           		}else if(data.statisticsTbls[i].depth == 2){
           			  console.log("####################data.statisticsTbls[i].categoryNm  "+data.statisticsTbls[i].categoryNm);
    		     console.log("####################data.statisticsTbls[i].regist  "+data.statisticsTbls[i].regist);
    		   
           			tab += '<td></td><td></td><td>'+data.statisticsTbls[i].categoryNm+'</td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"001"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].regist)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"002"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].beforeArrange)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"003"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].completeArrange)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"004"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].discard)+'</a></td><td><a href="#" onClick="openWindow('+data.statisticsTbls[i].categoryId+',\''+"005"+'\',\''+"year"+'\');return false;">'+BlankCheck(data.statisticsTbls[i].error)+'</a></td>'
           		
           			//각 컬럼의 값을 각각 더한다.
           			temp_regist_total += parseInt(BlankToZero(data.statisticsTbls[i].regist))
           			temp_beforeArrange_total += parseInt(BlankToZero(data.statisticsTbls[i].beforeArrange))
           			temp_completeArrange_total += parseInt(BlankToZero(data.statisticsTbls[i].completeArrange))
           			temp_discard_total += parseInt(BlankToZero(data.statisticsTbls[i].discard))
           			temp_error_total += parseInt(BlankToZero(data.statisticsTbls[i].error))
           			           			
           		}
           		           		
           		tab +='</tr>'
           	
           	if(i == data.statisticsTbls.length-1){
           			
           			tab += '<tr>'
           			tab += '<th></th><th>카테고리별 합</th><th></th><th>'+temp_regist_total+'</th><th>'+temp_beforeArrange_total+'</th><th>'+temp_completeArrange_total+'</th><th>'+temp_discard_total+'</th><th>'+temp_error_total+'</th>'
           			tab += '</tr>'
           			
           			//각 컬럼의 임시데이터를 합한다
           			regist_total += parseInt(temp_regist_total)
					beforeArrange_total += parseInt(temp_beforeArrange_total)
					completeArrange_total += parseInt(temp_completeArrange_total)
					error_total += parseInt(temp_error_total)
					discard_total  += parseInt(temp_discard_total)
					
					var totalSum = parseInt(regist_total) + parseInt(error_total)
           			//임시데이터 초기화
           			temp_regist_total = 0
           			temp_beforeArrange_total = 0
           			temp_completeArrange_total = 0
           			temp_discard_total = 0
           			temp_error_total = 0           			
           			
           			tab += '<tr>'
           			tab += '<th></th><th>전체 합</th><th></th><th>'+regist_total+'</th><th>'+beforeArrange_total+'</th><th>'+completeArrange_total+'</th><th>'+discard_total+'</th><th>'+error_total+'</th>'
           			tab += '</tr>'
           			
           			}
           			
           		temp_groupId = data.statisticsTbls[i].groupId;
           		
        	}
        	
    		tab += '</tbody>'
    		tab += '</table>'
    		    		
			$jq('#statisticListForYear').append(tab);
			
			//모든값 초기화 다음 검색을 위해서
			
			
			//통계데이터 계산용 임시 변수
			 temp_regist_total = 0;
			 temp_beforeArrange_total = 0;
			 temp_completeArrange_total = 0;
			 temp_discard_total = 0;
			 temp_error_total = 0;
			 temp_groupId = 0;
			
			//전체합을 위한 변수
			 regist_total = 0;
			 beforeArrange_total = 0;
			 completeArrange_total = 0;
			 discard_total = 0;
			 error_total = 0;
						
		}else{
		alert(data.reason)
		}
		}
	});
	
}

function nodeCateogry(){

	categoryTreeSearch.depth.value = 0;
	categoryTreeSearch.categoryId.value = 0;
	
	$jq.ajax({
		url: '<@spring.url "/admin/category/findCategoryList.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#categoryTreeSearch').serialize(),
		
	success: function(data){
	if(data.result =="Y"){
		if(data.categoryTbls == "N"){
		
			alert("카테고리가 없습니다 카테고리를 신규로 추가해주세요");
		
		}else{

			$jq('#cate_tree').empty();
			var tree = "";
			var icon_open = "<@spring.url '/images/tree_open.gif'/>";
			var icon_close = "<@spring.url '/images/tree_close.gif'/>";
			
			for(var i = 0; i < data.categoryTbls.length; i++){	
			
				if(data.categoryTbls[i].finalYn == "N"){
				
					tree += '<li id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'" ><a   class="control" onClick="subCategroy('+data.categoryTbls[i].categoryId+','+(data.categoryTbls[i].depth+1)+',\''+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'\')"><img src="' + icon_open + '" /></a><a href="#" id="'+data.categoryTbls[i].categoryId+'" onClick="changeBold('+data.categoryTbls[i].categoryId+');showHide(\'theLayer\');returnValue(\''+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');">'+data.categoryTbls[i].categoryNm+'</a></li>';
				
				}else {
					
					tree += '<li   id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'"><a  href="#" id="'+data.categoryTbls[i].categoryId+'" onClick="changeBold('+data.categoryTbls[i].categoryId+');showHide(\'theLayer\');returnValue(\''+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');">'+data.categoryTbls[i].categoryNm+'</a></li>';

				}
						
			}

			$jq('#cate_tree').append(tree);
			tree_call("cate_tree",'<@spring.url ""/>');
			Ids[0] = 'cate_tree';
			
		}
		
	}else{
	alert(data.reason);
	}
	}
	})
	
}

function subCategroy(categoryId,depth, rootId){

	categoryTreeSearch.categoryId.value = categoryId;
	categoryTreeSearch.depth.value=depth;

	$jq.ajax({
		url: '<@spring.url "/admin/category/findCategoryList.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#categoryTreeSearch').serialize(),
		
	success: function(data){
if(data.result == "Y"){
		var tree = "";
		var icon_open = '<@spring.url "/images/tree_open.gif"/>';
		var icon_close = '<@spring.url "/images/tree_close.gif"/>';
		var depth;

		//해당 노드에서 최초 생성일 경우
		tempId = 'root'+data.categoryTbls[0].categoryId+'_'+data.categoryTbls[0].depth
		
				if($jq('#root'+data.categoryTbls[0].categoryId+'_'+data.categoryTbls[0].depth).length == 0){

					tree += '<ul id="root'+data.categoryTbls[0].categoryId+'_'+data.categoryTbls[0].depth+'">'

							for(var i = 0; i < data.categoryTbls.length; i++){	

								if(data.categoryTbls[i].finalYn == "N"){
								
									tree += '<li id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'" ><a  class="control" onClick="subCategroy('+data.categoryTbls[i].categoryId+','+(data.categoryTbls[i].depth+1)+',\''+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'\');return false;"><img src="' + icon_open + '" /></a><a  href="#" id="'+data.categoryTbls[i].categoryId+'"  onClick="changeBold('+data.categoryTbls[i].categoryId+');showHide(\'theLayer\');returnValue(\''+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');">'+data.categoryTbls[i].categoryNm+'</a></li>';
								
								}else {
								
									tree += '<li id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'"><a href="#" id="'+data.categoryTbls[i].categoryId+'"  onClick="changeBold('+data.categoryTbls[i].categoryId+');showHide(\'theLayer\');returnValue(\''+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');">'+data.categoryTbls[i].categoryNm+'</a></li>';		
								
								}

								if(i == data.categoryTbls.length-1){
								
									tree += '</ul>'
									
								}
								
								$jq('#'+(data.categoryTbls[i].categoryId-(1+i))+'_'+(data.categoryTbls[i].depth-1)).find('li:has("ul")').append(tree);
								
								if(i == data.categoryTbls.length-1){

									$jq('#'+rootId).append(tree);
									
								}
								
							}

				}

		var existYn = "N";
		
		if(Ids.length != 0){

			for(i = 0; i < Ids.length; i++){
			
				if(Ids[i] == tempId){
				
					existYn = "Y"
					
				}

			}

			if(existYn != "Y"){
			
				Ids[Ids.length] = tempId
				tree_call(tempId,'<@spring.url ""/>');
				
			}
			
		}
		}else{
		alert(data.reason);
		}
	}
	
	});
	
}

function returnValue(value,name){

	console.log(value);
	document.getElementById("category").value = name;
	categoryTreeSearch.categoryId.value = value;
	statisticSearch.categoryId.value = value;
	
}


function returnInputValue(value,name){

	document.getElementById("inputcategory").value = name;
	var Id = value.split("Input");
	categorySearch.categoryId.value = Id[1];
	
}

function findYearList(){

	$jq.ajax({
		url: '<@spring.url "/statistic/findYearList.ssc" />',
		type: 'GET',
		dataType: 'json',		
		
	success: function(data){
	if(data.result == "Y"){
	var option = "";
		$jq('#selectYear').empty();
		for(i = 0; i < data.years.length ; i++){
		
		option += '<option value="'+data.years[i].yearList+'">'+data.years[i].yearList+'</option>'
		
		}
		
		$jq('#selectYear').append(option);
		
		
		}else{
		alert(data.reason);
		}
		}
	})
	
}


function openWindow(categoryId,  gubun,searchGubun){

	var form = document.statisticSearchForDetail;
	var newWin = "<@spring.url '/statistic/popup/statistic.ssc' />"
	window.open("","newWin","top=0,left=0,width=1000,height=680,resizable=1,scrollbars=no").focus();
	
	form.startDD.value = statisticSearch.startDD.value
	form.endDD.value = statisticSearch.endDD.value
	form.categoryId.value = categoryId
	form.gubun.value = gubun
	form.yearList.value = statisticSearch.yearList.value
	form.findgubun.value = searchGubun
	form.target = "newWin"
	form.method = "get"
	form.action = newWin
	
	form.submit();
	
	statisticSearch.categoryId.value = '';
	
}

function drawChart(xml,searchGun){

var chartUrl = '<@spring.url "/js/FusionCharts_Enterprise/Charts/MSColumn2D.swf"/>';

 var periodChart = new FusionCharts(
 
 chartUrl,"ChartId","900","310","0","0"
 
 )
  
 periodChart.setDataXML(xml);
 periodChart.setTransparent(false);
 
 if(searchGun == 'period'){
  var periodChart = new FusionCharts(
 
 chartUrl,"ChartId","900","310","0","0"
 
 )
  
 periodChart.setDataXML(xml);
 periodChart.setTransparent(false);
 periodChart.render("graphForPeriod");
 
 }else {
  var periodChart = new FusionCharts(
 
 chartUrl,"ChartId","950","310","0","0"
 
 )
  
 periodChart.setDataXML(xml);
 periodChart.setTransparent(false);
 periodChart.render("graphForYear");
 
 }
 
}

function findStatisticForYearGraph(value){

	statisticSearch.yearList.value=$jq('#selectYear').val();

	if(value == "first"){
	
		statisticSearch.yearList.value = getYear(new Date());
		
	}else if($jq('#selectYear').val() == ""){
	
 		alert("검색 연도를 선택해주세요");
 		return false;
 		
	}
	
	console.log(statisticSearch.categoryId.value);
	
	$jq.ajax({
		url: '<@spring.url "/statistic/findStatisticListForYearForGraph.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#statisticSearch').serialize(),
		
	success: function(data){
	
			var xml = jsonToXmlForYear(data);
			
			drawChart(xml,'year');
			
		}
		
	})
	
}

function printArea(value) { 

var options = {printMode:"iframe",leaveOpen:true}

		if(value != 'year'){
		
			$jq("#statisticListForPeriod").printElement(options);
			
		}else{
		
			$jq("#statisticListForYear").printElement(options);
			
		}
		
} 

function downloadExcel(){

 if(document.all){
 
        if(!document.all.excelExportFrame) // explore일때
        {
        
            var excelFrame = document.createElement("iframe"); 
            excelFrame.id = "excelExportFrame";
            excelFrame.position = "absolute"; 
            excelFrame.style.zIndex = -1; 
            excelFrame.style.top = "-10px"; 
            excelFrame.style.left = "-10px"; 
            excelFrame.style.height = "0px"; 
            excelFrame.style.width = "0px"; 
            document.body.appendChild(excelFrame);  
            
        }
        
        var frmTarget = document.all.excelExportFrame.contentWindow.document;  
         
        frmTarget.open("application/vnd.ms-excel","replace"); 
        frmTarget.write("<html>");
        frmTarget.write("<meta http-equiv=\"Content-Type\" content=\"application/vnd.ms-excel; charset=UTF-8\">\r\n");  
        frmTarget.write("<body>");
        frmTarget.write(temp_table);   
        frmTarget.write("</body>");
        frmTarget.write("</html>");
        frmTarget.close();      
        frmTarget.charset="UTF-8";  
       
        frmTarget.focus();
        var  SaveFileName = "";
        
        if(!SaveFileName)
        {
            SaveFileName = "test.xls";
            
        }
        
        frmTarget.execCommand("SaveAs", "false", SaveFileName);
          
    }else{
    
    //크롬, 파이어폭스
     console.log(temp_table);
  	 window.open('data:application/vnd.ms-excel,' +temp_table);
  
    }
 
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
	<input type="hidden" name="yearList" />

</form>

<form name="statisticSearchForDetail" id="statisticSearchForDetail" method="post" >
	
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

<form name="beforeSearch" id="beforeSearch" method="post" >
	<input type="hidden" name="startDD" />
	<input type="hidden" name="endDD" />
</form>


<!-- 섹션1시작 -->
<div  class="westpanel" id="westpanel"> 
    <section id="section1"> 

        <!-- 기간별 통계조회 시작 -->
		<div id="forPeriod" class="forPeriod"> 
            <nav class="tabcover"> <!-- 탭시작 -->
                <ul class="tab1">
                <li class="on"><a href="#forPeriod" onclick="v1h2();return false;"><span>기간검색</span></a></li>
                  <li class="off"><a href="#forYear" onclick="v2h1();findStatisticListForYear('first');findStatisticForYearGraph('first');return false;"><span>연별검색</span></a></li>
                </ul>
            </nav> <!-- 탭끝 -->
            <div class="period">
        	<div class="searchbox1">
            	<dl>	
                <dt>카테고리</dt>																																		
                <dd><input id="category" name="category" style="height:21px;width:223px;" type="text"><a href="#theLayer" onclick="showHide('theLayer');return false;"><img id="categorySearchButton" src="<@spring.url '/images/selectbox_icon.gif'/>"></a></dd>
                <dd><a href="#" onclick="findStatisticListForPeriod();findStatisticForPeriodForGraph('',1);return false;"><img id = "" src="<@spring.url '/images/srch.gif'/>"></a></dd>
               
                </dl>
                <div id="theLayer" style="position : absolute;z-index :1000;opacity:99;"> <!-- selectbox popup -->
                <ul id ="tree_start" >
  					<li><a id="ww" onClick="changeBold(0);returnValue('0','');">전체</a><ul id="cate_tree"></ul></li>
  					</ul>
               </div>
               
                </div>
                
                <div class="searchbox2" >
            
                <dt>조회기간</dt>
                <dd><input type="text" id="from" style="width : 95px;height: 21px;text-align: center;"> ~ <input type="text" id="to" style="width : 95px;height: 21px;text-align: center;"></dd>
                  <dd style="margin-left:-3px"><a href="#" onclick="findStatisticListForPeriod();findStatisticForPeriodForGraph('',1);return false;"><img src="<@spring.url '/images/srch.gif'/>" title="검색" alt="검색" class="ml3"></a></dd>
              
               
                </div>
               
        </div>
        
        
          <div class="statisticChartForPeriod" id = "statisticChartForPeriod">
          
         <div class="graphForPeriod" id = "graphForPeriod" ></div>
          
        	  <!-- paginate -->
  				<article id="paging" class="paginate">
  				<Strong>1<Strong>
   				 </article>  
				<!-- paginate -->
			
       	  </div>
        	  <nav class="tabcover" style="top : 5px"> <!-- 탭시작 -->
                <ul class="tab1">
                <li class="on"><a href="#forPeriod"><span>상세내용</span></a></li>
                 
                </ul>
            </nav> <!-- 탭끝 -->
             <div class="btncover16" style="left : 717px;top : -34px">
               		
           			<span class="btn_pack gry" id ="print"><a href="#" onClick="printArea();return false;">출력</a></span>
                    <span class="btn_pack gry"><a href="#layer4" onClick="downloadExcel();return false;">Excel 다운</a></span>
                    </div>
                    
              <div class="box2" id ="statisticListForPeriod">
                  <table summary="" class="board4" id ="board">
                <colgroup><col width="100px"/><col width="100px"/><col width="100px"/><col width="100px"/><col width="100px"/><col width=100px"/><col width="100px"/><col width="100px"/></colgroup>
                <tbody>
                <tr>
                <th>상위카테고리</th><th>중간카테고리</th><th>하위카테고리</th><th>등록건</th><th>정리전건</th><th>정리완료건</th><th>폐기건</th><th>총합</th>
                </tr>
                </tbody>
                </table>
            </div>
       
        </div>  
       <!-- 기간별 통계조회 끝 -->
       
       
       
      <!-- 연간 통계조회 시작 -->
       <div id="forYear" class="forYear"> 
            <nav class="tabcover"> <!-- 탭시작 -->
                <ul class="tab1">
                <li class="off"><a href="#forPeriod" onclick="v1h2();return false;"><span>기간검색</span></a></li>
                <li class="on"><a href="#forYear" onclick="v2h1();return false;"><span>연별검색</span></a></li>
                </ul>
            </nav> <!-- 탭끝 -->
               <div class="year">
        	
                <div class="searchbox2">
            
                <dt>조회년도</dt>
               <select id="selectYear" onChange="findStatisticListForYear(value);findStatisticForYearGraph(value);"><option value="">전체</option></select>
                
                </div>
               
        </div>
        
          <div class="statisticChartForYear" id = "statisticChartForYear">
        	  <div class="graphForYear" id = "graphForYear"  ></div>
        	  
       	  </div>
       	  
       	     <nav class="tabcover" style="top : 15px"> <!-- 탭시작 -->
                <ul class="tab1">
                <li class="on"><a href="#forPeriod"><span>상세내용</span></a></li>
                 
                </ul>
            </nav> <!-- 탭끝 -->
             <div class="btncover17" style="left : 717px;top : -25px">
               		<!-- <div>통계 상세 내역</div> -->
           			<span class="btn_pack gry" id ="print"><a href="#" onClick="printArea('year');return false;">출력</a></span>
                    <span class="btn_pack gry"><a href="#layer4" onClick="downloadExcel();return false;">Excel 다운</a></span>
                    </div>
            <div class="box2" id ="statisticListForYear">
                 <table summary="" class="board4">
                <colgroup><col width="100px"/><col width="100px"/><col width="100px"/><col width="100px"/><col width="100px"/><col width=100px"/><col width="100px"/><col width="100px"/><col width="100px"/></colgroup>
                <tbody>
                <tr>
                <th>상위카테고리</th><th>중간카테고리</th><th>하위카테고리</th><th>등록건</th><th>정리전건</th><th>정리완료건</th><th>폐기건</th><th>오류</th><th>총합</th>
                </tr>
                </tbody>
                </table>
            </div>
               
        </div>  
        <!-- 연간 통계조회 끝 -->
    </section>
   
    </div>
    
                
<!-- 섹션1끝 -->