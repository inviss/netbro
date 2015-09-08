
Ext.define('DownloadInfo',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'ctiId', type : 'Integer'},
		           {name : 'workStatCd', type : 'string',convert:function(v,record){
					return record.raw.sclNm;
					}},
		           {name : 'errorCd', type : 'string'},
		           {name : 'cont', type : 'string'},
		           {name : 'regDt', type : '**datetime**',convert:function(v,record){
						return Ext.Date.format(new Date(v),'Y-m-d')
					} },
		           {name : 'approveId', type : 'Integer'},
		           {name : 'prgrs', type : 'Integer'},
		           {name : 'ctNm', type : 'string'},
		           {name : 'categoryNm', type : 'string'},
		           {name : 'episodeNm', type : 'string'} ,
		           {name : 'sclNm', type : 'string'} ,
		           {name : 'seq', type : 'Integer'} ,
		           {name : 'userNm', type : 'string'} ,
		           {name : 'ctLeng', type : 'string'} 
		           ]

});  
