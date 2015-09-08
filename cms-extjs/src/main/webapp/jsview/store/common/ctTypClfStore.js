var ctTypStore = Ext.create('Ext.data.Store',{
			model : 'CodeModel'
		   ,proxy : {
			   			type : 'ajax'
			   			  ,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
						,headers: { 'Content-Type': 'application/json' }
						,noCache: false   
			   			,reader : {
			   			type : 'json',
			   			root : 'codeInfos'
			   			}
		   }
			 
			 ,autoLoad :false
			 
	 
		});

