 Ext.define('BaseInfo',{
	extend : 'Ext.data.Model'
		,fields : [
		            {name : 'ctId', type : 'long'} 
		           ,{name : 'ctTyp', type : 'string'}
		           ,{name : 'ctCla', type : 'string'} 
		           ,{name : 'ctNm', type : 'string'} 
		           ,{name : 'cont', type : 'string'}
		           ,{name : 'keyWords', type : 'string'}
		           ,{name : 'rpimgKfrmSeq', type : 'string'}
		           ,{name : 'useYn', type : 'string'}
		           ,{name : 'regDt', type : '**datetime**',convert:function(v,record){
		        	   return Ext.Date.format(new Date(v),'Y-m-d')
		           } }
		           ,{name : 'dataStatCd', type : 'string'}
		           ,{name : 'categoryNm', type : 'string'}
		           ,{name : 'episodeNm', type : 'string'} 
		           ,{name : 'segmentNm', type : 'string'}
		           ,{name : 'ctLeng', type : 'string'} 
		           ,{name : 'duration', type : 'long'} 
		           ,{name : 'brdDd', type : 'string'} 
		           ,{name : 'spcInfo', type : 'string'} 
		           ,{name : 'categoryId', type : 'long'} 
		           ,{name : 'episodeId', type : 'long'} 
		           ,{name : 'sclNm', type : 'string'} 
		           ,{name : 'aspRtoCd', type : 'string'}  
		           ,{name : 'vdVresol', type : 'string'} 
		           ,{name : 'vdHresol', type : 'string'} 
		           ,{name : 'frmPerSec', type : 'string'} 
		           ,{name : 'vdQlty', type : 'string'} 
		           ,{name : 'dataStatNm', type : 'string'} 
		           ,{name : 'ristClfCd', type : 'string'} 
		           ,{name : 'ristClfNm', type : 'string'} 
		           ,{name : 'contentsInst'} 
		           
					
					]
 		,hasMany : {model : 'ContentsInstModel' ,name : 'contentsInst' }
				
	});  
 