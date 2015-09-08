
 
var clipBasicStore = Ext.create('Ext.data.Store',{
				model : 'BaseInfo'
			   ,proxy : {
				   			type : 'ajax'
				   			// ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
				   			// ,url : '/clip/getClipSearchBasicInfo.ssc'
				   			 
				   			,reader : {
				   			type : 'json'
				   			,root : 'contentsTbl'
				   			 
				   			}
			   }
				,listeners : {
					load : function(view,records){
						 
						baseInfoStore.loadRawData(view.proxy.reader.jsonData)
						
					}
				}
			   ,getBasicInfo : function(){
				  return  baseInfoStore
			   }
				 
			 
		 
			});


var baseInfoStore = Ext.create('Ext.data.Store',{
		model : 'MetaModel'
		,proxy : {
			type : 'memory'
			,reader : {
				type : 'json'
				
				}
			}
})