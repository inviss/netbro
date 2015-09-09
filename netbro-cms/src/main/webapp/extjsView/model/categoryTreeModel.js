var categoryTree =	Ext.define('categoryTreeList',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'categoryNm', type : 'string'}
		           ,{name : 'categoryId', type : 'int'}
		           ,{name : 'depth', type : 'int'}]
		
	});
