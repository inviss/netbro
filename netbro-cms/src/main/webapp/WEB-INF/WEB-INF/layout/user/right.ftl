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

var count=0;
var dboxcount = 1;
var storyboard;
var fullPath;
var duration;

var ids=new Array();
var tempIds=new Array();
var tempId="";

window.onload=function(){	
	//ex): N(초기화),A(더하기)
	findClipSearchList('N');
	//nodeCateogry();
	//tree_call("tree_start",'<@spring.url ""/>');
	findNoticeList();
}


//클립검색 List scroll의 끝 부분을 알아내는 function
jQuery(
	function($jq) {
		$jq('#clipList').scroll(function() {
			if($jq(this).scrollTop() + $jq(this).innerHeight() >= $jq(this)[0].scrollHeight) {
				findClipSearchList('A');
				
			}
		})
	}
);


jQuery(
	function($jq) {
		$jq('#clipTableList').scroll(function() {
			if($jq(this).scrollTop() + $jq(this).innerHeight() >= $jq(this)[0].scrollHeight) {
				findClipSearchListForGrid('A');
				
			}
		})
	}
);


function findClipSearchList(appendGb){

	var keyword = $jq('#keyword').val();
	var categoryId = categoryTreeSearch.categoryId.value;
	var startDt = $jq('#from').val();
	var endDt = $jq('#to').val();
	
	var pageNo;

	var table = "";
	var list = "";
	
	if(startDt != '' && endDt != ''){
	
		clipSearch.startDt.value = startDt
		clipSearch.endDt.value = endDt
	
	}else{
		clipSearch.startDt.value = getbeforeToday(new Date());
		clipSearch.endDt.value = getToDay(new Date());
		$jq('#from').val(clipSearch.startDt.value);
		$jq('#to').val(clipSearch.endDt.value);
	}		
	 
	 var sdt = new Date($jq('#from').val().split("-"));
	 var edt = new Date($jq('#to').val().split("-"));
	
	if( edt.getTime() < sdt.getTime() ){
	
    	alert("시작날짜가 종료날짜보다 큽니다");
    	return;
	
	}
	
	
		clipSearch.keyword.value = keyword;

	
	clipSearch.categoryId.value = categoryId;
	
	if(appendGb == 'A') {
		pageNo = Number(clipSearch.pageNo.value)+1;
		clipSearch.imagePageNo.value=pageNo;
		clipSearch.pageNo.value = pageNo;
	}else{
		clipSearch.pageNo.value =1;
		clipSearch.imagePageNo.value=1;
	
	}

	clipSearch.searchTyp.value="image"
	$jq.ajax({
		url: '<@spring.url "/clip/findClipSearchList.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch').serialize(),
			error: function(request,status,error){
			  if(request.status==0){
            alert('네트워크를 체크해주세요.');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(request.status==404){
            alert('페이지를 찾을수 없습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(request.status==500){
            alert('서버에러 발생하였습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(error=='parsererror'){
            alert('json 파싱에 에러가 발행하였습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(error=='timeout'){
            alert('응답 대기시간이 초과하였습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else {
            alert('알수없는 에러입니다 ');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }
			clipSearch.keyword.value = "";
			clipSearch.startDt.value = "";
			clipSearch.endDt.value = "";
			$jq('#from').val('');
			$jq('#to').val('');
		},
		success: function(data){
		//alert(data.errorYn);
		
		if(appendGb == 'N') {
			$jq('#clipList').empty();
			clipSearch.pageNo.value = data.search.pageNo;
		}
		
		if(data.result == "Y"){	
		for(var i=0;i<data.metas.length;i++){
			var brdDd = new Date(parseInt(data.metas[i].brdDd, 10));
			var date = new Date(parseInt(data.metas[i].regDt, 10));
			
			if(data.total != 0 ){
			
				table += '<dl class="viewpart bgc1">'
				table += '<dt class="list">'  
				table += '<dl class="listcontent">'
				table += '<dt><a href="javascript:" onclick="getClipSearchBasicInfo('+data.metas[i].ctId+',\'F\')">'+data.metas[i].ctNm+'</a></dt>'
				table += '<dd>'+data.metas[i].ctLeng+'</dd>'
				table += '<dd>'+date.format("yyyy-MM-dd")+'</dd>'
				table += '<dd><span class="fc1">'+BlankCheck(data.metas[i].vdQlty)+'</span></dd>'
				
			  	if(BlankCheck(data.metas[i].vdVresol) == "" && BlankCheck(data.metas[i].vdHresol) == ""){
			  		table += '<dd></dd>'			  	
			  	}else{
			  		table += '<dd>'+data.metas[i].vdVresol+'x'+data.metas[i].vdHresol+'</dd>'
			  	}
				  	table += '</dl>'
				  	table += '<dd class="in_01">&nbsp;&nbsp;&nbsp;&nbsp;</dd>'
			 
		  		var filePath = data.metas[i].flPath.substr(0, data.metas[i].flPath.lastIndexOf('/')+1);
			  	var fileNm = data.metas[i].flPath.substr(data.metas[i].flPath.lastIndexOf('/')+1);
			  	var name = fileNm.substr(fileNm.lastIndexOf('_')+1,14);
				var imgPath = data.apacheIp + filePath + name + '/' + data.metas[i].rpImgKfrmSeq +'.jpg';
			
			  	table += '<dd class="thumb"><a href="javascript:" onclick="getClipSearchBasicInfo('+data.metas[i].ctId+',\'F\')"><img src="'+imgPath+'" width="120px" height="67px"   onError="this.src=\'<@spring.url  "/images/thumbtest.gif" />\'"/></a></dd>'
			  	table += '</dt>'
			  	table += '</dl>'
			  	
			  	}
		    }
		    
		
		
		$jq('#clipList').append(table);
		//최초검색시에만 표기
		}else if(data.result == "N" && appendGb == 'N'){
		
			$jq('#clipList').append('<div style="margin-left:101px;margin-top:32px;font-size:medium;font-weight:bold;">'+data.reason+'</div>');
		}
		hideDetailSearch();
		findClipSearchListForGrid('N');
		
		}
	});
}



function findClipSearchListForGrid(appendGb){

	var keyword = $jq('#keyword').val();
	var categoryId = categoryTreeSearch.categoryId.value;
	var startDt = $jq('#from').val();
	var endDt = $jq('#to').val();
	
	var pageNo;

	var table = "";
	var list = "";
	
	if(startDt != '' && endDt != ''){
	
		clipSearch.startDt.value=startDt
		clipSearch.endDt.value=endDt
	
	}		
	 
	 var sdt = new Date($jq('#from').val().split("-"));
	 var edt = new Date($jq('#to').val().split("-"));
	
	if( edt.getTime() < sdt.getTime() ){
	
    	alert("시작날짜가 종료날짜보다 큽니다");
    	return;
	
	}
	
	if(keyword != '') {
		clipSearch.keyword.value = keyword;
	}
	
	clipSearch.categoryId.value = categoryId;
	
	if(appendGb == 'A') {
		pageNo = Number(clipSearch.listPageNo.value)+1;
		clipSearch.listPageNo.value=pageNo;
		clipSearch.pageNo.value = pageNo;
	}else{
		clipSearch.listPageNo.value=1;
		clipSearch.pageNo.value = 1;
	}
	clipSearch.searchTyp.value="list"
	$jq.ajax({
		url: '<@spring.url "/clip/findClipSearchList.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch').serialize(),
		error: function(request,status,error){
			  if(request.status==0){
            alert('네트워크를 체크해주세요.');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(request.status==404){
            alert('페이지를 찾을수 없습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(request.status==500){
            alert('서버에러 발생하였습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(error=='parsererror'){
            alert('json 파싱에 에러가 발행하였습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else if(error=='timeout'){
            alert('응답 대기시간이 초과하였습니다');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }else {
            alert('알수없는 에러입니다 ');
            console.log("code:"+request.status+"\n"+"error:"+error);
            }
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
		
		
		
		if(appendGb == 'N' && data.result == "Y"){
		for(var i=0;i<data.metas.length;i++){
			var brdDd = new Date(parseInt(data.metas[i].brdDd, 10));
			var date = new Date(parseInt(data.metas[i].regDt, 10));
			list += '<tr >';
			list += '<td><input name="discardCheck" type="checkbox" value="'+data.metas[i].ctId+'"></td>';
			list += '<td><a href="#" onclick="getClipSearchBasicInfo('+data.metas[i].ctId+',\'F\');">'+data.metas[i].ctNm+'</a></td>'
			list += '<td>'+date.format("yyyy-MM-dd")+'</td>';
			list += '<td>'+BlankCheck(data.metas[i].ctLeng)+'</td>';
			list += '<td>'+BlankCheck(data.metas[i].vdQlty)+'</td>'
			list += '</tr>';
		}
	
		$jq('#clipTableList').append(list);
		
		}else if(data.result == "Y"){
		
		for(var i=0;i<data.metas.length;i++){
			var brdDd = new Date(parseInt(data.metas[i].brdDd, 10));
			var date = new Date(parseInt(data.metas[i].regDt, 10));
			list += '<tr ">';
			list += '<td><input name="discardCheck" type="checkbox" value="'+data.metas[i].ctId+'"></td>';
			list += '<td><a href="#" onclick="getClipSearchBasicInfo('+data.metas[i].ctId+',\'F\');">'+data.metas[i].ctNm+'</a></td>'
			list += '<td>'+date.format("yyyy-MM-dd")+'</td>';
			list += '<td>'+BlankCheck(data.metas[i].ctLeng)+'</td>';
			list += '<td>'+BlankCheck(data.metas[i].vdQlty)+'</td>'
			list += '</tr>';
		}
			$jq('#clipTableBody').append(list);
		}else{
		
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



function getClipSearchBasicInfo(ctId,gubun){

	clipSearch.ctId.value = ctId;

	var tab1 ="";
	var tab2 ="";
	var tab3 ="";
	var tabtitle="";
	//스토리보드의 코너정보를 위해서 가지고있던 폼데이터 초기화
 		dividImgForm.dividImgs.value='';
 		dividImgForm.ctId.value=''; 		
 		dividImgs=''
	$jq.ajax({
		url: '<@spring.url "/clip/getClipSearchBasicInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch').serialize(),		
		success: function(data){
		if(data.result =="Y"){
		//ex): http://14.36.147.23:8002/201310/17/20131017103105  	
		fullPath = data.fullPath;
		if(gubun == "F" && data.mediaExistYn == "Y" ){
		$jq('#videoplay').attr("src", fullPath+'.mp4');
		}else if(data.mediaExistYn == "N"){
		 alert("해당 저해상도 영상을 찾을수가 없습니다");
		 $jq('#videoplay').attr("src", "");
		}
		<#--$jq('#mediaPlayer').attr('URL', fullPath+'.wmv');-->
		
		
		 	$jq('#dbox1').empty();
			$jq('#dbox2').empty();
		    $jq('#dbox3').empty();
          	tab1 += '<nav class="tabcover">' <!-- 탭시작 -->
            tab1 += '<ul class="tab1">'
            tab1 +='<li class="on"><a href="#dbox1" onclick="v1h23()"><span>기본정보</span></a></li>'
            tab1 +='<li class="off"><a href="#dbox2" onclick="v2h13();getClipSearchStoryboardInfo('+data.contentsTbl.ctId+');return false;"><span>스토리보드</span></a></li>'
            tab1 +='<li class="off"><a href="#dbox3" onclick="v3h12();getClipSearchAttachInfo('+data.contentsTbl.ctId+');return false;"><span>첨부파일</span></a></li>'
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
        	tab1 += '<dd><input name="contentNm"  readonly="readonly" id="contentNm'+data.contentsTbl.ctId+'" style="height:18px;width:515px;" type="text" value="'+data.contentsTbl.ctNm+'" /></dd>'
			tab1 += '<dt>카테고리</dt>'
			tab1 += '<dd><select name="categoryId" disable="true" id="categoryId'+data.contentsTbl.ctId+'" style="height:24px;width:100px;">'
			tab1 += '<option>'+data.contentsTbl.categoryNm+'</option>'
            tab1 += '</select>'
            tab1 += '</dd>'
			tab1 += '<dt class="none">회차</dt>'
			tab1 += '<dd><select name="episodeId" disable="true"  id="episodeId'+data.contentsTbl.ctId+'" style="height:24px;width:100px;">'
			tab1 += '<option>'+data.contentsTbl.episodeNm+'</option></select></dd>'
			// 2013.10.09 미구현으로 막음 tab1 += '<dt class="none">세그먼트</dt>'
			// tab1 += '<dd><select name="segmentId" id="segmentId'+data.contentsTbl.ctId+'" style="height:24px;width:100px;">'
			// tab1 += '<option>'+data.contentsTbl.segmentId+'</option></select></dd>'
			tab1 += '<dt >등록일</dt>'
			tab1 += '<dt class="none tal" name="brdDd" id="brdDd"><span class="fc2">'+data.contentsTbl.brdDd+'</span></dt>'
			tab1 += '<dt class="none">영상길이</dt>'
			tab1 += '<dt class="none tal"><span class="fc2">'+BlankCheck(data.contentsTbl.ctLeng)+'</span></dt>'
			tab1 += '<dt>검색키워드</dt>'
			tab1 += '<dd><input name="keyWord"  readonly="readonly" id="keyWord'+data.contentsTbl.keyWord+'" style="height:18px;width:515px;" type="text" value="'+BlankCheck(data.contentsTbl.keyWords)+'" /></dd>'
			tab1 += '<dt>특이사항</dt>'
			tab1 += '<dd><textarea name="spcInfo" readonly="readonly"  id="spcInfo'+data.contentsTbl.ctId+'" cols="" rows="" style="height:142px;width:520px;">'+BlankCheck(data.contentsTbl.spcInfo)+'</textarea></dd>'
			tab1 += '</dl>'
			tab1 += '<div class="btncover">'
			if(data.contentsTbl.dataStatCd != '003'){
			tab1 += '<span class="btn_pack xlarge"><a href="javascript:" onclick="updateErrorArrange('+data.contentsTbl.ctId+')">오류등록</a></span>'
			}
			tab1 += '</div>'
			tab1 += '</div>' <!-- 상세정보끝 -->
			console.log(data.contentsTbl.dataStatCd);
			$jq('#dbox1').append(tab1);
			v1h23();
			}else{
				alert(data.reason);
			}
          
		}
	});
}



function nodeCateogry(){

	categoryTreeSearch.depth.value = 0;
	categoryTreeSearch.categoryId.value = 0;
	
	$jq.ajax({
		url: '<@spring.url "/clip/findCategoryList.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#categoryTreeSearch').serialize(),
		
	success: function(data){
	console.log('data    '+data.categoryTbls);
		if(data.categoryTbls == "N"){
			
			alert("카테고리가 없습니다 카테고리를 신규로 추가해주세요");
		}else{
			$jq('#cate_tree').empty();
			var tree ="";
			var icon_open = "<@spring.url '/images/tree_open.gif'/>";
			var icon_close = "<@spring.url '/images/tree_close.gif'/>";

			for(var i=0;i<data.categoryTbls.length;i++){	
				if(data.categoryTbls[i].finalYn == "N"){
					tree += '<li id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'" ><a class="control" onClick="subCategroy('+data.categoryTbls[i].categoryId+','+(data.categoryTbls[i].depth+1)+',\''+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'\')"><img src="' + icon_open + '" /></a><a href="#" id="'+data.categoryTbls[i].categoryId+'" onClick="changeBold('+data.categoryTbls[i].categoryId+');showHide(\'theLayer\');returnValue(\''+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');">'+data.categoryTbls[i].categoryNm+'</a></li>';
				}else {
					tree += '<li id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'"><a href="#" id="'+data.categoryTbls[i].categoryId+'" onClick="changeBold('+data.categoryTbls[i].categoryId+');showHide(\'theLayer\');returnValue(\''+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');">'+data.categoryTbls[i].categoryNm+'</a></li>';
				}		
			}

			$jq('#cate_tree').append(tree);
			tree_call("cate_tree",'<@spring.url ""/>');
			ids[0]='cate_tree';
		}
	}
	})
}


function subCategroy(categoryId, depth, rootId){
	
	categoryTreeSearch.categoryId.value = categoryId;
	categoryTreeSearch.depth.value = depth;
	
	$jq.ajax({
		url: '<@spring.url "/clip/findCategoryList.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#categoryTreeSearch').serialize(),
		
	success: function(data){
	
		var icon_open = "<@spring.url '/images/tree_open.gif'/>";
		var icon_close = "<@spring.url '/images/tree_close.gif'/>";
		
		var tree = "";
		var depth;

		//해당 노드에서 최초 생성일 경우
		tempId = 'root'+data.categoryTbls[0].categoryId+'_'+data.categoryTbls[0].depth;
		
		if($jq('#root'+data.categoryTbls[0].categoryId+'_'+data.categoryTbls[0].depth).length==0){

			tree += '<ul id="root'+data.categoryTbls[0].categoryId+'_'+data.categoryTbls[0].depth+'">'
			
			for(var i=0;i<data.categoryTbls.length;i++){	
				if(data.categoryTbls[i].finalYn == "N"){
					tree += '<li id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'" ><a  class="control" onClick="subCategroy('+data.categoryTbls[i].categoryId+','+(data.categoryTbls[i].depth+1)+',\''+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'\');return false;"><img src="' + icon_open + '" /></a><a href="#" id="'+data.categoryTbls[i].categoryId+'"  onClick="changeBold('+data.categoryTbls[i].categoryId+');showHide(\'theLayer\');returnValue(\''+data.categoryTbls[i].categoryId+'\',\''+data.categoryTbls[i].categoryNm+'\');">'+data.categoryTbls[i].categoryNm+'</a></li>';
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
		
		if(ids.length!=0){
			for(i =0 ; i<ids.length ; i++){
				if(ids[i] == tempId) existYn="Y";
			}

			if(existYn !="Y"){
				ids[ids.length] = tempId
				tree_call(tempId,'<@spring.url ""/>');
			}
		}
	}
	});
}


function changeViewList(changeView){
	$jq('#viewicon').empty();
	
	var viewicon = "";
	
	if(changeView == 'list'){
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
		console.log('###############################' + $jq('#detailDataStatCd').val())
		clipSearch.keyword.value = "";
		clipSearch.startDt.value = "";
		clipSearch.endDt.value= "" ;
		clipSearch.dataStatCd.value= "" ;
		clipSearch.ctTyp.value= "" ;
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
		returnValue('','');
		}
}



function updateErrorArrange(ctId){
		
 	var ctId = 	$jq("#ctId"+ctId).val();
 	
 	clipSearch1.ctId.value = ctId;
	console.log("ctId   "+ctId   );
 	$jq.ajax({
		url: '<@spring.url "/clip/updateErrorArrange.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch1').serialize(),
		
		success: function(data){
		
			 getClipSearchBasicInfo(ctId);
			 alert("저장 되었습니다.");
			 
		}
		
	});
	
}


function findCodeInfos(clfCd){
		 	codeInfo.clfCD.value = clfCd
		 
	$jq.ajax({
		url: '<@spring.url "/clip/findSclListForClf.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#codeInfo').serialize(),
		
		success: function(data){
		
		if(clfCd == 'DSCD'){
		var option = '';
		console.log("#########"+data.codeInfos.length);
		$jq('#detailDataStatCd').empty();
		option += '<option>선택해주세요</option>';
		for(var i=0; i<data.codeInfos.length;i++){
			option += '<option value="'+data.codeInfos[i].id.sclCd+'">'+data.codeInfos[i].sclNm+'</option>';
			
		}
		$jq('#detailDataStatCd').append(option);
		}else if(clfCd == 'CTYP'){
		var option = '';
		console.log("#########"+data.codeInfos.length);
		$jq('#detailCtTyp').empty();
		option += '<option>선택해주세요</option>';
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
	<input type="hidden" name="lft"  />
	<input type="hidden" name="rgt"  />
	<input type="hidden" name="startDt" />
	<input type="hidden" name="endDt" />
	<input type="hidden" name="searchTyp" />
	<input type="hidden" name="imagePageNo" />
	<input type="hidden" name="listPageNo" />
	<input type="hidden" name="dataStatCd" />
	<input type="hidden" name="ctTyp" />
</form>
<form name="noticeInfo" id="noticeInfo" method="post" >
<@spring.bind "notice" />
	<input type="hidden" name="startDd" />
</form>
<form name="codeInfo" id="codeInfo" method="post" >
<@spring.bind "codeId" />
	<input type="hidden" name="clfCD" />
</form>
<form name="categoryTreeSearch" id="categoryTreeSearch" method="post" >
	<input type="hidden" name="depth"  />
	<input type="hidden" name="categoryId"  />
	<input type="hidden" name="preParent"  />
</form>

<!-- left 시작 -->

    <div id="left"></div>
<!-- left 끝 -->


 	<iframe name="uploadFrame" hight="0"/>

<!-- 본문끝 -->