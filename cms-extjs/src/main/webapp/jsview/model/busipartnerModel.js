Ext.define('BusipartnerModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'busiPartnerId', type : 'long'}
		           ,{name : 'password', type : 'string'}
		           ,{name : 'company', type : 'string'} 
		           ,{name : 'tmpProflId', type : 'string'} 
		           ,{name : 'servYn', type : 'string'}  
		           ,{name : 'ftpServYn', type : 'string'} 
		           ,{name : 'ip', type : 'string'}
		           ,{name : 'port', type : 'string'}
		           ,{name : 'transMethod', type : 'string'}
		           ,{name : 'remoteDir', type : 'string'} 
		           ,{name : 'ftpId', type : 'string'} 
		           ,{name : 'srvUrl', type : 'string'} 
		           ,{name : 'regDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ,{name : 'modDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ]
});  
