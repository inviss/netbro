var episodeStore = Ext.create('Ext.data.Store',{
	model : 'EpisodeModel'
		   ,proxy : {
			   			type : 'ajax'
			   			,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
						,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
						,noCache: false 
			   			//,url : '/contents/getEpisodeSearch.ssc'
			   			 
				   			,reader : {
				   			type : 'json'
				   		   ,root : 'episodeTbls'
				   			 
				   			 
				   			}
			   		 
			   		
		   }
,autoLoad : false
 
		 
		 
	 
		});
