 
Left = function() {

	return {
    	init : function() {
    		Ext.Loader.setConfig({
    			disableCaching: false
    		});
    		Ext.require('/extjsView/store/contents/categoryTreeStore' );
    		Ext.require('/extjsView/store/contents/contentsSearchStore');
			var  initList = new Ext.data.Operation({
				action :'read'
				,params : {'keyword' :  '' 
							,'startDt' : '2014-04-01'
							,'endDt' : '2014-05-31'
							,'categoryId' : '0'}
			});
			contentsStore.load(initList);
			
			var searchTest = new Ext.data.Operation({
				action :'read'
				,params : {'categoryId' : 5,'depth' : 1 }
			});
			contentsCategoryTreestore.load(searchTest);
    		var rightBox = Ext.create('Ext.panel.Panel',{
				xtype : 'vbox'
				,height : 800
				,renderTo : 'left'
				,items : [{
					html : '<h>트리뷰 위치</h>'
						
				}		        
						,{
					xtype : 'combobox'
						,fieldLabel : '카테고리'
						,itemId : 'categoryComboBox'
						,store : contentsCategoryTreestore
					    ,displayField : 'categoryNm'
					    ,valueField : 'categoryId'
					     
					},{
						xtype : 'textfield'
							,id : 'searchField'
							,fieldLabel : '검색'
							,  listeners:{  
								scope : this
								,specialkey : function(f,e){
									 
									if(e.getKey() == e.ENTER){ 
										var value = Ext.getCmp('searchField').getValue();
																		
										var  List = new Ext.data.Operation({
											action :'read'
											,params : {'keyword' : value}
										});
										store.load(List);
									}
								}
							}
						}]
    		
			});
    		
		
    	}
	}
}();








/*Ext.create('Ext.grid.Panel',{
	store : store
	,columns : [{text : 'ctNm',flex :1 ,dataIndex : 'ctNm' }
	,{text : 'categoryId',flex :1 ,dataIndex : 'categoryId' }
	,{text : 'depth',flex :1 ,dataIndex : 'depth' }
				 ]
	
		,height :800
		,renderTo : Ext.getBody()
	})*/
