Ext.define('UsageModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'total', type : 'int'},
		           {name : 'usage', type : 'int'}
		          ]

});  

Ext.define('ThirdModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'eqNm', type : 'string'},
		           {name : 'categoryNm', type : 'string'},
		           {name : 'episodeNm', type : 'string'},
		           {name : 'ctNm', type : 'string'},
		           {name : 'profileNm', type : 'string'},
		           {name : 'progress', type : 'int'},
		           {name : 'status', type : 'string'},
		          ]

});
