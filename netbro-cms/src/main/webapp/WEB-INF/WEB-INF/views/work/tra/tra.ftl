<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">
<script type="text/javascript">

String.prototype.string = function (len) {
    var s = '',
        i = 0;
    while(i++ < len) {
        s += this;
    }
    return s;
};
String.prototype.zf = function (len) {
    return "0".string(len - this.length) + this;
};
Number.prototype.zf = function (len) {
    return this.toString().zf(len);
};

window.onload = function () {
    findTraList(0);
    findTraListPrgrs();

}

function refreshTime() {
    //window.setTimeout("findTraList(traInfo.pageNo.value)", 1000 * 5 );
}

function refreshTime2() {
    //window.setTimeout("findTraListPrgrs()", 1000 * 1 );
}

function qcr(id) {

    var newId = id.replace(" ", "");
    var style = $jq('#' + newId).attr("style");
    var x;
    var y;

    x = window.event.clientY - 101;
    //x=0;
    y = -298;

    $jq('#' + newId).attr("style", " posion : absolute; z-index: 99999;display : block; margin-top : " + x + "px; margin-left:" + y + "px;")
    $jq('#' + showLayer).attr("style", "");
    showLayer = newId;
}

//진행률만 따로 체크.
function findTraListPrgrs() {

    if(traInfo.checkTra.value != "") {

        $jq.ajax({
            url: '<@spring.url "/tra/findTraList.ssc" />',
            type: 'GET',
            dataType: 'json',
            data: $jq('#traInfo').serialize(),
            
            success: function (data) {

                for(var i = 0; i < data.traTbls.length; i++) {
                    var table = "";
                    $jq('#' + data.traTbls[i].seq).empty();
                    table += '<article class="ly_loading2"><p class="dsc_loading_no"><span class="progress">'
                    table += '<em>' + isEmpty(data.traTbls[i].prgrs) + '</em> %</span></p><article class="loading_bar">'
                    table += '<span style="width:' + isProgress(data.traTbls[i].prgrs) + '%;"></span></article></article>'
                    $jq('#' + data.traTbls[i].seq).append(table);

                }
            }
        });
    }
    refreshTime2();
}

function findTraList(pageNum) {

    var pageView = 5;
    var preKeyWord = "";

    traInfo.pageNo.value = pageNum;
    traInfo.selectContentNm.value = $jq('#selectContentNm').val();

    if(traInfo.selectContentNm.value == "undefined") {
        traInfo.selectContentNm.value = "";
    }

    if(pageNum == "all") {
        pageNum = 0;
        traInfo.pageNo.value = 0;
        traInfo.selectContentNm.value = "";
        traInfo.firstSelectBox.value = "";
        traInfo.secondSelectBox.value = "";
    }

    if(pageNum != 1 && preKeyWord != $jq('#selectContentNm').val()) {
        traInfo.selectContentNm.value = $jq('#selectContentNm').val();
    } else if(pageNum == 0) {
        traInfo.selectContentNm.value = $jq('#selectContentNm').val();
    } else {
        traInfo.selectContentNm.value = "";
        $jq('#selectContentNm').val('');
    }
    preKeyWord = $jq('#searchKey').val();

    $jq.ajax({
        url: '<@spring.url "/tra/findTraList.ssc" />',
        type: 'POST',
        dataType: 'json',
        data: $jq('#traInfo').serialize(),
        
        success: function (data) {

            $jq('#traList').empty();

            var table = "";
            var seq = "";
            var checkTra = "";

            table += '<table summary="" class="board1">'
            table += '<colgroup><col width="50px"/><col width="180px"/><col width="180px"/><col width="100px"/><col width="100px"/><col width="100px"/><col width="80px"/></colgroup>'
            table += '<tbody>'
            table += '<tr>'
            table += '<th>장비ID</th><th>컨텐츠명</th><th>카테고리명</th><th>등록일</th><th>시작시간</th><th>종료시간</th><th>상태</th><th>진행률</th>'
            table += '</tr>'
            for(var i = 0; i < data.traTbls.length; i++) {

                var regDt = new Date(parseInt(data.traTbls[i].regDt, 10));
                var reqDt = new Date(parseInt(data.traTbls[i].reqDt, 10));
                var tmpJobStatus = "";

                if(i % 2 == 0) {
                    table += '<tr>'
                } else {
                    table += '<tr class="bgc2">';
                }
                table += '<td><title="' + data.traTbls[i].ctId + '">' + data.traTbls[i].seq + '</td>'
                table += '<td style="text-align: left;"><a href="javascript:" style="CURSOR:hand;" title="' + data.traTbls[i].ctId + '"  onclick=qcr("layer1")>&nbsp;' + data.traTbls[i].ctNm + '</td>'
                //table +='<td style="text-align: left;"><a href="javascript:" style="CURSOR:hand;" title="'+data.traTbls[i].ctId+'"  onclick=qcr();>&nbsp;'+data.traTbls[i].ctNm+'</td>'
                table += '<td style="text-align: left;"><title="' + data.traTbls[i].ctId + '">&nbsp;' + data.traTbls[i].categoryNm + '</td>'
                table += '<td><title="' + data.traTbls[i].ctId + '">' + regDt.format("yyyy-MM-dd") + '</td>'
                table += '<td><title="' + data.traTbls[i].ctId + '">' + reqDt.format("yyyy-MM-dd") + '</td>'
                table += '<td><title="' + data.traTbls[i].ctId + '">' + regDt.format("yyyy-MM-dd") + '</td>'
                if(data.traTbls[i].jobStatus == "C") {
                    tmpJobStatus = "완료"
                } else {
                    tmpJobStatus = "진행중"
                }
                table += '<td><title="' + data.traTbls[i].ctId + '">' + tmpJobStatus + '</td></td>'
                table += '<td id="' + data.traTbls[i].seq + '"><article class="ly_loading2"><p class="dsc_loading_no"><span class="progress">'
                table += '<em>' + isEmpty(data.traTbls[i].prgrs) + '</em> %</span></p><article class="loading_bar">'
                table += '<span style="width:' + isProgress(data.traTbls[i].prgrs) + '%;"></span></article></article></td>'
                table += '</tr>'

                if(data.traTbls[i].prgrs != 100) {
                    if(checkTra == "") {
                        checkTra = data.traTbls[i].seq;
                    } else {
                        checkTra = checkTra + "," + data.traTbls[i].seq;
                    }

                }

                traInfo.checkTra.value = checkTra;

            }

            table += '</tbody>'
            table += '</table>'
            table += '</div>'

            $jq('#traList').append(table);

            $jq('#paging').empty();

            paging += '<article class="paginate">'
            paging += '<a class="direction " href="#" >'
            //table +='<span>?</span> 이전</a>'

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
            var pages = Math.ceil(data.totalCount / data.pageSize);

            //총 페이지bar수를 구한다.
            var totalPageBar = Math.ceil(pages / pageView);

            //마지막 페이지 bar 여부를 판단한다. pageView값과 비교해 같거나 작다면 마지막 페이지 bar 이다.
            var lastNum = "";

            var lastPageBarYn = "";

            if(pageNum % pageView != 0) {
                lastPageBarYn = Math.ceil(pageNum / pageView);
            } else {
                lastPageBarYn = Math.ceil(pageNum / pageView) + 1;
            }

            if(pageNum < pageView && (pageNum / pageView) < 1 && pages <= pageView) { //최초 페이지bar화면이라면 기본값 셋팅
                currentMaxPaging = pages;
                startPage = 0;
            } else {
                //마지막 페이지 bar이라면 총페이지를 마지막 페이지로 설정한다.
                if(lastPageBarYn == totalPageBar || pages < (currentMaxPaging * (Math.ceil(pageNum / pageView))) || pages == (pageNum + 1)) {

                    currentMaxPaging = pages;
                    if((pages % pageView) == 0) {
                        startPage = pages - (pageView);
                    } else {
                        startPage = pages - (pages % pageView);
                    }

                    maxPaging = currentMaxPaging * (Math.ceil(pageNum / pageView));
                } else {
                    if((pageNum % pageView) != 0) { //현재페이지와 설정pageView의 나머지가 0이 아니라면...

                        currentMaxPaging = currentMaxPaging * (Math.ceil(pageNum / pageView));
                        maxPaging = currentMaxPaging * (Math.ceil(pageNum / pageView) - 1);
                    } else { //현재페이지와 설정pageView 나머지가 0이라면 ...
                        currentMaxPaging = currentMaxPaging * (Math.floor(pageNum / pageView) + 1);
                        maxPaging = currentMaxPaging * (Math.floor(pageNum / pageView));
                    }
                    startPage = currentMaxPaging - pageView;
                }

            }

            if(pageView > pageNum || pageView == pages || pageView > pages) {

            } else {
                paging += '<a class="direction" href="#" onClick="findTraList(' + (startPage - pageView) + ');return false;">';
                paging += '<span>‹</span>이전</a>';
            }

            for(var page = startPage; page < currentMaxPaging; page++) {
                if(pageNum == page) {
                    paging += '<strong>' + (page + 1) + '</strong>';
                } else {
                    paging += '<a href="#" onClick="findTraList(' + (page) + ')">' + (page + 1) + '</a>';
                }
            }

            if(lastPageBarYn == totalPageBar || (pageNum + 1) == pages || (pages - currentMaxPaging) <= 0) {

            } else if(pages != pageView) {
                paging += '<a class="direction" href="#" onClick="findTraList(' + (currentMaxPaging) + ');return false;">다음 <span>›</span></a>';
            } else if(pageView < data.totalCount / data.pageSize) {
                paging += '<a class="direction" href="#" onClick="findTraList(' + (currentMaxPaging) + ');return false;">다음 <span>›</span></a>';
            }
            //table +='<a class="direction" href="#">다음 <span>›</span></a>'

            paging += '</article>'

            $jq('#paging').append(paging);

        }
    });

    refreshTime();

}

function findTrsList(pageNum) {

    var pageView = 5;
    var preKeyWord = "";

    traInfo.pageNo.value = pageNum;
    traInfo.selectContentNm.value = $jq('#selectContentNm').val();

    if(traInfo.selectContentNm.value == "undefined") {
        traInfo.selectContentNm.value = "";
    }

    if(pageNum == "all") {
        pageNum = 0;
        traInfo.pageNo.value = 0;
        traInfo.selectContentNm.value = "";
        traInfo.firstSelectBox.value = "";
        traInfo.secondSelectBox.value = "";
    }

    if(pageNum != 1 && preKeyWord != $jq('#selectContentNm').val()) {
        traInfo.selectContentNm.value = $jq('#selectContentNm').val();
    } else if(pageNum == 0) {
        traInfo.selectContentNm.value = $jq('#selectContentNm').val();
    } else {
        traInfo.selectContentNm.value = "";
        $jq('#selectContentNm').val('');
    }
    preKeyWord = $jq('#searchKey').val();

    $jq.ajax({
        url: '<@spring.url "/tra/findTraList.ssc" />',
        type: 'POST',
        dataType: 'json',
        data: $jq('#traInfo').serialize(),
        
        success: function (data) {

            $jq('#trsList').empty();

            var table = "";

            table += '<colgroup><col width="50px"/><col width="180px"/><col width="180px"/><col width="100px"/><col width="100px"/><col width="100px"/><col width="80px"/></colgroup>'
            table += '<tbody>'
            table += '<tr>'
            table += '<th>장비ID</th><th>컨텐츠명</th><th>카테고리명</th><th>등록일</th><th>시작시간</th><th>종료시간</th><th>상태</th><th>진행률</th>'
            table += '</tr>'
            for(var i = 0; i < data.traTbls.length; i++) {

                var regDt = new Date(parseInt(data.traTbls[i].regDt, 10));
                var reqDt = new Date(parseInt(data.traTbls[i].reqDt, 10));
                var tmpJobStatus = "";

                if(i % 2 == 0) {
                    table += '<tr>'
                } else {
                    table += '<tr class="bgc2">';
                }
                table += '<td><a href="javascript:" style="CURSOR:hand;" title="' + data.traTbls[i].ctId + '">' + data.traTbls[i].seq + '</td>'
                table += '<td style="text-align: left;"><a href="javascript:" style="CURSOR:hand;" title="' + data.traTbls[i].ctId + '"  onclick=showHide("layer1")>&nbsp;' + data.traTbls[i].ctNm + '</td>'
                table += '<td style="text-align: left;"><a href="javascript:" style="CURSOR:hand;" title="' + data.traTbls[i].ctId + '" >&nbsp;' + data.traTbls[i].categoryNm + '</td>'
                table += '<td><a href="javascript:" style="CURSOR:hand;" title="' + data.traTbls[i].ctId + '"  onclick=showHide("layer1")>' + regDt.format("yyyy-MM-dd") + '</td>'
                table += '<td><a href="javascript:" style="CURSOR:hand;" title="' + data.traTbls[i].ctId + '"  onclick=showHide("layer1")>' + reqDt.format("yyyy-MM-dd") + '</td>'
                table += '<td><a href="javascript:" style="CURSOR:hand;" title="' + data.traTbls[i].ctId + '"  onclick=showHide("layer1")>' + regDt.format("yyyy-MM-dd") + '</td>'
                if(data.traTbls[i].jobStatus == "C") {
                    tmpJobStatus = "완료"
                } else {
                    tmpJobStatus = "진행중"
                }
                table += '<td><a href="javascript:" style="CURSOR:hand;" title="' + data.traTbls[i].ctId + '"  onclick=showHide("layer1")>' + tmpJobStatus + '</td></td>'
                table += '<td><article class="ly_loading2"><p class="dsc_loading_no"><span class="progress">'
                table += '<em>' + isEmpty(data.traTbls[i].prgrs) + '</em> %</span></p><article class="loading_bar">'
                table += '<span style="width:' + isProgress(data.traTbls[i].prgrs) + '%;"></span></article></article></td>'
                table += '</tr>'
            }

            table += '</tbody>'
            table += '</table>'
            table += '</div>'

            $jq('#trsList').append(table);

            $jq('#paging').empty();

            paging += '<article class="paginate">'
            paging += '<a class="direction " href="#" >'
            //table +='<span>?</span> 이전</a>'

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
            var pages = Math.ceil(data.totalCount / data.pageSize);

            //총 페이지bar수를 구한다.
            var totalPageBar = Math.ceil(pages / pageView);

            //마지막 페이지 bar 여부를 판단한다. pageView값과 비교해 같거나 작다면 마지막 페이지 bar 이다.
            var lastNum = "";

            var lastPageBarYn = "";

            if(pageNum % pageView != 0) {
                lastPageBarYn = Math.ceil(pageNum / pageView);
            } else {
                lastPageBarYn = Math.ceil(pageNum / pageView) + 1;
            }

            if(pageNum < pageView && (pageNum / pageView) < 1 && pages <= pageView) { //최초 페이지bar화면이라면 기본값 셋팅
                currentMaxPaging = pages;
                startPage = 0;
            } else {
                //마지막 페이지 bar이라면 총페이지를 마지막 페이지로 설정한다.
                if(lastPageBarYn == totalPageBar || pages < (currentMaxPaging * (Math.ceil(pageNum / pageView))) || pages == (pageNum + 1)) {

                    currentMaxPaging = pages;
                    if((pages % pageView) == 0) {
                        startPage = pages - (pageView);
                    } else {
                        startPage = pages - (pages % pageView);
                    }

                    maxPaging = currentMaxPaging * (Math.ceil(pageNum / pageView));
                } else {
                    if((pageNum % pageView) != 0) { //현재페이지와 설정pageView의 나머지가 0이 아니라면...

                        currentMaxPaging = currentMaxPaging * (Math.ceil(pageNum / pageView));
                        maxPaging = currentMaxPaging * (Math.ceil(pageNum / pageView) - 1);
                    } else { //현재페이지와 설정pageView 나머지가 0이라면 ...
                        currentMaxPaging = currentMaxPaging * (Math.floor(pageNum / pageView) + 1);
                        maxPaging = currentMaxPaging * (Math.floor(pageNum / pageView));
                    }
                    startPage = currentMaxPaging - pageView;
                }

            }

            if(pageView > pageNum || pageView == pages || pageView > pages) {

            } else {
                paging += '<a class="direction" href="#" onClick="findTrsList(' + (startPage - pageView) + ');return false;">';
                paging += '<span>?</span>이전</a>';
            }

            for(var page = startPage; page < currentMaxPaging; page++) {
                if(pageNum == page) {
                    paging += '<strong>' + (page + 1) + '</strong>';
                } else {
                    paging += '<a href="#" onClick="findTrsList(' + (page) + ')">' + (page + 1) + '</a>';
                }
            }

            if(lastPageBarYn == totalPageBar || (pageNum + 1) == pages || (pages - currentMaxPaging) <= 0) {

            } else if(pages != pageView) {
                paging += '<a class="direction" href="#" onClick="findTrsList(' + (currentMaxPaging) + ');return false;">다음 <span>›</span></a>';
            } else if(pageView < data.totalCount / data.pageSize) {
                paging += '<a class="direction" href="#" onClick="findTrsList(' + (currentMaxPaging) + ');return false;">다음 <span>›</span></a>';
            }
            //table +='<a class="direction" href="#">다음 <span>›</span></a>'

            paging += '</article>'

            $jq('#paging').append(paging);

        }
    });

    refreshTime();

}

function firstSelectBox(val) {

    traInfo.firstSelectBox.value = $jq('#firstSelectBox').val();
    traInfo.selectContentNm.value = $jq('#selectContentNm').val();

    if(traInfo.firstSelectBox.value == "all") {
        pageNum = 0;
        traInfo.pageNo.value = 0;
        traInfo.selectContentNm.value = "";
        traInfo.firstSelectBox.value = "";
        traInfo.secondSelectBox.value = "";
    }

    var table = "";

    var pageView = 5;

    var secondLayer = document.getElementById('secondSelectBox');

    //control.js에 있는 함수.
    //전체, 선택하세요를 제외한 값에는 secondSelectbox를 보여준다.        

    $jq.ajax({
        url: '<@spring.url "/tra/findTraList.ssc" />',
        type: 'POST',
        dataType: 'json',
        data: $jq('#traInfo').serialize(),
        
        success: function (data) {

            if(val == "category") {
                table += '<option value="">선택하세요</option>';
                for(var i = 0; i < data.categoryTbls.length; i++) {
                    table += '<option value="' + data.categoryTbls[i].categoryId + '">' + data.categoryTbls[i].categoryNm + '</option>';

                }
            } else if(val == "status") {
                table += '<option value="">선택하세요</option>';
                table += '<option value="C">완료</option>';
                table += '<option value="S">진행중</option>';
                table += '<option value="E">에러</option>';
            }
            $jq('#secondSelectBox').empty();
            $jq('#secondSelectBox').append(table);

        }
    });
}

function secondSelectBox(val) {

    traInfo.secondSelectBox.value = $jq('#secondSelectBox').val();
    var pageView = 5;

    $jq.ajax({
        url: '<@spring.url "/tra/findTraList.ssc" />',
        type: 'POST',
        dataType: 'json',
        data: $jq('#traInfo').serialize(),
       
        success: function (data) {

            findTraList(0);
        }
    });
}

function recommandTraObj(seq) {

    traInfo.seq.value = seq;

    $jq.ajax({
        url: '<@spring.url "/tra/recommandTraObj.ssc" />',
        type: 'POST',
        dataType: 'json',
        data: $jq('#traInfo').serialize(),
        
        success: function (data) {

            alert('재요청이 완료되었습니다.');
            findTraList(0);

        }
    });
}

function isProgress(value) {
    return(value === undefined || value == null || value.length <= 0) ? 0 : value;
}

function isEmpty(value) {
    if(typeof (value) == 'number') {
        return(value === undefined || value == null || value.length <= 0) ? 0 : value;
    } else {
        return(value === undefined || value == null || value.length <= 0) ? 0 : value;
    }
}

function test() {
    alert('모두재생성 되었습니다.');
    showHide("layer1");
}

function test1() {
    alert('재생성 되었습니다.');
    showHide("layer1");
}


</script>


<form name="traInfo" id="traInfo" method="post">
    <@spring.bind "search" />
    <@spring.bind "traTbl" />
    <input type="hidden" name="pageNo">
    <input type="hidden" name="ctId">
    <input type="hidden" name="seq">
    <input type="hidden" name="firstSelectBox">
    <input type="hidden" name="secondSelectBox">
    <input type="hidden" name="selectContentNm">
    <input type="hidden" name="checkTra">

</form>


<div class="westpanel" id="westpanel">
    <section id="section4">
        <div id="trabox1">
            <!-- 상세정보시작 -->
            <nav class="tratabcover">
                <div id="layer1" class="pop-layer1" style="margin-left: -298px; margin-top: 44px;">

                    <div class="pop-container">


                        <div class="btn-r">
                            <@traButton '4' '${user.userId}' 'test()' '모두재생성' />
                            <@traButton '4' '${user.userId}' 'test1()' '재생성' />
                            <!--// content-->

                        </div>

                    </div>

                </div>


                <!-- 탭시작 -->
                <ul class="tab1">
                    <li class="on"><a href="#trabox1" onclick="v1h2Tra();return false;"><span>변환정보</span></a>
                    </li>
                    <li class="off"><a href="#trabox2" onclick="v2h1Tra();findTrsList(0);return false;"><span>전송정보</span></a>
                    </li>
                    <select id="firstSelectBox" onChange="firstSelectBox(value)" style="margin-left:667px;height:28px;">
                        <option value="">선택하세요</option>
                        <option value="category">카테코리</option>
                        <option value="status">상태</option>
                    </select>
                    <select id="secondSelectBox" onChange="secondSelectBox(value)" style="height:28px;width:105px;">
                    </select>
                    
                   <!--  <div class="searchbox2" >
            
                <dt>조회기간</dt>
                <dd><input type="text" id="from" style="width : 95px;height: 21px;text-align: center;"> ~ <input type="text" id="to" style="width : 95px;height: 21px;text-align: center;"></dd>
                  
               
                </div>
				-->
                </ul>

            </nav>
            <!-- 탭끝 -->


            <div class="box2" id="traList">
                <table summary="" class="board1">


                </table>
            </div>

            <div class="btncover18">
                <select id="" onChange="changeValue(value)" style="height : 20px;">
                    <option value="">컨텐츠명</option>
                </select>

                <input name="selectContentNm" type="text" id="selectContentNm" onkeypress="if(event.keyCode==13){findTraList(0);}">

                <!--<span class="btn_pack medium" style="margin-left:383px; margin-top:-4px;"><a href="javascript:" onclick="window.location.reload(true);">목차</a></span>-->
            </div>
        </div>
</div>
<!-- 첨부파일끝 -->
<div id="trabox2">
    <!-- 스토리보드시작 -->
    <nav class="tabcover">
        <!-- 탭시작 -->
        <ul class="tab1">
            <li class="off"><a href="#trabox1" onclick="v1h2Tra();findTraList(0);return false;"><span>변환정보</span></a>
            </li>
            <li class="on"><a href="#trabox2" onclick="v2h1Tra();return false;"><span>전송정보</span></a>
            </li>
        </ul>
    </nav>
    <!-- 탭끝 -->

    <table summary="" class="board1" id="trsList">

    </table>


</div>
<!-- 스토리보드끝 -->
</section>
</div>
<article id="paging" class="paginate" style="margin-top:-95px" ;>

</article>

<article id="paging1" class="paginate" style="margin-top:-95px" ;>

</article>


<!-- Popup start -->

<!-- //Popup end -->
