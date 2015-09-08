Ext.define('MenuModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'menuId', type : 'int'},
		           {name : 'menuEnNm', type : 'string'},
		           {name : 'menuNm', type : 'string'},
		           {name : 'url', type : 'string'},
		           {name : 'userId', type : 'string'},
		           {name : 'controlGubun', type : 'string'},
		           {name : 'authId', type : 'string'}
		           ]

});  
