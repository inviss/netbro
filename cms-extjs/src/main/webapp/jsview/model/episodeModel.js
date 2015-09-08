Ext.define('EpisodeModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'categoryId', type : 'int',convert:function(v,record){
		        	 
		        	   if(v == undefined){
		        		   return ''
		        	   }else{
		        		   return v
		        	   } 
					}}
		           ,{name : 'episodeId', type : 'int',convert:function(v,record){
		        	 
		        	   if(v == undefined){
		        		   return ''
		        	   }else{
		        		   return v
		        	   } 
					}}
		           ,{name : 'episodeNm', type : 'string'}
		           ,{name : 'regDt', type : '**datetime**',convert:function(v,record){
		        	   if(v != ''){
						return Ext.Date.format(new Date(v),'Y-m-d')
		        	   }else{
		        		  return ''
		        	   }
					} }
		           
		 
		          
		           ]

});  
