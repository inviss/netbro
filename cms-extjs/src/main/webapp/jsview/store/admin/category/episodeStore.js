
var episodeStore = Ext.create('Ext.data.Store',{
				model : 'EpisodeModel'
				,pageSize : 23
			   ,proxy : {
				   			type : 'ajax'
				   		  ,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
							  
				   			,reader : {
				   				type : 'json'
				   				,root: 'episodeTbls'
				   				,totalProperty : 'total'
				   			}
				   		 
							} 
		 		,listeners : {
		 	    	load : function(view,records){
		 	     
		 	    		//공백의 리스트를 생성한다.
		 	    		var emptyRaw=23;
		 	    		if((emptyRaw - records.length) > 0){
		 	    			var empty =  emptyRaw - records.length;
		 	    			for(var i =0; i<empty;i++){
		 	    				episodeStore.add({"categoryId":'',"episodeId":'',"useYn":'',"regrId":'',"regDt":'',"modrId":'',"modDt":'',"episodeNm":''});
		 	    			}
		 	    		}
		 	    	}
		 	    }
			});
	
 