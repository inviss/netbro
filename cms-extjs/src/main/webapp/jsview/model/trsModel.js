Ext.define('TrsModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'ctId', type : 'long'}
		           ,{name : 'priority', type : 'string'}
		           ,{name : 'retryCnt', type : 'Integer'}
		           ,{name : 'seq', type : 'long'} 
		           ,{name : 'ctNm', type : 'string'} 
		           ,{name : 'ctiId', type : 'long'} 
		           ,{name : 'flPath', type : 'string'}
		           ,{name : 'orgFileNm', type : 'string'}
		           ,{name : 'wrkFileNm', type : 'string'}
		           ,{name : 'flExt', type : 'string'}
		           ,{name : 'proFlnm', type : 'string'}
		           ,{name : 'categoryNm', type : 'string'}
		           ,{name : 'episodeNm', type : 'string'}
		           ,{name : 'ctLeng', type : 'string'}
		           ,{name : 'vdoBitRate', type : 'string'}
		           ,{name : 'remoteDir', type : 'string'}
		           ,{name : 'method', type : 'string'}
		           ,{name : 'prgrs', type : 'integer',convert:function(v,record){
		        	   return v + "%"
		        	   }
		           }
		           ,{name : 'company', type : 'string'}
		           ,{name : 'workStatcd', type : 'string',convert:function(v,record){
		        	   if(v == '000'){
		        		   return "대기"
		        	   }else if(v == '001'){
		        		   return "준비"
		        	   }else if(v == '002'){
		        		   return "전달"
		        	   }else if(v == '003'){
		        		   return "진행"
		        	   }else if(v == '004'){
		        		   return "완료"
		        	   }else if(v == '005'){
		        		   return "전송오류"
		        	   }else if(v == '006'){
		        		   return "기타오류"
		        	   }else{
		        		   
		        	   }
		           } }
		           ,{name : 'deviceId', type : 'string'} 
		           ,{name : 'regDt', type : '**datetime**',convert:function(v,record){
		        	   if(v != null){
		        		   return Ext.Date.format(new Date(v),'Y-m-d')
		        	   }else{
		        		   return "";
		        	   }
		           } }
		           ,{name : 'reqDt', type : '**datetime**',convert:function(v,record){
		        	   if(v != null){
		        		   return Ext.Date.format(new Date(v),'H:i:s')
		        	   }else{
		        		   return "";
		        	   }
		           } }
		           ,{name : 'modDt', type : '**datetime**',convert:function(v,record){
		        	   if(v != null){
		        		   return Ext.Date.format(new Date(v),'H:i:s')
		        	   }else{
		        		   return "";
		        	   }
		           } }
		           ,{name : 'trsEndDt', type : '**datetime**',convert:function(v,record){
		        	   if(v != null){
		        		   return Ext.Date.format(new Date(v),'H:i:s')
		        	   }else{
		        		   return "";
		        	   }
		           } }
		           ,{name : 'trsStrDt', type : '**datetime**',convert:function(v,record){
		        	   if(v != null){
		        		   return Ext.Date.format(new Date(v),'H:i:s')
		        	   }else{
		        		   return "";
		        	   }
		           } }

		           ]

});  
