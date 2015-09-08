Top = function() {

    return {
        init: function() {
            //화면로드시 필요한 정보 모두 로드
            //메뉴 선택표시
            hilight('clipsrch');

            
            //기간 검색 조회 유형(방송일,등록일) store
            var states = Ext.create('Ext.data.Store', {
                fields: ['value', 'name'],
                data : [
                    {"value":"regDt", "name":"등록일"},
                    {"value":"brdDd", "name":"방송일"} 
                ]
            });
            //공지사항의 팝없이 있는지 확인후 있다면 공지사항을 띄운다.
            var myCookiesVale = Ext.util.Cookies.get('popUp');
           
            // if(myCookiesVale != 'close'){
            findNoticePopUpStore.load({
                    url: context + '/clip/findNoticeList.ssc',
                    action: 'read',
                    callback: function(records, operation, success) {
                        var obj = Ext.JSON.decode(operation.response.responseText);

                    },
                    scope: this
                })
                // } 
            var tmpFromDate = new Date();
            tmpFromDate.setDate(tmpFromDate.getDate() - 30);

            //상세검색의 콤보박스안의 데이터를 불러들인다
            ristClfStore.load({
                url: context + '/clip/findSclListForClf.ssc',
                action: 'read',
                params: {
                    'clfCd': 'RIST'
                }
            });
            ctTypStore.load({
                url: context + '/clip/findSclListForClf.ssc',
                action: 'read',
                params: {
                    'clfCd': 'CTYP'
                }
            });
            ctClaStore.load({
                url: context + '/clip/findSclListForClf.ssc',
                action: 'read',
                params: {
                    'clfCd': 'CCLA'
                }
            });

            //context 를 넣기위해서 tree에서 url을 강제로 삽입해준다.
            var store = Ext.data.StoreManager.get("clipCategory");
            store.setProxy({
                type: 'ajax',
                url: context + '/admin/category/findCategoryListForExtJs.ssc',
                actionMethods: {
                    create: "POST",
                    read: "GET",
                    update: "POST",
                    destroy: "POST"
                },
                headers: {
                    'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache'
                },
                noCache: false,
                reader: {
                    type: 'json',
                    root: 'store'
                }
            });
            store.load();

            //context path가 변경될 경우를 대비해 url과 param정보를 동시에 넣는다.
            clipStore.load({
                url: context + '/clip/findClipSearchList.ssc',
                action: 'read',
                params: {
                    'keyword': '',
                    'startDt': tmpFromDate,
                    'endDt': new Date,
                    'categoryId': 0,
                    'dateGb': 'regDt',
                    'searchTyp': 'image'
                }
            });
            //로드 종료

            //트리패널
            // 트리패널


            // 상세검색 패널 
            var detailView = Ext.create('Ext.panel.Panel', {
                layout: 'vbox',
                width: 280,
                height: 170,
                frame: true,
                items: [Ext.create('Ext.ux.TreeCombo', {
                    fieldLabel: '카테고리',
                    labelSeparator: '',
                    treeHeight: 133,
                    treeWidth: 170,
                    // margin : '1 1 1 1',
                    id: 'treeCombo',
                    store: store,
                    selectChildren: false,
                    canSelectFolders: true,
                    name: 'categoryNm',
                    listeners: {
                        change: function(view, newValue, oldValue, eOpts) {

                        }
                    }

                }), {
                    name: 'ctTyp',
                    id: 'ctTyp',
                    xtype: 'combobox',
                    labelSeparator: '',
                    store: ctTypStore,
                    fieldLabel: '영상유형',
                    editable: false,
                    queryMode: 'local',
                    displayField: 'sclNm',
                    valueField: 'sclCd',
                    width: 260
                }, {
                    name: 'ctCla',
                    id: 'ctCla',
                    xtype: 'combobox',
                    labelSeparator: '',
                    store: ctClaStore,
                    fieldLabel: '영상구분',
                    editable: false,
                    queryMode: 'local',
                    displayField: 'sclNm',
                    valueField: 'sclCd',
                    width: 260
                }, {
                    name: 'ristClfCd',
                    id: 'ristClfCd',
                    xtype: 'combobox',
                    labelSeparator: '',
                    store: ristClfStore,
                    fieldLabel: '사용등급',
                    editable: false,
                    queryMode: 'local',
                    displayField: 'sclNm',
                    valueField: 'sclCd',
                    width: 260
                }],
                buttons: [{
                    text: '상세검색',
                    handler: function() {

                    	
                        var period = Ext.ComponentQuery.query('[name=period]')[0].value;
                        
                        var tree = Ext.ComponentQuery.query('#treeCombo');

                        var keyword = Ext.getCmp('keyword').getValue();
                        var fromDate = Ext.getCmp('fromDate').getValue();
                        var toDate = Ext.getCmp('toDate').getValue();

                        var cateogoryId = tree[0].value;
                        var ristClfCd = Ext.getCmp('ristClfCd').getValue();
                        var ctCla = Ext.getCmp('ctCla').getValue();
                        var ctTyp = Ext.getCmp('ctTyp').getValue();
 
                        if (cateogoryId == undefined) {

                            categroyId = 0
                        } else {
                            if (tree[0].value != 'total') {
                                categroyId = tree[0].value
                            } else {
                                categroyId = 0
                            }
                        }
                     
                        var tem = clipStore.load({
                            url: context + '/clip/findClipSearchList.ssc',
                            action: 'read',
                            params: {
                                'keyword': keyword,
                                'startDt': fromDate,
                                'endDt': toDate,
                                'categoryId': categroyId,
                                'dateGb': period,
                                'ristClfCd': ristClfCd,
                                'ctCla': ctCla,
                                'ctTyp': ctTyp,
                                'searchTyp': 'image'
                            }
                        });

                        var grid = Ext.getCmp('grid').show();
                        var searchResult = Ext.getCmp('searchResult').show();
                    }
                }, {
                    text: '닫기',
                    handler: function() {
                        detailWin.close();
                    }
                }]
            })


            var detailWin = Ext.create('Ext.window.Window', {
                xtype: 'panel',
                title: '상세검색',
                closable: true,
                closeAction: 'hide',
                items: [detailView]
            });



            var searchPanel = Ext.create('Ext.panel.Panel', {

                bodyPadding: '0 5 0 5',
                layout: {
                    type: 'vbox',
                    align: 'center',
                    pack: 'center'

                },
                border: false,
                items: [{
                    layout: {
                        type: 'hbox',
                        align: 'center',
                        pack: 'center'

                    },
                    border: false,
                    height: 50,
                    margin: '0 0 -10 0',
                    items: [ {
                       name: 'period',
                        id: 'period',
                        xtype: 'combobox',
                        labelSeparator: '',
                        store: states,                       
                        editable: false,
                        queryMode: 'local',
                        displayField: 'name',
                        valueField: 'value',
                        margin: '6 5 0 0',
                        width: 100 
                    }, {
                        name: 'startDate',
                        id: 'fromDate',
                        xtype: 'datefield',
                        labelSeparator: '',
                        labelWidth: 50,
                        width: 100,
                        margin: '6 5 0 0',
                        format: 'Y-m-d',
                        allowBlank: false
                    }, {
                    	xtype : 'panel',
                    	border : false,
                    	margin : '6 -20 0 10',
                        html : '<font size=4> ~ </font>'
                    },{
                        name: 'endDt',
                        labelWidth: 50,
                        labelSeparator: '',
                        id: 'toDate',
                        xtype: 'datefield',
                        width: 100,
                        format: 'Y-m-d',
                        margins: '6 5 0 40',
                        allowBlank: false


                    }, {
                        xtype: 'textfield',
                        labelSeparator: '',
                        width: 300,
                        labelWidth: 50,
                        margin: '6 15 0 25',
                        id: 'keyword',
                        listeners: {
                            scope: this,
                            specialkey: function(f, e) {

                                if (e.getKey() == e.ENTER) {

                                	var period = Ext.ComponentQuery.query('[name=period]')[0].value;
                                    
                                    var tree = Ext.ComponentQuery.query('#treeCombo');

                                    var keyword = Ext.getCmp('keyword').getValue();
                                    var fromDate = Ext.getCmp('fromDate').getValue();
                                    var toDate = Ext.getCmp('toDate').getValue();

                                    var cateogoryId = tree[0].value;
                                    var ristClfCd = Ext.getCmp('ristClfCd').getValue();
                                    var ctCla = Ext.getCmp('ctCla').getValue();
                                    var ctTyp = Ext.getCmp('ctTyp').getValue();
 

                                    if (cateogoryId != undefined) {

                                        categroyId = 0
                                    } else {
                                        if (tree[0].value != 'total') {
                                            categroyId = tree[0].value
                                        } else {
                                            categroyId = 0
                                        }
                                    }
                                    var tem = clipStore.load({
                                        url: context + '/clip/findClipSearchList.ssc',
                                        action: 'read',
                                        params: {
                                            'keyword': keyword,
                                            'startDt': fromDate,
                                            'endDt': toDate,
                                            'categoryId': categroyId,
                                            'dateGb': period,
                                            'ristClfCd': ristClfCd,
                                            'ctCla': ctCla,
                                            'ctTyp': ctTyp,
                                            'searchTyp': 'image'
                                        }
                                    });

                                    var grid = Ext.getCmp('grid').show();
                                    var searchResult = Ext.getCmp('searchResult').show();
                                }
                            }
                        }
                    },{
                        xtype: 'button',
                        text: '검색',
                        width: 100,
                        margin: '6 0 0 0',
                        handler: function() {

                        	var period = Ext.ComponentQuery.query('[name=period]')[0].value;
                            
                            var tree = Ext.ComponentQuery.query('#treeCombo');

                            var keyword = Ext.getCmp('keyword').getValue();
                            var fromDate = Ext.getCmp('fromDate').getValue();
                            var toDate = Ext.getCmp('toDate').getValue();

                            var cateogoryId = tree[0].value;
                            var ristClfCd = Ext.getCmp('ristClfCd').getValue();
                            var ctCla = Ext.getCmp('ctCla').getValue();
                            var ctTyp = Ext.getCmp('ctTyp').getValue();
 

                            if (cateogoryId != undefined) {

                                categroyId = 0
                            } else {
                                if (tree[0].value != 'total') {
                                    categroyId = tree[0].value
                                } else {
                                    categroyId = 0
                                }
                            }
                            var tem = clipStore.load({
                                url: context + '/clip/findClipSearchList.ssc',
                                action: 'read',
                                params: {
                                    'keyword': keyword,
                                    'startDt': fromDate,
                                    'endDt': toDate,
                                    'categoryId': categroyId,
                                    'dateGb': period,
                                    'ristClfCd': ristClfCd,
                                    'ctCla': ctCla,
                                    'ctTyp': ctTyp,
                                    'searchTyp': 'image'
                                }
                            });

                            var grid = Ext.getCmp('grid').show();
                            var searchResult = Ext.getCmp('searchResult').show();
                        }
                    }, {
                        xtype: 'button',
                        text: '초기화',
                        width: 100,
                        margin: '6 0 0 5',
                        handler: function() {
                            Ext.getCmp('keyword').setValue('');
                            Ext.getCmp('ristClfCd').setValue('');
                            Ext.getCmp('ctCla').setValue('');
                            Ext.getCmp('ctTyp').setValue('');
                            Ext.getCmp('fromDate').setValue(tmpFromDate);
                            Ext.getCmp('toDate').setValue(new Date);
                            contentsCategoryTreestore.reload();

                        }
                    }, {
                        xtype: 'button',
                        text: '상세검색',
                        width: 100,
                        margin: '6 0 0 5',
                        handler: function() {

                            detailWin.show();
                        }
                    }]
                }],
                listeners: {
                    afterrender: function(view, eOpts) {

                        Ext.getCmp('fromDate').setValue(tmpFromDate);
                        Ext.getCmp('toDate').setValue(new Date);
                        Ext.getCmp('period').setValue("regDt");
                    }
                }

            });

            //상단 화면 그리기 시작.
            var topSearch = Ext.create('Ext.panel.Panel', {
               border: false,
                padding: '0 5 0 5',
                margin: '0 5 2 0',
                height : 35,
                renderTo: 'top',
                items: [searchPanel]
            })

            Ext.EventManager.onWindowResize(function() {
                topSearch.setSize(undefined, undefined)
            });

            Ext.EventManager.onWindowResize(function() {
                searchPanel.setSize(undefined, undefined)
            });

            
          //화면로딩후  키워드필드에 focus를 자동으로 놓는다.
			var field = Ext.ComponentQuery.query('#keyword');
			field[0].focus();
        }
    }
}();