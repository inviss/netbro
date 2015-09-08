 Ext.define('TestSearch',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'categoryId', type : 'int'}
		           ,{name : 'episodeId', type : 'int'}
		           ,{name : 'segmentId', type : 'int'} 
					,{name : 'categoryNm', type : 'string'}
					,{name : 'episodeNm', type : 'string'} 
					,{name : 'segmentNm', type : 'string'}
					,{name : 'ctId', type : 'long'} 
					//,{name : 'vdHresol', type : 'int'}
					//,{name : 'vdVresol', type : 'int'} 
					,{name : 'rpImgKfrmSeq', type : 'long'}
					,{name : 'depth', type : 'int'} 
					,{name : 'regDt', type : '**datetime**',convert:function(v,record){
						return Ext.Date.format(new Date(v),'Y-m-d')
					} }
					,{name : 'duration', type : 'long'} 
					,{name : 'flPath', type : 'string'}
					,{name : 'prodRoute', type : 'string'} 
					,{name : 'ctTyp', type : 'string'}
					,{name : 'ctCla', type : 'string'} 
					,{name : 'ctiFmt', type : 'string'}
					,{name : 'ctNm', type : 'string'} 
					,{name : 'keyWords', type : 'string'}
					,{name : 'brdDd', type : 'string'} 
					,{name : 'cont', type : 'string'}
					,{name : 'regrid', type : 'string'} 
					,{name : 'aspRtpCd', type : 'string'}
					,{name : 'vdQlty', type : 'string'} 
					,{name : 'nodes', type : 'string'}
					,{name : 'ctLeng', type : 'string'} 
					,{name : 'svrFlNm', type : 'string'}
					,{name : 'ristClfCd', type : 'string'} 
					,{name : 'lockStatCd', type : 'string'}
					]
				
	});  
 