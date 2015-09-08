Ext.define('MenuTreeModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'read', type : 'boolean'}
		           ,{name : 'write', type : 'boolean'}
		           ,{name : 'limit', type : 'boolean'} 
		           ,{name : 'text', type : 'string'} 
		           ]

});  
