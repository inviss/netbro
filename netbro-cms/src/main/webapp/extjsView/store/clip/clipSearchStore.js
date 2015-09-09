	var clipStore = Ext.create('Ext.data.Store',{
				model : 'clipList'
			   ,proxy : {
				   			type : 'ajax'
				   			// ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json' }
							,noCache: false 
				   			,url : '/clip/findClipSearchList.ssc'
				   			 
				   			,reader : {
				   			type : 'json'
				   			,root : 'metas'
				   			}
			   }
		 
			});
			