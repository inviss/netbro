Ext.define('ProfileModel',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'proFlId', type : 'long'}
		           ,{name : 'regrId', type : 'string'}
		           ,{name : 'servBit', type : 'string'}
		           ,{name : 'modrId', type : 'string'} 
		           ,{name : 'ext', type : 'string'} 
		           ,{name : 'vdoCodec', type : 'string'}
		           ,{name : 'vdoBitRate', type : 'string'}
		           ,{name : 'vdoHori', type : 'string'} 
		           ,{name : 'vdoVert', type : 'string'} 
		           ,{name : 'vdoFS', type : 'string'} 
		           ,{name : 'vdoSync', type : 'string'} 
		           ,{name : 'audCodec', type : 'string'} 
		           ,{name : 'audBitRate', type : 'string'} 
		           ,{name : 'audChan', type : 'string'} 
		           ,{name : 'audSRate', type : 'string'} 
		           ,{name : 'keyFrame', type : 'string'} 
		           ,{name : 'proFlnm', type : 'string'}
		           ,{name : 'picKind', type : 'string'} 
		           ,{name : 'useYn', type : 'string'}  
		           ,{name : 'flNameRule', type : 'string'} 
		           ,{name : 'chanPriority', type : 'integer'} 
		           ,{name : 'priority', type : 'integer'} 
		           ,{name : 'regDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ,{name : 'modDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		          
		           ]

});  
