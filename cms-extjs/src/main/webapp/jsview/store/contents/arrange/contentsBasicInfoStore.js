
var contentsBasicStore = Ext.create('Ext.data.Store',{
				model : 'BaseInfo'
			   ,proxy : {
				   			type : 'ajax'
				   			,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
				   		//	,url : '/contents/getContentsBasicInfo.ssc'
				   		/*	,api : {
				   		//		read : '/contents/getContentsBasicInfo.ssc',
				   			//	  update :  '/contents/updateBasicInfo.ssc'
				   				
				   			}*/
				   			,reader : {
				   			type : 'json'
				   			,root : 'contentsTbl'
				   			 
				   			}
				   			,success : function(){
				   				Ext.Msg.Alert("저장 되었습니다")
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