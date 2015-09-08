
var discardStore = Ext.create('Ext.data.Store',{
				model : 'BaseInfo'
			   ,proxy : {
				   			type : 'ajax'
				   			,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
				   		/*	,url : '/contents/getContentsBasicInfo.ssc'
				   			,api : {
				   				update :  '/disuse/insertDisUse.ssc'
				   				,destroy :  '/disuse/cancleDisUse.ssc'
				   			}*/
				   		 
				   		
			   }
		 
			 
		 
			});

 