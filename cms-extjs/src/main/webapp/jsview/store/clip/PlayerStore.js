
var playerStore = Ext.create('Ext.data.Store',{
				model : 'Player'
			   ,proxy : {
				   			type : 'ajax'
				   			// ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
				   			//,url : '/clip/getVideoPlayInfo.ssc'
				   			 
				   			,reader : {
				   			type : 'json'
				   			 
				   			 
				   			}
			   }
				 
			 
		 
			});
			