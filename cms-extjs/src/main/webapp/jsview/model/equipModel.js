Ext.define('EquipModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'tmpDeviceId', type : 'string',convert:function(v,record){
		        	   if(v.substr(0,2) == "TC"){
		        		   return "트랜스코더";
		        	   }else{
		        		   return "트랜스퍼";
		        	   }
		           }}
		           ,{name : 'deviceId', type : 'string'}
		           ,{name : 'deviceNm', type : 'string'}
		           ,{name : 'deviceIp', type : 'string'}
		           ,{name : 'deviceNum', type : 'integer'}
		           ,{name : 'devicePort', type : 'integer'} 
		           ,{name : 'useYn', type : 'string'}  
		           ,{name : 'regrId', type : 'string'}
		           ,{name : 'modrId', type : 'string'}
		           ,{name : 'deviceClfCd', type : 'string'}
		           ,{name : 'ctiId', type : 'long'}
		           ,{name : 'workStatCd', type : 'string',convert:function(v,record){
		        	   if(v == "000"){
		        		   return "대기중"   
		        	   }else{
		        		   return "전송중"
		        	   }
		           }
		           }
		           ,{name : 'ctNm', type : 'string'}
		           ,{name : 'prgrs', type : 'integer',convert:function(v,record){
		        	   return v + "%"
		           }
		           }
		           ,{name : 'regDt', type : '**datetime**',convert:function(v,record){
		        	   if(v != null){
		        		   return Ext.Date.format(new Date(v),'Y-m-d')
		        	   }else{
		        		   return "";
		        	   }
		           } }
		           ,{name : 'modDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }

		           ]

});  
