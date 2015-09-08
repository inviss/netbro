
StatisticForYearSearch = function() {

	return {
		init : function() { 
			
			
			 var win=""
			
			 var graph = Ext.create('Ext.chart.Chart',{ 
				animate : true
				,shadow : true
				,store : statisticGraph
				  ,width : '85%'
				,height : '30%'
				,renderTo : Ext.getBody()
				
				,axes : [{  
					type : 'Category'
					,position : 'bottom'
					,fields :['regDd' ]
					,title : ''
				},{			
					type : 'Numeric'
					,position : 'left'
					,fields : ['regist','beforeArrange','completeArrange','discard' ]
					,grid : true
					,title :''
					,minimum : 0
				}]
			 ,series : [{
					type : 'column'
					,axis : 'left'
					,highlight : true
					
					,xField : ['categoryNm' ]
					,yField : ['regist','beforeArrange','completeArrange','discard' ]
			 		,title : ['등록','정리전','정리완료','폐기']
					
				}]
			,legend : {
				position : 'top'
			}
			 }) 
			
			 //상세 팝업리스트
			var detailList = Ext.create('Ext.grid.Panel',{
				height : '100%'
				,store : statisticDetail
				,margin : '10'
			    ,frame : true
			    ,style : {
					 'border-width':'1px'
				}
				,columns : [
				            {text : '컨텐츠명',flex : 3,dataIndex : 'ctNm'}
				            , {text : '방송길이',flex : 1,dataIndex : 'ctLeng'}
				            , {text : '방송일',flex : 1,dataIndex : 'brdDd'}
				            , {text : '회차',flex : 2,dataIndex : 'episodeNm'}
				            ]
				          
			});
			 
			 //전체화면 그리드 리스트
			var list = Ext.create('Ext.grid.Panel',{
				height : '100%', 
				store: statisticList,
				selType : 'cellmodel',
				 border : false,
			    features: [{
			           
			            ftype: 'groupingsummary',
			            groupHeaderTpl: '{name} 그룹'
			            
			        }, {
			            ftype: 'summary',
			            dock: 'bottom',
			            style : {
			            	'background-color': '#c5c5c5' 
			            }
			        }],
				columns: [
				          { text: '카테고리명',  dataIndex: 'categoryNm' ,flex :3,sortable:false
				        	,summaryType: 'count'
				        		,  style: 'text-align:center'  
				            ,summaryRenderer: function(value, summaryData, dataIndex) {
				            var summaryRow = list.getView().getFeature(0);
				            
				            //총합의 background 색상을 지정한다.
				            StyleObj = {
				            		'background-color': '#c5c5c5' 
				            		
				            }
				            summaryRow.view.el.setStyle(StyleObj);
				                return '총합';
				            }},
				          { text: '등록건', dataIndex: 'regist'  ,flex :3 ,sortable:false
				            ,summaryType: 'sum',
				            align: 'center',
				            summaryRenderer: function(value, summaryData, dataIndex) {
				            	
				                return value;
				            },
				            field: {
				                xtype: 'numberfield'
				            }
				            	},
				          { text: '정리전건', dataIndex: 'beforeArrange'  ,flex :3,sortable:false
				            ,summaryType: 'sum',
				            align: 'center',
				            summaryRenderer: function(value, summaryData, dataIndex) {
				                return value;
				            },
				            field: {
				                xtype: 'numberfield'
				            }},
				          { text: '정리완료건', dataIndex: 'completeArrange'  ,flex :3,sortable:false
				            ,summaryType: 'sum',
				             align: 'center',
				            summaryRenderer: function(value, summaryData, dataIndex) {
				                return value ;
				            },
				            field: {
				                xtype: 'numberfield'
				            }},
				          { text: '폐기건', dataIndex: 'discard'  ,flex :3,sortable:false,
				            	summaryType: 'sum',
				            	 align: 'center',
				                summaryRenderer: function(value, summaryData, dataIndex) {
				                    return value ;
				                },
				                field: {
				                    xtype: 'numberfield'
				                }},
				          { text: '오류건', dataIndex: 'error'  ,flex :3,sortable:false
				            ,summaryType: 'sum',
				             align: 'center',
				            summaryRenderer: function(value, summaryData, dataIndex) {
				                return value ;
				            },
				            field: {
				                xtype: 'numberfield'
				            }},
				          { text: '카테고리id', dataIndex: 'categoryId'  ,flex :1,sortable:false,hidden:true},
				          { text: '그룹id', dataIndex: 'groupId'  ,flex :1,sortable:false,hidden:true}
				          ],
				          height: '100%',
				          width: '100%' ,
				          listeners : {
				        	 //셀클릭시 이벤트를 가지고 상세 조회 팝업을 띄운다.
							 cellclick : function(view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
							 
								 //선택한 컬럼명
								 var searchcolomn =''
								//선택한 컬럼 한글명
								 var searchcolomnNm =''
								//선택한 컨럼의 value
							     var columnValue =''
							    //검색결과
							     var result =''
							    	 
				        		  if(cellIndex == 1){
				        			  searchcolomn = 'regist';
				        			  columnValue =  record.data.regist;	
				        			  searchcolomnNm = '등록건'
				        		  }else if(cellIndex == 2){
				        			  searchcolomn = 'beforeArrange'
				        			  columnValue =  record.data.beforeArrange;
				        			  searchcolomnNm = '정리전건'
				        		  }else if(cellIndex == 3){
				        			  searchcolomn = 'completeArrange'
				        			  columnValue =  record.data.completeArrange;	
				        			  searchcolomnNm = '정리완료건'
				        		  }else if(cellIndex == 4){
				        			  searchcolomn = 'discard'
				        			  columnValue =  record.data.discard;	
				        			  searchcolomnNm = '폐기건'
				        		  }else if(cellIndex == 5){
				        			  columnValue =  record.data.error;	
				        			  searchcolomn = 'error'
				        			  searchcolomnNm = '오류건'
				        		  }
								 
				        		  var categoryId = this.getSelectionModel().getSelection()[0].data.categoryId
				        		  var groupId = this.getSelectionModel().getSelection()[0].data.groupId
				        		  var year =Ext.getCmp('year').getValue();	
				        		  	 
				        		  	//컬럼값이 0이 아닐 경우에만 진행
				        			if(columnValue != 0){
				        				result = statisticDetail.load({
						                     url : context + '/statistic/findStatisticListForDetail.ssc',
						                      action: 'read',
						                      params: { 
						                    	  'startDD' : year+'-01-01'
				        							,'endDD' : year+'-12-31'
				        							,'categoryId' : categoryId
				        							,'gubun' : searchcolomn
				        							,'pageNum' : 0
				        							}         
									 });
				        				var winId = searchcolomn+'_'+categoryId;
				        					 
				        			if(!win){
				        			
				        		
				        			  win =  Ext.create('Ext.window.Window',{
				        					title : '상세리스트'
				        					,height : 500
				        					,width : 700
				        					,html : 'hello'
				        					// ,id : searchcolomn+'_'+categoryId
				        					,closable : true
				        					,closeAction : 'hide'
				        					,layout : {
				        						type : 'fit'
				        					}
				        					,items : [detailList]
				        					
				        				}) 
				        			}
				        					win.show();
				        			}else{
				        				Ext.Msg.alert('알림','데이터가 0인 건은 조회할수 없습니다')
				        			}
				        		  	
							}
			
				          },
				        
			
			});
			var statisticsView = Ext.create('Ext.panel.Panel',{
				layout : {
					type : 'vbox',
					align : 'stretch'
				}
			 ,border : false
			,height : '100%'
			,renderTo : 'body'
			,items : [{title : '그래프',margin : '0 5 0 5',flex :1,items:[graph]},{title : '그리드',margin : '0 5 0 5',flex :2,autoScroll :  true,items : [list]}]
		    ,listeners : {
		    	afterrender : function(view, eOpts ){
		    		var leftPanel = Ext.ComponentQuery.query('#leftPanel');
		    		 
		    		leftPanel[0].setHeight(view.getHeight());
		    	}
		    }
			});
			

			Ext.EventManager.onWindowResize(function(){
				statisticsView.setSize(undefined,undefined)
			});
			 Ext.EventManager.onWindowResize(function(){
				graph.setSize(undefined,undefined)
			}); 
			
			 Ext.EventManager.onWindowResize(function(){
				 list.setSize(undefined,undefined)
				}); 
				
		}
}
}();



