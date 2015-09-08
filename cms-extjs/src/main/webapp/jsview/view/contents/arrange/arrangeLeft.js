 contentsLeft = function () {

     return {
         init: function () {
        	//메뉴 선택표시
        	 hilight('contents');
        	 
        	 
        	 
        	 //검색엔진을 사용안할경우 이화면에서 공지사항을 띄어줜다
        	 if(searchEngine == 'N'){
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
        	 }
     		var tmpFromDate = new Date();
        	tmpFromDate.setDate(tmpFromDate.getDate()-30);
         
        	//상세검색의 콤보박스안의 데이터를 불러들인다
         	ristClfStore.load({
         		url : context +'/contents/arrange/findSclListForClf.ssc'
         		,action : 'read'
         		,params : {
         			'clfCd' : 'RIST' 
         		}
         	});
         	ctTypStore.load({
             		url : context +'/contents/arrange/findSclListForClf.ssc'
             		,action : 'read'
             		,params : {
             			'clfCd' : 'CTYP' 	
             		}
         	});
         	ctClaStore.load({
             		url : context +'/contents/arrange/findSclListForClf.ssc'
             		,action : 'read'
             		,params : {
             			'clfCd' : 'CCLA' 	
             		}
         	});
         	
         	var store = Ext.data.StoreManager.get("contentsCategoryTree"); 
         	store.setProxy({
			    type: 'ajax',
			    url:  context+'/contents/arrange/findCategoryListForExtJs.ssc'
			    	 ,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
			,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
			,noCache: false
			,reader : {
	   			type : 'json'
	   			,root : 'store'
	   			}
			});
         	store.load();
             //좌측 트리패널
             var treepanel = Ext.create('Ext.tree.Panel', {
                 width: 240,
                 height: 300,
                 id: 'categoryTree',
                 store: store,
                 rootVisible: true,
                 useArrows: true
             });

 
             contentsStore.load({
            	 url : context + '/contents/arrange/findSearchList.ssc',
                 action: 'read',
                 params: {
                     'keyword': '',
                     'startDt': tmpFromDate,
                     'endDt': new Date,
                     'categoryId': 0,
                     'searchTyp' : 'image'
                 }
             });

             //좌측 검색패널
             var searchPanel = Ext.create('Ext.Panel', {
                 width: 230,
                 //height: 900,
                 title : '검색조건',
                 layout: 'form',
                 bodyPadding: 15,
                 defaultType: 'textfield',
                 defaults: {
                     labelWidth: 50
                 },
                 items: [{
                     fieldLabel: '키워드',
                     id: 'keyword',
                   
                     listeners: {
                         scope: this,
                         specialkey: function (f, e) {

                             if (e.getKey() == e.ENTER) {
                                 var keyword = Ext.getCmp('keyword').getValue();
                                 var fromDate = Ext.getCmp('fromDate').getValue();
                                 var toDate = Ext.getCmp('toDate').getValue();
                                 var cateogory = treepanel.getSelectionModel().getSelection();
                             	var ristClfCd = Ext.getCmp('ristClfCd').getValue();
								var ctCla = Ext.getCmp('ctCla').getValue();
								var ctTyp = Ext.getCmp('ctTyp').getValue();
								 var categoryId = '';
						 
					 
								   if (cateogory.length == 0 || cateogory[0].data.id == 'total') {
			                    	   
				                          categoryId = 0
				                      } else {
				                          categoryId = cateogory[0].data.id
				                      }

                              
                                 var tem = contentsStore.load({
                                	 url :  context + '/contents/arrange/findSearchList.ssc',
                                     action: 'read',
                                     params: {
                                         'keyword': keyword,
                                         'startDt': fromDate,
                                         'endDt': toDate,
                                         'categoryId': categoryId,
                                         'ristClfCd' :ristClfCd,
										 'ctCla' :  ctCla,
										  'ctTyp' : ctTyp,  
										  'searchTyp' : 'image'
                                     }
                                 });   
                                 var grid = Ext.getCmp('grid').show();
                                 var searchResult = Ext.getCmp('searchResult').show(); 
                               
                             }  
                         }
                     }
                 },{
					xtype: 'fieldset',
					title: '기간검색',
					  margin : '10 0 10 0',
					layout: 'anchor',
					defaults: {
						labelWidth : 50
					},
					width : 200,
					items: [{
						layout  : 'hbox',
						 xtype      : 'fieldcontainer',
						 defaultType: 'radiofield',
						 id : 'period',
						items : [{
				        	 boxLabel : '등록일'
						         ,name : 'period'
						         ,inputValue : 'regDt'
						         ,id : 'regDt'						      
						          ,margin : '0 40 0 0'
				         },
						         {
						        	 boxLabel : '방송일'
						         ,name : 'period'
						         ,inputValue : 'brdDd'
						         ,id : 'brdDd'
						        	 }]},{
						name: 'startDate',
						id : 'fromDate',
						xtype : 'datefield',
						fieldLabel: '시작일',
						labelSeparator : '',
						width : 180,
						format: 'Y-m-d',
						allowBlank: false
					}, {
						name: 'endDt',
						fieldLabel: '종료일',
						labelSeparator : '',
						id : 'toDate',
						xtype : 'datefield',
						width : 180,
						format: 'Y-m-d',
						margins: '0 0 0 6',                       
						allowBlank: false
					}]              
				},{
					xtype: 'fieldset',
					title: '상세검색',
					collapsible: true,
					defaultType: 'textfield',
					 
					defaults: {
						 labelWidth : 60
					},
					items: [{
						name: 'ctTyp',
						id : 'ctTyp',
						xtype : 'combobox',
						labelSeparator : '',
						store : ctTypStore,
						fieldLabel: '영상유형', 
						editable: false,
						 queryMode: 'local',
						    displayField: 'sclNm',
						    valueField: 'sclCd',
						    width : 180
					}, {
						name: 'ctCla',
						id : 'ctCla',
						xtype : 'combobox',
						labelSeparator : '',
						store : ctClaStore,
						fieldLabel: '영상구분',
						flex: 2,
						editable: false,
						 queryMode: 'local',
						 displayField: 'sclNm',
						    valueField: 'sclCd',
						    width : 180
					}, {
						name: 'ristClfCd',
						id : 'ristClfCd',
						xtype : 'combobox',
						labelSeparator : '',
						store :ristClfStore ,
						fieldLabel: '사용등급',
						flex: 2,
						editable: false,
						 queryMode: 'local',
						 displayField: 'sclNm',
						    valueField: 'sclCd',
						    width : 180 
					}]              
				}]
                 ,listeners : {
                	 afterrender : function( view, eOpts ){
                		 var fromDate = Ext.getCmp('fromDate').setValue(tmpFromDate);
                         var toDate = Ext.getCmp('toDate').setValue(new Date);
                         Ext.getCmp('regDt').setValue(true);
                	 }
                 }
             });

             //좌측패널
             var left = Ext.create('Ext.panel.Panel', {
                 layout: {
                     type: 'vbox'
                 },
                 padding: '0 5 0 5',
               //  height: '100%',
                 width: 240,
                 renderTo: 'left',
                 id : 'arrangeLeft',
                 title: '좌측메뉴',
                 items: [treepanel, searchPanel]
                 , buttons: [{
                     text: '검색',
                     handler: function () {

                         var keyword = Ext.getCmp('keyword').getValue();
                         var fromDate = Ext.getCmp('fromDate').getValue();
                         var toDate = Ext.getCmp('toDate').getValue();
                         var cateogory = treepanel.getSelectionModel().getSelection();
                         var categoryId = '';
                         var ristClfCd = Ext.getCmp('ristClfCd').getValue();
                         var ctCla = Ext.getCmp('ctCla').getValue();
                         var ctTyp = Ext.getCmp('ctTyp').getValue();
                         var period = Ext.ComponentQuery.query('[name=period]');
                      
                         if (cateogory.length == 0 || cateogory[0].data.id == 'total') {
                             categoryId = 0
                         } else {
                             categoryId = cateogory[0].data.id
                         }

 
                         var tem = contentsStore.load({
                        	 url : context + '/contents/arrange/findSearchList.ssc',
                             action: 'read',
                             params: {
                                 'keyword': keyword,
                                 'startDt': fromDate,
                                 'endDt': toDate,
                                 'categoryId': categoryId,
                                 'ristClfCd' :ristClfCd,
								 'ctCla' :  ctCla,
								  'ctTyp' : ctTyp,
								  'period' : period[0].getGroupValue() ,
								  'searchTyp' : 'image'
                             }
                         });
                         var grid = Ext.getCmp('grid').show();
                         var searchResult = Ext.getCmp('searchResult').show(); 

                     }
                 },{
                	 text : '초기화',
                	 handler : function(){
                		  Ext.getCmp('keyword').setValue('');
                		  Ext.getCmp('ristClfCd').setValue('');
                		  Ext.getCmp('ctCla').setValue('');
                		  Ext.getCmp('ctTyp').setValue('');
                		  Ext.getCmp('fromDate').setValue(tmpFromDate);
                		  Ext.getCmp('toDate').setValue(new Date);
                		  contentsCategoryTreestore.reload();
                		 
                	 }
                 }]
             })


             Ext.EventManager.onWindowResize(function () {
                 left.setSize(undefined, undefined)
             });

             Ext.EventManager.onWindowResize(function () {
                 treepanel.setSize(undefined, undefined)
             });

             Ext.EventManager.onWindowResize(function () {
                 searchPanel.setSize(undefined, undefined)
             });

             //화면로딩후  키워드필드에 focus를 자동으로 놓는다.
 			var field = Ext.ComponentQuery.query('#keyword');
 			field[0].focus();
         }
     }
 }();

