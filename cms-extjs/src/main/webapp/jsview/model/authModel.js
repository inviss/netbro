
Ext.define('AuthModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'authId', type : 'integer'}
		           ,{name : 'authNm', type : 'string'}
		           ,{name : 'authSubNm', type : 'string'}
		           ,{name : 'regrId', type : 'string'} 
		           ,{name : 'modrId', type : 'string'} 
		           ,{name : 'userPass', type : 'string'}
		           ,{name : 'userPhone', type : 'string'}
		           ,{name : 'useYn', type : 'string'} 
		           ,{name : 'controlGubun', type : 'string'} 
		           ,{name : 'roleAuth',type : 'object'} 
		           ,{name : 'regDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ,{name : 'modDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		          
		           ]

});  
