<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type=text/javascript>	 
	</script>
<script type="text/javascript">


function getClipSearchStoryboardInfo(ctId){

	clipSearch.ctId.value = ctId;

	var tab1 ="";
	var tab2 ="";
	var tab3 ="";
	var tabtitle="";
	
	var attachTbl;

	$jq.ajax({
		url: '<@spring.url "/clip/getClipSearchStroyboardInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
		//스토리보드 txt파일data
		storyboard = data.storyboard;
		//변환된 duration data	
		duration = data.durationData;
		ctLeng = data.ctLengData;				
		//스토리보드 파일경로
		//ex): http://14.36.147.23:8002/201310/17/20131017103105 
		fullPath = data.fullPath;

		var date = new Date(parseInt(data.contentsTbl.brdDd, 10));

		$jq('#dbox1').empty();
		$jq('#dbox2').empty();
		$jq('#dbox3').empty();
	
	
		tab2 +='<nav class="tabcover">' <!-- 탭시작 -->
        tab2 +='<ul class="tab1">'
        tab2 +='<li class="off"><a href="#dbox1" onclick="v1h23();getClipSearchBasicInfo('+data.contentsTbl.ctId+',\'\');return false;"><span>기본정보</span></a></li>'
        tab2 +='<li class="on"><a href="#dbox2" onclick="v2h13()"><span>스토리보드</span></a></li>'
        tab2 +='<li class="off"><a href="#dbox3" onclick="v3h12();getClipSearchAttachInfo('+data.contentsTbl.ctId+');return false;"><span>첨부파일</span></a></li>'
         if(data.contentsTbl.dataStatCd == '003'){
             tab2 += '<li class="error">** 오류 등록 영상입니다 **</li>'
             }
        tab2 +='</ul>'
        tab2 +='</nav>' <!-- 탭끝 -->
     		
     	if(storyboard != null){
     		tab2 += '<span id="rMenu">'
        	tab2 +='<div class="box2">'

	    for(var i = 0; i<storyboard.length;i++){ 	
	  	    if(data.contentsTbl.rpimgKfrmSeq == storyboard[i]){
	    		tab2 +='<dl class="story" id="'+storyboard[i]+'" style="border:2px solid #FF0040">'
	    	}else{
	    		tab2 +='<dl class="story" id="'+storyboard[i]+'">'
	    	}
	        tab2 +='<dt class="thumb"><a href="#" onClick="videoSeeking('+duration[i]+')"><img src="'+fullPath+'/'+storyboard[i]+'.jpg" width="106px" height="60px" title="섬네일" alt="섬네일" /></a></dt>'
	        tab2 +='<dd>'+ctLeng[i]+'</dd>'
	        tab2 +='</dl>'
	  	}
	        tab2 +='</div>'
	        tab2 += '</span>'
        }else{
           	tab2 +='<div class="box2">'
           	tab2 +='<dl class="story">'
            tab2 +='<dt class="thumb"><a href=""><img src="" width="106px" height="60px" title="섬네일" alt="섬네일" /></a></dt>'
            tab2 +='<dd></dd>'
            tab2 +='</dl>'
           	tab2 +='</div>'
        }
			$jq('#dbox2').append(tab2);
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



function getClipSearchAttachInfo(ctId){

	clipSearch.ctId.value = ctId;

	var tab1 ="";
	var tab2 ="";
	var tab3 ="";
	var tabtitle="";
	
	var attachTbl;

	$jq.ajax({
		url: '<@spring.url "/clip/getClipSearchAttachInfo.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch').serialize(),
		
		success: function(data){
	 	if(data.result == "Y"){
		fullPath = data.fullPath;
		attachTbl = data.attachTbl
		
		var date = new Date(parseInt(data.contentsTbl.brdDd, 10));
		var attachDate = new Date(parseInt(data.attachTbl.regDt, 10));
		
		$jq('#dbox1').empty();
		$jq('#dbox2').empty();
		$jq('#dbox3').empty();
		
		tab3 +='<nav class="tabcover">' <!-- 탭시작 -->
		tab3 +='<ul class="tab1">'
		tab3 +='<li class="off"><a href="#dbox1" onclick="v1h23();getClipSearchBasicInfo('+data.contentsTbl.ctId+',\'\');return false;"><span>기본정보</span></a></li>'
		tab3 +='<li class="off"><a href="#dbox2" onclick="v2h13();getClipSearchStoryboardInfo('+data.contentsTbl.ctId+');return false;"><span>스토리보드</span></a></li>'
		tab3 +='<li class="on"><a href="#dbox3" onclick="v3h12()"><span>첨부파일</span></a></li>'
		 if(data.contentsTbl.dataStatCd == '003'){
             tab3 += '<li class="error">** 오류 등록 영상입니다 **</li>'
             }
		tab3 +='</ul>'
		tab3 +='</nav>' <!-- 탭끝 -->
		
		tab3 +='<div class="box2">'
		tab3 +='<table summary="" class="board4">'
		tab3 +='<colgroup><col width="40px"/><col width=""/><col width="140px"/><col width="120px"/></colgroup>'
		tab3 +='<tbody>'
		tab3 +='<tr>'
		tab3 +='<th></th><th>파일명</th><th>구분</th><th>등록일</th>'
		tab3 +='</tr>'
   
        for(var i=0;i<data.attachTbl.length;i++){	
        	var attachDate = new Date(parseInt(data.attachTbl[i].regDt, 10));
           	tab3 +='<tr>'
           	tab3 +='<td><input name="check" type="checkbox" value="'+data.attachTbl[i].seq+'"></td><td><a href="javascript:void(0)" onClick="fileDownload(\''+data.attachTbl[i].flPath+'\',\''+data.attachTbl[i].orgFilenm+'\',\''+data.attachTbl[i].transFilenm+'\');">'+data.attachTbl[i].orgFilenm+'</a></td><td></td><td>'+attachDate.format("yyyy-MM-dd")+'</td>'
           	tab3 +='</tr>'
       	}
        
        tab3 +='</tbody>'
        tab3 +='</table>'
        tab3 +='</div>'
        tab3 +='<div class="btncover8">'
        //tab3 +='<a href="#layer1" onClick=showHide("layer1");return false;"><img id="1" src="/images/reg_off.gif" title="등록" alt="등록" onMouseOver=this.src="/images/reg_on.gif" onMouseOut=this.src="/images/reg_off.gif"></a>'
        //tab3 +='<a href="javascript:" onclick="deleteAttachFile()"><img src="/images/del_off.gif" title="삭제" alt="삭제" onMouseOver=this.src="/images/del_on.gif" onMouseOut=this.src="/images/del_off.gif"></a>'
           
        <!-- Popup -->
        tab3 +='<div id="layer1" class="box4">'
        tab3 +='<form name="uploadForm" id="uploadForm" method="post" enctype="multipart/form-data">'
        tab3 +='<div class="srchfile">'
        tab3 +='<input type="file" name="uploadFile" id="uploadFile" style="width:150px">'
		tab3 +='<img src="/images/srchfile_on.gif" title="파일찾기" alt="파일찾기">'
		tab3 +='</div>'
		tab3 +='<table class="board5">'
		tab3 +='<colgroup><col width="20px"/><col width="230px"/></colgroup>'
		tab3 +='<tbody>'
		tab3 +='<tr><th><input id="filenum" name="" type="checkbox" value=""></th><td id="num"></td></tr>'
		tab3 +='</tbody>'
		tab3 +='</table>'
		tab3 +='<input type="hidden" name="ctId">'
		tab3 +='</form>'
		tab3 +='<div class="btncover6"><span class="btn_pack gry"><a href="#">선택삭제</a></span>'
		tab3 +='<span class="btncover6"><span class="btn_pack gry"><a href="javascript:" onclick="uploadFile()">등록</a></span>'
		tab3 +='<span class="btn_pack gry"><a href="#layer1" onClick=showHide("layer1");return false;>닫기</a></span>'
		tab3 +='</div>'
		tab3 +='</div>'
		<!-- //Popup -->

		tab3 +='</div>'
		tab3 +='</div> <!-- 첨부파일끝 -->'


		$jq('#dbox3').append(tab3);
		}else{
		alert(data.reason);
		}	
		}
	});
}



function returnValue(value,name){
	document.getElementById("category").value = name;
	categoryTreeSearch.categoryId.value=value;
	
}


var dividImgs='';
function dividStoryBoard(Id,cnNm,cnCont){
console.log('dividImgForm.ctId.value ' +dividImgForm.ctId.value);
console.log('dividImgs ' +dividImgs);
var divid = dividImgs.split(',');
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
		

		if(dividImgs == '' ){
	
			dividImgs = '0,'+Id

		}else{
	
			dividImgs += ',' + Id

		}
		dividImgForm.dividImgs.value = dividImgs
		
	
	
	} else {
	 
	 alert('이미 나눠진 부분입니다');
	 
	}
}

function insertCornerInfo(Id,cnNm,cnCont){
var cornerInfo="";
cornerInfo += '<div class="box" id="cnId_'+Id+'">'+cnNm+'</div>';
cornerInfo += '<textarea id="cnCont_'+Id+'" class="cnCont" disabled="true">'+cnCont+'</textarea>'
$jq('#corner_'+Id).append(cornerInfo);
}

function fileDownload(flPath, orgFilenm, transFilenm) {

clipSearch1.flPath.value = flPath;
clipSearch1.orgFilenm.value = orgFilenm;
clipSearch1.transFilenm.value = transFilenm;
	


	$jq.ajax({
		url: '<@spring.url "/clip/fileExistYn.ssc" />',
		type: 'POST',
		dataType: 'json',
		data: $jq('#clipSearch1').serialize(),
		
		success: function(data){
		if(data.result == "Y"){
				document.download.flPath.value = flPath;
			document.download.orgFileNm.value = orgFilenm;
			document.download.transFileNm.value = transFilenm;
	
			document.download.action="<@spring.url '/clip/filedownload.ssc'/>";
			document.download.submit();
		 }else{
			alert(data.reason);
			 }
		}
		
	});
	
	
	
	
}

</script>
<form name="dividImgForm" id="dividImgForm" method="post" >
	
	<input type="hidden" name="dividImgs" />
	<input type="hidden" name="ctId" />	
</form>
<form name="clipSearch1" id="clipSearch1" method="post" >
	<@spring.bind "search" />
	<input type="hidden" name="pageNo" />
	<input type="hidden" name="categoryId" />
	<input type="hidden" name="episodeId" />
	<input type="hidden" name="segmentId" />
	<input type="hidden" name="contentNm" />
	<input type="hidden" name="spcInfo" />
	<input type="hidden" name="ctId" />
	<input type="hidden" name="keyword" />
	<input type="hidden" name="searchTyp" />
	<input type="hidden" name="flPath" />
	<input type="hidden" name="orgFilenm" />
	<input type="hidden" name="transFilenm" />
</form>


<!-- 섹션1시작 -->
<div  class="westpanel" id="westpanel"> 
    <section id="section1"> 

    
    	 <!--<div class="video"> 영상표시시작 
    		
    		 
            <video width="640px" height="370px" autoplay="autoplay"  controls="true" ID="videoplay" name="Player">
            	<source src="" type="video/mp4">
            </video>
            
           <#--
          
            <object name="Player" ID="Player"  CLASSID="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6" width="640px" height="390px" >
                   <PARAM  name="URL"  value="http://14.36.147.23:8002/201310/09/20131009153330.mp4">
            </object>
             -->
            <#--
             <object id='mediaPlayer' width="640px" height="390px" classid='CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95' codebase='http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=5,1,52,701' standby='Loading Microsoft Windows Media Player components...' type='application/x-oleobject'>
            <param name='fileName' value="">
            <param name='animationatStart' value='true'>
            <param name='autoStart' value="false">
            <param name='showControls' value="true">
            <param name='loop' value="true">
            <embed autostart="0" loop="0"  type="application/x-mplayer2" pluginspage='http://microsoft.com/windows/mediaplayer/en/download/' id='mediaPlayer' name='mediaPlayer' showcontrols="true" width="640px" height="390px" src="" designtimesp='5311'></embed>
            </object>
            -->
            
        </div>  -->
     
    </section>
    </div>
<!-- 섹션1끝 -->
