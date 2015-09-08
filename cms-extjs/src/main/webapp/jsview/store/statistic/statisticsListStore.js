var statisticList = Ext.create('Ext.data.Store',{
			model : 'Statistic'
		   ,proxy : {
			   			type : 'ajax'
			   			// ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
						,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
						,noCache: false 
			   			//,url : '/statistic/findStatisticListForPeriod.ssc'
			   			 
			   			,reader : {
			   			type : 'json'
			   			,root : 'statisticsTbls'
			   			 
			   			}
		   }
			,groupField : 'groupNm'
			 ,autoLoad :false
			,listeners : {
				load : function(view,records){
					
					
				}
			}
		  
			 
		 
	 
		});
