
var imgStore = Ext.create('Ext.data.Store',{
				model : 'BaseInfo'
			   ,proxy : {
				   			type : 'ajax'
				   			,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
				   			
				   			,api : { 
				   				  update :  '/contents/updateRpimg.ssc'
				   				  ,destroy : '/contents/updateStoryBoardImgs.ssc'
				   			}
				   			,reader : {
				   			type : 'json'
				   			 
				   			 
				   			}
			   }
				  
			});
 