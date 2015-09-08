
StatisticsLeft = function() {

	return {
		init : function() {
			//메뉴 선택표시
			 hilight('statistic');
		 	var dt = new Date();
			var yearMonth = Ext.Date.format(dt, 'Y-m');
		   
			 statisticGraph.load({
                 url : context + '/statistic/findStatisticListForPeriod.ssc',
                      action: 'read',
                      params: { 
                    	  'startDD' : yearMonth+'-01'
							,'endDD' : yearMonth+'-31'
							,categoryId : 0
							}         
			 });
		
			 
				var store = Ext.data.StoreManager.get("statisticsCategory"); 
	         	store.setProxy({
				    type: 'ajax',
				    url:  context+'/statistic/findCategoryListForExtJs.ssc'
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
			var treepanel = Ext.create('Ext.tree.Panel',{
				width : '100%',
				height : 300,
				id : 'categoryTree',
				store: store,
				rootVisible: true,
				useArrows: true
			});  

			
			
	
			//좌측 검색패널
			var searchPanel = Ext.create('Ext.Panel', {
				width: 230,
				//height: 900,
				border : false,
				title : '검색조건',
				layout: 'form', 
				bodyPadding: 15,
				defaultType: 'textfield',
				defaults : {
					labelWidth : 50
				},
				items: [{
					xtype: 'fieldset',
					title: '기간검색',
					defaultType: 'textfield',
					layout: 'anchor',
					defaults: {
						anchor: '100%'
						,labelWidth : 50
					},
					items: [{
						name: 'startDate',
						id : 'fromDate',
						xtype : 'datefield',
						fieldLabel: '시작일',
						labelSeparator : '',
						flex: 2,
						format: 'Y-m-d',
						allowBlank: false
					}, {
						name: 'endDt',
						fieldLabel: '종료일',
						labelSeparator : '',
						id : 'toDate',
						xtype : 'datefield',
						flex: 2,
						format: 'Y-m-d',
						margins: '0 0 0 6',                       
						allowBlank: false
					}]              
				}],
				listeners : {
					afterrender :function( view, eOpts ){
						var start  = yearMonth+'-01';
						var end = yearMonth+'-31';
						  var fromDate =Ext.getCmp('fromDate').setValue( start );
							var toDate = Ext.getCmp('toDate').setValue(end );
					}
				}
		
			});

			//좌측패널
			var left = Ext.create('Ext.panel.Panel',{
				
				layout : {
					type : 'vbox'
				},
			height : '100%'
			,id:'leftPanel'
			,padding : '0 5 0 5'
		   
				,width : '100%'
					,renderTo : 'left'
						
						, title : '좌측메뉴'
							,items : [treepanel,searchPanel]
			,buttons : [
				        {xtype : 'button'
				        ,text : '검색'
				       
				        ,handler : function(){
				        	    var fromDate =Ext.getCmp('fromDate').getValue();
								var toDate = Ext.getCmp('toDate').getValue();
								var cateogory = treepanel.getSelectionModel().getSelection();
								var categroyId ='';
								
								if(cateogory.length==0 || cateogory[0].data.id == 'total'){
									categroyId = 0
								}else{
									categroyId =	cateogory[0].data.id
								}
									 
								if(fromDate != null || toDate != null){
				        		var  search = new Ext.data.Operation({
				    				action :'read'
				    				,params : { 'startDD' : fromDate
				    							,'endDD' : toDate
				    							,'categoryId' : categroyId}
				    			});
				    			 statisticGraph.load({
				                     url : context + '/statistic/findStatisticListForPeriod.ssc',
				                      action: 'read',
				                      params: { 
				                    	  	'startDD' : fromDate
			    							,'endDD' : toDate
			    							,'categoryId' : categroyId
			    							}         
							 });
				    			 
				    		
								}else{
									Ext.Msg.alert('알림','시작일 종료일값이 모두 있어야합니다.')
									
								}
				        }
				        	}
				        ]
			})


			Ext.EventManager.onWindowResize(function(){
				left.setSize(undefined,undefined)
			});
			
			Ext.EventManager.onWindowResize(function(){
				treepanel.setSize(undefined,undefined)
			});
			
			Ext.EventManager.onWindowResize(function(){
				searchPanel.setSize(undefined,undefined)
			});
			
		}
	}
}();




