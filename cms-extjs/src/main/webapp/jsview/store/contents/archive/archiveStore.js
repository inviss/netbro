 
var archiveStore = Ext.create('Ext.data.Store',{
			model : 'ArchiveInfo'
		   ,proxy : {
			   			type : 'ajax'
			   			,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
						,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
						,noCache: false 
						,reader : {
				   			type : 'json'
				   			,root : 'archiveInfo'
				   			 
				   			}
			   		 
			   		
		   },
		   listeners : {
			   load : function(view , recodes){
				   console.log(view);
				   console.log(recodes)
			   }
		   }
		 
		  
		 
	 
		});

 