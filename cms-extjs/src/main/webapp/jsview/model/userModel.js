Ext.define('UserModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'userId', type : 'string'}
		           ,{name : 'userNm', type : 'string'}
		           ,{name : 'regrId', type : 'string'}
		           ,{name : 'modrId', type : 'string'} 
		           ,{name : 'useYn', type : 'string'} 
		           ,{name : 'userPass', type : 'string'}
		           ,{name : 'userPhone', type : 'string'} 
		           ,{name : 'authId', type : 'string'}
		           ,{name : 'authNm', type : 'string'}
		           ,{name : 'regDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ,{name : 'modDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }

		           ]

});  
