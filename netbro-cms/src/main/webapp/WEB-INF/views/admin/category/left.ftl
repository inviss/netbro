<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">

<script type="text/javascript">

var ids=new Array();
var tempIds=new Array();
var bold='';
	
window.onload=function(){	
	categroyTreeSearch(0,'','');
	tree_call("treeStart",'<@spring.url ""/>');
}

window.ready = function(){	
	var tempId = "";
}


function categroyTreeSearch(treeDepth,parentId,preParentId){


	$jq.ajax({
		url: '<@spring.url "/admin/category/findCategoryList.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#treeSearch').serialize(),
		
		success: function(data){
		
		var tree = "";
	      
	      if(data.categoryTbls == "N" && data.result == undefined){
	      	alert("카테고리가 없습니다 카테고리를 신규로 추가해주세요");
	      }else if(data.categoryTbls == "N" && data.reason != undefined){
	      	alert(data.reason);
	      }else{
		  	$jq('#category_tree').empty();
		  	
			var icon_open = '<@spring.url "/images/tree_open.gif"  />';
			var icon_close = '<@spring.url "/images/tree_close.gif" />';
			
			for(var i = 0; i < data.categoryTbls.length; i++){		
				if(data.categoryTbls[i].finalYn == "N"){
				
					tree += '<li id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'" ><a   class="control" onClick="showSubTree('+data.categoryTbls[i].categoryId+','+(data.categoryTbls[i].depth+1)+',\''+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'\')"><img src="' + icon_open + '" /></a><a  href="#" id="'+data.categoryTbls[i].categoryId+'" onClick="changeCategoryId('+data.categoryTbls[i].categoryId+');changeCategoryNm(\''+data.categoryTbls[i].categoryNm+'\');episodeSearch('+data.categoryTbls[i].categoryId+',\'\''+');changeBold('+data.categoryTbls[i].categoryId+');return false;">'+data.categoryTbls[i].categoryNm+'</a></li>';
				
				}else {
					
				 	tree += '<li   id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'"><a  href="#" id="'+data.categoryTbls[i].categoryId+'" onClick="changeCategoryId('+data.categoryTbls[i].categoryId+');changeCategoryNm(\''+data.categoryTbls[i].categoryNm+'\');episodeSearch('+data.categoryTbls[i].categoryId+',\'\''+');changeBold('+data.categoryTbls[i].categoryId+');return false;">'+data.categoryTbls[i].categoryNm+'</a></li>';
					
				}	
					
			}
				
			$jq('#category_tree').append(tree);
			console.log('#####################3    '+bold);
			if(bold != ''){
				changeBold(bold);
				bold ='';
			}
			tree_call("category_tree",'<@spring.url ""/>');
			ids[0] = 'category_tree';
			
			}
			
		}
		
	})
	
}

<!-- 하위트리 호출부-->

function showSubTree(categoryId, depth, rootId){
	
	treeSearch.categoryId.value = categoryId;
	treeSearch.depth.value = depth;
	
	
	$jq.ajax({
		url: '<@spring.url "/admin/category/findCategoryList.ssc" />',
		type: 'GET',
		dataType: 'json',
		data: $jq('#treeSearch').serialize(),
		
		success: function(data){
		
		var tree = "";
		var icon_open =  '<@spring.url "/images/tree_open.gif"  />';
		var icon_close = '<@spring.url "/images/tree_close.gif" />';
		var depth;
		if(data.categoryTbls != "N"){	
		//해당 노드에서 최초 생성일 경우
		tempId = 'root'+data.categoryTbls[0].categoryId+'_'+data.categoryTbls[0].depth
		if($jq('#root'+data.categoryTbls[0].categoryId+'_'+data.categoryTbls[0].depth).length == 0){
			
			
			tree += '<ul id="root'+data.categoryTbls[0].categoryId+'_'+data.categoryTbls[0].depth+'">'
				for(var i = 0; i < data.categoryTbls.length; i++){	
				
					if(data.categoryTbls[i].finalYn == "N"){
					
						 tree += '<li id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'" ><a  class="control" onClick="showSubTree('+data.categoryTbls[i].categoryId+','+(data.categoryTbls[i].depth+1)+',\''+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'\');return false;"><img src="' + icon_open + '" /></a><a  href="#" id="'+data.categoryTbls[i].categoryId+'"  onClick="changeCategoryId('+data.categoryTbls[i].categoryId+');episodeSearch('+data.categoryTbls[i].categoryId+',\'\''+');changeCategoryNm(\''+data.categoryTbls[i].categoryNm+'\');changeBold('+data.categoryTbls[i].categoryId+');return false;">'+data.categoryTbls[i].categoryNm+'</a></li>';
					
					}else {
		 				 
		 				 tree += '<li id="'+data.categoryTbls[i].categoryId+'_'+data.categoryTbls[i].depth+'"><a href="#" id="'+data.categoryTbls[i].categoryId+'"  onClick="changeCategoryId('+data.categoryTbls[i].categoryId+');changeCategoryNm(\''+data.categoryTbls[i].categoryNm+'\');episodeSearch('+data.categoryTbls[i].categoryId+',\'\''+');changeBold('+data.categoryTbls[i].categoryId+');return false;">'+data.categoryTbls[i].categoryNm+'</a></li>';		
					
					}
						
						if(i == data.categoryTbls.length-1){
		    				
		    				tree += '</ul>'
						
						}
							
							$jq('#'+(data.categoryTbls[i].categoryId-(1+i))+'_'+(data.categoryTbls[i].depth-1)).find('li:has("ul")').append(tree);
							
							if(i == data.categoryTbls.length-1){
								
								$jq('#'+rootId).append(tree);
		   					
		   					}
		   					
		    	}
		    	
		}else{
			console.log('I` M HERE')
		}
		if(bold != ''){
				changeBold(bold);
				bold ='';
			}		
		var existYn = "N";
		if(ids.length != 0){						
			for(i = 0; i < ids.length ; i++){
			
				if(ids[i] == tempId){
				
					existYn="Y";
					
				}	     
						
			}
					     
		console.log('########################################existYn    '+existYn);
			if(existYn != "Y"){
			
				ids[ids.length] = tempId;
				tree_call(tempId,'<@spring.url ""/>');
				
			}
			
		}
		}else{
		alert(data.reason);
		}
		}
		
	});
	
}

function insertCategory(){
	
	if($jq('#categoryNm').val() == undefined || $jq('#categoryNm').val() == ""){
	
 		alert("카테고리 명을 입력하세요.");
		return;
		
	}
	
	if($jq("input[name='location']:checked").val() == undefined || $jq("input[name='location']:checked").val() == ""){
	
		alert("저장위치를 선택해주세요");
		return;
		
	};
categroy.categoryId.value=$jq('#categoryId').val();
categroy.categoryNm.value=$jq('#categoryNm').val();
categroy.type.value=$jq("input[name='location']:checked").val();
	$jq.ajax({
		url: '<@spring.url "/admin/category/insertCategory.ssc" />',
		type: 'POST',
		dataType: 'json',
		data : $jq('#categroy').serialize(),
		error: function(){
			console.log("저장에 실패햇습니다");
		},
		success: function(data){
		if(data.result == "Y"){
			$jq('#category_tree').empty();
		
			ids = new Array();
			
			treeSearch.depth.value = 0;
			treeSearch.categoryId.value = '';
			treeSearch.preParent.value = '';
	
			categroy.categoryNm.value='';
			
			$jq('#categoryNm').val('');
			alert("저장되었습니다.");
			categroyTreeSearch(0,'','');
			
		}else{
			alert(data.reason);
		}
		}
		
	});

}	

function updateCategory(){

if($jq('#categoryNm2').val()!= ""){

	categroy.categoryId.value = $jq('#categoryId').val()
	categroy.categoryNm.value = $jq('#categoryNm2').val();
	categroy.orderNum.value = 0;
$jq.ajax({
		url: '<@spring.url "/admin/category/updateCategory.ssc" />',
		type: 'POST',
		dataType: 'json',
		data : $jq('#categroy').serialize(),
		error: function(){
			alert("저장에 실패햇습니다");
		},
		success: function(data){
		
			$jq('#category_tree').empty();
			
			ids = new Array();
			
			treeSearch.depth.value = 0;
			treeSearch.categoryId.value = '';
			treeSearch.preParent.value = '';
			
			$jq('#categoryNm2').val('');
			alert("수정되었습니다.");
			categroyTreeSearch(0,'','');
			
		}
		
	});
	
	}else{
	
		alert("수정 회차명을 넣어주십시오");
		
	}
	
}


function deleteCategory(){

categroy.categoryId.value = $jq('#categoryId').val();
if($jq('#categoryId').val() == ""){
alert("선택된 카테고리가 없습니다.");
return false;
}
$jq.ajax({
		url: '<@spring.url "/admin/category/deleteCategory.ssc" />',
		type: 'POST',
		dataType: 'json',
		//data: {'categoryId' : $jq('#categoryId').val() },
		data : $jq('#categroy').serialize(),
		error: function(e){
			alert("삭제 실패햇습니다   "+e);
		},
		success: function(data){
		
			if(data.result == "Y"){
				alert("삭제 성공하였습니다");
				$jq('#category_tree').empty();
				
				ids = new Array();
				
				treeSearch.depth.value = 0;
				treeSearch.categoryId.value = '';
				treeSearch.preParent.value = '';
			
				categroyTreeSearch(0,'','');
				
				$jq('#categoryNm').val('');
				$jq('#categoryNm2').val('');
				
			}else{
			
				alert("삭제 실패  : " +data.reason);
				
			}
			
		}
		
	});

}

$jq('.control').click(function(){

	var temp_el = $jq(this).parent().find('>ul');
	
	if (temp_el.css('display') == 'none'){
	
		temp_el.slideDown(100);
		$jq(this).find('img').attr('src', icon_close);	
		
	}else{
	
		temp_el.slideUp(100);
		$jq(this).find('img').attr('src', icon_open);
		
	}
	
});

function initValue(){

	$jq('#categoryNm').val('');
	$jq('#categoryNm2').val('');
	$jq('#categoryId').val('');
	$jq('.cateNm').val('');
	
}


function changePosition(direction){

var cateId = $jq('#categoryId').val();
var parentId = $jq('#'+cateId).parents().attr('id')
var grandParentId = $jq('#'+parentId).parents().attr('id')
console.log('######################cateId  ' +cateId);
console.log('######################parentId ' +parentId);
console.log('######################grandParentId ' +grandParentId);
	if($jq('#categoryId').val() == 0){
		alert('카테고리를 선택해 주세요');
		return false;
	}else{
	
		categroy.categoryId.value =$jq('#categoryId').val();
		categroy.direction.value =direction;
		if(direction == 'up'){
		
			$jq.ajax({
		url: '<@spring.url "/admin/category/updateCategory.ssc" />',
		type: 'POST',
		dataType: 'json',
		data : $jq('#categroy').serialize(),
		error: function(e){
			alert("위치변경에 실패햇습니다   "+e);
		},
		success: function(data){
		
			if(data.result == "Y"){
				
				if(grandParentId == 'category_tree'){//부모 카테고리가 최상위 노드라면 전체를 리로드
				$jq('#'+parentId).remove();
				treeSearch.depth.value = 0;
				treeSearch.categoryId.value = '';
				treeSearch.preParent.value = '';
				
				categroyTreeSearch(0,'','');
				
				}else{//부모카테고리가 root으로 시작하는 sub node라면 해당부분을 삭제후 그부분만 리로드
				
					$jq('#'+grandParentId).remove();
					
					showSubTree(data.info.categoryId,data.info.depth+1,data.info.categoryId+'_'+data.info.depth);
					
				
				}
					//전역 변수에 bold로 유지시킬 id 입력
					bold = cateId;
			
					for(i = 0; i < ids.length ; i++){
								
									//if(ids[i] == grandParentId){
									
									ids[i] = null;
										
								//	}	   
									
					
						}
					
			}else{
			
				alert("위치변경 실패  : " + data.info );
				
			}
			
		}
		
	});
		}else{
			
			$jq.ajax({
		url: '<@spring.url "/admin/category/updateCategory.ssc" />',
		type: 'POST',
		dataType: 'json',
		data : $jq('#categroy').serialize(),
		error: function(e){
			alert("위치변경에 실패햇습니다   "+e);
		},
		success: function(data){
		
			if(data.result == "Y"){
				
				if(grandParentId == 'category_tree'){//부모 카테고리가 최상위 노드라면 전체를 리로드
					console.log('##########grandParentId    :  '+grandParentId);
					$jq('#'+parentId).remove();
					treeSearch.depth.value = 0;
					treeSearch.categoryId.value = '';
					treeSearch.preParent.value = '';
					
					categroyTreeSearch(0,'','');
					//$jq('#'+grandParentId).remove();
					
				}else{//부모카테고리가 root으로 시작하는 sub node라면 해당부분을 삭제후 그부분만 리로드
					
					
					
					$jq('#'+grandParentId).remove();
					showSubTree(data.info.categoryId,data.info.depth+1,data.info.categoryId+'_'+data.info.depth);
					
					
				}
				for(i = 0; i < ids.length ; i++){
								
						//if(ids[i] == grandParentId){
									
							ids[i] = null;
										
							//}	     
					
						}
				//전역 변수에 bold로 유지시킬 id 입력
					bold = cateId;
					
				
			}else{
			
				alert("위치변경 실패  : " + data.info );
				
			}
		}
		
	});
		}
	}

}
</script>

<form name="treeSearch" id="treeSearch" method="post" >
	<@spring.bind "search" />
	<input type="hidden" name="depth"  />
	<input type="hidden" name="categoryId"  />
	<input type="hidden" name="preParent"  />
</form>


<form name="categroy" id="categroy" method="post" >
	<@spring.bind "newCategory" />
	
	<input type="hidden" name="categoryNm"  />
	<input type="hidden" name="categoryId"  />
	<input type="hidden" name="type"  />
	<input type="hidden" name="orderNum"  />
	<input type="hidden" name="direction"  />
</form>



<div class="left_cate"> <!-- left시작 -->
	<div class="boardcover2" >
  	<ul id ="treeStart">
  	<li><a id="tree0" onclick="changeBold('tree0');initValue();">전체</a><ul id="category_tree"></ul></li></ul>
  
</div>
	<div id="imgBox">
  	<a href="#" onclick="changePosition('up');return false;"><img src="<@spring.url "/images/updirection.png"/>"></a>
  	<a href="#" onclick="changePosition('down');return false;"><img src="<@spring.url "/images/downdirection.png"/>"></a>
  	
  	</div>
    <div class="btncover7">
           <!--<a href="#layer1" onClick="showHide('layer1');return false;"><img src="<@spring.url "/images/add_off.gif"/>" id ="1" title="추가" alt="추가" onMouseOver="this.src='<@spring.url "/images/add_on.gif"/>'" onMouseOut="this.src='<@spring.url "/images/add_off.gif"/>'"></a>-->
           <!--<a href="#layer3" onClick="showHide('layer3');return false;"><img src="<@spring.url "/images/modify_off.gif"/>" id ="3" title="수정" alt="수정" onMouseOver="this.src='<@spring.url "/images/modify_on.gif"/>'" onMouseOut="this.src='<@spring.url "/images/modify_off.gif"/>'"></a>-->
           <!--<a onClick="deleteCategory();"><img src="<@spring.url "/images/del_off.gif"/>" title="삭제" alt="삭제" onMouseOver="this.src='<@spring.url "/images/del_on.gif"/>'" onMouseOut="this.src='<@spring.url "/images/del_off.gif"/>'"></a>-->
           
           <!--권한별 버튼-->
           <!--'6'  menuId-->
           <@button1 '6' '${user.userId}' "showHide('layer1')" '추가' 'categoryInsertButton'/>
           <@button1 '6' '${user.userId}' "showHide('layer3')" '수정' 'categoryUpdateButton'/>
           <@button1 '6' '${user.userId}' 'deleteCategory()' '삭제' 'categoryDeleteButton'/>
           
            <!--<a href=""><img src="/images/finalnode_off.gif" title="최종노드" alt="최종노드" onMouseOver="this.src='/images/finalnode_on.gif'" onMouseOut="this.src='/images/finalnode_off.gif'"></a> -->
                <!-- Popup 저장용-->
                <div id="layer1" class="box3" onblur="showHide('layer1');return false;">
                    <ul id="cateul">
                   		<ul>
                        <li id="cateli1">상위카테고리</li>
                         <li><input id="cateNm" class="cateNm" name="categoryNm" readonly="readonly" type="text" style="width:180px" value="전체"></li>
                        </ul>
                        <ul>
                        <li id="cateli2" style="margin-right : 11px;">카테고리명</li>
                        <li><input id="categoryNm" name="categoryNm" type="text" style="width:180px" value="" onkeypress="if(event.keyCode==13){insertCategory();showHide('layer1');return false;}"></li>
                         </ul>
                          <ul>
                        <li id="cateli2" style="margin-right : 11px;">위치지정</li>
                        <li id="li" style="margin-left: 10px">
                        	&nbsp;하위노드&nbsp;<input id="sub" name="location" type="radio"  value="SUB"/>
                        </li>
                        <li id="li">
                        	&nbsp;다음노드<input id="next" name="location" type="radio"  value="NEXT"/>
                        </li>
                         </ul>
                         <li class="cateogyId" id="categoryId" value="0"></li>
                    </ul>
                    <div class="btncover9"><span class="btn_pack gry"><a id="insertCategory" onClick="insertCategory();showHide('layer1');return false;">저장</a></span>
                    <span class="btn_pack gry"><a href="#layer1" onClick="showHide('layer1');return false;">닫기</a></span>
                    </div>
                </div>
                <!-- //Popup -->
                
                 <!-- Popup 수정용-->
                <div id="layer3" class="box5">
                    <ul id="cateul1">
                   		<ul>
                        <li id="cateli1" >수정전 카테고리</li>
                         <li><input id="cateNm" class="cateNm" name="categoryNm" readonly="readonly" type="text" style="width:180px" value="전체"></li>
                        </ul>
                        <ul>
                        <li id="cateul2">수정후 카테고리</li>
                        <li><input id="categoryNm2" name="categoryNm" type="text" style="width:180px" onkeypress="if(event.keyCode==13){updateCategory();showHide('layer3');return false;}"></li>
                         </ul>
                         <li class="cateogyId" id="categoryId" value="0"></li>
                    </ul>
                    <div class="btncover11"><span class="btn_pack gry"><a id="updateCategory" onClick="updateCategory();showHide('layer3');return false;">수정</a></span>
                    <span class="btn_pack gry"><a href="#layer3" onClick="showHide('layer3');return false;">닫기</a></span>
                    </div>
                </div>
                <!-- //Popup -->
        </div>
        </div>
<!-- left 끝 -->