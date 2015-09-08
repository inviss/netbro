var contentsCategoryTreestore = Ext.create('Ext.data.TreeStore',{
			
			  storeId : 'archiveCategoryTree'
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
