
StatisticsForYearLeft = function() {

	return {
		init : function() {
			var dt = new Date();
			var year = Ext.Date.format(dt, 'Y');
			//메뉴 선택표시
			hilight('statistic');
			statisticGraph.load({
				url : context + '/statistic/findStatisticListForYearForGraph.ssc',
				action: 'read',
				params: { 
					'yearList' : year
					,categoryId : 0
				}         
			});

			statisticList.load({
				url : context + '/statistic/findStatisticListForYear.ssc',
				action: 'read',
				params: { 
					'yearList' : year
					,'categoryId' : 0
				}         
			});

			statisticDetail.load({
				url : context + '/statistic/findYearList.ssc',
				action: 'read' 
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
					title: '연도검색',
					defaultType: 'textfield',
					layout: 'anchor',
					defaults: {
						anchor: '100%'
							,labelWidth : 50
					},
					items: [{
						name: 'year',
						id : 'year',
						store : statisticDetail,
						xtype : 'combobox',
						fieldLabel: '검색연도',
						labelSeparator : '',
						flex: 2,
						queryMode: 'local',
						displayField: 'yearList',
						valueField: 'yearList',
						allowBlank: false
					}]              
				}]

			});

			//좌측패널
			var left = Ext.create('Ext.panel.Panel',{

				layout : {
					type : 'vbox'
				}

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
			            			var year =Ext.getCmp('year').getValue();							 
			            			var cateogory = treepanel.getSelectionModel().getSelection();
			            			var categroyId ='';

			            			if(cateogory.length==0 || cateogory[0].data.id == 'total'){
			            				categroyId = 0
			            			}else{
			            				categroyId =	cateogory[0].data.id
			            			}

			            			if(year != null ){

			            				statisticGraph.load({
			            					url : context + '/statistic/findStatisticListForYearForGraph.ssc',
			            					action: 'read',
			            					params: { 
			            						'yearList' : year 
			            						,'categoryId' : categroyId
			            					}         
			            				});


			            				statisticList.load({
			            					url : context + '/statistic/findStatisticListForYear.ssc',
			            					action: 'read',
			            					params: { 
			            						'yearList' : year
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
				treepanel.setSize(undefined,undefined)
			});

			Ext.EventManager.onWindowResize(function(){
				searchPanel.setSize(undefined,undefined)
			});

		}
	}
}();




