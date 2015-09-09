<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">
Ext.onReady(
Left.init);

</script>
<script type="text/javascript">


String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};

var count = 0;
var dboxcount = 1;
var storyboard;
var fullPath;
var duration;

//카테고리 검색용 
var Ids = new Array();
var tempIds = new Array();
var tempId = "";
		
// 기본정보내의 input box용
var inputIds = new Array();
var inputtempIds = new Array();
var inputTempId = "";

window.onload=function(){	

	contentSearchList('N');

	nodeCateogry();
	tree_call("tree_start",'<@spring.url ""/>');
	initRigthMenu();
}


jQuery(

	function($jq) {
	
		$jq('#clipList').scroll(function() {
		
			if($jq(this).scrollTop() + $jq(this).innerHeight() >= $jq(this)[0].scrollHeight) {
			
				contentSearchList('A');
				
			}		
			
		})
		
	}
	
);

jQuery(

	function($jq) {
	
		$jq('#clipTableList').scroll(function() {
		
			if($jq(this).scrollTop() + $jq(this).innerHeight() >= $jq(this)[0].scrollHeight) {
			
				contentSearchListForGrid('A');
				
			}
	
		})
	}
	
);


function contentSearchList(appendGb){

	var keyword = $jq('#keyword').val();
	
	var startDt = $jq('#from').val();
	var endDt = $jq('#to').val();
	
	if(startDt != '' && endDt != ''){
	
		clipSearch.startDt.value = startDt
		clipSearch.endDt.value = endDt
	
	}else{
		clipSearch.startDt.value = getbeforeToday(new Date());
		clipSearch.endDt.value = getToDay(new Date());
		$jq('#from').val(clipSearch.startDt.value);
		$jq('#to').val(clipSearch.endDt.value);
	}		
	 
	 if($jq('#detailDataStatCd').val() != ''){
	 clipSearch.dataStatCd.value = $jq('#detailDataStatCd').val();
	 }
	  if($jq('#detailCtTyp').val() != ''){
	 clipSearch.ctTyp.value = $jq('#detailCtTyp').val();
	 }
	 if(appendGb == 'error'){
	 
	 	if($jq(":checkbox[name='errorCheck']:checked").length == 0){
	
		 	clipSearch.dataStatCd.value = ''
		
		}else {
	
			 clipSearch.dataStatCd.value = '003'
		}


	 }
	 
	 
	 var sdt = new Date($jq('#from').val().split("-"));
	 var edt = new Date($jq('#to').val().split("-"));
	
	if( edt.getTime() < sdt.getTime() ){
	
	alert("시작날짜가 종료날짜보다 큽니다");
	return;
	
	}
	
	
	if(startDt !='' && endDt != ''){
	
		clipSearch.startDt.value = startDt
		clipSearch.endDt.value = endDt
		
	}
	
		clipSearch.keyword.value = keyword;
	

	var pageNo;

	if(appendGb == 'A') {
	
		pageNo = Number(clipSearch.imagePageNo.value)+1;
		clipSearch.imagePageNo.value = pageNo;
		clipSearch.pageNo.value = pageNo;
		
	}else{
	
		clipSearch.imagePageNo.value = 1;
		clipSearch.pageNo.value = 1;
		
	}

	clipSearch.searchTyp.value = 'image'
	
	$jq.ajax({
		url: '<@spring.url "/contents/findSearchList.ssc" />',
		type: 'POST',
		dataType: 'json',
		
		//data: $jq('#clipSearch').serialize().replace(/%/g,'%25'),
		data: $jq('#clipSearch').serialize(),
		error: function(){
			//alert('Return Error.');
			clipSearch.keyword.value = "";
			clipSearch.startDt.value = "";
			clipSearch.endDt.value = "";
			$jq('#from').val('');
			$jq('#to').val('');
	},
	success: function(data){

		if(appendGb == 'N') {
		
			$jq('#clipList').empty();			
			clipSearch.pageNo.value = data.search.pageNo;
			
		}

		var table = "";
		if(appendGb == 'error'){
			$jq('#clipList').empty();
		}
		
		if(data.result == "Y"){	
		for(var i = 0; i < data.metas.length; i++){	

			var brdDd = new Date(parseInt(data.metas[i].brdDd, 10));
			var date = new Date(parseInt(data.metas[i].regDt, 10));

			table += '<dl class="viewpart bgc1">'
			table += '<dt class="list">'  
			table += '<dl class="listcontent">'
			console.log(data.metas[i].dataStatCd);
			if(data.metas[i].dataStatCd != "003"){
				table += '<dt><a href="javascript:" onclick="searchBasicInfo('+data.metas[i].ctId+',\'F\');">'+data.metas[i].ctNm+'</a></dt>'
			}else{
			//	table += '<dt><a href="javascript:" onclick="searchBasicInfo('+data.metas[i].ctId+',\'F\');">'+data.metas[i].ctNm+'<img src="<@spring.url "/images/error.png"/>" title="오류" alt="오류"></a></dt>'
			table += '<dt><a href="javascript:" onclick="searchBasicInfo('+data.metas[i].ctId+',\'F\');">'+data.metas[i].ctNm+'<font color="red"> (오류영상)</font></a></dt>'
			}
			table += '<dd>'+BlankCheck(data.metas[i].ctLeng)+'</dd>'
			table += '<dd>'+date.format("yyyy-MM-dd")+'</dd>'
			table += '<dd><span class="fc1">'+BlankCheck(data.metas[i].vdQlty)+'</span></dd>'
					
			if(BlankCheck(data.metas[i].vdVresol) == "" && BlankCheck(data.metas[i].vdHresol) == ""){
			
				table += '<dd></dd>'
				
			}else{
			
				table += '<dd>'+BlankCheck(data.metas[i].vdHresol)+'x'+BlankCheck(data.metas[i].vdVresol)+'</dd>'
			
			}
				table += '</dl>'
				table += '<dd class="in_01"><input name="discardCheck" type="checkbox" value="'+data.metas[i].ctId+'"></dd>'
				
				var filePath = data.metas[i].flPath;
				
				var wrkFileNm = data.metas[i].wrkFileNm;
				var subWrkFileNm = wrkFileNm.substr(wrkFileNm.lastIndexOf('_')+1);
				
				var subFilePath = filePath.substr(filePath.indexOf('/'),filePath.lastIndexOf('/'));
				
				var subFileNm = data.metas[i].flPath.substr(data.metas[i].flPath.lastIndexOf('/')+1);
				
				var imgPath = data.apacheIp + subFilePath+'/' +  subFileNm+'/'  + subWrkFileNm + '/'  + data.metas[i].rpimgKfrmSeq + '.jpg';
				
				table += '<dd class="thumb"><a href="javascript:" onclick="searchBasicInfo('+data.metas[i].ctId+',\'F\')"><img src="'+imgPath+'" width="120px" height="67px" onError="this.src=\'<@spring.url  "/images/thumbtest.gif" />\'"/></a></dd>'
				table += '</dt>'
				table += '</dl>'

		}
		
		$jq('#clipList').append(table);
		}else if(appendGb == "N" || appendGb == "error"){
				$jq('#clipList').append('<div style="margin-left:101px;margin-top:32px;font-size:medium;font-weight:bold;">'+data.reason+'</div>');
		}
		hideDetailSearch();		
	
	}
	
	});
	
}

function contentSearchListForGrid(appendGb){

	var keyword = $jq('#keyword').val();
	
	var startDt = $jq('#from').val();
	var endDt = $jq('#to').val();
	
	if(startDt != '' && endDt != ''){
	
		clipSearch.startDt.value=startDt
		clipSearch.endDt.value=endDt
	
	}		
	 	 if($jq('#detailDataStatCd').val() != null){
	 clipSearch.dataStatCd.value = $jq('#detailDataStatCd').val();
	 }
	  if($jq('#detailCtTyp').val() != null){
	 clipSearch.ctTyp.value = $jq('#detailCtTyp').val();
	 }
	 if(appendGb == 'error'){
	 
	 	if($jq(":checkbox[name='errorCheck']:checked").length == 0){
	
		 	clipSearch.dataStatCd.value = ''
		
		}else {
	
			 clipSearch.dataStatCd.value = '003'
		}


	 }
	 var sdt = new Date($jq('#from').val().split("-"));
	 var edt = new Date($jq('#to').val().split("-"));
	
	if( edt.getTime() < sdt.getTime() ){
	
	alert("시작날짜가 종료날짜보다 큽니다");
	return;
	
	}
		
	if(startDt !='' && endDt != ''){
	
		clipSearch.startDt.value=startDt
		clipSearch.endDt.value=endDt
		
	}
	
	if(keyword != '') {
	
		clipSearch.keyword.value = keyword;
		
	}

	var pageNo;
	
	if(appendGb == 'A') {
	
		pageNo = Number(clipSearch.listPageNo.value)+1;
		clipSearch.listPageNo.value = pageNo;
		clipSearch.pageNo.value = pageNo;
		
	}else{
	
		clipSearch.listPageNo.value = 1;
		clipSearch.pageNo.value = 1;
		
	}
	
	clipSearch.searchTyp.value = 'list'
	$jq.ajax({
		url: '<@spring.url "/contents/findSearchList.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch').serialize(),
		error: function(){
			//alert('Return Error.');
			clipSearch.keyword.value = "";
			clipSearch.startDt.value = "";
			clipSearch.endDt.value = "";
			$jq('#from').val('');
			$jq('#to').val('');
	},
	success: function(data){

		if(appendGb == 'N') {
			
			$jq('#clipTableList').empty();
			clipSearch.pageNo.value = data.search.pageNo;
			
		}

	
		var list = "";
		
		if(appendGb == 'N') {
		
			list += '<table summary="" class="board7">';
			list += '<colgroup><col width="30px"/><col width="130"/><col width="80px"/><col width="80px"/><col width="50px"/></colgroup>';
			list += '<tbody id="clipTableBody"><tr>';
			list += '<th></th><th>제목</th><th>방송일</th><th>방송길이</th><th>영상구분</th>';
			list += '</tr>';
		
		}
		
		if((appendGb == 'N' || appendGb == 'error' ) && data.result == "Y"){
		
		for(var i = 0; i < data.metas.length; i++){
		
			var brdDd = new Date(parseInt(data.metas[i].brdDd, 10));
			var date = new Date(parseInt(data.metas[i].regDt, 10));
			
			list += '<tr >';
			list += '<td><input name="discardCheck" type="checkbox" value="'+data.metas[i].ctId+'"></td>';
			list += '<td><a href="#" onclick="searchBasicInfo('+data.metas[i].ctId+',\'F\');">'+data.metas[i].ctNm+'</a></td>'
			list += '<td>'+date.format("yyyy-MM-dd")+'</td>';
			list += '<td>'+BlankCheck(data.metas[i].ctLeng)+'</td>';
			list += '<td>'+BlankCheck(data.metas[i].vdQlty)+'</td>'
			list += '</tr>';
			
		}
		
		$jq('#clipTableList').append(list);
		
		}else if(data.result == "Y"){
		
		for(var i = 0; i < data.metas.length; i++){
		
			var brdDd = new Date(parseInt(data.metas[i].brdDd, 10));
			var date = new Date(parseInt(data.metas[i].regDt, 10));
			list += '<tr >';
			list += '<td><input name="discardCheck" type="checkbox" value="'+data.metas[i].ctId+'"></td>';
			list += '<td><a href="#" onclick="searchBasicInfo('+data.metas[i].ctId+',\'F\');">'+data.metas[i].ctNm+'</a></td>'
			list += '<td>'+date.format("yyyy-MM-dd")+'</td>';
			list += '<td>'+BlankCheck(data.metas[i].ctLeng)+'</td>';
			list += '<td>'+BlankCheck(data.metas[i].vdQlty)+'</td>'
			list += '</tr>';
			
		}
		
			$jq('#clipTableBody').append(list);
			
		}else if((appendGb == 'N' || appendGb == 'error') &&  data.result == "N"){
		list += '<div style=position:absolute;margin-left:101px;margin-top:32px;font-size:medium;font-weight:bold;">'+data.reason+'</div>'
		$jq('#clipTableList').append(list);
		}
		
		if(appendGb == 'N') {
		
			list += '</tbody>';
			list += '</table>';
			
		}
		
	}
	
	});
	
}


function searchBasicInfo(ctId,gubun){

var tempDividData = dividImgForm.dividImgs.value;
	if(registRpimgForm.ctId.value != '' || deleteImgForm.ctId.value != '' || dividImgForm.ctId.value != '' ){
	
 		if(confirm("저장하지 않은 데이터가 있습니다. 저장을 취소하시겠습니까?")  == true){
 		//저장한 임시데이터 모두 초기화
 		dividImgForm.dividImgs.value='';
 		dividImgForm.ctId.value='';
 		deleteImgForm.ctId.value='';
 		deleteImgForm.deleteImgs.value='';
 		registRpimgForm.ctId.value=''
 		registRpimgForm.rpImg.value = '';
 		dividImgs='';
 		fobidenDelete='';
 		rpImgKfrm='';
 		imgDeletes='';
 		v1h23();
 		}else{
 		 return;
 		}
 	}
 	
 	//스토리보드의 코너정보를 위해서 가지고있던 폼데이터 초기화
 		dividImgForm.dividImgs.value='';
 		dividImgForm.ctId.value=''; 		
 		dividImgs='';
 		fobidenDelete='';
 		rpImgKfrm='';
 		imgDeletes='';
	var modrId = '${user.userId}';
	searchInfo.ctId.value = ctId;
	searchInfo.modrId.value = modrId;
	var tab1 = "";
	var tab2 = "";
	var tab3 = "";
	var tabtitle = "";
	
	var attachTbl;

	$jq.ajax({
		url: '<@spring.url "/contents/getContentsBasicInfo.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#searchInfo').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
		//스토리보드 txt파일data
		storyboard = data.storyboard;
		//변환된 duration data	
		duration = data.durationData;
						
		//스토리보드 파일경로
		fullPath = data.fullPath;
		//attachTbl = data.attachTbl;
		console.log("#################    "+data.mediaExistYn);
		
		if(gubun == "F" && data.mediaExistYn == "Y" ){
		$jq('#videoplay').attr("src", fullPath+'.mp4');
		}else if(data.mediaExistYn == "N"){
		 alert("해당 저해상도 영상을 찾을수가 없습니다");
		 $jq('#videoplay').attr("src", "");
		}
			var date = new Date(parseInt(data.contentsTbl.brdDd, 10));
			
		 	$jq('#dbox1').empty();
			
          	tab1 += '<nav class="tabcover">' <!-- 탭시작 -->
            tab1 += '<ul class="tab1">'
            tab1 += '<li class="on"><a href="#dbox1" onclick="v1h23()"><span>기본정보</span></a></li>'
            tab1 += '<li class="off"><a href="#dbox2" onclick="v2h13();getContentStoryboardInfo('+data.contentsTbl.ctId+');return false;"><span>스토리보드</span></a></li>'
            tab1 += '<li class="off"><a href="#dbox3" onclick="v3h12();getAttachInfo('+data.contentsTbl.ctId+');return false;"><span>첨부파일</span></a></li>'
             if(data.contentsTbl.dataStatCd == '003'){
             tab1 += '<li class="error">** 오류 등록 영상입니다 **</li>'
             }
            tab1 += '</ul>'
         
            tab1 += '</nav>' <!-- 탭끝 -->
            <!--CT_ID를 hidden으로 설정, 각 탭 별로 key값을 CT_ID로 전달-->
			tab1 += '<input type="hidden" id="ctId'+data.contentsTbl.ctId+'" value="'+data.contentsTbl.ctId+'" />'
			tab1 += '<div class="box1">'
        	tab1 += '<dl class="detailcon">'
        	
        	tab1 += '<dt>제목</dt>'
        	tab1 += '<dd><input name="contentNm" id="contentNm'+data.contentsTbl.ctId+'" style="height:18px;width:515px;" type="text" value="'+data.contentsTbl.ctNm+'" /></dd>'
			tab1 +='<div class="InCate">'
			tab1 += '<dt>카테고리</dt>'
			tab1 += '<dd style="width:100px">'
			tab1 += '<input id="inputcategory" name="inputcategory"   type="text" value="'+data.contentsTbl.categoryNm+'"/><a href="#theInputLayer" id="intree" onclick="showHide(\'theInputLayer\');return false;"><img  id= "intreeButton" src=\'<@spring.url "/images/option_off.png"/>\'></a>'
			tab1 += '</dd>'
            tab1 += '</select>'
            tab1 += '</dd>'
          
            tab1 +=	'<div id="theInputLayer">'
            tab1 += '<ul id ="inputTree_start" >'
  			tab1 +=	'<li>전체<ul id="inputTree"><li><ul></ul><li></ul></li>'
  			tab1 +=	'</ul>'
   			tab1 +=	'</div>'
   			tab1 +='</div>'	
   			   
			tab1 += '<dt class="none">회차</dt>'
			//추후 onChange="getSegmentInfos(value)" 이벤트 추가
			tab1 += '<dd><select name="episodeId" id="episodeId'+data.contentsTbl.ctId+'" style="height:24px;width:100px;" >'
			
			tab1 += '<option value="'+data.contentsTbl.categoryId+','+data.contentsTbl.episodeId+'">'+data.contentsTbl.episodeNm+'</option></select></dd>'
			
			getEpisodeInfos(data.contentsTbl.categoryId,data.contentsTbl.episodeId);
			//2013.10.04 일시적으로 막아둠
			//tab1 += '<dt class="none">세그먼트</dt>'
			//tab1 += '<dd><select name="segmentId" id="segmentId'+data.contentsTbl.ctId+'" style="height:24px;width:100px;">'
			//tab1 += '<option>'+data.contentsTbl.segmentId+'</option></select></dd>'
		
			tab1 += '<dt>등록일</dt>'
			tab1 += '<dt class="none tal" name="brdDd" id="brdDd"><span class="fc2">'+data.contentsTbl.brdDd+'</span></dt>'
			tab1 += '<dt class="none">정리상태</dt>'
			tab1 += '<dt class="none tal" name="dataStatCd" id="dataStatCd"><span class="fc2">'+data.contentsTbl.dataStatNm+'</span></dt>'
			
			tab1 += '<dt class="none">영상길이</dt>'
			tab1 += '<dt class="none tal"><span class="fc2">'+BlankCheck(data.contentsTbl.ctLeng)+'</span></dt>'
			tab1 += '<dt>검색키워드</dt>'
			tab1 += '<dd><input name="keyWord" id="keyWord'+data.contentsTbl.ctId+'" style="height:18px;width:515px;" type="text" value="'+BlankCheck(data.contentsTbl.keyWords)+'" /></dd>'
			tab1 += '<dt>특이사항</dt>'
			tab1 += '<dd><textarea name="spcInfo" id="spcInfo'+data.contentsTbl.ctId+'" cols="" rows="" style="height:142px;width:520px;">'+BlankCheck(data.contentsTbl.spcInfo)+'</textarea></dd>'
			tab1 += '</dl>'
			
			//버튼의 조회 권한이 "Y"라면 보여주고 아니라면 보여주지 않는다.
		 	if(data.button =="Y"){
   			if(data.contentsTbl.delDd == undefined || data.contentsTbl.delDd == ""){
	   			tab1 += '<div class="btncover">'
				//tab1 += '<a href="javascript:" onclick="updateContentInfo('+data.contentsTbl.ctId+')"><img src="<@spring.url "/images/save_on.gif"/>" title="저장" alt="저장"></a>'
				if(data.contentsTbl.dataStatCd == '000' || data.contentsTbl.dataStatCd == '001' || data.contentsTbl.dataStatCd == undefined){	
			
				tab1 += <@ajaxButton '3' '${user.userId}' "updateArrangeComplete('+data.contentsTbl.ctId+')" '정리완료' />
				tab1 += '&nbsp'
				tab1 += <@ajaxButton '3' '${user.userId}' "updateErrorArrange('+data.contentsTbl.ctId+')" '오류등록' />
				tab1 += '&nbsp'
				
				}else if(data.contentsTbl.dataStatCd=='002' ){
				
				tab1 += <@ajaxButton '3' '${user.userId}' "updateErrorArrange('+data.contentsTbl.ctId+')" '오류등록' />
				tab1 += '&nbsp'
				
				}
				
				tab1 += <@ajaxButton '3' '${user.userId}' "updateContentInfo('+data.contentsTbl.ctId+')" '저장' />
				tab1 += '</div>'
				tab1 += '</div>' <!-- 상세정보끝 -->
			
   			}else{
   			
	   			tab1 += '<div class="btncover">'
				//tab1 += '<a href="javascript:" onclick="cancleDisuse('+data.contentsTbl.ctId+')"><img src="<@spring.url "/images/delrecancle_on.gif"/>" title="폐기취소" alt="폐기취소"></a>'
				//tab1 += '<a href="javascript:" onclick="updateContentInfo('+data.contentsTbl.ctId+')"><img src="<@spring.url "/images/save_on.gif"/>" title="저장" alt="저장"></a>'
				tab1 += <@ajaxButton '3' '${user.userId}' "cancleDisuse('+data.contentsTbl.ctId+')" '폐기취소' />
			
				tab1 += '&nbsp'
				tab1 += <@ajaxButton '3' '${user.userId}' "updateContentInfo('+data.contentsTbl.ctId+')" '저장' />
				tab1 += '</div>'
				tab1 += '</div>' <!-- 상세정보끝 -->	
				
   			}
		}
			inputNodeCateogry();
			$jq('#dbox1').append(tab1);
			tree_call("inputTree_start",'<@spring.url ""/>');
			v1h23();
        }else{
        alert(data.reason);
        }
		}
		
	});
	
}




function initBaseInfo(){


 		//저장한 임시데이터 모두 초기화
 		dividImgForm.dividImgs.value='';
 		dividImgForm.ctId.value='';
 		deleteImgForm.ctId.value='';
 		deleteImgForm.deleteImgs.value='';
 		registRpimgForm.ctId.value=''
 		registRpimgForm.rpImg.value = '';
 		dividImgs='';
 		fobidenDelete='';
 		rpImgKfrm='';
 		imgDeletes='';
 	
 	
 	//스토리보드의 코너정보를 위해서 가지고있던 폼데이터 초기화
 		dividImgForm.dividImgs.value='';
 		dividImgForm.ctId.value=''; 		
 		dividImgs='';
 		fobidenDelete='';
 		rpImgKfrm='';
 		imgDeletes='';

	var tab1 = "";
	var tab2 = "";
	var tab3 = "";
	var tabtitle = "";
	
	var attachTbl;

		
		 $jq('#videoplay').attr("src", "");
		
			
		 	$jq('#dbox1').empty();
			
          	tab1 += '<nav class="tabcover">' <!-- 탭시작 -->
            tab1 += '<ul class="tab1">'
            tab1 += '<li class="on"><a href="#dbox1" onclick="v1h23()"><span>기본정보</span></a></li>'
            tab1 += '<li class="off"><a href="#dbox2" onclick="v2h13()"><span>스토리보드</span></a></li>'
            tab1 += '<li class="off"><a href="#dbox3" onclick="v3h12()"><span>첨부파일</span></a></li>'
           
            tab1 += '</ul>'
         
            tab1 += '</nav>' <!-- 탭끝 -->
          
			tab1 += '<input type="hidden" />'
			tab1 += '<div class="box1">'
        	tab1 += '<dl class="detailcon">'
        	
        	tab1 += '<dt>제목</dt>'
        	tab1 += '<dd><input name="contentNm"  style="height:18px;width:515px;" type="text" /></dd>'
			
			tab1 += '<dt>카테고리</dt>'
			tab1 += ' <dd><select name="category" style="height:24px;width:100px;"><option></option></select></dd>'
		
			tab1 += '<dt class="none">회차</dt>'
			
			tab1 += '<dd><select name="episodeId"  style="height:24px;width:100px;" >'
			
			tab1 += '<option ></option></select></dd>'
			
		
			tab1 += '<dt>등록일</dt>'
			tab1 += '<dt class="none tal" name="brdDd" id="brdDd"><span class="fc2"></span></dt>'
			tab1 += '<dt class="none">정리상태</dt>'
			tab1 += '<dt class="none tal" name="dataStatCd" id="dataStatCd"><span class="fc2"></span></dt>'
			
			tab1 += '<dt>영상길이</dt>'
			tab1 += '<dt class="none tal"><span class="fc2"></span></dt>'
			
			tab1 += '<dt>특이사항</dt>'
			tab1 += '<dd><textarea name="spcInfo"  cols="" rows="" style="height:142px;width:520px;"></textarea></dd>'
			tab1 += '</dl>'
			
		 
				
				tab1 += '</div>'
				tab1 += '</div>' <!-- 상세정보끝 -->
			
   		
		
			
			$jq('#dbox1').append(tab1);
			$jq('#dbox2').empty();
				tab2 += '<nav class="tabcover">' <!-- 탭시작 -->
                tab2 += '<ul class="tab1">'
                tab2 += '<li class="off"><a href="#dbox1" onclick="v1h23();return false;"><span>기본정보</span></a></li>'
                tab2 += '<li class="on"><a href="#dbox2" onclick="v2h13();return false;"><span>스토리보드</span></a></li>'
                tab2 += '<li class="off"><a href="#dbox3" onclick="v3h12();return false;"><span>첨부파일</span></a></li>'
                tab2 += '</ul>'
            	tab2 += '</nav> <!-- 탭끝 -->'
             	tab2 += '<span class="demo1">'
           	 	tab2 += '<div class="box2">'
            	tab2 += '</div>'
                tab2 += '</span>'
			$jq('#dbox2').append(tab2);
			$jq('#dbox3').empty();
			tab3 += '<nav class="tabcover">' <!-- 탭시작 -->
            tab3 += '<ul class="tab1">'
            tab3 += '<li class="off"><a href="#dbox1" onclick="v1h23();return false;"><span>기본정보</span></a></li>'
            tab3 += '<li class="off"><a href="#dbox2" onclick="v2h13();return false;"><span>스토리보드</span></a></li>'
            tab3 += '<li class="on"><a href="#dbox3" onclick="v3h12();return false;"><span>첨부파일</span></a></li>'
            tab3 += '</ul>'
            tab3 += '</nav>' <!-- 탭끝 -->
            tab3 += '<div class="box2">'
            tab3 += '<table summary="" class="board4">'
            tab3 += '<colgroup><col width="40px"/><col width=""/><col width="140px"/><col width="120px"/></colgroup>'
            tab3 += '<tbody>'
            tab3 += '<tr>'
            tab3 += '<th></th><th>파일명</th><th>구분</th><th>등록일</th>'
            tab3 += '</tr>'
            tab3 += '</tbody>'
            tab3 += '</table>'
            tab3 += '</div>'
            tab3 += '<div class="btncover8">'
          <#assign perm = tpl.getAccessRule('${user.userId}', '3')>
  
            <#if (perm == 'RW')>
            	tab3 += '<span class="btn_pack xlarge"><a href="javascript:" onClick=showHide("layer1");return false;">&nbsp;&nbsp;&nbsp;등록&nbsp;&nbsp;&nbsp;</a></span>'
                tab3 += '&nbsp;'
            	tab3 += '<span class="btn_pack xlarge"><a href="javascript:" onClick="deleteAttachFile()">&nbsp;&nbsp;&nbsp;삭제&nbsp;&nbsp;&nbsp;</a></span>'
            </#if>
            tab3 += '</div>'
			$jq('#dbox3').append(tab3);
			v1h23();
        
	
}


function nodeCateogry(){

	categoryTreeSearch.depth.value=0;
	categoryTreeSearch.categoryId.value=0;
	
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
			var tree ="";
			var icon_open = "<@spring.url '/images/tree_open.gif'/>";
			var icon_close = "<@spring.url '/images/tree_close.gif'/>";
			
			for(var i = 0; i < data.categoryTbls.length; i++){	
				
				if(data.categoryTbls[i].finalYn == "N"){
					
					tree += '<li id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'" ><a   class="control" onClick="subCategroy('+data.categoryTbls[i].categoryId+','+(data.categoryTbls[i].depth+1)+',\''+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'\');return false;"><img src="' + icon_open + '" /></a><a href="#" id="'+data.categoryTbls[i].categoryId+'" onClick="changeBold('+data.categoryTbls[i].categoryId+');showHide(\'theLayer\');returnValue(\''+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');return false;">'+data.categoryTbls[i].categoryNm+'</a></li>';
				
				}else {
					
					tree += '<li   id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'"><a  href="#" id="'+data.categoryTbls[i].categoryId+'" onClick="changeBold('+data.categoryTbls[i].categoryId+');showHide(\'theLayer\');returnValue(\''+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');return false;">'+data.categoryTbls[i].categoryNm+'</a></li>';

				}
						
			}

			$jq('#cate_tree').append(tree);
			tree_call("cate_tree",'<@spring.url ""/>');
			Ids[0]='cate_tree';
			
		}
		}else{
		alert(data.reason)
		}
	}
	
	})
	
}

function subCategroy(categoryId, depth, rootId){

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
								
									tree += '<li id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'" ><a  class="control" onClick="subCategroy('+data.categoryTbls[i].categoryId+','+(data.categoryTbls[i].depth+1)+',\''+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'\');return false;"><img src="' + icon_open + '" /></a><a  href="#" id="'+data.categoryTbls[i].categoryId+'"  onClick="changeBold('+data.categoryTbls[i].categoryId+');showHide(\'theLayer\');returnValue(\''+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');return false;">'+data.categoryTbls[i].categoryNm+'</a></li>';
								
								} else {
								
									tree += '<li id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'"><a href="#" id="'+data.categoryTbls[i].categoryId+'"  onClick="changeBold('+data.categoryTbls[i].categoryId+');showHide(\'theLayer\');returnValue(\''+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');return false;">'+data.categoryTbls[i].categoryNm+'</a></li>';		
								
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

			for(i = 0 ; i < Ids.length; i++){
			
				if(Ids[i] == tempId){
				
					existYn="Y"
					
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

function getEpisodeInfos(categoryId,episodeId){

	categorySearch.categoryId.value=categoryId;
	categorySearch.episodeNm.value="";

	$jq.ajax({
		url: '<@spring.url "/admin/category/getEpisodeSearch.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#categorySearch').serialize(),
		
	success: function(data){
	if(data.result == "Y"){
		var option="";
		
		if(data.episodeTbls.length != 0){
		
			$jq('#episodeId'+clipSearch.ctId.value).empty();

			for(i = 0;i < data.episodeTbls.length; i++){
			
				if(data.episodeTbls[i].id.episodeId == episodeId){
				
					option += '<option value="'+data.episodeTbls[i].id.categoryId+','+data.episodeTbls[i].id.episodeId+'"  selected="selected">'+data.episodeTbls[i].episodeNm+'</option>';
				
				}else{
				
					option += '<option value="'+data.episodeTbls[i].id.categoryId+','+data.episodeTbls[i].id.episodeId+'">'+data.episodeTbls[i].episodeNm+'</option>';			    	
				
				}
				
			}
			
			$jq('#episodeId'+clipSearch.ctId.value).append(option);
			
		}else{
		
			$jq('#episodeId'+clipSearch.ctId.value).empty();
				
		}
		}else{
		alert(data.reason);
		}
	}
	
	});
	
}

function getSegmentInfos(value){

	var values = value.split(",");

	categorySearch.categoryId.value = values[0];
	categorySearch.episodeId.value = values[1];
	categorySearch.segmentNm.value = "";
	
	$jq.ajax({
		url: '<@spring.url "/admin/category/getSegmentSearch.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#categorySearch').serialize(),
	
	success: function(data){
	
		var option="";
		
		if(data.segmentTbls.length != 0){
		
			$jq('#segmentId'+clipSearch.ctId.value).empty();
			
				for(i = 0; i < data.segmentTbls.length; i++){
				
					option += '<option value="'+data.episodeTbls[i].id.categoryId+','+data.episodeTbls[i].id.episodeId+'">'+data.episodeTbls[i].episodeNm+'</option>';
				
				}
			$jq('#segmentId'+clipSearch.ctId.value).append(option);
		
		}else{
		
			$jq('#segmentId'+clipSearch.ctId.value).empty();		
		
		}
		
	}
	
	});
	
}

function inputNodeCateogry(){

	categoryTreeSearch.depth.value=0;
	categoryTreeSearch.categoryId.value=0;

	$jq.ajax({
		url: '<@spring.url "/admin/category/findCategoryList.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#categoryTreeSearch').serialize(),
	
	success: function(data){
if(data.result == "Y"){
		if(data.categoryTbls == "N"){
		
			alert("카테고리가 없습니다 카테고리를 신규로 추가해주세요");
			
		}else{
		
			$jq('#inputTree').empty();

			var tree ="";
			var icon_open = '<@spring.url "/images/tree_open.gif"/>';
			var icon_close = '<@spring.url "/images/tree_close.gif"/>';
			
			for(var i = 0; i < data.categoryTbls.length; i++){	
			
				if(data.categoryTbls[i].finalYn == "N"){
				
					tree += '<li id="Input'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'" ><a   class="control" onClick="InputSubCategroy('+data.categoryTbls[i].categoryId+','+(data.categoryTbls[i].depth+1)+',\'Input'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'\');return false;"><img src="' + icon_open + '" /></a><a href="#" id="Input'+data.categoryTbls[i].categoryId+'" onClick="changeBold(\'Input'+data.categoryTbls[i].categoryId+'\');getEpisodeInfos('+data.categoryTbls[i].categoryId+',\'\');showHide(\'theInputLayer\');returnInputValue(\'Input'+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');return false;">'+data.categoryTbls[i].categoryNm+'</a></li>';
				
				}else {
				
					tree += '<li id="Input'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'"><a  href="#" id="Input'+data.categoryTbls[i].categoryId+'" onClick="changeBold(\'Input'+data.categoryTbls[i].categoryId+'\');getEpisodeInfos('+data.categoryTbls[i].categoryId+',\'\');showHide(\'theInputLayer\');returnInputValue(\'Input'+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');return false;">'+data.categoryTbls[i].categoryNm+'</a></li>';
				
				}	
					
			}

			$jq('#inputTree').append(tree);
			tree_call('inputTree','<@spring.url ""/>');
			inputIds[0]='inputTree';
			
		}
		}else{
		alert(data.reason);
		}
	}
	
	})
	
}

function InputSubCategroy(categoryId, depth, rootId){

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
		inputTempId = 'inputRoot'+data.categoryTbls[0].categoryId+'_'+data.categoryTbls[0].depth
		
		if($jq('#inputRoot'+data.categoryTbls[0].categoryId+'_'+data.categoryTbls[0].depth).length==0){
			
			tree += '<ul id="inputRoot'+data.categoryTbls[0].categoryId+'_'+data.categoryTbls[0].depth+'">'
			
			for(var i = 0; i < data.categoryTbls.length; i++){
				
				if(data.categoryTbls[i].finalYn == "N"){
				
					tree += '<li id="Input'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'" ><a  class="control" onClick="InputSubCategroy('+data.categoryTbls[i].categoryId+','+(data.categoryTbls[i].depth+1)+',\'Input'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'\');return false;"><img src="' + icon_open + '" /></a><a  href="#" id="Input'+data.categoryTbls[i].categoryId+'"  onClick="changeBold(\'Input'+data.categoryTbls[i].categoryId+'\');getEpisodeInfos('+data.categoryTbls[i].categoryId+',\'\');showHide(\'theInputLayer\');returnInputValue(\'Input'+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');return false;">'+data.categoryTbls[i].categoryNm+'</a></li>';
				
				}else {
				
					tree += '<li id="Input'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'"><a href="#" id="Input'+data.categoryTbls[i].categoryId+'"  onClick="changeBold(\'Input'+data.categoryTbls[i].categoryId+'\');getEpisodeInfos('+data.categoryTbls[i].categoryId+',\'\');showHide(\'theInputLayer\');returnInputValue(\'Input'+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');return false;">'+data.categoryTbls[i].categoryNm+'</a></li>';		
				
				}
				
					if(i == data.categoryTbls.length-1){
					
						tree += '</ul>'
						
					}
					
		$jq('#Input'+(data.categoryTbls[i].categoryId-(1+i))+'_'+(data.categoryTbls[i].depth-1)).find('li:has("ul")').append(tree);
				
				if(i == data.categoryTbls.length-1){
				
					$jq('#'+rootId).append(tree);
					
				}
				
			}
			
		}

		var existYn ="N";
		
		if(inputIds.length!=0){
		
			for(i =0 ; i<Ids.length ; i++){
			
				if(inputIds[i] == inputTempId){
				
					existYn="Y"
				}
				
			}

		if(existYn != "Y"){
		
			inputIds[inputIds.length]=inputTempId	
			tree_call(inputTempId,'<@spring.url ""/>');
			
			}
			
		}
		}else{
		alert(data.reason);
		}
	}
	
	});
	
}

function insertDisCard(){

	if($jq(":checkbox[name='discardCheck']:checked").length == 0){
	
		alert("폐기할 영상을 선택해 주세요.");
		
	}else {
	
		var chked_val = "";
		
		$(":checkbox[name='discardCheck']:checked").each(function(pi,po){
		
			chked_val += ","+po.value;
			
		});
		
	}

	if(chked_val != "")chked_val = chked_val.substring(1);

	$jq.ajax({
		url: '<@spring.url "/disuse/insertDisUse.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: {"ctIds" : chked_val},
		
		success: function(data){
		
			if(data.result == 'Y'){
			
			var Objs = document.getElementsByName("discardCheck");
			
			//폐기신청후 해당 체그박스를 초기화한다.
			for(i = 0; i < Objs.length; i++){
			
			 Objs[i].checked = false;
			 
			}
			
			alert('폐기 신청이 완료되었습니다.');
			
			}else{
			
				alert(data.errorCont);
				
			}
			
		}
		
	});
	
}

function cancleDisuse(ctId){
		 	
	$jq.ajax({
		url: '<@spring.url "/disuse/cancleDisUse.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: {"ctId" : ctId},
		
		success: function(data){
		
		searchBasicInfo(ctId,'');
		alert('폐기 취소가 완료되었습니다.');
		
		}
		
	});
	
}


function changeViewList(changeView){

	$jq('#viewicon').empty();
	var viewicon="";
	
	if(changeView=='list'){
	
		viewicon += '<li><img src="<@spring.url "/images/listview_on.gif"/>" title="리스트보기" alt="리스트보기"/></a></li>';
		viewicon += '<li><a href="#clipList" onClick="changeViewList(\'imgage\');changeListView(\'clipList\');return false;"><img src="<@spring.url "/images/thumbview_off.gif"/>" title="섬네일보기" alt="섬네일보기"/></a></li>';
    	viewicon += '<li><span class="btn_pack gry"><a   onClick="initValues();return false;">초기화</a></span></li>'
   
    }else {
    
	    viewicon += '<li><a href="#clipTableList" onClick="changeViewList(\'list\');changeListView(\'clipTableList\');return false;"><img src="<@spring.url "/images/listview_off.gif"/>" title="리스트보기" alt="리스트보기"/></a></li>';
		viewicon += '<li><img src="<@spring.url "/images/thumbview_on.gif"/>" title="섬네일보기" alt="섬네일보기"/></a></li>';                
    	viewicon += '<li><span class="btn_pack gry"><a   onClick="initValues();return false;">초기화</a></span></li>'
   
    }
  
    $jq('#viewicon').append(viewicon);
    
}


function initValues(){
		alert("초기화 되었습니다");
		clipSearch.keyword.value = "";
		clipSearch.startDt.value = "";
		clipSearch.endDt.value= "" ;
		clipSearch.dataStatCd.value= "" ;
		clipSearch.ctTyp.value= "" ;
		console.log('###############################' + $jq('#detailDataStatCd').val())
		if($jq('#detailDataStatCd').val() != null){
		DeleteDeatilSearchView('ul-dataStatCd')
		}
		if($jq('#detailCtTyp').val() != null){
		DeleteDeatilSearchView('ul-ctTyp')
		}
		$jq('#detailDataStatCd').val('');
		$jq('#detailCtTyp').val('');
		$jq('#from').val('');
		$jq('#to').val('');
		$jq('#keyword').val('');
		
		if(categoryTreeSearch.categoryId.value != 0){
		
		changeBold(categoryTreeSearch.categoryId.value);
		returnValue('','','','');
		
		}
		
}


function findCodeInfos(clfCd){
		 	codeInfo.clfCD.value = clfCd
		 
	$jq.ajax({
		url: '<@spring.url "/admin/code/findSclListForClf.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#codeInfo').serialize(),
		
		success: function(data){
		
		if(clfCd == 'DSCD'){
		var option = '';
		console.log("#########"+data.codeInfos.length);
		$jq('#detailDataStatCd').empty();
		option += '<option value="">선택해주세요</option>';
		for(var i=0; i<data.codeInfos.length;i++){
			option += '<option value="'+data.codeInfos[i].id.sclCd+'">'+data.codeInfos[i].sclNm+'</option>';
			
		}
		$jq('#detailDataStatCd').append(option);
		}else if(clfCd == 'CTYP'){
		var option = '';
		console.log("#########"+data.codeInfos.length);
		$jq('#detailCtTyp').empty();
		option += '<option value="">선택해주세요</option>';
		for(var i=0; i<data.codeInfos.length;i++){
			option += '<option value="'+data.codeInfos[i].id.sclCd+'">'+data.codeInfos[i].sclNm+'</option>';
			
		}
		$jq('#detailCtTyp').append(option);
		}
		
		
		}
		
	});
	
}
</script>

<form name="download" id="download" method="post" >
	<input type="hidden" name="flPath" />
	<input type="hidden" name="orgFileNm" />
	<input type="hidden" name="transFileNm" />
</form>
<form name="codeInfo" id="codeInfo" method="post" >
<@spring.bind "codeId" />
	<input type="hidden" name="clfCD" />
</form>
<form name="clipSearch" id="clipSearch" method="post" >
	<@spring.bind "search" />
	<input type="hidden" name="pageNo" />
	<input type="hidden" name="categoryId" />
	<input type="hidden" name="episodeId" />
	<input type="hidden" name="segmentId" />
	<input type="hidden" name="contentNm" />
	<input type="hidden" name="spcInfo" />
	<input type="hidden" name="ctId" />
	<input type="hidden" name="keyword" />
	<input type="hidden" name="brdDd" />
	<input type="hidden" name="startDt" />
	<input type="hidden" name="endDt" />
	<input type="hidden" name="searchTyp" />
	<input type="hidden" name="imagePageNo" />
	<input type="hidden" name="listPageNo" />
	<input type="hidden" name="dataStatCd" />
	<input type="hidden" name="ctTyp" />
	<input type="hidden" name="modrId" />
</form>

<form name="searchInfo" id="searchInfo" method="post" >
	<@spring.bind "search" />
	<input type="hidden" name="pageNo" />
	<input type="hidden" name="categoryId" />
	<input type="hidden" name="episodeId" />
	<input type="hidden" name="segmentId" />
	<input type="hidden" name="contentNm" />
	<input type="hidden" name="spcInfo" />
	<input type="hidden" name="ctId" />
	<input type="hidden" name="keyword" />
	<input type="hidden" name="brdDd" />
	<input type="hidden" name="startDt" />
	<input type="hidden" name="endDt" />
	<input type="hidden" name="searchTyp" />
	<input type="hidden" name="imagePageNo" />
	<input type="hidden" name="listPageNo" />
	<input type="hidden" name="dataStatCd" />
	<input type="hidden" name="modrId" />
</form>


<form name="categorySearch" id="categorySearch" method="post" >
	<@spring.bind "search" />
	<input type="hidden" name="categoryId" />
	<input type="hidden" name="episodeId" />
	<input type="hidden" name="segmentId" />
	<input type="hidden" name="episodeNm" />
	<input type="hidden" name="segmentNm" />
</form>

<form name="categoryTreeSearch" id="categoryTreeSearch" method="post" >
	<input type="hidden" name="depth"  />
	<input type="hidden" name="categoryId"  />
	<input type="hidden" name="preParent"  />
</form>
<!-- right 시작 -->

    <div id="left">

    </div>

<!-- right 끝 -->
 	<iframe name="uploadFrame" hight="0"/>
<!-- 본문끝 -->
