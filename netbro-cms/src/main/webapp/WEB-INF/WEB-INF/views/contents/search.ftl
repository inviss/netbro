<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type=text/javascript>	 
Ext.onReady(
contentsController.init);
	</script>
<script type="text/javascript">
//코너의 시작 프레임 이미지는 삭제 불가로 처리
var fobidenDelete ='';
//대표화면으로 설정된 프레임 이미지는 삭제불가 처리한다.
var rpImgKfrm ='';

function updateContentInfo(ctId){
	

	var tempId = $jq("#episodeId"+ctId).val().split(",");
 	var contentNm = $jq("#contentNm"+ctId).val();
 	var categoryId = tempId[0];
 	var episodeId =  tempId[1];
 	var segmentId = 1;
 	var spcInfo = $jq("#spcInfo"+ctId).val();
 	var keyword = $jq("#keyWord"+ctId).val();
 	var modrId = '${user.userId}';
 	var ctId = $jq("#ctId"+ctId).val();

 	if(categoryId == 'null') {
 		 alert("카테고리를 입력해 주십시오");
		 return false;
	}
 

	
 	clipSearch1.categoryId.value = categoryId;
 	clipSearch1.contentNm.value = contentNm;
 	clipSearch1.episodeId.value = episodeId;
 	clipSearch1.segmentId.value = segmentId;
 	clipSearch1.spcInfo.value = spcInfo;
 	clipSearch1.ctId.value = ctId;
	clipSearch1.keyword.value = keyword;
	clipSearch1.modrId.value = modrId;
	
 	$jq.ajax({
		url: '<@spring.url "/contents/updateBasicInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch1').serialize(),
		
		success: function(data){
			if(data.result == "Y"){
			 alert("저장 되었습니다.");
			 searchBasicInfo(ctId,'');
			 }else{
			  alert(data.reason);
			 }
			 
		}
		
	});
	
}
   
function returnValue(value,name){

	document.getElementById("category").value = name;
	categoryTreeSearch.categoryId.value = value;
	clipSearch.categoryId.value  = value;
}


function returnInputValue(value,name){

	document.getElementById("inputcategory").value = name;
	var Id = value.split("Input");
	categorySearch.categoryId.value=Id[1];
	
}

function uploadFile() {

	$jq.ajaxSetup({ mimeType: "text/plain" });
		$jq.ajaxFileUpload({ 
			url : '<@spring.url "/contents/uploadFile.ssc" />', 
			type: "POST",
			secureuri : false, 
			fileElementId : 'uploadFile',
			dataType : 'json', 
		data : {
		   name:'uploadFile', ctId : clipSearch.ctId.value
		}, 
		success: function (data, status){ 
		
 		   getAttachInfo(clipSearch.ctId.value);
 		   alert('업로드가 완료 되었습니다.');
 		   
		}, 
		error: function (data, status, e){
		
			  if(data.responseText == 'Y') {
			  
			  	  alert('업로드가 완료 되었습니다.');
			  	  getAttachInfo(clipSearch.ctId.value);
			  	  
			  }else {
			  
			  	  alert("첨부파일을 선택하시고 다시 시도하싶시오 첨부된 파일이 없습니다");
			  	  
			  }
			  
		}
		
	});
	
}



function deleteAttachFile(){

	if($jq(":checkbox[name='check']:checked").length == 0){
	
			alert("삭제할 파일을 선택해 주세요.");
			
		}else if($jq(":checkbox[name='check']:checked").length != 1){
		
			alert("삭제할 파일을 하나만 선택해 주세요.");
			
		}else {
		
  			var chked_val = "";
  			$(":checkbox[name='check']:checked").each(function(pi,po){
   			chked_val += ","+po.value;
   			
  		});
  		
 		 	if(chked_val != "")chked_val = chked_val.substring(1);
 		 	
 		$jq.ajax({
		url: '<@spring.url "/contents/deleteAttachFile.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: {'ctid': clipSearch.ctId.value,'seq':chked_val},
		
		success: function(data){
		
			getAttachInfo(clipSearch.ctId.value)
			alert('파일 삭제가 완료되었습니다.');
			
		}
		
		});
		
  }
  
}


function getContentStoryboardInfo(ctId){

	var modrId = '${user.userId}';
	clipSearch.ctId.value = ctId;
	clipSearch.modrId.value = modrId;
	var tab1 = "";
	var tab2 = "";
	var tab3 = "";
	var tabtitle = "";
	
	var attachTbl;

 		
	$jq.ajax({
		url: '<@spring.url "/contents/getContentStoryBoardInfo.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#clipSearch').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
		$jq('#dbox2').empty();
		
		//스토리보드 txt파일data
		storyboard = data.storyboard;
		//변환된 duration data	
		duration = data.durationData;
		ctLeng = data.ctLengData;				
		//스토리보드 파일경로
		fullPath = data.fullPath;
		//attachTbl = data.attachTbl

	
			var date = new Date(parseInt(data.contentsTbl.brdDd, 10));
			//var attachDate = new Date(parseInt(data.attachTbl.regDt, 10));
			

            tab2 += '<nav class="tabcover">' <!-- 탭시작 -->
            tab2 += '<ul class="tab1">'
            tab2 += '<li class="off"><a href="#dbox1" onclick="searchBasicInfo('+data.contentsTbl.ctId+',\'\');return false;"><span>기본정보</span></a></li>'
            tab2 += '<li class="on"><a href="#dbox2" onclick="v2h13()"><span>스토리보드</span></a></li>'
            tab2 += '<li class="off"><a href="#dbox3" onclick="getAttachInfo('+data.contentsTbl.ctId+');return false;"><span>첨부파일</span></a></li>'
            if(data.contentsTbl.dataStatCd == '003'){
             tab2 += '<li class="error">** 오류 등록 영상입니다 **</li>'
             }
            tab2 += '</ul>'
            tab2 += '</nav>' <!-- 탭끝 -->
     		     
            if(storyboard != null){
            	tab2 += '<span id="rMenu">'
            	tab2 += '<div class="box2">'
	            
	           
	            	for(var i = 0; i < storyboard.length; i++){ 
	            	  
	  	 			   if(data.contentsTbl.rpimgKfrmSeq == storyboard[i]){
	    					tab2 +='<dl class="story" id="'+storyboard[i]+'" style="border:2px solid #FF0040"  onClick="changeCheckBoxCss()">'
	    				}else{
	    					tab2 +='<dl class="story" id="'+storyboard[i]+'" onClick="changeCheckBoxCss()">'
	    			}
		            	tab2 += '<dt class="storyCheck"><input name="storyboardCheck" type="checkbox" value="'+storyboard[i]+'" style ="position: relative;" onClick="check_shift();"><input type="hidden" class="ctId" value="'+data.contentsTbl.ctId+'"></dt>'
		            	tab2 += '<dt class="thumb"><a href="#" onClick="videoSeeking('+duration[i]+')"><img src="'+fullPath+'/'+storyboard[i]+'.jpg" width="106px" height="60px" title="섬네일test" alt="섬네일" /></a></dt>'
		            	tab2 += '<dd>'+ctLeng[i]+'</dd>'
		            	tab2 += '</dl>'
	           		}
	           		
	         		 rpImgKfrm = data.contentsTbl.rpimgKfrmSeq
	           		tab2 += '</div>'
	           		tab2 += '</span>'
            
            }else{
            	alert("이미지 파일이 존재하지 않습니다");
            	tab2 += '<div class="box2">'
            	tab2 += '<dl class="story">'
            	tab2 += '<dt class="thumb"><a href=""><img src="" width="106px" height="60px" title="섬네일" alt="섬네일" /></a></dt>'
            	tab2 += '<dd></dd>'
            	tab2 += '</dl>'
            	tab2 += '</div>'
            	
            }
            
            if(data.button == "Y"){
		      tab2 += '<div class="btncover8">'
            
            // 권한별 버튼 보이기 --> contents쪽만 macro_fomat 적용이 안되어서 따로 빼서 구현함.
            <#assign perm = tpl.getAccessRule('${user.userId}', '3')>
  
            <#if (perm == 'RW')>
            	tab2 += '<span class="btn_pack xlarge"><a  href="javascript:" onClick="updateArrangeComplete(\''+data.contentsTbl.ctId+'\');return false;">정리완료</a></span>'
            	tab2 +='&nbsp;'
           		tab2 += '<span class="btn_pack xlarge"><a  href="javascript:" onClick="updateErrorArrange(\''+data.contentsTbl.ctId+'\');return false;">오류등록</a></span>'
           		tab2 +='&nbsp;'
            	tab2 += '<span class="btn_pack xlarge"><a  href="javascript:" onClick=saveStoryBoard();return false;">&nbsp;&nbsp;&nbsp;저장&nbsp;&nbsp;&nbsp;</a></span>'
               
            </#if>
            }
			$jq('#dbox2').append(tab2);
			
			if(data.button == "Y"){
				initRigthMenu();
			}
		if(data.cornerTbls.length != 0){
			for(var i = 0; i < data.cornerTbls.length; i++){
	
				dividStoryBoard(data.cornerTbls[i].sDuration,data.cornerTbls[i].cnNm,data.cornerTbls[i].cnCont);
			}
		}
		}else{
		alert(data.reason);
		}
		}
		
	});
	
}

function getAttachInfo(ctId){

	clipSearch.ctId.value = ctId;

	var tab1 = "";
	var tab2 = "";
	var tab3 = "";
	var tabtitle = "";
	
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
 		v3h12();
 		}else{
 		 return;
 		}
 	}
	 dividImgs='';
	 dividConerNm =[];
	 dividConerCont =[] ;
	 dividImgForm.dividImgs.value='';
 		dividImgForm.ctId.value='';
 		deleteImgForm.ctId.value='';
 		deleteImgForm.deleteImgs.value='';
 		registRpimgForm.ctId.value=''
 		registRpimgForm.rpImg.value = ''
	var attachTbl;

	$jq.ajax({
		url: '<@spring.url "/contents/getContentsAttachInfo.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#clipSearch').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
			var date = new Date(parseInt(data.contentsTbl.brdDd, 10));
			var attachDate = new Date(parseInt(data.attachTbl.regDt, 10));
		
			
		    $jq('#dbox3').empty();
		
			tab3 += '<nav class="tabcover">' <!-- 탭시작 -->
            tab3 += '<ul class="tab1">'
            tab3 += '<li class="off"><a href="#dbox1" onclick="v1h23();searchBasicInfo('+data.contentsTbl.ctId+',\'\');return false;"><span>기본정보</span></a></li>'
            tab3 += '<li class="off"><a href="#dbox2" onclick="v2h13();getContentStoryboardInfo('+data.contentsTbl.ctId+');return false;"><span>스토리보드</span></a></li>'
            tab3 += '<li class="on"><a href="#dbox3" onclick="v3h12()"><span>첨부파일</span></a></li>'
            if(data.contentsTbl.dataStatCd == '003'){
             tab3 += '<li class="error">** 오류 등록 영상입니다 **</li>'
             }
            tab3 += '</ul>'
            tab3 += '</nav>' <!-- 탭끝 -->
            
            tab3 += '<div class="box2">'
    		tab3 += '<table summary="" class="board4">'
            tab3 += '<colgroup><col width="40px"/><col width=""/><col width="140px"/><col width="120px"/></colgroup>'
            tab3 += '<tbody>'
            tab3 += '<tr>'
            tab3 += '<th></th><th>파일명</th><th>구분</th><th>등록일</th>'
            tab3 += '</tr>'
         
 
            for(var i = 0; i < data.attachTbl.length; i++){	
            	
            	var attachDate = new Date(parseInt(data.attachTbl[i].regDt, 10));
           		tab3 += '<tr>'
           		tab3 += '<td><input name="check" type="checkbox" value="'+data.attachTbl[i].seq+'"></td><td><a href="javascript:void(0)" onClick="fileDownload(\''+data.attachTbl[i].flPath+'\',\''+data.attachTbl[i].orgFilenm+'\',\''+data.attachTbl[i].transFilenm+'\');">'+data.attachTbl[i].orgFilenm+'</a></td><td></td><td>'+attachDate.format("yyyy-MM-dd")+'</td>'
           		tab3 += '</tr>'
        	
        	}
        
            tab3 += '</tbody>'
            tab3 += '</table>'
            tab3 += '</div>'
            tab3 += '<div class="btncover8">'
            
            // 권한별 버튼 보이기 --> contents쪽만 macro_fomat 적용이 안되어서 따로 빼서 구현함.
            <#assign perm = tpl.getAccessRule('${user.userId}', '3')>
  
            <#if (perm == 'RW')>
            	tab3 += '<span class="btn_pack xlarge"><a href="javascript:" onClick=showHide("layer1");return false;">&nbsp;&nbsp;&nbsp;등록&nbsp;&nbsp;&nbsp;</a></span>'
                tab3 += '&nbsp;'
            	tab3 += '<span class="btn_pack xlarge"><a href="javascript:" onClick="deleteAttachFile()">&nbsp;&nbsp;&nbsp;삭제&nbsp;&nbsp;&nbsp;</a></span>'
            </#if>
            
                   
            <!-- Popup -->
            tab3 += '<div id="layer1" class="box4">'
            tab3 += '<form name="uploadForm" id="uploadForm" method="post" enctype="multipart/form-data">'
            tab3 += '<div class="srchfile">'
            tab3 += '<input type="file" name="uploadFile" id="uploadFile" style="width:190px">'
            tab3 += '<img src="/images/srchfile_on.gif" title="파일찾기" alt="파일찾기">'
            tab3 += '</div>'           
            tab3 += '<input type="hidden" name="ctId">'
            tab3 += '</form>'
            tab3 += '<span class="btncover6"><span class="btn_pack gry"><a  id="registAttach" href="javascript:" onclick="uploadFile()">등록</a></span>'
            tab3 += '<span class="btn_pack gry"><a href="#layer1" onClick=showHide("layer1");return false;>닫기</a></span>'
            tab3 += '</div>'
            tab3 += '</div>'
            <!-- //Popup -->
            	
            tab3 += '</div>'
        	tab3 += '</div> <!-- 첨부파일끝 -->'
			
			
			$jq('#dbox3').append(tab3);
			}else{
			alert(data.reason);
			}
		}
		
	});
	
}

function fileDownload(flPath, orgFilenm, transFilenm) {

clipSearch1.flPath.value = flPath;
clipSearch1.orgFilenm.value = orgFilenm;
clipSearch1.transFilenm.value = transFilenm;
	


	$jq.ajax({
		url: '<@spring.url "/contents/fileExistYn.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch1').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
				document.download.flPath.value = flPath;
			document.download.orgFileNm.value = orgFilenm;
			document.download.transFileNm.value = transFilenm;
	
			document.download.action="<@spring.url '/contents/filedownload.ssc'/>";
			document.download.submit();
		 }else{
			 alert("파일 다운로드를 요청했으나 다운로드를 할수 없습니다 운영자에게 문의해주세요");
			 }
		}
		
	});
	
	
	
	
}

function updateArrangeComplete(ctId){
	
 	var ctId = $jq("#ctId"+ctId).val();
 	var modrId = ${user.userId};
 	clipSearch1.ctId.value = ctId;
	clipSearch1.modrId.value = modrId;
 	$jq.ajax({
		url: '<@spring.url "/contents/updateCompleteArrange.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch1').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
			 searchBasicInfo(ctId,'');
			 alert("저장 되었습니다.");
			 }else{
			 alert(data.reason);
			 }
		}
		
	});
	
}

function updateErrorArrange(ctId){
		
 	var ctId = 	$jq("#ctId"+ctId).val();
 	var modrId = ${user.userId};
 	clipSearch1.ctId.value = ctId;
	clipSearch1.modrId.value = modrId;
 	$jq.ajax({
		url: '<@spring.url "/contents/updateErrorArrange.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch1').serialize(),
		
		success: function(data){
			
			if(data.result == "Y"){
			 alert("저장 되었습니다.");
			 searchBasicInfo(ctId,'');
			 }else{
			 alert(data.reason);
			 }
			 
		}
		
	});
	
}

function updateCancleError(){
	
 	if($jq(":checkbox[name='discardCheck']:checked").length == 0){
	
		alert("취소할 영상을 선택해 주세요.");
		
	}else {
	
		var chked_val = "";
		
		$(":checkbox[name='discardCheck']:checked").each(function(pi,po){
		
			chked_val += ","+po.value;
			
		});
		
	}

	if(chked_val != "")chked_val = chked_val.substring(1);
	console.log("chked_val     "+chked_val)
	var modrId = ${user.userId};
 	clipSearch1.ctIds.value = chked_val;
	clipSearch1.modrId.value = modrId;
 	$jq.ajax({
		url: '<@spring.url "/contents/updateCancleError.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch1').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
			 contentSearchList('N');
			//에러등록건 조회를 초기화한다.
			var Objs = document.getElementsByName("errorCheck");
			
			//폐기신청후 해당 체그박스를 초기화한다.
			for(i = 0; i < Objs.length; i++){
			
			 Objs[i].checked = false;
			 
			}
			 initBaseInfo();
			 alert("취소 되었습니다.");
			 }else{
			 alert(data.reason);
			 }
		}
		
	});
	
}


var oldCheckValue = ""; // 기존 클릭한 체크 박스의 값을 저장하기 위한 변수 : 시작점을 구분하기 위함
function check_shift() {
var obj = window.event.srcElement; // 클릭한 체크 박스 객체

var oObject = document.getElementsByName(obj.name); // 동일한 이름을 가진 체크 박스 객체 배열
var vCheck = (obj.checked == true) ? true : false; // 선택 혹은 해제 여부
var vBreak = false; // 선택 혹은 해제를 시작/종료 여부

// SHIFT 키가 눌려있는지를 체크
if(event.shiftKey) {
// 체크 박스 객체 갯수만큼 루프
for(var i = 0; i < oObject.length; i++) {
// 기존 클릭한 체크 박스값이 존재하는가를 확인하여
// 값이 없으면 처음부터 적용하고 그렇지 않으면 기존
// 클릭한 체크 박스 부터 지금 클릭한 체크 박스 까지 선택 혹은 해제를 적용한다.
if(oldCheckValue == "") {
if(oObject[i].value == obj.value) break; // 지금 클릭한 체크 박스의 값과 루프의 체크 박스의 값이 같으면 루프를 빠져나간다.
else oObject[i].checked = vCheck; // 값이 다르면 선택/해제를 적용한다.


}else {
// 기존 체크 박스의 값과 같으면 선택/해제 여부를 결정하고
// 적용을 시작한다.
if(oObject[i].value == oldCheckValue) {
vCheck = (oObject[i].checked == true) ? true : false;
vBreak = true;
}

if(vBreak) oObject[i].checked = vCheck; // 적용이 종료 되지 않았으면 적용한다.

	if($jq('#'+oObject[i].value).css("display") == "none"){
	
	}else if(oObject[i].checked == true){
		$jq('#'+oObject[i].value).attr("style","border:2px solid #00FFC2")
	}else if(rpImgKfrm == oObject[i].value){
		$jq('#'+oObject[i].value).attr("style","border:2px solid #FF0040")
	}else{
		$jq('#'+oObject[i].value).attr("style","")
	}
if(oObject[i].value == obj.value) break; // 지금 클릭한 체크 박스의 값과 같으면 적용을 종료 한다.
}
}
}else {
oldCheckValue = obj.value; // SHIFT키가 눌리지 않았으면 기존 체크 박스 값을 변경해준다.
}
}


var imgDeletes = "";
function initRigthMenu(){
      $jq('#rMenu').contextMenu('myMenu1', {
		
		onShowMenu : function(e,menu){
		
			if($jq(":checkbox[name='storyboardCheck']:checked").length == 0){
			 $jq('#rMenu').contextMenu(false);
			}else if($jq(":checkbox[name='storyboardCheck']:checked").length != 1){
			
			$jq('#registRpImg', menu).remove();
			$jq('#imgDivid', menu).remove();
			}
			
			  return menu;
			  
		},
		
        bindings: {

          'registRpImg': function(t) {
			
				if($jq(":checkbox[name='storyboardCheck']:checked").length != 0){
	
					var chked_val = "";
		
					$(":checkbox[name='storyboardCheck']:checked").each(function(pi,po){
		
					chked_val += ","+po.value;
			
					});
		
				}

				if(chked_val != "")chked_val = chked_val.substring(1);
				console.log("chked_val     "+chked_val)
         	   
				registRpimgForm.ctId.value = $jq('.ctId').val();
				registRpimgForm.rpImg.value = chked_val;
         	 },

          'imgDelete': function(t) {

           if($jq(":checkbox[name='storyboardCheck']:checked").length != 0){
	
					
		
					$(":checkbox[name='storyboardCheck']:checked").each(function(pi,po){
		
					imgDeletes += ","+po.value;
			
					});
		
				}

				if(imgDeletes != "")imgDeletes = imgDeletes.substring(1);
				console.log("imgDeletes     "+imgDeletes)
			 	console.log("fobidenDelete     "+fobidenDelete)		    
			    var deleteImages = imgDeletes.split(',');
			    var fobiden = fobidenDelete.split(',');
			   
			   console.log("deleteImages.length     "+deleteImages.length)		 
			    for(var i = 0; i < deleteImages.length; i++){
			  console.log("deleteImages[     "+deleteImages[i])
			    	if(fobiden.indexOf(deleteImages[i]) != -1){
			    		alert("코너의 첫 시작점은 삭제하실수 없습니다");
			    		return;
			    	}else if(rpImgKfrm == deleteImages[i]){
			    		alert("대표화면프레임으로 지정된 샷은 삭제 할수 없습니다.");
			    		return;
			    	}else if(registRpimgForm.rpImg.value == deleteImages[i]){
			    		alert("신규 대표화면프레임으로 지정된 샷이 존재합니다.");
			    		return;
			    	}else{
			    	if(deleteImages[i]==''){
			    	
			    		$jq('#0').attr("style","diplay : none");
			    	
			    	}else{
			    	
			    		$jq('#'+deleteImages[i]).attr("style","display : none");
			    	
			    	}
			    }
			    
			    
			    var Objs = document.getElementsByName("storyboardCheck");
			
				// 해당 체그박스를 초기화한다.
					for(var k = 0; k < Objs.length; k++){
				
						 Objs[k].checked = false;
			 
					}
			    }
				
				deleteImgForm.deleteImgs.value=imgDeletes;
				deleteImgForm.ctId.value=$jq('.ctId').val();
				
				
				
				console.log("deleteImgForm.deleteImgs.value     "+deleteImgForm.deleteImgs.value)
				console.log("deleteImgForm.ctId.value     "+deleteImgForm.ctId.value)
				
				
          },

          'imgMemo': function(t) {
          console.log("i`m here");
          alert("구현예정입니다");
          },
          

          'imgDivid': function(t) {

          if($jq(":checkbox[name='storyboardCheck']:checked").length != 0){
	
					var chked_val = "";
		
					$(":checkbox[name='storyboardCheck']:checked").each(function(pi,po){
		
					chked_val += ","+po.value;
			
					});
		
				}

				if(chked_val != "")chked_val = chked_val.substring(1);
				console.log("chked_val     "+chked_val)
         	  dividImgForm.ctId.value = $jq('.ctId').val();
				dividStoryBoard(chked_val,'','');
				
					var Objs = document.getElementsByName("storyboardCheck");
			
			// 해당 체그박스를 초기화한다.
			for(i = 0; i < Objs.length; i++){
			
			 Objs[i].checked = false;
			 
			}
			
          }

        }

      });
}


function updateRpimg(){
		
 	var ctId = 	$jq("#ctId"+ctId).val();
	var modrId = ${user.userId};
	
	registRpimgForm.modrId.value = modrId; 	
 	registRpimgForm.ctId.value = ctId;
 	$jq.ajax({
		url: '<@spring.url "/contents/updateRpimg.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#registRpimgForm').serialize(),
		
		success: function(data){
			alert("대표화면이미지가 변경되었습니다");
			 getContentStoryboardInfo(registRpimgForm.ctId.value);
			 registRpimgForm.ctId.value = '';
			 registRpimgForm.rpImg.value = '';
		}
		
	});
	
}

function deleteImg(){
		
 	var ctId = 	$jq("#ctId"+ctId).val();
 	var modrId = ${user.userId};
	
 	deleteImgForm.ctId.value = ctId;
	deleteImgForm.modrId.value = modrId; 	
 	$jq.ajax({
		url: '<@spring.url "/contents/updateStoryBoardImgs.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#deleteImgForm').serialize(),
		
		success: function(data){
		
			
			 alert("저장 되었습니다.");
			// getContentStoryboardInfo(deleteImgForm.ctId.value);
			 deleteImgForm.deleteImgs.value='';
			 deleteImgForm.ctId.value='';
		}
		
	});
	
}



function dividCorner(){
		
	dividImgForm.cnNm.value = dividConerNm.join(',');
	var divid= dividImgs.split(',');
	var modrId = ${user.userId};
	var tempConerInfo ="";
	for(var i=0;i<divid.length; i++){
		dividConerCont[i]=$jq('#cnCont_'+divid[i]).val();
	}
	dividImgForm.cnCont.value = dividConerCont.join(',');
	dividImgForm.modrId.value = modrId;
 	$jq.ajax({
		url: '<@spring.url "/contents/insertCornerInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#dividImgForm').serialize(),
		
		success: function(data){
		
			
			 alert("저장 되었습니다.");
			 dividImgs='';
			 dividConerNm =[];
			 dividConerCont =[] 
			 getContentStoryboardInfo(dividImgForm.ctId.value);
			dividImgForm.dividImgs.value='';
			dividImgForm.ctId.value='';
		}
		
	});
	
}


function saveStoryBoard(){
		console.log("registRpimgForm.ctId.value     "+registRpimgForm.ctId.value);
		console.log("deleteImgForm.ctId.value       "+deleteImgForm.ctId.value);
			console.log("dividImgForm.ctId.value       "+dividImgForm.ctId.value);
 	if(registRpimgForm.ctId.value != ''){
 		updateRpimg();
 	}
 	if(deleteImgForm.ctId.value != ''){
 		deleteImg();
 	}
 	if(dividImgForm.ctId.value != ''){
 		dividCorner();
 	}
 	if(deleteImgForm.ctId.value  == '' && registRpimgForm.ctId.value == '' && dividImgForm.ctId.value == ''){
 		alert("저장 할것이 없습니다");
 	}
	
}


var dividImgs='';
var dividConerNm =[];
var dividConerCont =[] ;
function dividStoryBoard(Id,cnNm,cnCont){

var divid ;
if(dividImgs == ''){
divid =['']
}else{
divid= dividImgs.split(',');
}

var dupliYN = 'N'

for(var i = 0;i < divid.length; i++){ 
	if(divid[i] == Id){
			if(Id == 0){
			dupliYN = 'N'
			}else{
			dupliYN = 'Y'
			}
		
		}
		
}
if(dupliYN == 'N'){

	$jq('#'+Id).before('<div class="blank"></div><div id="corner_'+Id+'" class="blank"></div>');
		insertCornerInfo(Id,cnNm,cnCont);
		
	
			dividImgs += Id + ','
			dividConerNm[i-1] = cnNm
			dividConerCont[i-1] = cnCont
			fobidenDelete +=  Id +',' 
		
		dividImgForm.dividImgs.value = dividImgs
	
	
	
	} else {
	 
	 alert('이미 나눠진 부분입니다');
	 
	}
}

function insertCornerInfo(Id,cnNm,cnCont){
var cornerInfo="";
cornerInfo += '<div class="box" id="cnId_'+Id+'" ondblclick="changeCnNm(\''+Id+'\',\''+cnNm+'\');">'+cnNm+'</div>';
cornerInfo += '<textarea id="cnCont_'+Id+'" class="cnCont" onclick="changeCnCont(\''+Id+'\',value)">'+cnCont+'</textarea>'
$jq('#corner_'+Id).append(cornerInfo);
}


function changeCnNm(Id,cnNm){
$jq('#cnId_'+Id).empty();
$jq('#cnId_'+Id).append('<input id="inputCnId_'+Id+'"type="text" style="width:510px;margin-right:7px;" value="'+cnNm+'" onkeypress="if(event.keyCode==13){inserteCnNm(\''+Id+'\',value)};"><span class="btn_pack gry"><a href="#"  onclick="inserteCnNm(\''+Id+'\',\'\');return false;">변경</a></span>');

}

function inserteCnNm(Id,cnNm){

console.log('#cnId_  '+$jq("#inputCnId_"+Id).val());
if(cnNm == ""){
cnNm = $jq('#inputCnId_'+Id).val();
}
$jq('#cnId_'+Id).remove();
$jq('#cnCont_'+Id).before( '<div class="box" id="cnId_'+Id+'" ondblclick="changeCnNm(\''+Id+'\',\''+cnNm+'\');">'+cnNm+'</div>')

var count =0;
var divid = dividImgs.split(',');
for(var j = 0; j < divid.length; j++ ){
	if(divid[j] == Id){
	count = j;
	}
	dividImgForm.ctId.value = $jq('.ctId').val();
}
dividConerNm[count] = cnNm;
}

function changeCnCont(Id,cnCont){
console.log('i`m here !!!');
var count =0;
var divid = dividImgs.split(',');
for(var j = 0; j < divid.length; j++ ){
	if(divid[j] == Id){
	count = j;
	}
	dividImgForm.ctId.value = $jq('.ctId').val();
}
dividConerCont[count] = cnCont;
}
</script>



<form name="clipSearch1" id="clipSearch1" method="post" >
	<@spring.bind "search" />
	<input type="hidden" name="pageNo" />
	<input type="hidden" name="categoryId" />
	<input type="hidden" name="episodeId" />
	<input type="hidden" name="segmentId" />
	<input type="hidden" name="contentNm" />
	<input type="hidden" name="spcInfo" />
	<input type="hidden" name="ctId" />
	<input type="hidden" name="ctIds" />
	<input type="hidden" name="keyword" />
	<input type="hidden" name="flPath" />
	<input type="hidden" name="orgFilenm" />
	<input type="hidden" name="transFilenm" />
	<input type="hidden" name="modrId" />
</form>

<form name="registRpimgForm" id="registRpimgForm" method="post" >
	<@spring.bind "storyBoard" />
	<input type="hidden" name="rpImg" />
	<input type="hidden" name="ctId" />
	<input type="hidden" name="modrId" />
</form>

<form name="deleteImgForm" id="deleteImgForm" method="post" >
	<@spring.bind "storyBoard" />
	<input type="hidden" name="deleteImgs" />
	<input type="hidden" name="ctId" />	
	<input type="hidden" name="modrId" />
</form>

<form name="dividImgForm" id="dividImgForm" method="post" >
	<@spring.bind "storyBoard" />
	<input type="hidden" name="dividImgs" />
	<input type="hidden" name="cnNm" />
	<input type="hidden" name="cnCont" />
	<input type="hidden" name="ctId" />	
	<input type="hidden" name="modrId" />
</form>
<!-- 섹션1시작 -->
<div  class="westpanel" id="westpanel"> 
    <section id="section1"> 

    </section>
   
    </div>
   
   <div class="contextMenu" id="myMenu1" style="display:none">

      <ul>

        <li id="registRpImg">대표화면등록</li>

        <li id="imgDelete">샷삭제</li>
        
       <!-- <li id="imgMemo">샷 메모추가</li> -->

        <li id="imgDivid">샷 나누기<li>  
      </ul>

    </div>
    
   