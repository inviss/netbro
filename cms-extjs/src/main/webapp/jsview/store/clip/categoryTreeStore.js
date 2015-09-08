var clipCategoryTreestore = Ext.create('Ext.data.TreeStore',{
		 
				storeId : 'clipCategory'
			  ,autoLoad : false
               ,root : {
            	   text : '전체'
            	   ,id : 'total'
                   ,expanded : true
                   ,single : true
               }
			   ,viewConfig : {
				   containerScroll : true
			   }
			
			});
