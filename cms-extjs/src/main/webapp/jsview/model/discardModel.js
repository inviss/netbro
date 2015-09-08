
Ext.define('DiscardModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'ctId', type : 'integer'}
		           ,{name : 'disuseNo', type : 'string'}
		           ,{name : 'disuseRsl', type : 'string'}
		           ,{name : 'disuseClf', type : 'string',convert:function(v,record){
		        	   if(v != null){
			        	 if(v == '001') {
			        		 v = "폐기신청"
			        	 }else if(v == '002'){
			        		 v = "폐기완료"
			        	 }else if(v == '003'){
			        		 v = "폐기취소"
			        	 }
			           } 
		        	   return v;
		        	   } 
		           }
		           ,{name : 'regrId', type : 'string'} 
		           ,{name : 'cancelCont', type : 'string'}
		           ,{name : 'ctNm', type : 'string'}		
		           ,{name : 'disuseDd', type : '**datetime**',convert:function(v,record){
		        	   if(v != null){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		        	   }else{
		        		   return "";
		        	   }
		           } }
		           ,{name : 'regDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		          
		           ]

});  
