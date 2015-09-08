
var categroyStore = Ext.create('Ext.data.Store',{
				model : 'CategoryModel'
			   ,proxy : {
				   			type : 'ajax'
				   		  ,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
							,api : {
								update : '/admin/category/insertCategory.ssc'
							}
				   			,reader : {
				   			type : 'json'
				   			,root: ''
				   			}
				   		 
							} 
			
		 
			});
	
 