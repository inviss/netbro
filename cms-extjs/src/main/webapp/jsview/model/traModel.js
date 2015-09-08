Ext.define('TraModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'seq', type : 'long'}
		           ,{name : 'reqUsrid', type : 'string'}
		           ,{name : 'modrId', type : 'string'}
		           ,{name : 'proFlnm', type : 'string'}
		           ,{name : 'ctId', type : 'long'} 
		           ,{name : 'categoryNm', type : 'string'} 
		           ,{name : 'ctNm', type : 'string'} 
		           ,{name : 'modrId', type : 'string'} 
		           ,{name : 'prgrs', type : 'integer',convert:function(v,record){
		        	   return v + "%"
		        	   }
		           }
		           ,{name : 'jobStatus', type : 'string',convert:function(v,record){
		        	   if(v == 'S'){
		        		   return "진행중"
		        	   }else if(v == 'C'){
		        		   return "완료"
		        	   }else{
		        		   return "에러"
		        	   }
		           } }
		           ,{name : 'deviceId', type : 'string'} 
		           ,{name : 'regDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ,{name : 'reqDt', type : '**datetime**',convert:function(v,record){
		        	   if(v != null){
		        		   return Ext.Date.format(new Date(v),'Y-m-d')
		        	   }else{
		        		   return "";
		        	   }
		           } }
		           ,{name : 'modDt', type : '**datetime**',convert:function(v,record){
		        	   if(v != null){
		        		   return Ext.Date.format(new Date(v),'Y-m-d')
		        	   }else{
		        		   return "";
		        	   }
		           } }

		           ]

});  
