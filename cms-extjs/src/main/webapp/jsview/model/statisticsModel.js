 Ext.define('Statistic',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'categoryId', type : 'long'}
		           ,{name : 'regDd', type : 'string', convert : function(v,record){
		        	   return v+'월'
		           }}
		           ,{name : 'regist', type : 'long',convert:function(v,record){
		        	   if(v == null || v == ''){
		        		   return 0;
		        	   }else{
		        		   return v;
		        	   }
					}} 
		           ,{name : 'beforeArrange', type : 'long',convert:function(v,record){
		        	   if(v == null || v == ''){
		        		   return 0;
		        	   }else{
		        		   return v;
		        	   }
					}}
		           ,{name : 'completeArrange', type : 'long',convert:function(v,record){
		        	   if(v == null || v == ''){
		        		   return 0;
		        	   }else{
		        		   return v;
		        	   }
					}} 
		           ,{name : 'discard', type : 'long',convert:function(v,record){
		        	   if(v == null || v == ''){
		        		   return 0;
		        	   }else{
		        		   return v;
		        	   }
					}}
		           ,{name : 'depth', type : 'long',convert:function(v,record){
		        	   if(v == null || v == ''){
		        		   return 0;
		        	   }else{
		        		   return v;
		        	   }
					}} 
		           ,{name : 'groupId', type : 'long',convert:function(v,record){
		        	   if(v == null || v == ''){
		        		   return 0;
		        	   }else{
		        		   return v;
		        	   }
					}}
		           ,{name : 'nodes', type : 'string'} 
		           ,{name : 'categoryNm', type : 'string'  }
		           ,{name : 'startDD', type : 'date'} 
		           ,{name : 'endDD', type : 'date'}
		           ,{name : 'gubun', type : 'string'} 
		           ,{name : 'userNm', type : 'string'}
		           ,{name : 'ctNm', type : 'string'} 
		           ,{name : 'episodeNm', type : 'string'}
		           ,{name : 'ctLeng', type : 'string'} 
		           ,{name : 'brdDd', type : 'string'}
		           ,{name : 'vdQlty', type : 'string'} 
		           ,{name : 'error', type : 'long',convert:function(v,record){
		        	   if(v == null || v == ''){
		        		   return 0;
		        	   }else{
		        		   return v;
		        	   }
					}}
		           ,{name : 'yearList', type : 'string'} 
		           ,{name : 'regDt', type : 'date'}
		           ,{name : 'vdHresol', type : 'string'} 
		           ,{name : 'vdVresol', type : 'string'}
		           ,{name : 'count', type : 'string'} 
		           ,{name : 'groupNm', type : 'string'} 
		           ,{name : 'pageNum', type : 'int'} 
					]
				
	});  
 