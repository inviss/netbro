<#include "/WEB-INF/views/includes/commonTags.ftl">
<#include "/WEB-INF/views/includes/macro_fomat.ftl">

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript" src="<@spring.url '/js/highcharts.js'/>"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>
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
    findMonitoringList(0);
    getHighStorage();
    getLowStorage();
}

var newShowLayer="";

function qcr(id) {

    var newId = id.replace(" ", "");
    var style = $jq('#' + newId).css("display");
    var x;
    var y;

    var table;
    console.log(newShowLayer);

    x = window.event.clientY - 312;
    y = -123;
    if((newShowLayer == newId && style == "") || (newShowLayer == newId && style == undefined)) {

        $jq('#' + newId).attr("style", " posion : absolute; z-index: 99999;display : block; margin-top : " + x + "px; margin-left:" + y + "px;")

    } else if(newShowLayer == newId || (newShowLayer == newId + 'temp' && style == "none")) {

        var layer = document.getElementById(newId);
        layer.style.display = 'none';
        newShowLayer = null;

    } else if(newShowLayer != newId) {

        $jq('#' + newId).attr("style", " posion : absolute; z-index: 99999;display : block; margin-top : " + x + "px; margin-left:" + y + "px;")
        $jq('#' + newShowLayer).attr("style", "")
        newShowLayer = newId;

    }

    $jq.ajax({
        url: '<@spring.url "/admin/monitoring/findStorageMonitoringGraph.ssc" />',
        type: 'GET',
        dataType: 'json',
        data: $jq('#monitoringInfo').serialize(),

        success: function (data) {

            if(id == "layer9") {
                $jq('#layer9').empty();
            } else {
                $jq('#layer8').empty();
            }

            table = "";
            table += '<div class="pop-container">'
            if(id == "layer9") {
                table += '<tspan x="120" style="margin-left:34px;font-size:18px;">HighStorage</tspan>'
                table += '<br></br>'
            } else {
                table += '<tspan x="120" style="margin-left:34px;font-size:18px;">LowStorage</tspan>'
                table += '<br></br>'
            }
            table += '<ul>'
            table += '<li id="cateli1" style="margin-left:-9px;">Total:</li>'
            if(id == "layer9") {
                table += '<li><input id="total" class="total" name="total" readonly="readonly" type="text" style="width:90px;margin-left:-2px;" value="' + Math.round((data.highStorageTbl.totalVolume * data.highStorageTbl.highLimit) / 100) + ' KB"></li></ul>'
            } else {
                table += '<li><input id="total" class="total" name="total" readonly="readonly" type="text" style="width:90px;margin-left:-2px;" value="' + Math.round((data.lowStorageTbl.totalVolume * data.lowStorageTbl.lowLimit) / 100) + ' KB"></li></ul>'
            }
            table += '<ul>'
            table += '<li id="cateli1" style="margin-left:-9px;">Use:</li>'
            table += '<li>'
            if(id == "layer9") {
                table += '<li><input id="use" class="use" name="use" readonly="readonly" type="text" style="width:90px;margin-left:4px;" value="' + data.highStorageTbl.useVolume + ' KB"></li></ul>'
            } else {
                table += '<li><input id="use" class="use" name="use" readonly="readonly" type="text" style="width:90px;margin-left:4px;" value="' + data.lowStorageTbl.useVolume + ' KB"></li></ul>'
            }
            table += '</li>'
            table += '</ul>'
            table += '<ul>'
            table += '<li id="cateli1" style="margin-left:-9px;">Free:</li>'
            table += '<li>'
            if(id == "layer9") {
                table += '<li><input id="free" class="free" name="free" readonly="readonly" type="text" style="width:90px;margin-left:0px;" value="' + Math.round(((data.highStorageTbl.totalVolume * data.highStorageTbl.highLimit) / 100) - data.highStorageTbl.useVolume) + ' KB"></li></ul>'
            } else {
                table += '<li><input id="free" class="free" name="free" readonly="readonly" type="text" style="width:90px;margin-left:0px;" value="' + Math.round(((data.lowStorageTbl.totalVolume * data.lowStorageTbl.lowLimit) / 100) - data.lowStorageTbl.useVolume) + ' KB"></li></ul>'
            }
            table += '</li>'
            table += '</ul>'
            table += '<ul>'
            table += '<li id="cateli1" style="margin-left:-9px;">Limit:</li>'
            table += '<li>'

            if(id == "layer9") {
                table += '<input id="highLimit" class="highLimit" name="highLimit" type="text" style="width:90px" value="' + data.highStorageTbl.highLimit + '">'
            } else {
                table += '<input id="lowLimit" class="lowLimit" name="lowLimit" type="text" style="width:90px" value="' + data.lowStorageTbl.lowLimit + '">'
            }
            table += '</li>'
            table += '</li>'
            table += '</ul>'
            table += '<div class="btn-r" style=" margin-top:45px;">'
            if(id == "layer9") {
                table +=<@ajaxMonitoringButton "11" "${user.userId}" "saveStorageLimit(1)" "저장" />
            }else{
                table +=<@ajaxMonitoringButton "11" "${user.userId}" "saveStorageLimit(2)" "저장" />
            }
            table += '</div>'
            table += '</div>'
            table += '</div>'
            if(id == "layer9") {
                $jq('#layer9').append(table);
            } else {
                $jq('#layer8').append(table);
            }
        }
    });
}

function findMonitoringList(pageNum) {

    var pageView = 5;
    monitoringInfo.pageNo.value = pageNum;

    $jq.ajax({
        url: '<@spring.url "/admin/monitoring/findMonitoringList.ssc" />',
        type: 'GET',
        dataType: 'json',
        data: $jq('#monitoringInfo').serialize(),
        
        success: function (data) {
        
        if(data.result == "Y"){
            $jq('#monitoringList').empty();
    
                var table = "";
                var tmpWorkStatCd = "";
    
                table += '<div id="container">'
                table += '<div class="monitoringListcover" style="margin-bottom:96px;margin-top:80px;">'
                table += '<table summary="" class="board1">'
                table += '<colgroup><col width="160px"/><col width=""/><col width="120px"/><col width="120px"/></colgroup>'
                table += '<tbody>'
                table += '<tr>'
                table += '<th>장비 구분</th><th>장비IP</th><th>장비명</th><th>장비상태</th><th>작업 컨텐츠명</th><th>진행률</th>'
                table += '</tr>'
    
                for(var i = 0; i < data.monitorings.length; i++) {
    
                    var regDt = new Date(parseInt(data.monitorings[i].regDt, 10));
    
                    if(i % 2 == 0) {
                        table += '<tr>'
                    } else {
                        table += '<tr class="bgc2">';
                    }
                    if(data.monitorings[i].workStatCd == "01") {
                        tmpWorkStatCd = "완료";
                    } else {
                        tmpWorkStatCd = "진행중";
                    }
                    table += '<td>' + data.monitorings[i].deviceClfCd + '</td><td>' + data.monitorings[i].deviceIp + '</td><td>' + data.monitorings[i].deviceNm + '</td><td>' + tmpWorkStatCd + '</td><td>' + BlankCheck(data.monitorings[i].ctNm) + '</td><td>' + BlankCheck(data.monitorings[i].prgrs) + '</td>'
                    table += '</tr>'
                }
    
                table += '</tbody>'
                table += '</table>'
    
                table += '</div>'
                table += '</div>'
    
                $jq('#monitoringList').append(table);
        }else{
            alert(data.reason);
        }

        }
    });
}

function saveStorageLimit(check) {

    if(check == 1) {
        monitoringInfo.highLimit.value = $jq('#highLimit').val();
        if(monitoringInfo.highLimit.value >= 101) {
            alert("임계치 최대값은 100입니다.");
            return false;
        }

        if(monitoringInfo.highLimit.value < 80) {
            alert("임계치 최소값은 80입니다.");
            return false;
        }

    } else {
        monitoringInfo.lowLimit.value = $jq('#lowLimit').val();

        if(monitoringInfo.lowLimit.value >= 101) {
            alert("임계치 최대값은 100입니다.");
            return false;
        }

        if(monitoringInfo.lowLimit.value < 80) {
            alert("임계치 최소값은 80입니다.");
            return false;
        }

    }
    


    $jq.ajax({
        url: '<@spring.url "/admin/monitoring/saveStorageLimit.ssc" />',
        type: 'POST',
        dataType: 'json',
        data: $jq('#monitoringInfo').serialize(),
        
        success: function (data) {

        if(data.result == "Y"){
        alert('저장 되었습니다.');   
        
        newShowLayer = '';
       
            if(check == 1) {
                hide("layer9");
                $jq('#monitoringGraph2').empty();
                getHighStorage();
            } else {
                hide("layer8"); 
                $jq('#monitoringGraph1').empty();
                getLowStorage();
            }
        }else{
            alert(data.reason);
        }
           
        }
    });
}


//cpu
$(function () {

    var cpu;

    $(document).ready(function () {
        Highcharts.setOptions({
            global: {
                useUTC: false
            }
        });

        var chart;
        $('#monitoringGraph4').highcharts({
            chart: {
                type: 'spline',
                animation: Highcharts.svg, // don't animate in old IE
                marginRight: 10,
                events: {
                    load: function () {

                        // set up the updating of the chart each second
                        var series = this.series[0];
                        setInterval(function () {

                            $jq.ajax({
                                url: '<@spring.url "/admin/monitoring/findCpuMemMonitoringGraph.ssc" />',
                                type: 'GET',
                                dataType: 'json',
                                data: $jq('#monitoringInfo').serialize(),
                                error : {},
                                success: function (data) {
                                    cpu = data.cpu.useCpu;
                                }
                            });

                            if(cpu.value == 'undefined') {
                                cpu = 0;
                            }

                            var x = (new Date()).getTime(), // current time

                                y = cpu * 1;

                            series.addPoint([x, y], true, true);
                        }, 1000);
                    }
                }
            },
            title: {
                text: 'CPU'
            },
            xAxis: {
                type: 'datetime',
                tickPixelInterval: 150
            },
            yAxis: {
                title: {
                    text: 'Value'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + this.series.name + '</b><br/>' +
                        Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
                        Highcharts.numberFormat(this.y, 2);
                }
            },
            legend: {
                enabled: false
            },
            exporting: {
                enabled: false
            },
            series: [{
                name: 'CPU',
                data: (function () {
                    // generate an array of random data
                    var data = [],
                        time = (new Date()).getTime(),
                        i;

                    for(i = -5; i <= 0; i++) {
                        data.push({
                            x: time + i * 1000,
                            y: 0
                        });
                    }
                    return data;
                })()
            }]
        });
    });

});

//mem
$(function () {

    var useMem;
    var totalMem;
    var percMem;

    $(document).ready(function () {
        Highcharts.setOptions({
            global: {
                useUTC: false
            }
        });

        var chart;
        $('#monitoringGraph3').highcharts({
            chart: {
                type: 'spline',
                animation: Highcharts.svg, // don't animate in old IE
                marginRight: 10,
                events: {
                    load: function () {
                        // set up the updating of the chart each second
                        var series = this.series[0];
                        setInterval(function () {

                            $jq.ajax({
                                url: '<@spring.url "/admin/monitoring/findCpuMemMonitoringGraph.ssc" />',
                                type: 'GET',
                                dataType: 'json',
                                data: $jq('#monitoringInfo').serialize(),
                                 error : {},
                                success: function (data) {

                                    useMem = data.mem.useMem;
                                    totalMem = data.mem.totalMem;
                                    percMem = Math.round((useMem / totalMem) * 100)

                                }
                            });

                            if(percMem.value == 'undefined') {
                                percMem = 0;
                            }

                            var x = (new Date()).getTime(), // current time
                                y = percMem;
                            series.addPoint([x, y], true, true);
                        }, 1000);
                    }
                }
            },
            title: {
                text: 'MEM'
            },
            xAxis: {
                type: 'datetime',
                tickPixelInterval: 150
            },
            yAxis: {
                title: {
                    text: 'Value'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + this.series.name + '</b><br/>' +
                        Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
                        Highcharts.numberFormat(this.y, 2);
                }
            },
            legend: {
                enabled: false
            },
            exporting: {
                enabled: false
            },
            series: [{
                name: 'MEM',
                data: (function () {
                    // generate an array of random data
                    var data = [],
                        time = (new Date()).getTime(),
                        i;

                    for(i = -5; i <= 0; i++) {
                        data.push({
                            x: time + i * 1000,
                            y: 0
                        });
                    }
                    return data;
                })()
            }]
        });
    });

});



function getHighStorage(){
var chart;
    $(document).ready(function () {
        var options = {
            chart: {
                renderTo: 'monitoringGraph2',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: 'HighStorage'
            },
            tooltip: {
                followTouchMove: false,
                formatter: function () {
                     return '<b>' + this.point.name + '</b>: ' + Math.round(this.percentage*100)/100 + ' %';
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false,
                        color: '#000000',
                        connectorColor: '#000000',
                        formatter: function () {
                            return '<b>' + this.point.name + '</b>: ' + this.percentage + ' %';
                        }

                    },
                    showInLegend: true

                }
            },
            series: [{
                type: 'pie',
                name: '',
                data: []
            }]
        }

        $.getJSON('<@spring.url "/admin/monitoring/findStorageMonitoringGraph.ssc"/>', function (data) {
            options.series[0].data = [
                ["use", data.highStorageTbl.useVolume],
                ["free", (((data.highStorageTbl.totalVolume*data.highStorageTbl.highLimit)/100)-data.highStorageTbl.useVolume)]
            ];
            chart = new Highcharts.Chart(options);
        }).error(function () {
            console.log('error');
        });
    });
}


function getLowStorage(){
var chart;
  $(document).ready(function () {
        var options = {
            chart: {
                renderTo: 'monitoringGraph1',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: 'LowStorage'
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + this.point.name + '</b>: ' + Math.round(this.percentage*100)/100 + ' %';
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false,
                        color: '#000000',
                        connectorColor: '#000000',

                    },
                    showInLegend: true

                }
            },
            series: [{
                type: 'pie',
                name: '',
                data: []
            }]
        }

        $.getJSON('<@spring.url "/admin/monitoring/findStorageMonitoringGraph.ssc"/>', function (data) {
            options.series[0].data = [
                ["use",data.lowStorageTbl.useVolume],
                ["free", (((data.lowStorageTbl.totalVolume*data.lowStorageTbl.lowLimit)/100)-data.lowStorageTbl.useVolume)]
            ];
            chart = new Highcharts.Chart(options);
        }).error(function () {
            console.log('error');
        });
    });
}


        </script>

        <form name="monitoringInfo" id="monitoringInfo" method="post">
            <@spring.bind "search" />
            <@spring.bind "highStorage" />
            <@spring.bind "lowStorage" />
            <input type="hidden" name="pageNo">
            <input type="hidden" name="ctId">
            <input type="hidden" name="seq">
            <input type="hidden" name="highLimit">
            <input type="hidden" name="lowLimit">

        </form>

        <div class="box2">
            <dl>
                <dd class="pdl20">
        <#assign perm = tpl.getAccessRule('${user.userId}', '7')>
        <#assign perm1 = tpl.getAccessRule('${user.userId}', '8')>
        <#assign perm2 = tpl.getAccessRule('${user.userId}', '9')>
        <#assign perm3 = tpl.getAccessRule('${user.userId}', '10')>
        <#assign perm4 = tpl.getAccessRule('${user.userId}', '11')>
        <#assign perm5 = tpl.getAccessRule('${user.userId}', '12')>
        <#assign perm6 = tpl.getAccessRule('${user.userId}', '13')>
        
        <select name="user" style="height:24px;width:130px; margin-left:907px;margin-top:20px;" onChange="linkView(value);">
        <option value="<@spring.url"/admin/monitoring/monitoring.ssc"/>">모니터링 관리</option>
        <#if (perm == 'RW' || perm =='R')>
            <option value="<@spring.url"/admin/category/category.ssc"/>">카테고리 관리</option>
        </#if>
        <#if (perm1 == 'RW' || perm1 =='R')>
            <option value="<@spring.url"/admin/auth/auth.ssc"/>">역할&권한 관리</option>    
        </#if>
        <#if (perm2 == 'RW' || perm2 =='R')>
            <option value="<@spring.url"/admin/user/user.ssc"/>">사용자 관리</option>
        </#if>
        <#if (perm3 == 'RW' || perm3 =='R')>
            <option value="<@spring.url"/admin/code/code.ssc"/>">코드 관리</option>
        </#if>
        <#if (perm4 == 'RW' || perm4 =='R')>
            <option value="<@spring.url"/admin/monitoring/monitoring.ssc"/>">모니터링 관리</option>
        </#if>
        <#if (perm5 == 'RW' || perm5 =='R')>
            <option value="<@spring.url"/admin/equipment/equipment.ssc"/>">장비 관리</option> 
        </#if>      
         <#if (perm6 == 'RW' || perm6 =='R')>
            <option value="<@spring.url"/admin/notice/notice.ssc"/>">공지사항 관리</option> 
        </#if>  
        </select>
                </dd>
            </dl>
        </div>


        <nav class="tratabcover">
            <div class="monitoringGraph" id="monitoringGraph">
                <div id="layer9" class="pop-layer2" style="margin-left: -205px; margin-top: 40px;">

                    <div class="pop-container">
                        <tspan x="120" style="margin-left:34px;font-size:18px;">HighStorage</tspan>
                        <br></br>
                        <ul>
                            <li id="cateli1" style="margin-left:-9px;">Total:</li>
                            <li>
                                <input id="total" class="total" name="total" readonly="readonly" type="text" style="width:90px;margin-left:-2px;" value="">
                            </li>
                        </ul>
                        <ul>
                            <li id="cateli1" style="margin-left:-9px;">Use:</li>
                            <li>
                                <input id="use" class="use" name="use" readonly="readonly" type="text" style="width:90px;margin-left:4px;" value="">
                            </li>
                        </ul>
                        <ul>
                            <li id="cateli1" style="margin-left:-9px;">Free:</li>
                            <li>
                                <input id="free" class="free" name="free" readonly="readonly" type="text" style="width:90px" value="">
                            </li>
                        </ul>
                        <ul>
                            <li id="cateli1" style="margin-left:-9px;">Limit:</li>
                            <li>
                                <input id="highLimit" class="highLimit" name="highLimit" type="text" style="width:90px" value="">
                            </li>
                        </ul>
                        <div class="btn-r" style=" margin-top:45px;">

            
                            <!--// content-->
                        </div>
                    </div>
                </div>

                <div id="layer8" class="pop-layer2" style="margin-left: -205px; margin-top: 40px;">
                     <div class="pop-container">
                        <tspan x="120" style="margin-left:34px;font-size:18px;">LowStorage</tspan>
                        <br></br>
                        <ul>
                            <li id="cateli1" style="margin-left:-9px;">Total:</li>
                            <li>
                                <input id="total" class="total" name="total" readonly="readonly" type="text" style="width:90px;margin-left:-2px;" value="">
                            </li>
                        </ul>
                        <ul>
                            <li id="cateli1" style="margin-left:-9px;">Use:</li>
                            <li>
                                <input id="use" class="use" name="use" readonly="readonly" type="text" style="width:90px;margin-left:4px;" value="">
                            </li>
                        </ul>
                        <ul>
                            <li id="cateli1" style="margin-left:-9px;">Free:</li>
                            <li>
                                <input id="free" class="free" name="free" readonly="readonly" type="text" style="width:90px" value="">
                            </li>
                        </ul>
                        <ul>
                            <li id="cateli1" style="margin-left:-9px;">Limit:</li>
                            <li>
                                <input id="lowLimit" class="lowLimit" name="lowLimit" type="text" style="width:90px">
                            </li>
                        </ul>
                        <div class="btn-r" style=" margin-top:45px;">

                            
                            <!--// content-->
                        </div>
                    </div>
                </div>

                <div id="monitoringGraph1"></div>
                <div id="monitoringGraph2"></div>
                <div id="monitoringGraph3"></div>
                <div id="monitoringGraph4"></div>
                <div id="btn" style="margin-left:736px;margin-top:290px;">
                    <@traButtonId '11' '${user.userId}' "qcr('layer9')" '설정' 'button1'/>
                </div>
                <div id="btn1" style="margin-left:995px;margin-top:-23px;">
                    <@traButtonId '11' '${user.userId}' "qcr('layer8')" '설정' 'button2' />
                </div>
            </div>
        </nav>
        
         
        <select name="user" style="height:24px;width:130px; margin-left:927px;margin-top:20px;visibility  : hidden;" onChange="linkView(value); ">
       
            <option value="<@spring.url"/admin/category/category.ssc"/>">카테고리 관리</option>
        
            <option value="<@spring.url"/admin/auth/auth.ssc"/>">역할&권한 관리</option>    
        
            <option value="<@spring.url"/admin/user/user.ssc"/>">사용자 관리</option>
       
            <option value="<@spring.url"/admin/code/code.ssc"/>">코드 관리</option>
       
            <option value="<@spring.url"/admin/monitoring/monitoring.ssc"/>">모니터링 관리</option>
        
            <option value="<@spring.url"/admin/equipment/equipment.ssc"/>">장비 관리</option> 
     
        </select>

        <div id="monitoringList" style="position : absolute;margin-top:-80px;">
            <!-- left시작 -->
            <div class="boardcover">
                <table summary="" class="board1">
                    <colgroup>
                        <col width="160px" />
                        <col width="" />
                        <col width="120px" />
                        <col width="120px" />
                    </colgroup>
                    <tbody>

                    </tbody>
                </table>
                <!-- paginate 
    <article class="paginate">
        <a class="direction" href="#">
        <span>?</span> 이전</a>
        <a href="#">1</a>
        <strong>2</strong>
        <a href="#">3</a>
        <a href="#">4</a>
        <a href="#">5</a>
        <a href="#">6</a>
        <a href="#">7</a>
        <a href="#">8</a>
        <a href="#">9</a>
        <a href="#">10</a>
        <a class="direction" href="#">다음 <span>?</span></a>
    </article>-->
                <!-- //paginate -->
            </div>
        </div>
        <!-- left 끝 -->
        <!-- right 시작 -->

        <!-- right 끝 -->
        </div>
        <!-- 본문끝 -->
        </div>
