	var contentsArchiveStore = Ext.create('Ext.data.Store',{
				model : 'ArchiveInfo'
			    ,pageSize: 10
			   ,proxy : {
				   			type : 'ajax'
				   			// ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
				   			//,url :  '/contents/findSearchList.ssc'
				   			 
			   }
		 
			});
			