var code = Ext.define('CodeModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'clfCD', type : 'string'}
		           ,{name : 'sclCd', type : 'string'}
		           ,{name : 'clfNm', type : 'string'}
		           ,{name : 'sclNm', type : 'string'} 
		           ,{name : 'codeCont', type : 'string'} 
		           ,{name : 'rmk1', type : 'string'}
		           ,{name : 'rmk2', type : 'string'} 
		           ,{name : 'modrId', type : 'string'} 
		           ,{name : 'clfGubun', type : 'string'} 
		           ,{name : 'useYn', type : 'string'}  
		           ,{name : 'clfCd', type : 'string'} 
		           ,{name : 'gubun', type : 'string'} 
		           ,{name : 'keyWord', type : 'string'} 
		           ,{name : 'createWay', type : 'string'} 
		           ,{name : 'regDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ,{name : 'modDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }

		           ]

});  
