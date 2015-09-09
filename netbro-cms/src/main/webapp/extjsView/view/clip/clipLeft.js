 
Left = function() {

	return {
    	init : function() {
    		Ext.Loader.setConfig({
    			disableCaching: false
    		});
    		Ext.require('/extjsView/store/clip/categoryTreeStore' );
    		Ext.require('/extjsView/store/clip/clipSearchStore');
    		Ext.require('/extjsView/view/clip/clipSearch');
			var  initList = new Ext.data.Operation({
				action :'read'
				,params : {'keyword' :  '' 
							,'startDt' : '2014-04-01'
							,'endDt' : '2014-05-31'}
			});
			clipStore.load(initList);
			
			var searchTest = new Ext.data.Operation({
				action :'read'
				,params : {'categoryId' : 0,'depth' : 0 }
			});
			clipCategoryTreestore.load(searchTest);
    	/*	var rightBox = Ext.create('Ext.panel.Panel',{
				xtype : 'vbox'
				,renderTo : 'left'
				,width : 300
				//,height : '100%'
				,items : [{
					html : '<h>트리뷰 위치</h>'
				 ,height : 300
						
				}		        
						,{
					xtype : 'combobox'
						,fieldLabel : '카테고리'
						,itemId : 'categoryComboBox'
						,store : clipCategoryTreestore
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
										alert('enter value ' + value);								
										var  List = new Ext.data.Operation({
											action :'read'
											,params : {'keyword' : value}
										});
										store.load(List);
									}
								}
							}
						}]
    		
			});*/
			var treepaenl = Ext.create('Ext.panel.Panel',{
					html : '<h>트리뷰 위치</h>'
					 ,flex :4
			});
			
			var combox =  Ext.create('Ext.form.Panel',{

				xtype : 'combobox'
					,fieldLabel : '카테고리'
					,itemId : 'categoryComboBox'
					,store : clipCategoryTreestore
				    ,displayField : 'categoryNm'
				    ,valueField : 'categoryId'
				     
				
			});
			 var panel = Ext.create('Ext.panel.Panel',{
				 layout: {
					type: 'hbox',
					padding: '5',
					align: 'stretch'
				},
				items : [panel2 ,panel3]});
			 var panel2 = Ext.create('Ext.panel.Panel',{
				 html : '111'
			     ,flex : 1
			 });
			 var panel3 = Ext.create('Ext.panel.Panel',{
				 html : '111'
			     ,flex : 1
			 });
			var searchField =  Ext.create('Ext.form.Panel',{
				xtype : 'textfield'
					,id : 'searchField'
					,fieldLabel : '검색'
					
					,  listeners:{  
						scope : this
						,specialkey : function(f,e){
							 
							if(e.getKey() == e.ENTER){ 
								var value = Ext.getCmp('searchField').getValue();
								alert('enter value ' + value);								
								var  List = new Ext.data.Operation({
									action :'read'
									,params : {'keyword' : value}
								});
								store.load(List);
							}
						}
					}
				});
			var rightBox = Ext.create('Ext.panel.Panel',{
				//id : 'mainMenu',
				//alias : 'topmenu',
				bodyStyle:{"background-color" : "#041588"},
				layout: {
					type: 'hbox',
					padding: '5',
					align: 'stretch'
				},
				height : 50,
				 
				items: [panel]
			});
				
    		
    		console.log(videoHtml);
/*		var viewport = Ext.create('Ext.panel.Panel',{
			layout: {
				type: 'hbox',
				padding: '5',
				align: 'stretch'
			}
		//,width : '100%'
			  //,height : '100%'
				,renderTo : 'left'
					 
				,items : [ rightBox]
		});*/
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
