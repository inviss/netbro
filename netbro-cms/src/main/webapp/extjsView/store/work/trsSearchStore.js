	var trsStore = Ext.create('Ext.data.Store',{
				model : 'trsList'
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
			