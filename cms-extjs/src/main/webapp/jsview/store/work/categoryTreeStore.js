 
var workCategoryTreestore = Ext.create('Ext.data.TreeStore',{
	 
	storeId : 'workCategory'
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

