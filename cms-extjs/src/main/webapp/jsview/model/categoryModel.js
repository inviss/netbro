Ext.define('CategoryModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'categoryId', type : 'int'}
		           ,{name : 'categoryNm', type : 'string'}
		           ,{name : 'type', type : 'string'}
		           ,{name : 'direction', type : 'string'}
		           ,{name : 'result', type : 'string'}
		           ,{name : 'reason', type : 'string'} 
		           ]

});  
