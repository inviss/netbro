	var traStore = Ext.create('Ext.data.Store',{
				model : 'traList'
			   ,proxy : {
				   			type : 'ajax'
				   			// ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json' }
							,noCache: false 
				   			,url : '/contents/findSearchList.ssc'
				   			 
				   			,reader : {
				   			type : 'json'
				   			,root : 'metas'
				   			}
			   }
		 
			});
			