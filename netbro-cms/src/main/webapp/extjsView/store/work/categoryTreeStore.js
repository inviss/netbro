var workCategoryTreestore = Ext.create('Ext.data.Store',{
				model : 'categoryTreeList'
			   ,proxy : {
				   			type : 'ajax'
				   			 //,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json' }
							,noCache: false
				   			,url : '/work/findCategoryList.ssc'
				   			,reader : {
				   			type : 'json'
				   			,root : 'categoryTbls'
				   			}
			   }
			//,autoLoad : true
			});

