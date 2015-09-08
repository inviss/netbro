//초기날짜 셋팅
var tmpFromDate = new Date();
tmpFromDate.setDate(tmpFromDate.getDate()-30);

var keyword = '';
var fromDate = tmpFromDate;
var toDate = new Date();
var tabGb = 'archive';           
var categroyId = 0;
var tempPage = 1;

archiveLeft = function () {

     return {
         init: function () {
        	 
        	 

         	var reloadArchive = function () {
         		archiveListStore.load({
 					url: context + '/contents/archive/findArchiveList.ssc',
 					action: 'read',
 					params: {
                         'keyword': keyword,
                         'startDt': tmpFromDate,
                         'endDt': toDate,
                         'categoryId': categroyId ,
                         'pageNo' : tempPage
                     }
 				});
 			}
         	
         	
         	var reloadDownload = function () {
         		downloadListStore.load({
 					url: context + '/contents/archive/findDownloadList.ssc',
 					action: 'read',
 					params: {
                         'keyword': keyword,
                         'startDt': fromDate,
                         'endDt': toDate,
                         'categoryId': categroyId ,
                         'pageNo' : tempPage
                     }
 				});
 			}

         	
      
         	
         	
        	//메뉴 선택표시
        	 hilight('contents');
     	
         	var store = Ext.data.StoreManager.get("archiveCategoryTree"); 
         	store.setProxy({
			    type: 'ajax',
			    url:  context+'/contents/archive/findCategoryListForExtJs.ssc'
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

 
             archiveListStore.load({
            	 url : context + '/contents/archive/findArchiveList.ssc',
                 action: 'read',
                 params: {
                     'pageNo': 1 ,
                     'startDt' : tmpFromDate,
                     'endDt' : new Date()
                 }
             }); 

             //string검색 컬럼 선택 박스 생성
             var searchTextCombo = Ext.create('Ext.data.Store',{
            	 fields : ['name','value'],
            	 data : [
            	         {'name' : '컨텐츠 명', 'value' : 'ctNm'},
            	         {'name' : '사용자 명', 'value' : 'userNm'}
            	         ]
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
					xtype: 'fieldset',
					title: '기간검색',
					
					layout: 'anchor',
					defaults: {
						labelWidth : 50
					},
					width : 200,
					items: [{
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
				}, Ext.create('Ext.form.ComboBox', {
				    fieldLabel: '필드선택',
				    store: searchTextCombo,
				    queryMode: 'local',
				    id : 'comboBox',
				    displayField: 'name',
				    valueField: 'value' 
				}), {
                     fieldLabel: '키워드',
                     id: 'keyword',
                     listeners: {
                         scope: this,
                         specialkey: function (f, e) {

                             if (e.getKey() == e.ENTER) {
                                  keyword = Ext.getCmp('keyword').getValue();
                                  fromDate = Ext.getCmp('fromDate').getValue();
                                  toDate = Ext.getCmp('toDate').getValue();
                                  searchGb = Ext.getCmp('comboBox').getValue();
                                  var cateogory = treepanel.getSelectionModel().getSelection();
								  categroyId = '';
						 
					 
								   if (cateogory.length == 0 || cateogory[0].data.id == 'total') {
			                    	   
				                          categroyId = 0
				                      } else {
				                          categroyId = cateogory[0].data.id
				                      }

                              
                                 var tem = archiveListStore.load({
                                	 url :  context + '/contents/archive/findArchiveList.ssc',
                                     action: 'read',
                                     params: {
                                         'keyword': keyword,
                                         'startDt': fromDate,
                                         'endDt': toDate,
                                         'categoryId': categroyId ,
                                         'searchGb' : searchGb,
                                         'pageNo' : 1
                                     }
                                 });   
                                // var grid = Ext.getCmp('grid').show();
                               //  var searchResult = Ext.getCmp('searchResult').show(); 
                               
                             }  
                         }
                     }
                 }]
                 ,listeners : {
                	 afterrender : function( view, eOpts ){
                		 var fromDate = Ext.getCmp('fromDate').setValue(tmpFromDate);
                         var toDate = Ext.getCmp('toDate').setValue(new Date); 
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
                 id : 'searchPanel',
                 title: '좌측메뉴',
                 items: [treepanel, searchPanel]
                 , buttons: [{
                     text: '검색',
                     handler: function () {

                          keyword = Ext.getCmp('keyword').getValue();
                          fromDate = Ext.getCmp('fromDate').getValue();
                          toDate = Ext.getCmp('toDate').getValue();
                          searchGb = Ext.getCmp('comboBox').getValue();
                         var cateogory = treepanel.getSelectionModel().getSelection();
                          categroyId = '';
                      

                         if (cateogory.length == 0 || cateogory[0].data.id == 'total') {
                             categroyId = 0
                         } else {
                             categroyId = cateogory[0].data.id
                         }

                         if(tabGb == 'archive'){
                        	 archiveListStore.load({
                        		 url : context + '/contents/archive/findArchiveList.ssc',
                        		 action: 'read',
                        		 params: {
                        			 'pageNo': 1 ,
                        			 'startDt' : fromDate,
                        			 'endDt' : toDate,
                        			 'keyword' : keyword,
                        			 'searchGb' : searchGb,
                        			 'categoryId': categroyId 
                        		 }
                        	 });
                        	 
                        	 //아카이브 목록을 주기적으로 조회한다
                        	 setInterval(reloadArchive, 5000);
                         }else{
                        	 downloadListStore.load({
               	            	 url : context + '/contents/archive/findDownloadList.ssc',
               	                 action: 'read',
               	                 params: {
                        			 'pageNo': 1 ,
                        			 'startDt' : fromDate,
                        			 'endDt' : toDate,
                        			 'keyword' : keyword,
                        			 'searchGb' : searchGb,
                        			 'categoryId': categroyId 
                        		 }
               	             }); 
                        	 
                        	 //다운로드 목록을 주기적으로 업데이트 한다.
                        	 setInterval(reloadDownload, 5000);
                         }
                        	 

                     }
                 },{
                	 text : '초기화',
                	 handler : function(){
                		  Ext.getCmp('keyword').setValue('');
                		  Ext.getCmp('userNm').setValue('');                 		 
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

         }
     }
 }();

