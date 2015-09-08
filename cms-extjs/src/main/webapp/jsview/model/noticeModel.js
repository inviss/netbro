Ext.define('NoticeModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'noticeId', type : 'long'}
		           ,{name : 'title', type : 'string'}
		           ,{name : 'cont', type : 'string'}
		           ,{name : 'regId', type : 'string'} 
		           ,{name : 'modId', type : 'string'} 
		           ,{name : 'keyword', type : 'string'} 
		           ,{name : 'searchFiled', type : 'string'} 
		           ,{name : 'userNm', type : 'string'} 
		           ,{name : 'pageNo', type : 'integer'} 
		           ,{name : 'popUpYn', type : 'string'} 
		           ,{name : 'startDd', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ,{name : 'endDd', type : '**datetime**',convert:function(v,record){
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
