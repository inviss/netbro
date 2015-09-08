 
var categoryTreestore = Ext.create('Ext.data.TreeStore',{
		 storeId : 'adminCategoryTree'
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
