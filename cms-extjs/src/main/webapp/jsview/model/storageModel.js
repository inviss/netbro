Ext.define('StorageModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'storageId', type : 'integer'}
		           ,{name : 'totalVolume', type : 'long',convert:function(v,record){
		        	   return v + "KB"
		           }
		           }
		           ,{name : 'useVolume', type : 'long',convert:function(v,record){
		        	   return v + "KB"
		           }
		           }
		           ,{name : 'idleVolume', type : 'long',convert:function(v,record){
		        	   return v + "KB"
		           }
		           }
		           ,{name : 'limit', type : 'integer'} 
		           ,{name : 'storagePath', type : 'string'}
		           ,{name : 'storageGubun', type : 'string',convert:function(v,record){
		        	   if(v == "H"){
		        		   return "고용량"
		        	   }else{
		        		   return "저용량"
		        	   }
		           }
		           }
		           ,{name : 'volume'}
		           ,{name : 'partNm', type : 'string'}
		           ,{name : 'data'}
		           ,{name : 'name', type : 'string'}
		           ]
});  
