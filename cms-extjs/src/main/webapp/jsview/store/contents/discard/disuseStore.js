
var disuseStore = Ext.create('Ext.data.Store',{
				model : 'DiscardModel'
			   ,pageSize: 16
			   ,proxy : {
				   			type : 'ajax'
				   			,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 				   	
							,reader : {
					   			type : 'json'
					   			,root : 'discards'
					   			,totalProperty : 'total'
					   			}
			   } 
			 
			 
			});

 
