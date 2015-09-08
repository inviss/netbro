 discardLeft = function () {

     return {
         init: function () {
        	//메뉴 선택표시
        	 hilight('contents');
     		var tmpFromDate = new Date();
        	tmpFromDate.setDate(tmpFromDate.getDate()-30);
         
        	//상세검색의 콤보박스안의 데이터를 불러들인다
         
         	var store = Ext.data.StoreManager.get("contentsCategoryTree"); 
         	store.setProxy({
			    type: 'ajax',
			    url:  context+'/contents/discard/findCategoryListForExtJs.ssc'
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
             
             //폐기 코드를 조회한다.
             discardCodeStore.load({
          		url : context +'/contents/discard/findSclListForClf.ssc'
          		,action : 'read'
          		,params : {
          			'clfCd' : 'DECD' 
          		}
          	});
             disuseStore.load({
            	 url : context + '/contents/discard/findDiscardInfoList.ssc',
                 action: 'read',
                 params: {
                     'keyword': '',
                     'startDt': tmpFromDate,
                     'endDt': new Date,
                     'pageNo': 1
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
					xtype: 'fieldset',
					title: '등록일검색',
					
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
				},{
					xtype: 'fieldset',
					title: '상세검색',
					collapsible: true,
					defaultType: 'textfield',
					 
					defaults: {
						 labelWidth : 60
					},
					items: [{
						name: 'discardCode',
						id : 'discardCode',
						xtype : 'combobox',
						labelSeparator : '',
						store : discardCodeStore,
						fieldLabel: '폐기상태', 
						editable: false,
						 queryMode: 'local',
						    displayField: 'sclNm',
						    valueField: 'sclCd',
						    width : 180
					}]              
				}, {
                     fieldLabel: '키워드',
                     id: 'keyword',
                     listeners: {
                         scope: this,
                         specialkey: function (f, e) {

                             if (e.getKey() == e.ENTER) {
                                 var keyword = Ext.getCmp('keyword').getValue();
                                 var fromDate = Ext.getCmp('fromDate').getValue();
                                 var toDate = Ext.getCmp('toDate').getValue();
                                 var disuseClf = Ext.getCmp('discardCode').getValue();
						 
                                 
                                 var tem = disuseStore.load({
                                	 url :  context + '/contents/discard/findDiscardInfoList.ssc',
                                     action: 'read',
                                     params: {
                                         'keyword': keyword,
                                         'disuseClf' : disuseClf,
                                         'startDt': fromDate,
                                         'endDt': toDate,                                         
										  'pageNo' : 1
                                     }
                                 });   
                               
                               
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
                 id : 'discardLeft',
                 title: '좌측메뉴',
                 items: [treepanel, searchPanel]
                 , buttons: [{
                     text: '검색',
                     handler: function () {

                         var keyword = Ext.getCmp('keyword').getValue();
                         var fromDate = Ext.getCmp('fromDate').getValue();
                         var toDate = Ext.getCmp('toDate').getValue();
                         var disuseClf = Ext.getCmp('discardCode').getValue();
						 

 
                         var tem = disuseStore.load({
                        	 url : context + '/contents/discard/findDiscardInfoList.ssc',
                             action: 'read',
                             params: {
                                 'keyword': keyword,
                                 'disuseClf' : disuseClf,
                                 'startDt': fromDate,
                                 'endDt': toDate,                                         
								  'pageNo' : 1
                             }
                         });
                       

                     }
                 },{
                	 text : '초기화',
                	 handler : function(){
                		  Ext.getCmp('keyword').setValue('');
                		  Ext.getCmp('discardCode').setValue('');
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

