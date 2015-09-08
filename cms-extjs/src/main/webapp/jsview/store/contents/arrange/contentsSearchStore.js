	var contentsStore = Ext.create('Ext.data.Store',{
				model : 'ContentsList'
			    ,pageSize: 10
			   ,proxy : {
				   			type : 'ajax'
				   			// ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
				   			//,url :  '/contents/findSearchList.ssc'
				   			,reader : {
				   			type : 'json'
				   			,root : 'metas'
				   			,totalProperty : 'total'
				   			}
			   }
		 
			});
			