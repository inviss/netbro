Ext.define('BusicategoryModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'categoryId', type : 'integer'}
		           ,{name : 'ctTyp', type : 'string'}
		           ,{name : 'busiPartnerId', type : 'long'} 
		           ,{name : 'recYn', type : 'string'} 
		           ,{name : 'audioModeCode', type : 'string'}
		           ,{name : 'tmpBusiPartnerId', type : 'string'}
		           ,{name : 'bgnTime', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ,{name : 'endTime', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ,{name : 'regDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ,{name : 'modDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ]
});  
